package net.neoforged.neoforge.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public class RenderLivingEvent extends Event {
    public LivingEntity getEntity() {
        return null;
    }

    public PoseStack getPoseStack() {
        return new PoseStack();
    }

    public MultiBufferSource getMultiBufferSource() {
        return null;
    }

    public float getPartialTick() {
        return 0;
    }

    public static class Pre<T extends LivingEntity, M extends EntityModel<T>> extends RenderLivingEvent implements ICancellableEvent {
        private final T entity;
        private final PoseStack poseStack;
        private final MultiBufferSource multiBufferSource;
        private final float partialTick;
        private boolean canceled;

        public Pre(T entity, PoseStack poseStack, MultiBufferSource multiBufferSource, float partialTick) {
            this.entity = entity;
            this.poseStack = poseStack;
            this.multiBufferSource = multiBufferSource;
            this.partialTick = partialTick;
        }

        public T getEntity() {
            return entity;
        }

        public PoseStack getPoseStack() {
            return poseStack;
        }

        public MultiBufferSource getMultiBufferSource() {
            return multiBufferSource;
        }

        public float getPartialTick() {
            return partialTick;
        }

        @Override
        public boolean isCanceled() {
            return canceled;
        }

        @Override
        public void setCanceled(boolean canceled) {
            this.canceled = canceled;
        }
    }

    public static class Post<T extends LivingEntity, M extends EntityModel<T>> extends RenderLivingEvent {
        private final T entity;
        private final PoseStack poseStack;
        private final MultiBufferSource multiBufferSource;
        private final float partialTick;

        public Post(T entity, PoseStack poseStack, MultiBufferSource multiBufferSource, float partialTick) {
            this.entity = entity;
            this.poseStack = poseStack;
            this.multiBufferSource = multiBufferSource;
            this.partialTick = partialTick;
        }

        public T getEntity() {
            return entity;
        }

        public PoseStack getPoseStack() {
            return poseStack;
        }

        public MultiBufferSource getMultiBufferSource() {
            return multiBufferSource;
        }

        public float getPartialTick() {
            return partialTick;
        }
    }
}
