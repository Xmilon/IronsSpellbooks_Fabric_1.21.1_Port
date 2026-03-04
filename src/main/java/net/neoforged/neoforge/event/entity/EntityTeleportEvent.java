package net.neoforged.neoforge.event.entity;

import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.Event;

public class EntityTeleportEvent extends Event {
    private final Entity entity;
    private double targetX;
    private double targetY;
    private double targetZ;

    public EntityTeleportEvent(Entity entity, double targetX, double targetY, double targetZ) {
        this.entity = entity;
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetZ = targetZ;
    }

    public Entity getEntity() { return entity; }
    public double getTargetX() { return targetX; }
    public double getTargetY() { return targetY; }
    public double getTargetZ() { return targetZ; }
    public void setTargetX(double v){targetX=v;}
    public void setTargetY(double v){targetY=v;}
    public void setTargetZ(double v){targetZ=v;}
}