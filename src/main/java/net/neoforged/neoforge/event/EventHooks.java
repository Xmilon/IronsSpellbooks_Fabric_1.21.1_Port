package net.neoforged.neoforge.event;

public class EventHooks {
    public static boolean canEntityGrief(net.minecraft.world.level.Level level, net.minecraft.world.entity.Entity entity) {
        return true;
    }

    public static boolean onEntityDestroyBlock(net.minecraft.world.entity.Entity entity, net.minecraft.core.BlockPos pos, net.minecraft.world.level.block.state.BlockState state) {
        return true;
    }

    public static boolean checkSpawnPosition(net.minecraft.world.entity.Mob mob, net.minecraft.server.level.ServerLevel level, net.minecraft.world.entity.MobSpawnType spawnType) {
        return true;
    }
}


