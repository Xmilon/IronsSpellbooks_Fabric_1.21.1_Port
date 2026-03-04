package net.neoforged.neoforge.registries;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings({"rawtypes", "unchecked"})
public class DeferredRegister<T> {
    private final ResourceKey<? extends Registry<T>> registryKey;
    private final String modId;
    private final List<DeferredHolder<T, ? extends T>> entries = new ArrayList<>();

    private DeferredRegister(ResourceKey<? extends Registry<T>> registryKey, String modId) {
        this.registryKey = registryKey;
        this.modId = modId;
    }

    public static <T> DeferredRegister<T> create(ResourceKey<? extends Registry<T>> registryKey, String modId) {
        return new DeferredRegister<>(registryKey, modId);
    }

    public static DeferredRegister<net.minecraft.world.item.Item> createItems(String modId) {
        return new DeferredRegister<>(net.minecraft.core.registries.Registries.ITEM, modId);
    }

    public <I extends T> DeferredHolder<T, I> register(String path, Supplier<I> supplier) {
        DeferredHolder<T, I> holder = new DeferredHolder<>(registryKey, ResourceLocation.fromNamespaceAndPath(modId, path), supplier);
        entries.add(holder);
        return holder;
    }

    public Collection<DeferredHolder<T, ? extends T>> getEntries() {
        return entries;
    }

    public ResourceKey<? extends Registry<T>> getRegistryKey() {
        return registryKey;
    }

    public void register(IEventBus eventBus) {
        Registry<T> registry = resolveRegistry();
        for (DeferredHolder<T, ? extends T> entry : entries) {
            T value = entry.get();
            if (registry != null) {
                ResourceLocation id = entry.getLocation();
                if (!registry.containsKey(id)) {
                    Registry.register(registry, id, value);
                }
                entry.bindKey(ResourceKey.create(registryKey, id));
            }
        }
    }

    @SuppressWarnings("unchecked")
    private Registry<T> resolveRegistry() {
        Registry<? extends Registry<?>> root = BuiltInRegistries.REGISTRY;
        return (Registry<T>) root.get(registryKey.location());
    }
}
