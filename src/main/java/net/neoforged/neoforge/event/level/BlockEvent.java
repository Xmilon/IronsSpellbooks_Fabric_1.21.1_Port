package net.neoforged.neoforge.event.level;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.Event;

public class BlockEvent extends Event {
    private final Level level;
    private final BlockPos pos;

    protected BlockEvent(Level level, BlockPos pos) {
        this.level = level;
        this.pos = pos;
    }

    public Level getLevel() { return level; }
    public BlockPos getPos() { return pos; }

    public static class BreakEvent extends BlockEvent {
        private final Player player;
        public BreakEvent(Level level, BlockPos pos, Player player) {
            super(level, pos);
            this.player = player;
        }
        public Player getPlayer() { return player; }
    }
}


