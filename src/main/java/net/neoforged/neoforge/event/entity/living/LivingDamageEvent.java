package net.neoforged.neoforge.event.entity.living;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class LivingDamageEvent extends LivingEvent {
    private float amount;
    private final DamageSource source;

    public LivingDamageEvent(LivingEntity entity, DamageSource source, float amount) {
        super(entity);
        this.source = source;
        this.amount = amount;
    }

    public float getAmount() { return amount; }
    public void setAmount(float amount) { this.amount = amount; }
    public DamageSource getSource() { return source; }

    public static class Pre extends LivingDamageEvent {
        public Pre(LivingEntity entity, DamageSource source, float amount) { super(entity, source, amount); }
        public float getOriginalDamage() { return getAmount(); }
        public void setNewDamage(float amount) { setAmount(amount); }
    }

    public static class Post extends LivingDamageEvent {
        public Post(LivingEntity entity, DamageSource source, float amount) { super(entity, source, amount); }
        public float getNewDamage() { return getAmount(); }
    }
}
