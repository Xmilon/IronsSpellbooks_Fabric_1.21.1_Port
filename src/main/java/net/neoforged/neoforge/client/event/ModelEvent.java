package net.neoforged.neoforge.client.event;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.neoforged.bus.api.Event;

import java.util.Map;

public class ModelEvent extends Event {
    public static class RegisterAdditional extends ModelEvent {
        public void register(ModelResourceLocation id) {
        }
    }

    public static class ModifyBakingResult extends ModelEvent {
        private final Map<ModelResourceLocation, BakedModel> models;
        private final ModelBakery modelBakery;

        public ModifyBakingResult(Map<ModelResourceLocation, BakedModel> models, ModelBakery modelBakery) {
            this.models = models;
            this.modelBakery = modelBakery;
        }

        public Map<ModelResourceLocation, BakedModel> getModels() {
            return models;
        }

        public ModelBakery getModelBakery() {
            return modelBakery;
        }
    }
}
