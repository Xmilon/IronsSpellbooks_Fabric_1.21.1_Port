package io.redspace.ironsspellbooks.jei;

import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
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
        var sortedInks = itemFinder.inkItems.stream()
                .sorted(Comparator
                        .comparing((Item ink) -> ((io.redspace.ironsspellbooks.item.InkItem) ink).getRarity().ordinal())
                        .thenComparing(ink -> BuiltInRegistries.ITEM.getKey(ink).toString()))
                .toList();

        SchoolRegistry.REGISTRY.stream()
                .sorted(Comparator.comparing(school -> school.getId().toString()))
                .forEach(school -> {
                    var focusInput = Ingredient.of(school.getFocus());
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

    private static ItemStack getScrollStack(AbstractSpell spell, int spellLevel) {
        var scrollStack = new ItemStack(ItemRegistry.SCROLL.get());
        ISpellContainer.createScrollContainer(spell, spellLevel, scrollStack);
        return scrollStack;
    }
}
