package net.neoforged.neoforge.event.entity.living;

import net.minecraft.world.entity.LivingEntity;

public class LivingKnockBackEvent extends net.neoforged.bus.api.Event {
    private final LivingEntity entity;

    public LivingKnockBackEvent(LivingEntity entity) {
        this.entity = entity;
    }

    public LivingEntity getEntity() {
        return entity;
    }
}
