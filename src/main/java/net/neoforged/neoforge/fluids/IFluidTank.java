package net.neoforged.neoforge.fluids;

public interface IFluidTank {
    FluidStack getFluid();
    int getCapacity();
    default int getFluidAmount() { return getFluid().getAmount(); }
    default boolean isFluidValid(FluidStack stack) { return true; }
    default int fill(FluidStack stack, net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction action) { return 0; }
    default FluidStack drain(FluidStack resource, net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction action) { return FluidStack.EMPTY; }
    default FluidStack drain(int maxDrain, net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction action) { return FluidStack.EMPTY; }
}
