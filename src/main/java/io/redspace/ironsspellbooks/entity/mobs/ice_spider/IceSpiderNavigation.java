package io.redspace.ironsspellbooks.entity.mobs.ice_spider;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public class IceSpiderNavigation extends GroundPathNavigation {

    public IceSpiderNavigation(Mob pMob, Level pLevel) {
        super(pMob, pLevel);
    }

    public void setPath(Path path) {
        this.path = path;
    }

    @Override
    protected void trimPath() {
        super.trimPath();
    }
}


