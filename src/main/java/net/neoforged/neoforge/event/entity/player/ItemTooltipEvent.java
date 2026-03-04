package net.neoforged.neoforge.event.entity.player;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class ItemTooltipEvent extends net.neoforged.bus.api.Event {
    private final ItemStack itemStack;
    private final List<Component> toolTip;
    private final TooltipFlag flags;

    public ItemTooltipEvent(ItemStack itemStack, List<Component> toolTip, TooltipFlag flags) {
        this.itemStack = itemStack;
        this.toolTip = toolTip;
        this.flags = flags;
    }

    public ItemTooltipEvent() {
        this(ItemStack.EMPTY, new ArrayList<>(), TooltipFlag.Default.NORMAL);
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public List<Component> getToolTip() {
        return toolTip;
    }

    public TooltipFlag getFlags() {
        return flags;
    }
}
