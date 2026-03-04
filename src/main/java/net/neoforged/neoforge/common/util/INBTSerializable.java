package net.neoforged.neoforge.common.util;

public interface INBTSerializable<T> {
    T serializeNBT(net.minecraft.core.HolderLookup.Provider registries);
    void deserializeNBT(net.minecraft.core.HolderLookup.Provider registries, T nbt);
}