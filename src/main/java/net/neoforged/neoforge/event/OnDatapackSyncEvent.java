package net.neoforged.neoforge.event;

import net.minecraft.server.players.PlayerList;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.Event;

public class OnDatapackSyncEvent extends Event {
    private final PlayerList playerList;
    private final ServerPlayer player;

    public OnDatapackSyncEvent(PlayerList playerList, ServerPlayer player) {
        this.playerList = playerList;
        this.player = player;
    }

    public PlayerList getPlayerList() { return playerList; }
    public ServerPlayer getPlayer() { return player; }
}

