package io.redspace.ironsspellbooks.jei;

import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.util.ModTags;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * - Upgrade scroll: (scroll level x) + (scroll level x) = (scroll level x+1)
 * - Imbue Weapon:   weapon + scroll = imbued weapon with spell/level of scroll
 * - Upgrade item:   item + upgrade orb =
 **/
public final class ScrollForgeRecipeMaker {
    private ScrollForgeRecipeMaker() {
        //private constructor prevents anyone from instantiating this class
    }

    public static List<ScrollForgeRecipe> getRecipes(IVanillaRecipeFactory vanillaRecipeFactory, JeiPlugin.ItemFinder itemFinder) {
        var recipes = new ArrayList<ScrollForgeRecipe>();
        var paperInput = Ingredient.of(Items.PAPER);
        var sortedInks = resolveInkItems(itemFinder).stream()
                .sorted(Comparator
                        .comparing((Item ink) -> ((io.redspace.ironsspellbooks.item.InkItem) ink).getRarity().ordinal())
                        .thenComparing(ink -> BuiltInRegistries.ITEM.getKey(ink).toString()))
                .toList();

        SchoolRegistry.REGISTRY.stream()
                .sorted(Comparator.comparing(school -> school.getId().toString()))
                .forEach(school -> {
                    var focusInput = resolveFocusIngredient(school.getFocus());
                    if (focusInput.isEmpty()) {
                        return;
                    }
                    var spells = SpellRegistry.getSpellsForSchool(school).stream()
                            .sorted(Comparator.comparing(spell -> SpellRegistry.REGISTRY.getKey(spell).toString()))
                            .toList();

                    for (AbstractSpell spell : spells) {
                        if (!spell.isEnabled() || !spell.allowCrafting() || spell == SpellRegistry.none()) {
                            continue;
                        }

                        for (Item ink : sortedInks) {
                            var inkItem = (io.redspace.ironsspellbooks.item.InkItem) ink;
                            var spellLevel = spell.getMinLevelForRarity(inkItem.getRarity());
                            if (spellLevel <= 0) {
                                continue;
                            }

                            recipes.add(new ScrollForgeRecipe(
                                    List.of(new ItemStack(inkItem)),
                                    paperInput,
                                    focusInput,
                                    List.of(getScrollStack(spell, spellLevel))
                            ));
                        }
                    }
                });

        return recipes;
    }

    private static List<io.redspace.ironsspellbooks.item.InkItem> resolveInkItems(JeiPlugin.ItemFinder itemFinder) {
        var inks = new ArrayList<io.redspace.ironsspellbooks.item.InkItem>();
        inks.addAll(itemFinder.inkItems);
        addInkIfMissing(inks, ItemRegistry.INK_COMMON.get());
        addInkIfMissing(inks, ItemRegistry.INK_UNCOMMON.get());
        addInkIfMissing(inks, ItemRegistry.INK_RARE.get());
        addInkIfMissing(inks, ItemRegistry.INK_EPIC.get());
        addInkIfMissing(inks, ItemRegistry.INK_LEGENDARY.get());
        return inks;
    }

    private static void addInkIfMissing(List<io.redspace.ironsspellbooks.item.InkItem> inks, Item ink) {
        if (ink instanceof io.redspace.ironsspellbooks.item.InkItem inkItem && !inks.contains(inkItem)) {
            inks.add(inkItem);
        }
    }

    private static Ingredient resolveFocusIngredient(net.minecraft.tags.TagKey<Item> focusTag) {
        var focusInput = Ingredient.of(focusTag);
        if (!focusInput.isEmpty()) {
            return focusInput;
        }
        return Ingredient.of(ModTags.SCHOOL_FOCUS);
    }

    private static ItemStack getScrollStack(AbstractSpell spell, int spellLevel) {
        var scrollStack = new ItemStack(ItemRegistry.SCROLL.get());
        ISpellContainer.createScrollContainer(spell, spellLevel, scrollStack);
        return scrollStack;
    }
}
