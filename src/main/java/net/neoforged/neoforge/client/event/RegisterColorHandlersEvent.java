package net.neoforged.neoforge.client.event;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;

public class RegisterColorHandlersEvent extends Event {
    public static class Item extends RegisterColorHandlersEvent {
        @FunctionalInterface
        public interface ItemColor {
            int getColor(ItemStack stack, int layer);
        }

        public void register(ItemColor color, Item... items) {
        }
    }
}
