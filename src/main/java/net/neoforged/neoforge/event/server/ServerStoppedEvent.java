package net.neoforged.neoforge.event.server;

import net.minecraft.server.MinecraftServer;

public class ServerStoppedEvent extends net.neoforged.bus.api.Event {
    private final MinecraftServer server;

    public ServerStoppedEvent(MinecraftServer server) {
        this.server = server;
    }

    public MinecraftServer getServer() {
        return server;
    }
}
