package net.neoforged.neoforge.event.entity.player;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.ICancellableEvent;

public class PlayerInteractEvent extends PlayerEvent implements ICancellableEvent {
    private boolean canceled;
    private InteractionResult cancellationResult = InteractionResult.PASS;

    public PlayerInteractEvent(Player player) {
        super(player);
    }

    @Override
    public boolean isCanceled() {
        return canceled;
    }

    @Override
    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public void setCancellationResult(InteractionResult result) {
        this.cancellationResult = result;
    }

    public InteractionResult getCancellationResult() {
        return cancellationResult;
    }

    public static class RightClickBlock extends PlayerInteractEvent {
        private final BlockPos pos;
        private final Level level;
        private final ItemStack itemStack;

        public RightClickBlock(Player player, Level level, BlockPos pos, ItemStack itemStack) {
            super(player);
            this.level = level;
            this.pos = pos;
            this.itemStack = itemStack;
        }

        public BlockPos getPos() {
            return pos;
        }

        public Level getLevel() {
            return level;
        }

        public ItemStack getItemStack() {
            return itemStack;
        }

        public BlockHitResult getHitVec() {
            return new BlockHitResult(Vec3.atCenterOf(pos), Direction.UP, pos, false);
        }
    }

    public static class RightClickItem extends PlayerInteractEvent {
        private final ItemStack itemStack;
        private final InteractionHand hand;

        public RightClickItem(Player player, ItemStack itemStack, InteractionHand hand) {
            super(player);
            this.itemStack = itemStack;
            this.hand = hand;
        }

        public ItemStack getItemStack() {
            return itemStack;
        }

        public InteractionHand getHand() {
            return hand;
        }
    }

    public static class EntityInteractSpecific extends PlayerInteractEvent {
        private final Entity target;
        private final InteractionHand hand;

        public EntityInteractSpecific(Player player, Entity target, InteractionHand hand) {
            super(player);
            this.target = target;
            this.hand = hand;
        }

        public Entity getTarget() {
            return target;
        }

        public InteractionHand getHand() {
            return hand;
        }
    }
}


