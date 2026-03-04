package net.neoforged.neoforge.fluids;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.redspace.ironsspellbooks.fluids.PotionFluid;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class FluidStack {
    public static final FluidStack EMPTY = new FluidStack(Fluids.EMPTY, 0);
    public static final Codec<FluidStack> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.FLUID.byNameCodec().optionalFieldOf("id").forGetter(stack -> Optional.of(stack.getFluid())),
            BuiltInRegistries.FLUID.byNameCodec().optionalFieldOf("fluid").forGetter(stack -> Optional.empty()),
            Codec.INT.optionalFieldOf("amount", 1).forGetter(FluidStack::getAmount),
            PotionContents.CODEC.optionalFieldOf("potion_contents").forGetter(stack ->
                    Optional.ofNullable(stack.get(DataComponents.POTION_CONTENTS))),
            PotionFluid.BottleType.CODEC.optionalFieldOf("potion_bottle_type").forGetter(stack ->
                    Optional.ofNullable(stack.get(ComponentRegistry.POTION_BOTTLE_TYPE))),
            Codec.PASSTHROUGH.optionalFieldOf("components").forGetter(stack -> Optional.empty())
    ).apply(instance, (idFluid, legacyFluid, amount, potionContents, bottleType, components) -> {
        Fluid fluid = idFluid.orElseGet(() -> legacyFluid.orElse(Fluids.EMPTY));
        FluidStack stack = new FluidStack(fluid, amount);
        potionContents.ifPresent(value -> stack.set(DataComponents.POTION_CONTENTS, value));
        bottleType.ifPresent(value -> stack.set(ComponentRegistry.POTION_BOTTLE_TYPE, value));
        // Datagen recipes serialize fluid components under a "components" object.
        // Support both old flat fields and this map format.
        components.ifPresent(dynamic -> applyComponents(dynamic, stack));
        return stack;
    }));
    @SuppressWarnings("unchecked")
    public static final StreamCodec<RegistryFriendlyByteBuf, FluidStack> STREAM_CODEC =
            (StreamCodec<RegistryFriendlyByteBuf, FluidStack>) (Object) ByteBufCodecs.fromCodec(CODEC);

    private final Fluid fluid;
    private int amount;
    private DataComponentPatch components = DataComponentPatch.EMPTY;
    private final Map<DataComponentType<?>, Object> componentValues = new HashMap<>();

    public FluidStack(Fluid fluid, int amount) {
        this.fluid = fluid;
        this.amount = amount;
    }

    public FluidStack(DeferredHolder<Fluid, ? extends Fluid> fluid, int amount) {
        this(fluid.get(), amount);
    }

    public Fluid getFluid() { return fluid; }
    public net.minecraft.core.Holder<Fluid> getFluidHolder() { return fluid.builtInRegistryHolder(); }
    public int getAmount() { return amount; }
    public boolean isEmpty() { return amount <= 0 || fluid == Fluids.EMPTY; }
    public FluidStack copy() {
        var c = new FluidStack(fluid, amount);
        c.components = components;
        c.componentValues.putAll(this.componentValues);
        return c;
    }
    public FluidStack copyWithAmount(int amount) {
        var c = new FluidStack(fluid, amount);
        c.components = components;
        c.componentValues.putAll(this.componentValues);
        return c;
    }
    public void shrink(int value) { this.amount = Math.max(0, amount - value); }
    public void grow(int value) { this.amount += value; }
    @SuppressWarnings("unchecked")
    public <T> T get(DataComponentType<T> type) { return (T) componentValues.get(type); }

    @SuppressWarnings("unchecked")
    public <T> T get(Object key) {
        if (key instanceof DataComponentType<?> dataComponentType) {
            return (T) componentValues.get(dataComponentType);
        }
        return null;
    }

    public <T> void set(DataComponentType<T> type, T value) {
        if (value == null) {
            componentValues.remove(type);
        } else {
            componentValues.put(type, value);
        }
    }

    public <T> void set(Object key, T value) {
        if (key instanceof DataComponentType<?> dataComponentType) {
            @SuppressWarnings("unchecked")
            DataComponentType<T> typed = (DataComponentType<T>) dataComponentType;
            set(typed, value);
        }
    }

    public <T> T getOrDefault(DataComponentType<T> type, T defaultValue) {
        T value = get(type);
        return value != null ? value : defaultValue;
    }

    public boolean has(DataComponentType<?> type) { return componentValues.containsKey(type); }
    public void remove(DataComponentType<?> type) { componentValues.remove(type); }
    public boolean is(net.minecraft.tags.TagKey<Fluid> key) { return fluid.defaultFluidState().is(key); }
    public boolean is(net.minecraft.core.Holder<Fluid> other) { return fluid.isSame(other.value()); }
    public net.neoforged.neoforge.fluids.FluidType getFluidType() { return new net.neoforged.neoforge.fluids.FluidType(net.neoforged.neoforge.fluids.FluidType.Properties.create()); }
    public FluidStack(net.minecraft.core.Holder<Fluid> fluid, int amount) { this(fluid.value(), amount); }

    public CompoundTag save(net.minecraft.core.HolderLookup.Provider access) {
        CompoundTag tag = new CompoundTag();
        ResourceLocation fluidId = BuiltInRegistries.FLUID.getKey(fluid);
        tag.putString("fluid", fluidId.toString());
        tag.putInt("amount", amount);

        PotionContents potionContents = get(DataComponents.POTION_CONTENTS);
        if (potionContents != null) {
            PotionContents.CODEC.encodeStart(NbtOps.INSTANCE, potionContents).result().ifPresent(element -> tag.put("potion_contents", element));
        }
        PotionFluid.BottleType bottleType = get(ComponentRegistry.POTION_BOTTLE_TYPE);
        if (bottleType != null) {
            tag.putString("potion_bottle_type", bottleType.getSerializedName());
        }
        return tag;
    }

    public static FluidStack parseOptional(net.minecraft.core.HolderLookup.Provider access, CompoundTag tag) {
        if (tag == null || !tag.contains("fluid")) {
            return EMPTY;
        }
        Fluid fluid = BuiltInRegistries.FLUID.get(ResourceLocation.parse(tag.getString("fluid")));
        int amount = tag.getInt("amount");
        FluidStack stack = new FluidStack(fluid, amount);

        if (tag.contains("potion_contents")) {
            PotionContents.CODEC.parse(NbtOps.INSTANCE, tag.get("potion_contents")).result()
                    .ifPresent(value -> stack.set(DataComponents.POTION_CONTENTS, value));
        }
        if (tag.contains("potion_bottle_type")) {
            String raw = tag.getString("potion_bottle_type");
            for (PotionFluid.BottleType value : PotionFluid.BottleType.values()) {
                if (Objects.equals(value.getSerializedName(), raw)) {
                    stack.set(ComponentRegistry.POTION_BOTTLE_TYPE, value);
                    break;
                }
            }
        }
        return stack;
    }

    public static boolean isSameFluidSameComponents(FluidStack a, FluidStack b) {
        return a.getFluid().isSame(b.getFluid())
                && Objects.equals(a.get(DataComponents.POTION_CONTENTS), b.get(DataComponents.POTION_CONTENTS))
                && Objects.equals(a.get(ComponentRegistry.POTION_BOTTLE_TYPE), b.get(ComponentRegistry.POTION_BOTTLE_TYPE));
    }

    private static void applyComponents(Dynamic<?> dynamic, FluidStack stack) {
        dynamic.get("minecraft:potion_contents").result()
                .flatMap(value -> PotionContents.CODEC.parse(value).result())
                .ifPresent(value -> stack.set(DataComponents.POTION_CONTENTS, value));
        dynamic.get("irons_spellbooks:potion_bottle_type").result()
                .flatMap(value -> PotionFluid.BottleType.CODEC.parse(value).result())
                .ifPresent(value -> stack.set(ComponentRegistry.POTION_BOTTLE_TYPE, value));
    }
}


