package io.redspace.ironsspellbooks.registries;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = IronsSpellbooks.MODID, bus = EventBusSubscriber.Bus.MOD)
public final class CreativeTabRegistry {
    private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, IronsSpellbooks.MODID);

    private static final List<String> EQUIPMENT_ORDER = List.of(
            "netherite_spell_book", "diamond_spell_book", "gold_spell_book", "iron_spell_book", "copper_spell_book",
            "evoker_spell_book", "necronomicon_spell_book", "rotten_spell_book", "blaze_spell_book", "dragonskin_spell_book",
            "villager_spell_book", "druidic_spell_book", "cursed_doll_spell_book", "ice_spell_book",
            "blood_staff", "graybeard_staff", "ice_staff", "artificer_cane", "lightning_rod", "pyrium_staff",
            "magehunter", "spellbreaker", "amethyst_rapier", "boreal_blade", "twilight_gale",
            "keeper_flamberge", "legionnaire_flamberge", "decrepit_scythe", "hellrazor", "autoloader_crossbow", "wayward_compass",
            "wandering_magician_helmet", "wandering_magician_chestplate", "wandering_magician_leggings", "wandering_magician_boots",
            "pumpkin_helmet", "pumpkin_chestplate", "pumpkin_leggings", "pumpkin_boots",
            "pyromancer_helmet", "pyromancer_chestplate", "pyromancer_leggings", "pyromancer_boots",
            "electromancer_helmet", "electromancer_chestplate", "electromancer_leggings", "electromancer_boots",
            "archevoker_helmet", "archevoker_chestplate", "archevoker_leggings", "archevoker_boots",
            "cultist_helmet", "cultist_chestplate", "cultist_leggings", "cultist_boots",
            "cryomancer_helmet", "cryomancer_chestplate", "cryomancer_leggings", "cryomancer_boots",
            "shadowwalker_helmet", "shadowwalker_chestplate", "shadowwalker_leggings", "shadowwalker_boots",
            "priest_helmet", "priest_chestplate", "priest_leggings", "priest_boots",
            "plagued_helmet", "plagued_chestplate", "plagued_leggings", "plagued_boots",
            "netherite_mage_helmet", "netherite_mage_chestplate", "netherite_mage_leggings", "netherite_mage_boots",
            "wizard_helmet", "wizard_hat", "wizard_chestplate", "wizard_leggings", "wizard_boots",
            "infernal_sorcerer_chestplate", "paladin_chestplate", "speed_boots", "tarnished_crown", "hither_thither_wand",
            "mana_ring", "silver_ring", "cooldown_ring", "cast_time_ring", "heavy_chain", "emerald_stoneplate_ring",
            "fireward_ring", "frostward_ring", "poisonward_ring", "conjurers_talisman", "greater_conjurers_talisman",
            "affinity_ring", "concentration_amulet", "amethyst_resonance_charm", "expulsion_ring", "visibility_ring",
            "teleportation_amulet", "betrayer_signet", "invisibility_ring"
    );

    private static final List<String> MATERIALS_ORDER = List.of(
            "common_ink", "uncommon_ink", "rare_ink", "epic_ink", "legendary_ink",
            "lesser_spell_slot_upgrade", "upgrade_orb", "fire_upgrade_orb", "ice_upgrade_orb", "lightning_upgrade_orb",
            "holy_upgrade_orb", "ender_upgrade_orb", "blood_upgrade_orb", "evocation_upgrade_orb", "nature_upgrade_orb",
            "mana_upgrade_orb", "cooldown_upgrade_orb", "protection_upgrade_orb",
            "lightning_bottle", "frozen_bone_shard", "blood_vial", "ice_venom_vial", "divine_pearl",
            "cloth", "hogskin", "bloody_vellum", "dragonskin", "arcane_essence", "ruined_book", "chained_book",
            "chronicle", "cinder_essence", "timeless_slurry", "mithril_ingot", "mithril_scrap", "raw_mithril",
            "weapon_parts", "mithril_weave", "divine_soulshard", "pyrium_ingot", "arcane_ingot", "shriving_stone",
            "eldritch_manuscript", "ancient_knowledge_fragment", "icy_fang", "ice_crystal", "frosted_helve", "energized_core",
            "ice_spider_furled_map", "citadel_furled_map",
            "decrepit_key", "cinderous_soulcaller",
            "blank_rune", "fire_rune", "ice_rune", "lightning_rune", "ender_rune", "holy_rune", "blood_rune",
            "evocation_rune", "mana_rune", "cooldown_rune", "protection_rune", "nature_rune",
            "oakskin_elixir", "greater_oakskin_elixir", "greater_healing_elixir", "invisibility_elixir",
            "greater_invisibility_elixir", "evasion_elixir", "greater_evasion_elixir", "fire_ale", "netherward_tincture",
            "music_disc_dead_king_lullaby", "music_disc_flame_still_burns", "disc_fragment_flame_still_burns", "music_disc_whispers_of_ice",
            "keeper_spawn_egg", "dead_king_corpse_spawn_egg", "archevoker_spawn_egg", "necromancer_spawn_egg",
            "cryomancer_spawn_egg", "pyromancer_spawn_egg", "priest_spawn_egg", "apothecarist_spawn_egg", "ice_spider_spawn_egg"
    );

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> SPELL_EQUIPMENT_TAB = CREATIVE_MODE_TABS.register("spell_equipment_tab",
            () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                    .icon(() -> new ItemStack(ItemRegistry.IRON_SPELL_BOOK.get()))
                    .title(Component.translatable("itemGroup.irons_spellbooks.spell_equipment_tab"))
                    .displayItems((parameters, output) -> addOrderedItems(output, EQUIPMENT_ORDER))
                    .build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> SPELL_MATERIALS_TAB = CREATIVE_MODE_TABS.register("spell_materials_tab",
            () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 1)
                    .icon(() -> new ItemStack(ItemRegistry.DIVINE_PEARL.get()))
                    .title(Component.translatable("itemGroup.irons_spellbooks.spell_materials_tab"))
                    .displayItems((parameters, output) -> addOrderedItems(output, MATERIALS_ORDER))
                    .build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BLOCKS_TAB = CREATIVE_MODE_TABS.register("blocks_tab",
            () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 2)
                    .icon(() -> new ItemStack(BlockRegistry.INSCRIPTION_TABLE_BLOCK.get()))
                    .title(Component.translatable("itemGroup.irons_spellbooks.blocks_tab"))
                    .displayItems((parameters, output) -> BlockRegistry.blocks().forEach(holder -> {
                        var stack = new ItemStack(holder.get().asItem());
                        acceptSingle(output, stack);
                    }))
                    .build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> SPELLBOOK_SCROLLS_TAB = CREATIVE_MODE_TABS.register("spellbook_scrolls_tab",
            () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 3)
                    .icon(() -> new ItemStack(ItemRegistry.SCROLL.get()))
                    .title(Component.translatable("itemGroup.irons_spellbooks.spellbook_scrolls_tab"))
                    .displayItems((parameters, output) -> allScrollStates().forEach(stack -> {
                        acceptSingle(output, stack);
                    }))
                    .build());

    private CreativeTabRegistry() {
    }

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }

    private static void addOrderedItems(CreativeModeTab.Output output, List<String> orderedIds) {
        for (String path : orderedIds) {
            var id = ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, path);
            Item item = BuiltInRegistries.ITEM.get(id);
            if (item != null && item != ItemStack.EMPTY.getItem()) {
                acceptSingle(output, new ItemStack(item));
            }
        }
    }

    private static void acceptSingle(CreativeModeTab.Output output, ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return;
        }
        stack.setCount(1);
        output.accept(stack);
    }

    private static List<ItemStack> allScrollStates() {
        var stacks = new ArrayList<ItemStack>();
        SpellRegistry.getEnabledSpells().stream()
                .filter(spell -> spell != SpellRegistry.none())
                .forEach(spell -> {
                    for (int level = spell.getMinLevel(); level <= spell.getMaxLevel(); level++) {
                        var scrollStack = new ItemStack(ItemRegistry.SCROLL.get());
                        ISpellContainer.createScrollContainer(spell, level, scrollStack);
                        stacks.add(scrollStack);
                    }
                });
        return stacks;
    }
}
