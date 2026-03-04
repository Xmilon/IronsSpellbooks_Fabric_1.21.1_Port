package net.neoforged.neoforge.client.extensions.common;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.fluids.PotionClientFluidType;
import io.redspace.ironsspellbooks.fluids.SimpleClientFluidType;
import io.redspace.ironsspellbooks.fluids.SimpleTintedClientFluidType;
import io.redspace.ironsspellbooks.registries.FluidRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.FluidStack;

public interface IClientFluidTypeExtensions {
    IClientFluidTypeExtensions DEFAULT = new IClientFluidTypeExtensions() {};

    static IClientFluidTypeExtensions of(Fluid fluid) {
        if (fluid == null) {
            return DEFAULT;
        }
        if (fluid.isSame(FluidRegistry.BLOOD.value())) {
            return new SimpleClientFluidType(IronsSpellbooks.id("block/blood"));
        }
        if (fluid.isSame(FluidRegistry.TIMELESS_SLURRY_FLUID.value())) {
            return new SimpleClientFluidType(IronsSpellbooks.id("block/timeless_slurry"));
        }
        if (fluid.isSame(FluidRegistry.POTION_FLUID.value())) {
            return new PotionClientFluidType(ResourceLocation.withDefaultNamespace("block/water_still"));
        }
        if (fluid.isSame(FluidRegistry.COMMON_INK.value())) {
            return new SimpleTintedClientFluidType(ResourceLocation.withDefaultNamespace("block/water_still"), 0xFF222222);
        }
        if (fluid.isSame(FluidRegistry.UNCOMMON_INK.value())) {
            return new SimpleTintedClientFluidType(ResourceLocation.withDefaultNamespace("block/water_still"), 0xFF124300);
        }
        if (fluid.isSame(FluidRegistry.RARE_INK.value())) {
            return new SimpleTintedClientFluidType(ResourceLocation.withDefaultNamespace("block/water_still"), 0xFF0F3844);
        }
        if (fluid.isSame(FluidRegistry.EPIC_INK.value())) {
            return new SimpleTintedClientFluidType(ResourceLocation.withDefaultNamespace("block/water_still"), 0xFFA52EA0);
        }
        if (fluid.isSame(FluidRegistry.LEGENDARY_INK.value())) {
            return new SimpleTintedClientFluidType(ResourceLocation.withDefaultNamespace("block/water_still"), 0xFFFCAC1C);
        }
        if (fluid.isSame(FluidRegistry.OAKSKIN_ELIXIR_FLUID.value())) {
            return new SimpleTintedClientFluidType(ResourceLocation.withDefaultNamespace("block/water_still"), 0xFFFFEF95);
        }
        if (fluid.isSame(FluidRegistry.GREATER_OAKSKIN_ELIXIR_FLUID.value())) {
            return new SimpleTintedClientFluidType(ResourceLocation.withDefaultNamespace("block/water_still"), 0xFFFFEF95);
        }
        if (fluid.isSame(FluidRegistry.EVASION_ELIXIR_FLUID.value())) {
            return new SimpleTintedClientFluidType(ResourceLocation.withDefaultNamespace("block/water_still"), 0xFFF17BF4);
        }
        if (fluid.isSame(FluidRegistry.GREATER_EVASION_ELIXIR_FLUID.value())) {
            return new SimpleTintedClientFluidType(ResourceLocation.withDefaultNamespace("block/water_still"), 0xFFF17BF4);
        }
        if (fluid.isSame(FluidRegistry.INVISIBILITY_ELIXIR_FLUID.value())) {
            return new SimpleTintedClientFluidType(ResourceLocation.withDefaultNamespace("block/water_still"), 0xFF7F83F2);
        }
        if (fluid.isSame(FluidRegistry.GREATER_INVISIBILITY_ELIXIR_FLUID.value())) {
            return new SimpleTintedClientFluidType(ResourceLocation.withDefaultNamespace("block/water_still"), 0xFF7F83F2);
        }
        if (fluid.isSame(FluidRegistry.GREATER_HEALING_ELIXIR_FLUID.value())) {
            return new SimpleTintedClientFluidType(ResourceLocation.withDefaultNamespace("block/water_still"), 0xFFF82423);
        }
        if (fluid.isSame(FluidRegistry.ICE_VENOM_FLUID.value())) {
            return new SimpleTintedClientFluidType(ResourceLocation.withDefaultNamespace("block/water_still"), 0x73BABA);
        }
        return DEFAULT;
    }

    default ResourceLocation getStillTexture() {
        return ResourceLocation.withDefaultNamespace("block/water_still");
    }

    default ResourceLocation getFlowingTexture() {
        return ResourceLocation.withDefaultNamespace("block/water_flow");
    }

    default ResourceLocation getOverlayTexture() {
        return null;
    }

    default ResourceLocation getStillTexture(FluidState state, Level level, BlockPos pos) {
        return getStillTexture();
    }

    default int getTintColor(FluidStack stack) {
        return 0xFFFFFF;
    }

    default int getTintColor(FluidState state, Level level, BlockPos pos) {
        return 0xFFFFFF;
    }
}


