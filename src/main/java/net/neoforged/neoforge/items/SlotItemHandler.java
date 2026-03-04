package net.neoforged.neoforge.items;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SlotItemHandler extends Slot {
    private final IItemHandler itemHandler;
    private final int index;

    public SlotItemHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(new Container() {
            @Override
            public int getContainerSize() { return itemHandler.getSlots(); }
            @Override
            public boolean isEmpty() { return false; }
            @Override
            public ItemStack getItem(int slot) { return itemHandler.getStackInSlot(slot); }
            @Override
            public ItemStack removeItem(int slot, int amount) { return itemHandler.extractItem(slot, amount, false); }
            @Override
            public ItemStack removeItemNoUpdate(int slot) { return itemHandler.extractItem(slot, itemHandler.getStackInSlot(slot).getCount(), false); }
            @Override
            public void setItem(int slot, ItemStack stack) { itemHandler.insertItem(slot, stack, false); }
            @Override
            public void setChanged() { }
            @Override
            public boolean stillValid(Player player) { return true; }
            @Override
            public void clearContent() { }
        }, index, xPosition, yPosition);
        this.itemHandler = itemHandler;
        this.index = index;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return itemHandler.isItemValid(index, stack);
    }
}
