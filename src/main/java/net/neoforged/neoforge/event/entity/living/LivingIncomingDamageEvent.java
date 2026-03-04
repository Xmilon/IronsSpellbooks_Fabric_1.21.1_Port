package net.neoforged.neoforge.event.entity.living;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class LivingIncomingDamageEvent extends net.neoforged.bus.api.Event {
    private final LivingEntity entity;
    private final DamageSource source;
    private float amount;
    private final DamageContainer container = new DamageContainer();

    public LivingIncomingDamageEvent(LivingEntity entity, DamageSource source, float amount) {
        this.entity = entity;
        this.source = source;
        this.amount = amount;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public DamageSource getSource() {
        return source;
    }

    public float getAmount() {
        return amount;
    }

    public float getOriginalAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public DamageContainer getContainer() {
        return container;
    }

    public static class DamageContainer {
        public void setPostAttackInvulnerabilityTicks(int ticks) {
        }
    }
}
