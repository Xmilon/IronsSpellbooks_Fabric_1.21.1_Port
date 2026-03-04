package net.neoforged.neoforge.event.entity.player;

import net.minecraft.world.entity.player.Player;

public class PlayerContainerEvent extends PlayerEvent {
    public PlayerContainerEvent(Player player) {
        super(player);
    }

    public static class Open extends PlayerContainerEvent {
        public Open(Player player) {
            super(player);
        }
    }
}
