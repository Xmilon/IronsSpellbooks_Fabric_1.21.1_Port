package top.theillusivec4.curios.api.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class CurioChangeEvent {
    private final LivingEntity entity;
    private final ItemStack from;
    private final ItemStack to;

    public CurioChangeEvent(LivingEntity entity, ItemStack from, ItemStack to) {
        this.entity = entity;
        this.from = from;
        this.to = to;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public ItemStack getFrom() {
        return from;
    }

    public ItemStack getTo() {
        return to;
    }
}
