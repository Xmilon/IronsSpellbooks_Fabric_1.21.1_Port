package io.redspace.ironsspellbooks.setup;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.config.ClientConfigs;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.entity.mobs.SummonedHorse;
import io.redspace.ironsspellbooks.entity.mobs.SummonedSkeleton;
import io.redspace.ironsspellbooks.entity.mobs.SummonedVex;
import io.redspace.ironsspellbooks.entity.mobs.SummonedZombie;
import io.redspace.ironsspellbooks.entity.mobs.dead_king_boss.DeadKingBoss;
import io.redspace.ironsspellbooks.entity.mobs.debug_wizard.DebugWizard;
import io.redspace.ironsspellbooks.entity.mobs.frozen_humanoid.FrozenHumanoid;
import io.redspace.ironsspellbooks.entity.mobs.ice_spider.IceSpiderEntity;
import io.redspace.ironsspellbooks.entity.mobs.keeper.KeeperEntity;
import io.redspace.ironsspellbooks.entity.mobs.necromancer.NecromancerEntity;
import io.redspace.ironsspellbooks.entity.mobs.wizards.alchemist.ApothecaristEntity;
import io.redspace.ironsspellbooks.entity.mobs.wizards.archevoker.ArchevokerEntity;
import io.redspace.ironsspellbooks.entity.mobs.wizards.cryomancer.CryomancerEntity;
import io.redspace.ironsspellbooks.entity.mobs.wizards.cultist.CultistEntity;
import io.redspace.ironsspellbooks.entity.mobs.wizards.cursed_armor_stand.CursedArmorStandEntity;
import io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss.FireBossEntity;
import io.redspace.ironsspellbooks.entity.mobs.wizards.priest.PriestEntity;
import io.redspace.ironsspellbooks.entity.mobs.wizards.pyromancer.PyromancerEntity;
import io.redspace.ironsspellbooks.entity.spells.root.RootEntity;
import io.redspace.ironsspellbooks.entity.spells.spectral_hammer.SpectralHammer;
import io.redspace.ironsspellbooks.entity.spells.summoned_weapons.SummonedClaymoreEntity;
import io.redspace.ironsspellbooks.entity.spells.summoned_weapons.SummonedRapierEntity;
import io.redspace.ironsspellbooks.entity.spells.summoned_weapons.SummonedSwordEntity;
import io.redspace.ironsspellbooks.entity.spells.void_tentacle.VoidTentacle;
import io.redspace.ironsspellbooks.entity.spells.wisp.WispEntity;
import io.redspace.ironsspellbooks.registries.BlockRegistry;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.FeatureRegistry;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

@EventBusSubscriber(modid = IronsSpellbooks.MODID, bus = EventBusSubscriber.Bus.MOD)
public class CommonSetup {
    public static void bootstrapFabric() {
        registerPlayerAttributesFabricFallback();

        FabricDefaultAttributeRegistry.register(EntityRegistry.DEBUG_WIZARD.get(), DebugWizard.prepareAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.PYROMANCER.get(), PyromancerEntity.prepareAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.NECROMANCER.get(), NecromancerEntity.prepareAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.SPECTRAL_STEED.get(), SummonedHorse.prepareAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.WISP.get(), WispEntity.prepareAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.SPECTRAL_HAMMER.get(), SpectralHammer.prepareAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.SUMMONED_VEX.get(), SummonedVex.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.SUMMONED_ZOMBIE.get(), SummonedZombie.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.SUMMONED_SKELETON.get(), SummonedSkeleton.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.FROZEN_HUMANOID.get(), FrozenHumanoid.prepareAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.SUMMONED_POLAR_BEAR.get(), PolarBear.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.DEAD_KING.get(), DeadKingBoss.prepareAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.DEAD_KING_CORPSE.get(), DeadKingBoss.prepareAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.CATACOMBS_ZOMBIE.get(), Zombie.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.MAGEHUNTER_VINDICATOR.get(), Vindicator.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.ARCHEVOKER.get(), ArchevokerEntity.prepareAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.PRIEST.get(), PriestEntity.prepareAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.KEEPER.get(), KeeperEntity.prepareAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.SCULK_TENTACLE.get(), VoidTentacle.createLivingAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.CRYOMANCER.get(), CryomancerEntity.prepareAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.ROOT.get(), RootEntity.createLivingAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.FIREFLY_SWARM.get(), WispEntity.prepareAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.APOTHECARIST.get(), ApothecaristEntity.prepareAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.CULTIST.get(), CultistEntity.prepareAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.FIRE_BOSS.get(), FireBossEntity.prepareAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.CURSED_ARMOR_STAND.get(), CursedArmorStandEntity.prepareAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.SUMMONED_SWORD.get(), SummonedSwordEntity.prepareAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.SUMMONED_CLAYMORE.get(), SummonedClaymoreEntity.prepareAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.SUMMONED_RAPIER.get(), SummonedRapierEntity.prepareAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.ICE_SPIDER.get(), IceSpiderEntity.prepareAttributes());

        SpawnPlacements.register(
                EntityRegistry.NECROMANCER.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, spawnType, pos, random) -> Utils.checkMonsterSpawnRules(level, spawnType, pos, random)
        );

