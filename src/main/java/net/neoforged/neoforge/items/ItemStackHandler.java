package net.neoforged.neoforge.items;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemStackHandler implements IItemHandler {
    protected final List<ItemStack> stacks;

    public ItemStackHandler(int size) {
        this.stacks = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            this.stacks.add(ItemStack.EMPTY);
        }
    }

    @Override
    public int getSlots() {
        return stacks.size();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return stacks.get(slot);
    }

    public void setStackInSlot(int slot, ItemStack stack) {
        stacks.set(slot, stack);
        onContentsChanged(slot);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (!isItemValid(slot, stack)) {
            return stack;
        }
        if (!simulate) {
            setStackInSlot(slot, stack.copy());
            return ItemStack.EMPTY;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        ItemStack existing = getStackInSlot(slot);
        if (existing.isEmpty()) {
            return ItemStack.EMPTY;
        }
        ItemStack out = existing.copy();
        out.setCount(Math.min(amount, out.getCount()));
        if (!simulate) {
            existing.shrink(out.getCount());
            setStackInSlot(slot, existing);
        }
        return out;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return true;
    }

    public CompoundTag serializeNBT(HolderLookup.Provider access) {
        return new CompoundTag();
    }

    public void deserializeNBT(HolderLookup.Provider access, CompoundTag tag) {
    }

    protected void onContentsChanged(int slot) {
    }
}
