package net.neoforged.neoforge.network;

import io.redspace.ironsspellbooks.setup.Messages;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public class PacketDistributor {
    public static void sendToServer(Object payload) {
        Messages.sendToServer(payload);
    }

    public static void sendToPlayer(ServerPlayer player, Object payload) {
        Messages.sendToPlayer(payload, player);
    }

    public static void sendToAllPlayers(Object payload) {
        Messages.sendToAll(payload);
    }

    public static void sendToPlayersTrackingEntity(Entity entity, Object payload) {
        Messages.sendToTrackingEntity(payload, entity);
    }

    public static void sendToPlayersTrackingEntityAndSelf(Entity entity, Object payload) {
        Messages.sendToTrackingEntityAndSelf(payload, entity);
    }
}


