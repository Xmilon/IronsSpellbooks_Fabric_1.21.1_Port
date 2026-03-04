package io.redspace.ironsspellbooks.entity.mobs.wizards.fire_boss;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;

public class NotIdioticNavigation extends GroundPathNavigation {
    public NotIdioticNavigation(Mob pMob, Level pLevel) {
        super(pMob, pLevel);
    }

    @Override
    protected void trimPath() {
        super.trimPath();
    }
}


