package net.neoforged.neoforge.client.event;

import net.minecraft.client.player.LocalPlayer;
import net.neoforged.bus.api.Event;

public class ClientPlayerNetworkEvent extends Event {
    public static class LoggingOut extends ClientPlayerNetworkEvent {
        private final LocalPlayer player;

        public LoggingOut(LocalPlayer player) {
            this.player = player;
        }

        public LocalPlayer getPlayer() {
            return player;
        }
    }
}
