package net.neoforged.neoforge.client.event;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.Event;

import java.util.Collections;

public class EntityRenderersEvent extends Event {
    public static class RegisterLayerDefinitions extends EntityRenderersEvent {
        public void registerLayerDefinition(ModelLayerLocation location, java.util.function.Supplier<LayerDefinition> supplier) {
        }
    }

    public static class RegisterRenderers extends EntityRenderersEvent {
        public <T extends Entity> void registerEntityRenderer(EntityType<? extends T> entityType, EntityRendererProvider<T> provider) {
        }

        public <T extends BlockEntity> void registerBlockEntityRenderer(BlockEntityType<? extends T> blockEntityType, BlockEntityRendererProvider<T> provider) {
        }
    }

    public static class AddLayers extends EntityRenderersEvent {
        public Iterable<EntityType<?>> getEntityTypes() {
            return Collections.emptyList();
        }

        public EntityRenderer<?> getRenderer(EntityType<?> type) {
            return null;
        }

        public EntityRenderer<? extends net.minecraft.world.entity.player.Player> getSkin(PlayerSkin.Model model) {
            return null;
        }
    }
}


