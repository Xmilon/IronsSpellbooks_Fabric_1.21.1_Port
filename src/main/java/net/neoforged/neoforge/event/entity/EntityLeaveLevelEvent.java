package net.neoforged.neoforge.event.entity;

import net.minecraft.world.entity.Entity;

public class EntityLeaveLevelEvent extends net.neoforged.bus.api.Event {
    private final Entity entity;

    public EntityLeaveLevelEvent(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }
}
