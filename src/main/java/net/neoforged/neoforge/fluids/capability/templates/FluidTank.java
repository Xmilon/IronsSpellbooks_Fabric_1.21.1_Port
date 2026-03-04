package net.neoforged.neoforge.fluids.capability.templates;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.IFluidTank;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.function.Predicate;

public class FluidTank implements IFluidTank, IFluidHandler {
    private FluidStack stack = FluidStack.EMPTY;
    private final int capacity;
    private final Predicate<FluidStack> validator;

    public FluidTank(int capacity, Predicate<FluidStack> validator) {
        this.capacity = capacity;
        this.validator = validator;
    }

    public FluidTank(int capacity) {
        this(capacity, f -> true);
    }

    @Override
    public FluidStack getFluid() { return stack; }

    @Override
    public int getCapacity() { return capacity; }

    @Override
    public int getTanks() { return 1; }

    @Override
    public FluidStack getFluidInTank(int tank) { return stack; }

    @Override
    public int getTankCapacity(int tank) { return capacity; }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack) { return validator.test(stack); }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (!validator.test(resource) || resource.isEmpty()) return 0;
        int filled = Math.min(capacity - stack.getAmount(), resource.getAmount());
        if (action == FluidAction.EXECUTE && filled > 0) {
            stack = resource.copyWithAmount(stack.getAmount() + filled);
            onContentsChanged();
        }
        return filled;
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        if (resource.isEmpty() || stack.isEmpty() || !FluidStack.isSameFluidSameComponents(stack, resource)) {
            return FluidStack.EMPTY;
        }
        return drain(resource.getAmount(), action);
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        int drained = Math.min(maxDrain, stack.getAmount());
        if (drained <= 0) return FluidStack.EMPTY;
        FluidStack out = stack.copyWithAmount(drained);
        if (action == FluidAction.EXECUTE) {
            stack.shrink(drained);
            onContentsChanged();
        }
        return out;
    }

    @Override
    public boolean isFluidValid(FluidStack stack) {
        return validator.test(stack);
    }

    protected void onContentsChanged() {
    }
}
