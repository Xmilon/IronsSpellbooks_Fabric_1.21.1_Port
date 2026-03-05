package io.redspace.ironsspellbooks.compat.trinkets;

import net.minecraft.world.item.ItemStack;

public class TrinketSlotResult {
    private final TrinketSlotContext slotContext;
    private final ItemStack stack;

    public TrinketSlotResult(TrinketSlotContext slotContext, ItemStack stack) {
        this.slotContext = slotContext;
        this.stack = stack;
    }

    public TrinketSlotContext slotContext() {
        return slotContext;
    }

    public ItemStack stack() {
        return stack;
    }
}
