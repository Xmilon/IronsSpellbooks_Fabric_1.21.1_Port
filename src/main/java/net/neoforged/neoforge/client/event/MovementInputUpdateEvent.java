package net.neoforged.neoforge.client.event;

import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.bus.api.Event;

public class MovementInputUpdateEvent extends Event {
    private final LocalPlayer entity;
    private final Input input;

    public MovementInputUpdateEvent(LocalPlayer entity, Input input) {
        this.entity = entity;
        this.input = input;
    }

    public LocalPlayer getEntity() {
        return entity;
    }

    public Input getInput() {
        return input;
    }
}
