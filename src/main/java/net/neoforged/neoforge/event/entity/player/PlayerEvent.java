package net.neoforged.neoforge.event.entity.player;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;

public class PlayerEvent extends Event {
    private final Player entity;

    public PlayerEvent(Player entity) {
        this.entity = entity;
    }

    public Player getEntity() {
        return entity;
    }

    public static class PlayerLoggedOutEvent extends PlayerEvent {
        public PlayerLoggedOutEvent(Player entity) {
            super(entity);
        }
    }

    public static class PlayerLoggedInEvent extends PlayerEvent {
        public PlayerLoggedInEvent(Player entity) {
            super(entity);
        }
    }

    public static class PlayerRespawnEvent extends PlayerEvent {
        public PlayerRespawnEvent(Player entity) {
            super(entity);
        }
    }

    public static class PlayerChangedDimensionEvent extends PlayerEvent {
        public PlayerChangedDimensionEvent(Player entity) {
            super(entity);
        }
    }

    public static class StartTracking extends PlayerEvent {
        private final Entity target;

        public StartTracking(Player entity, Entity target) {
            super(entity);
            this.target = target;
        }

        public Entity getTarget() {
            return target;
        }
    }

    public static class Clone extends PlayerEvent {
        private final Player original;
        private final boolean wasDeath;

        public Clone(Player entity, Player original, boolean wasDeath) {
            super(entity);
            this.original = original;
            this.wasDeath = wasDeath;
        }

        public Player getOriginal() {
            return original;
        }

        public boolean isWasDeath() {
            return wasDeath;
        }
    }

    public static class BreakSpeed extends PlayerEvent {
        private float newSpeed;

        public BreakSpeed(Player entity, float newSpeed) {
            super(entity);
            this.newSpeed = newSpeed;
        }

        public float getNewSpeed() {
            return newSpeed;
        }

        public void setNewSpeed(float newSpeed) {
            this.newSpeed = newSpeed;
        }
    }
}
