package top.theillusivec4.curios.api;

import net.minecraft.world.item.ItemStack;

public class SlotResult {
    private final SlotContext slotContext;
    private final ItemStack stack;

    public SlotResult(SlotContext slotContext, ItemStack stack) {
        this.slotContext = slotContext;
        this.stack = stack;
    }

    public SlotContext slotContext() {
        return slotContext;
    }

    public ItemStack stack() {
        return stack;
    }
}
