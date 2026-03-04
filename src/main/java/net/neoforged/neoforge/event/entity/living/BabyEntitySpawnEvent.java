package net.neoforged.neoforge.event.entity.living;

import net.minecraft.world.entity.AgeableMob;

public class BabyEntitySpawnEvent extends LivingEvent {
    private final AgeableMob parentA;
    private final AgeableMob parentB;
    private final AgeableMob child;

    public BabyEntitySpawnEvent(AgeableMob parentA, AgeableMob parentB, AgeableMob child) {
        super(child);
        this.parentA = parentA;
        this.parentB = parentB;
        this.child = child;
    }

    public AgeableMob getParentA() {
        return parentA;
    }

    public AgeableMob getParentB() {
        return parentB;
    }

    public AgeableMob getChild() {
        return child;
    }
}
