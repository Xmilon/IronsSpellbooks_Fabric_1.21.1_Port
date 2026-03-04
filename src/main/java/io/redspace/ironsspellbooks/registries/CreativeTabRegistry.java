package io.redspace.ironsspellbooks.registries;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.item.InkItem;
import io.redspace.ironsspellbooks.item.Scroll;
import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.item.curios.CurioBaseItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.SwordItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

@EventBusSubscriber(modid = IronsSpellbooks.MODID, bus = EventBusSubscriber.Bus.MOD)
public final class CreativeTabRegistry {
    private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, IronsSpellbooks.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> SPELL_MATERIALS_TAB = CREATIVE_MODE_TABS.register("spell_materials_tab",
            () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                    .icon(() -> new ItemStack(ItemRegistry.ARCANE_ESSENCE.get()))
                    .title(Component.translatable("itemGroup.irons_spellbooks.spell_materials_tab"))
                    .displayItems((parameters, output) -> sortedItems().stream()
                            .filter(item -> !isSpellbookOrScrollItem(item))
                            .filter(item -> !isEquipmentItem(item))
                            .filter(item -> !isBlockLikeItem(item))
                            .forEach(output::accept))
                    .build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> SPELL_EQUIPMENT_TAB = CREATIVE_MODE_TABS.register("spell_equipment_tab",
            () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 1)
                    .icon(() -> new ItemStack(ItemRegistry.IRON_SPELL_BOOK.get()))
                    .title(Component.translatable("itemGroup.irons_spellbooks.spell_equipment_tab"))
                    .displayItems((parameters, output) -> sortedItems().stream()
                            .filter(CreativeTabRegistry::isEquipmentItem)
                            .forEach(output::accept))
                    .build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BLOCKS_TAB = CREATIVE_MODE_TABS.register("blocks_tab",
            () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 2)
                    .icon(() -> new ItemStack(BlockRegistry.INSCRIPTION_TABLE_BLOCK.get()))
                    .title(Component.translatable("itemGroup.irons_spellbooks.blocks_tab"))
                    .displayItems((parameters, output) -> BlockRegistry.blocks().stream()
                            .map(holder -> holder.get().asItem())
                            .filter(item -> item != null && item != ItemStack.EMPTY.getItem())
                            .sorted(Comparator.comparing(item -> BuiltInRegistries.ITEM.getKey(item).toString()))
                            .forEach(output::accept))
                    .build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> SPELLBOOK_SCROLLS_TAB = CREATIVE_MODE_TABS.register("spellbook_scrolls_tab",
            () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 3)
                    .icon(() -> new ItemStack(ItemRegistry.SCROLL.get()))
                    .title(Component.translatable("itemGroup.irons_spellbooks.spellbook_scrolls_tab"))
                    .displayItems((parameters, output) -> {
                        sortedItems().stream()
                                .filter(CreativeTabRegistry::isSpellbookOrScrollItem)
                                .filter(item -> !(item instanceof Scroll))
                                .forEach(output::accept);
                        for (ItemStack stack : allScrollStates()) {
                            output.accept(stack);
                        }
                    })
                    .build());

    private CreativeTabRegistry() {
    }

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }

    private static List<Item> sortedItems() {
        return ItemRegistry.getIronsItems().stream()
                .map(holder -> (Item) holder.get())
                .sorted(Comparator.comparing(item -> BuiltInRegistries.ITEM.getKey(item).toString()))
                .toList();
    }

    private static boolean isSpellbookOrScrollItem(Item item) {
        String path = BuiltInRegistries.ITEM.getKey(item).getPath();
        return item instanceof SpellBook
                || item instanceof Scroll
                || item instanceof InkItem
                || path.contains("spell_book")
                || path.contains("scroll")
                || path.contains("ink")
                || path.contains("manuscript")
                || path.contains("spell_slot");
    }

    private static boolean isEquipmentItem(Item item) {
        String path = BuiltInRegistries.ITEM.getKey(item).getPath();
        return item instanceof ArmorItem
                || item instanceof SwordItem
                || item instanceof CurioBaseItem
                || item instanceof SpawnEggItem
                || path.contains("staff")
                || path.contains("wand")
                || path.contains("crossbow")
                || path.contains("amulet")
                || path.contains("ring")
                || path.contains("talisman")
                || path.contains("helmet")
                || path.contains("chestplate")
                || path.contains("leggings")
                || path.contains("boots");
    }

    private static boolean isBlockLikeItem(Item item) {
        return item instanceof BlockItem;
    }

    private static List<ItemStack> allScrollStates() {
        var stacks = new ArrayList<ItemStack>();
        stacks.add(new ItemStack(ItemRegistry.SCROLL.get()));

        SpellRegistry.getEnabledSpells().stream()
                .filter(spell -> spell != SpellRegistry.none())
                .sorted(Comparator.comparing(spell -> spell.getSpellResource().toString()))
                .forEach(spell -> {
                    int minLevel = Math.max(spell.getMinLevel(), 1);
                    int maxLevel = Math.max(spell.getMaxLevel(), minLevel);
                    for (int level = minLevel; level <= maxLevel; level++) {
                        var scrollStack = new ItemStack(ItemRegistry.SCROLL.get());
                        ISpellContainer.createScrollContainer(spell, level, scrollStack);
                        stacks.add(scrollStack);
                    }
                });

        return stacks;
    }
}
