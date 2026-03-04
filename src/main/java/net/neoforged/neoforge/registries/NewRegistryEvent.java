package net.neoforged.neoforge.registries;

import net.minecraft.core.Registry;
import net.neoforged.bus.api.Event;

public class NewRegistryEvent extends Event {
    public <T> Registry<T> register(Registry<T> registry) {
        return registry;
    }
}
