package net.neoforged.neoforge.event.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Collections;
import java.util.Set;

public class EntityAttributeModificationEvent extends net.neoforged.bus.api.Event {
    public Set<EntityType<?>> getTypes() {
        return Collections.emptySet();
    }

    public void add(EntityType<?> entity, DeferredHolder<Attribute, Attribute> attribute) {
    }
}
