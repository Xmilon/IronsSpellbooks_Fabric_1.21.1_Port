package net.neoforged.neoforge.event.entity.living;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.Event;

public class LivingEvent extends Event {
    private final LivingEntity entity;
    public LivingEvent(LivingEntity entity){ this.entity = entity; }
    public LivingEntity getEntity(){ return entity; }
}