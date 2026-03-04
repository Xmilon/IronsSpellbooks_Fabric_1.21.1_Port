package net.neoforged.neoforge.registries;

import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class RegistryBuilder<T> {
    private final ResourceKey<Registry<T>> key;

    public RegistryBuilder(ResourceKey<Registry<T>> key) {
        this.key = key;
    }

    public Registry<T> create() {
        return new MappedRegistry<>(key, com.mojang.serialization.Lifecycle.stable(), false);
    }
}
