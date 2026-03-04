package net.neoforged.neoforge.entity;

import net.minecraft.network.RegistryFriendlyByteBuf;

public interface IEntityWithComplexSpawn {
    default void writeSpawnData(RegistryFriendlyByteBuf buffer) {
    }

    default void readSpawnData(RegistryFriendlyByteBuf buffer) {
    }
}
