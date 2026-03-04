package net.neoforged.neoforge.event.entity.player;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.portal.DimensionTransition;

public class PlayerRespawnPositionEvent extends net.neoforged.bus.api.Event {
    private final DimensionTransition dimensionTransition;

    public PlayerRespawnPositionEvent(ServerPlayer player, DimensionTransition dimensionTransition, boolean forced) {
        this.dimensionTransition = dimensionTransition;
    }

    public DimensionTransition getDimensionTransition() {
        return dimensionTransition;
    }
}
