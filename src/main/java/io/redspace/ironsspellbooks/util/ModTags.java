package io.redspace.ironsspellbooks.util;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.material.Fluid;

public class ModTags {
    public static final TagKey<Item> SCHOOL_FOCUS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, "school_focus"));
    public static final TagKey<Item> FIRE_FOCUS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, "fire_focus"));
    public static final TagKey<Item> ICE_FOCUS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, "ice_focus"));
    public static final TagKey<Item> LIGHTNING_FOCUS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, "lightning_focus"));
    public static final TagKey<Item> ENDER_FOCUS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, "ender_focus"));
    public static final TagKey<Item> HOLY_FOCUS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, "holy_focus"));
    public static final TagKey<Item> BLOOD_FOCUS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, "blood_focus"));
    public static final TagKey<Item> EVOCATION_FOCUS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, "evocation_focus"));
    public static final TagKey<Item> ELDRITCH_FOCUS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, "eldritch_focus"));
    public static final TagKey<Item> NATURE_FOCUS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, "nature_focus"));
    public static final TagKey<Item> INSCRIBED_RUNES = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, "inscribed_rune"));
    public static final TagKey<Item> MITHRIL_INGOT = TagKey.create(Registries.ITEM, ResourceLocation.parse("c:ingots/mithril"));
    public static final TagKey<Item> CAN_BE_UPGRADED = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, "upgrade_whitelist"));
    public static final TagKey<Item> CAN_BE_IMBUED = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, "imbue_whitelist"));
    public static final TagKey<Item> BASE_WIZARD_HELMET = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, "wizard_base_helmet"));
    public static final TagKey<Item> BASE_WIZARD_CHESTPLATE = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, "wizard_base_chestplate"));
    public static final TagKey<Item> BASE_WIZARD_LEGGINGS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, "wizard_base_leggings"));
    public static final TagKey<Item> BASE_WIZARD_BOOTS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, "wizard_base_boots"));
    public static final TagKey<Item> SPELLBOOK_CURIO = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, "is_spellbook"));
    public static final TagKey<Block> SPECTRAL_HAMMER_MINEABLE = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, "spectral_hammer_mineable"));
    public static final TagKey<Block> GUARDED_BY_WIZARDS = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, "guarded_by_wizards"));
    public static final TagKey<Block> PREVENT_POCKET_DIMENSION_PLACEMENT = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, "pocket_dimension_prevent_placement"));

    public static final TagKey<MobEffect> CLEANSE_IMMUNE = TagKey.create(Registries.MOB_EFFECT, ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, "cleanse_immune"));
    public static final TagKey<MobEffect> AFFECTED_BY_SPIDER_ASPECT = TagKey.create(Registries.MOB_EFFECT, ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, "affected_by_spider_aspect"));

    public static final TagKey<Structure> WAYWARD_COMPASS_LOCATOR = TagKey.create(Registries.STRUCTURE, ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, "wayward_compass_locator"));

    public static final TagKey<EntityType<?>> ALWAYS_HEAL = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, "always_heal"));
    public static final TagKey<EntityType<?>> CANT_ROOT = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, "cant_root"));
    public static final TagKey<EntityType<?>> VILLAGE_ALLIES = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, "village_allies"));
    public static final TagKey<EntityType<?>> CANT_USE_PORTAL = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, "cant_use_portal"));
    public static final TagKey<EntityType<?>> INFERNAL_ALLIES = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, "infernal_allies"));
    public static final TagKey<EntityType<?>> GUIDING_BOLT_IMMUNE = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, "guiding_bolt_immune"));
    public static final TagKey<EntityType<?>> CANT_PRODUCE_BLOOD = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, "cant_produce_blood"));
    public static final TagKey<EntityType<?>> CANT_PARRY = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, "cant_parry"));

    public static final TagKey<Biome> ICE_SPIDER_PATROLS = TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, "ice_spider_patrols"));

    public static final TagKey<Fluid> CAULDRON_FLUID_DISALLOW = TagKey.create(Registries.FLUID, ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, "alchemist_cauldron_disallow"));

    private static TagKey<DamageType> create(String tag) {
        return TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, tag));
    }
}


