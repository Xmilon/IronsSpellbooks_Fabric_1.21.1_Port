package io.redspace.ironsspellbooks.setup;

import io.redspace.ironsspellbooks.capabilities.magic.PocketDimensionManager;
import io.redspace.ironsspellbooks.capabilities.magic.SummonManager;
import io.redspace.ironsspellbooks.api.config.SpellConfigManager;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.compat.CompatHandler;
import io.redspace.ironsspellbooks.data.IronsDataStorage;
import io.redspace.ironsspellbooks.effect.guiding_bolt.GuidingBoltManager;
import io.redspace.ironsspellbooks.network.SyncManaPacket;
import io.redspace.ironsspellbooks.worldgen.IceSpiderPatrolSpawner;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameRules;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;


public class ModSetup {
    private static final IceSpiderPatrolSpawner ICE_SPIDER_PATROL_SPAWNER = new IceSpiderPatrolSpawner();

    public static void setup() {
        Messages.register();
        CompatHandler.init();

        // Drive server-level magic and pocket-dimension ticking on Fabric.
        ServerTickEvents.START_WORLD_TICK.register(world -> {
            if (world.isClientSide) {
                return;
            }
            io.redspace.ironsspellbooks.IronsSpellbooks.MAGIC_MANAGER.tick(world);
            PocketDimensionManager.INSTANCE.tick(world);
        });

        // Drive summon expiration logic that previously depended on NeoForge tick events.
        ServerTickEvents.END_SERVER_TICK.register(server -> SummonManager.levelTick(new ServerTickEvent.Post(server)));

        // Restore custom patrol spawner behavior.
        ServerTickEvents.END_WORLD_TICK.register(world -> {
            if (world.isClientSide) {
                return;
            }
            boolean doMobSpawning = world.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING);
            ICE_SPIDER_PATROL_SPAWNER.tick(world, doMobSpawning, doMobSpawning);
            GuidingBoltManager.serverTick(new LevelTickEvent.Post(world));
        });

        // Mirror entity join hooks used by summon/guiding-bolt managers.
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            EntityJoinLevelEvent event = new EntityJoinLevelEvent(entity, world);
            SummonManager.onSummonerLogin(event);
            GuidingBoltManager.onProjectileShot(event);
        });

        // Persist summon ownership when players disconnect.
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            if (handler.getPlayer() instanceof ServerPlayer serverPlayer) {
                SummonManager.onPlayerLogout(new PlayerEvent.PlayerLoggedOutEvent(serverPlayer));
            }
        });

        // Sync player-side spell/mana state and spell config when a player joins.
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            if (handler.getPlayer() instanceof ServerPlayer serverPlayer) {
                var magicData = MagicData.getPlayerMagicData(serverPlayer);
                magicData.getSyncedData().syncToPlayer(serverPlayer);
                magicData.getPlayerCooldowns().syncToPlayer(serverPlayer);
                magicData.getPlayerRecasts().syncAllToPlayer();
                PacketDistributor.sendToPlayer(serverPlayer, new SyncManaPacket(magicData));
                SpellConfigManager.onDatapackSync(new OnDatapackSyncEvent(server.getPlayerList(), serverPlayer));
            }
        });

        // Persist summons on server stop.
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> SummonManager.onServerStopping(new ServerStoppingEvent(server)));

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            io.redspace.ironsspellbooks.IronsSpellbooks.MCS = server;
            io.redspace.ironsspellbooks.IronsSpellbooks.OVERWORLD = server.overworld();
            IronsDataStorage.init(server.overworld().getDataStorage());
            SpellConfigManager.onDatapackSync(new OnDatapackSyncEvent(server.getPlayerList(), null));
        });

        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            io.redspace.ironsspellbooks.IronsSpellbooks.MCS = null;
            io.redspace.ironsspellbooks.IronsSpellbooks.OVERWORLD = null;
        });

    }
}
