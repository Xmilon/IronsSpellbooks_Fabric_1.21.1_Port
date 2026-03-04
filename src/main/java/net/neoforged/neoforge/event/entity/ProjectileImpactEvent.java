package net.neoforged.neoforge.event.entity;

import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.HitResult;

public class ProjectileImpactEvent extends net.neoforged.bus.api.Event {
    private final Projectile projectile;
    private final HitResult rayTraceResult;

    public ProjectileImpactEvent(Projectile projectile, HitResult rayTraceResult) {
        this.projectile = projectile;
        this.rayTraceResult = rayTraceResult;
    }

    public Projectile getProjectile() {
        return projectile;
    }

    public HitResult getRayTraceResult() {
        return rayTraceResult;
    }
}
