package net.neoforged.neoforge.event.entity.living;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.ICancellableEvent;

public class LivingChangeTargetEvent extends LivingEvent implements ICancellableEvent {
    private final LivingEntity newTarget;
    private boolean canceled;

    public LivingChangeTargetEvent(LivingEntity entity, LivingEntity newTarget) {
        super(entity);
        this.newTarget = newTarget;
    }

    public LivingEntity getNewAboutToBeSetTarget() {
        return newTarget;
    }

    @Override
    public boolean isCanceled() {
        return canceled;
    }

    @Override
    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }
}
