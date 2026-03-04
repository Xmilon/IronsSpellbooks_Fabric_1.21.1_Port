package net.neoforged.neoforge.fluids;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

public abstract class BaseFlowingFluid extends FlowingFluid {
    protected final Properties properties;

    protected BaseFlowingFluid(Properties properties) {
        this.properties = properties;
    }

    @Override
    public Fluid getFlowing() { return this; }

    @Override
    public Fluid getSource() { return this; }

    @Override
    protected boolean canConvertToSource(Level level) { return false; }

    @Override
    protected void beforeDestroyingBlock(LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState) {
    }

    @Override
    protected int getSlopeFindDistance(LevelReader levelReader) { return 1; }

    @Override
    protected int getDropOff(LevelReader levelReader) { return 1; }

    @Override
    public int getTickDelay(LevelReader levelReader) { return 5; }

    @Override
    protected float getExplosionResistance() { return 100.0F; }

    @Override
    protected boolean canBeReplacedWith(FluidState fluidState, BlockGetter blockGetter, BlockPos blockPos, Fluid fluid, net.minecraft.core.Direction direction) {
        return false;
    }

    @Override
    protected BlockState createLegacyBlock(FluidState fluidState) { return net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(); }

    @Override
    public boolean isSource(FluidState fluidState) { return true; }

    @Override
    public int getAmount(FluidState fluidState) { return 8; }

    public static class Properties {
        public Properties(Object... ignored) {
        }

        public Properties(java.util.function.Supplier<net.neoforged.neoforge.fluids.FluidType> type, java.util.function.Supplier<? extends Fluid> still, java.util.function.Supplier<? extends Fluid> flowing) {
        }

        public Properties bucket(java.util.function.Supplier<net.minecraft.world.item.Item> bucketItem) {
            return this;
        }
    }
}


