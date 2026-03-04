package net.neoforged.neoforge.attachment;

public interface IAttachmentSerializer<T, V> {
    V read(IAttachmentHolder holder, T tag, net.minecraft.core.HolderLookup.Provider registries);
    T write(V value, net.minecraft.core.HolderLookup.Provider registries);
}