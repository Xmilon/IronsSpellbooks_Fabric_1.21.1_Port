package net.neoforged.neoforge.registries;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class DeferredHolder<R, T extends R> implements Supplier<T>, Holder<R> {
    private final ResourceLocation id;
    private final ResourceKey<? extends Registry<R>> registryKey;
    private ResourceKey<R> key;
    private final Supplier<T> supplier;
    private T value;

    public DeferredHolder(ResourceKey<? extends Registry<R>> registryKey, ResourceLocation id, Supplier<T> supplier) {
        this.registryKey = registryKey;
        this.id = id;
        this.supplier = supplier;
    }

    public static <R, T extends R> DeferredHolder<R, T> create(ResourceKey<? extends net.minecraft.core.Registry<R>> registryKey, ResourceLocation id) {
        DeferredHolder<R, T> holder = new DeferredHolder<>(registryKey, id, () -> null);
        holder.key = ResourceKey.create(registryKey, id);
        return holder;
    }

    public String getId() {
        return id.getPath();
    }

    public ResourceLocation getLocation() {
        return id;
    }

    public void bindKey(ResourceKey<R> key) {
        this.key = key;
    }

    @SuppressWarnings("unchecked")
    private Optional<Registry<R>> resolveRegistry() {
        Registry<? extends Registry<?>> root = BuiltInRegistries.REGISTRY;
        Registry<R> registry = (Registry<R>) root.get(registryKey.location());
        return Optional.ofNullable(registry);
    }

    private Optional<Holder.Reference<R>> resolveReference() {
        if (key == null) {
            return Optional.empty();
        }
        return resolveRegistry().flatMap(registry -> registry.getHolder(key).map(holder -> (Holder.Reference<R>) holder));
    }

    private Optional<R> resolveRegisteredValue() {
        return resolveRegistry().flatMap(registry -> registry.containsKey(id) ? Optional.ofNullable(registry.get(id)) : Optional.empty());
    }

    @Override
    public T get() {
        if (value == null) {
            value = (T) resolveRegisteredValue().orElseGet(supplier);
        }
        return value;
    }

    public boolean isPresent() {
        return true;
    }

    @Override
    public R value() {
        return resolveReference().map(Holder::value).orElseGet(this::get);
    }

    @Override
    public boolean isBound() {
        return resolveReference().isPresent() || get() != null;
    }

    @Override
    public boolean is(ResourceLocation location) {
        return id.equals(location) || (key != null && key.location().equals(location));
    }

    @Override
    public boolean is(ResourceKey<R> resourceKey) {
        return key != null && key.equals(resourceKey);
    }

    @Override
    public boolean is(Predicate<ResourceKey<R>> predicate) {
        return unwrapKey().filter(predicate).isPresent();
    }

    @Override
    public boolean is(TagKey<R> tagKey) {
        return resolveReference().map(holder -> holder.is(tagKey)).orElse(false);
    }

    @Override
    public boolean is(Holder<R> holder) {
        if (holder == this) {
            return true;
        }
        return this.unwrapKey().isPresent() && this.unwrapKey().equals(holder.unwrapKey());
    }

    @Override
    public Stream<TagKey<R>> tags() {
        return resolveReference().map(Holder.Reference::tags).orElseGet(Stream::empty);
    }

    @Override
    public Either<ResourceKey<R>, R> unwrap() {
        return resolveReference().map(Holder::unwrap).orElseGet(() -> key != null ? Either.left(key) : Either.right(get()));
    }

    @Override
    public Optional<ResourceKey<R>> unwrapKey() {
        return resolveReference().flatMap(Holder::unwrapKey).or(() -> Optional.ofNullable(key));
    }

    @Override
    public Kind kind() {
        return resolveReference().map(Holder::kind).orElse(key == null ? Kind.DIRECT : Kind.REFERENCE);
    }

    @Override
    public boolean canSerializeIn(HolderOwner<R> owner) {
        return resolveReference().map(holder -> holder.canSerializeIn(owner)).orElse(key != null);
    }
}
