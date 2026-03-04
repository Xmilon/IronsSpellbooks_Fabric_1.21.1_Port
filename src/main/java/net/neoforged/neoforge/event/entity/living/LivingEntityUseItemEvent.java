package net.neoforged.neoforge.event.entity.living;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class LivingEntityUseItemEvent extends LivingEvent {
    private final ItemStack item;

    public LivingEntityUseItemEvent(LivingEntity entity, ItemStack item) {
        super(entity);
        this.item = item;
    }

    public ItemStack getItem() { return item; }

    public static class Finish extends LivingEntityUseItemEvent {
        public Finish(LivingEntity entity, ItemStack item) { super(entity, item); }
    }
}