        // Port of data/irons_spellbooks/neoforge/biome_modifier/necromancer_spawns.json.
        BiomeModifications.addSpawn(
                BiomeSelectors.foundInOverworld(),
                MobCategory.MONSTER,
                EntityRegistry.NECROMANCER.get(),
                18,
                1,
                1
        );

        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(),
                GenerationStep.Decoration.UNDERGROUND_ORES,
                FeatureRegistry.MITHRIL_ORE_PLACEMENT
        );
    }

    private static void registerPlayerAttributesFabricFallback() {
        // PlayerMixin should already inject these into Player#createAttributes. This fallback keeps Fabric builds
        // resilient in case that mixin fails to apply for any reason.
        AttributeSupplier.Builder playerAttributes = Player.createAttributes()
                .add(AttributeRegistry.MAX_MANA)
                .add(AttributeRegistry.MANA_REGEN)
                .add(AttributeRegistry.COOLDOWN_REDUCTION)
                .add(AttributeRegistry.SPELL_POWER)
                .add(AttributeRegistry.SPELL_RESIST)
                .add(AttributeRegistry.CAST_TIME_REDUCTION)
                .add(AttributeRegistry.SUMMON_DAMAGE)
                .add(AttributeRegistry.CASTING_MOVESPEED)
                .add(AttributeRegistry.FIRE_MAGIC_RESIST)
                .add(AttributeRegistry.ICE_MAGIC_RESIST)
                .add(AttributeRegistry.LIGHTNING_MAGIC_RESIST)
                .add(AttributeRegistry.HOLY_MAGIC_RESIST)
                .add(AttributeRegistry.ENDER_MAGIC_RESIST)
                .add(AttributeRegistry.BLOOD_MAGIC_RESIST)
                .add(AttributeRegistry.EVOCATION_MAGIC_RESIST)
                .add(AttributeRegistry.NATURE_MAGIC_RESIST)
                .add(AttributeRegistry.ELDRITCH_MAGIC_RESIST)
                .add(AttributeRegistry.FIRE_SPELL_POWER)
                .add(AttributeRegistry.ICE_SPELL_POWER)
                .add(AttributeRegistry.LIGHTNING_SPELL_POWER)
                .add(AttributeRegistry.HOLY_SPELL_POWER)
                .add(AttributeRegistry.ENDER_SPELL_POWER)
                .add(AttributeRegistry.BLOOD_SPELL_POWER)
                .add(AttributeRegistry.EVOCATION_SPELL_POWER)
                .add(AttributeRegistry.NATURE_SPELL_POWER)
                .add(AttributeRegistry.ELDRITCH_SPELL_POWER);
        try {
            FabricDefaultAttributeRegistry.register(net.minecraft.world.entity.EntityType.PLAYER, playerAttributes);
        } catch (IllegalStateException ignored) {
            // Already registered by another path (for example successful PlayerMixin injection).
        }
    }

    @SubscribeEvent
    public static void onModConfigLoadingEvent(ModConfigEvent.Loading event) {
        //IronsSpellbooks.LOGGER.debug("onModConfigLoadingEvent");
        if (event.getConfig().getType() == ModConfig.Type.SERVER) {
            SpellRegistry.onConfigReload();
            ServerConfigs.onConfigReload();
        } else if (event.getConfig().getType() == ModConfig.Type.CLIENT) {
            ClientConfigs.onConfigReload();
        }
    }

    @SubscribeEvent
    public static void onModConfigReloadingEvent(ModConfigEvent.Reloading event) {
        //IronsSpellbooks.LOGGER.debug("onModConfigReloadingEvent");
        if (event.getConfig().getType() == ModConfig.Type.SERVER) {
            SpellRegistry.onConfigReload();
            ServerConfigs.onConfigReload();
        } else if (event.getConfig().getType() == ModConfig.Type.CLIENT) {
            ClientConfigs.onConfigReload();
        }
    }

    @SubscribeEvent
    public static void registerCapabilitiesEvent(RegisterCapabilitiesEvent event) {
        // NeoForge block capability wiring is not used in this Fabric-targeted port shim.
    }

    @SubscribeEvent
    public static void onAttributeCreate(EntityAttributeCreationEvent event) {
        event.put(EntityRegistry.DEBUG_WIZARD.get(), DebugWizard.prepareAttributes().build());
        event.put(EntityRegistry.PYROMANCER.get(), PyromancerEntity.prepareAttributes().build());
        event.put(EntityRegistry.NECROMANCER.get(), NecromancerEntity.prepareAttributes().build());
        event.put(EntityRegistry.SPECTRAL_STEED.get(), SummonedHorse.prepareAttributes().build());
        event.put(EntityRegistry.WISP.get(), WispEntity.prepareAttributes().build());
        event.put(EntityRegistry.SPECTRAL_HAMMER.get(), SpectralHammer.prepareAttributes().build());
        event.put(EntityRegistry.SUMMONED_VEX.get(), SummonedVex.createAttributes().build());
        event.put(EntityRegistry.SUMMONED_ZOMBIE.get(), SummonedZombie.createAttributes().build());
        event.put(EntityRegistry.SUMMONED_SKELETON.get(), SummonedSkeleton.createAttributes().build());
        event.put(EntityRegistry.FROZEN_HUMANOID.get(), FrozenHumanoid.prepareAttributes().build());
        event.put(EntityRegistry.SUMMONED_POLAR_BEAR.get(), PolarBear.createAttributes().build());
        event.put(EntityRegistry.DEAD_KING.get(), DeadKingBoss.prepareAttributes().build());
        event.put(EntityRegistry.DEAD_KING_CORPSE.get(), DeadKingBoss.prepareAttributes().build());
        event.put(EntityRegistry.CATACOMBS_ZOMBIE.get(), Zombie.createAttributes().build());
        event.put(EntityRegistry.MAGEHUNTER_VINDICATOR.get(), Vindicator.createAttributes().build());
        event.put(EntityRegistry.ARCHEVOKER.get(), ArchevokerEntity.prepareAttributes().build());
        event.put(EntityRegistry.PRIEST.get(), PriestEntity.prepareAttributes().build());
        event.put(EntityRegistry.KEEPER.get(), KeeperEntity.prepareAttributes().build());
        event.put(EntityRegistry.SCULK_TENTACLE.get(), VoidTentacle.createLivingAttributes().build());
        event.put(EntityRegistry.CRYOMANCER.get(), CryomancerEntity.prepareAttributes().build());
        event.put(EntityRegistry.ROOT.get(), RootEntity.createLivingAttributes().build());
        event.put(EntityRegistry.FIREFLY_SWARM.get(), WispEntity.prepareAttributes().build());
        event.put(EntityRegistry.APOTHECARIST.get(), ApothecaristEntity.prepareAttributes().build());
        event.put(EntityRegistry.CULTIST.get(), CultistEntity.prepareAttributes().build());
        event.put(EntityRegistry.FIRE_BOSS.get(), FireBossEntity.prepareAttributes().build());
        event.put(EntityRegistry.CURSED_ARMOR_STAND.get(), CursedArmorStandEntity.prepareAttributes().build());
        event.put(EntityRegistry.SUMMONED_SWORD.get(), SummonedSwordEntity.prepareAttributes().build());
        event.put(EntityRegistry.SUMMONED_CLAYMORE.get(), SummonedClaymoreEntity.prepareAttributes().build());
        event.put(EntityRegistry.SUMMONED_RAPIER.get(), SummonedRapierEntity.prepareAttributes().build());
        event.put(EntityRegistry.ICE_SPIDER.get(), IceSpiderEntity.prepareAttributes().build());
    }

    @SubscribeEvent
    public static void spawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(EntityRegistry.NECROMANCER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (type, serverLevelAccessor, spawnType, blockPos, random) -> Utils.checkMonsterSpawnRules(serverLevelAccessor, spawnType, blockPos, random), RegisterSpawnPlacementsEvent.Operation.OR);
    }


}


