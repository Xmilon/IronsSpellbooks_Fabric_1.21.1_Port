package net.neoforged.neoforge.registries;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.fluids.FluidType;

public class NeoForgeRegistries {
    @SuppressWarnings("unchecked")
    public static final ResourceKey<Registry<FluidType>> FLUID_TYPES =
            (ResourceKey<Registry<FluidType>>) (ResourceKey<?>) ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath("neoforge", "fluid_type"));

    public static class Keys {
        @SuppressWarnings("unchecked")
        public static final ResourceKey<Registry<AttachmentType<?>>> ATTACHMENT_TYPES =
                (ResourceKey<Registry<AttachmentType<?>>>) (ResourceKey<?>) ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath("neoforge", "attachment_type"));
        @SuppressWarnings("unchecked")
        public static final ResourceKey<Registry<BiomeModifier>> BIOME_MODIFIERS =
                (ResourceKey<Registry<BiomeModifier>>) (ResourceKey<?>) ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath("neoforge", "biome_modifier"));
        @SuppressWarnings("unchecked")
        public static final ResourceKey<Registry<com.mojang.serialization.MapCodec<? extends net.neoforged.neoforge.common.loot.IGlobalLootModifier>>> GLOBAL_LOOT_MODIFIER_SERIALIZERS =
                (ResourceKey<Registry<com.mojang.serialization.MapCodec<? extends net.neoforged.neoforge.common.loot.IGlobalLootModifier>>>) (ResourceKey<?>) ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath("neoforge", "global_loot_modifier_serializers"));
    }
}
