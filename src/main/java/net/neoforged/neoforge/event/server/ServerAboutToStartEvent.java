package net.neoforged.neoforge.event.server;

import net.minecraft.server.MinecraftServer;

public class ServerAboutToStartEvent extends net.neoforged.bus.api.Event {
    public MinecraftServer getServer() {
        return null;
    }
}
