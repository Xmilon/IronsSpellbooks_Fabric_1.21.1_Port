package io.redspace.ironsspellbooks.fluids;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.fluids.FluidStack;

public class SimpleTintedClientFluidType extends SimpleClientFluidType {
    final int color;

    public SimpleTintedClientFluidType(ResourceLocation texture, int color) {
        super(texture);
        this.color = color | 0xFF000000; // force full opacity
    }

    public int getTintColor() {
        return color;
    }

    @Override
    public int getTintColor(FluidStack stack) {
        return color;
    }

    @Override
    public int getTintColor(FluidState state, Level level, BlockPos pos) {
        return color;
    }
}
