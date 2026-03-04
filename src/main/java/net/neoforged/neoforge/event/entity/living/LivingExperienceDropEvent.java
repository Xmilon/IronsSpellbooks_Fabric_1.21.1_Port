package net.neoforged.neoforge.event.entity.living;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class LivingExperienceDropEvent extends LivingEvent {
    private final Player attackingPlayer;
    private int droppedExperience;

    public LivingExperienceDropEvent(LivingEntity entity, Player attackingPlayer, int droppedExperience) {
        super(entity);
        this.attackingPlayer = attackingPlayer;
        this.droppedExperience = droppedExperience;
    }

    public Player getAttackingPlayer() {
        return attackingPlayer;
    }

    public int getDroppedExperience() {
        return droppedExperience;
    }

    public void setDroppedExperience(int droppedExperience) {
        this.droppedExperience = droppedExperience;
    }
}
