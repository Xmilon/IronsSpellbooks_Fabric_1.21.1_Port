package net.neoforged.neoforge.client.event;

import com.mojang.blaze3d.vertex.PoseStack;

public class RenderLevelStageEvent extends net.neoforged.bus.api.Event {
    public enum Stage {
        AFTER_ENTITIES
    }

    private final Stage stage;
    private final PoseStack poseStack;

    public RenderLevelStageEvent() {
        this(Stage.AFTER_ENTITIES, new PoseStack());
    }

    public RenderLevelStageEvent(Stage stage, PoseStack poseStack) {
        this.stage = stage;
        this.poseStack = poseStack;
    }

    public Stage getStage() {
        return stage;
    }

    public PoseStack getPoseStack() {
        return poseStack;
    }
}
