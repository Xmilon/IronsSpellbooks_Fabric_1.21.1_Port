package net.neoforged.neoforge.event.entity.living;

import net.minecraft.world.entity.Mob;

public class FinalizeSpawnEvent extends LivingEvent {
    public FinalizeSpawnEvent(Mob entity) {
        super(entity);
    }

    @Override
    public Mob getEntity() {
        return (Mob) super.getEntity();
    }
}
