package net.neoforged.neoforge.event.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

public class EntityAttributeCreationEvent extends net.neoforged.bus.api.Event {
    public void put(EntityType<? extends LivingEntity> entityType, AttributeSupplier attributeSupplier) {
    }
}
