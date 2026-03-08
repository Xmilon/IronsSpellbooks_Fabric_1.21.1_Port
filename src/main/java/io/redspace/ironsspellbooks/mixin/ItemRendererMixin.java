package io.redspace.ironsspellbooks.mixin;

import io.redspace.ironsspellbooks.item.SpellBook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
    @ModifyVariable(
            method = "render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)V",
            at = @At("HEAD"),
            argsOnly = true
    )
    private BakedModel irons_spellbooks$useFlatSpellbookInGui(BakedModel model, ItemStack stack, ItemDisplayContext displayContext) {
        if (displayContext != ItemDisplayContext.GUI || !(stack.getItem() instanceof SpellBook)) {
            return model;
        }
        var modelManager = Minecraft.getInstance().getModelManager();
        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
        if (itemId == null) {
            return model;
        }
        ResourceLocation flatId = ResourceLocation.fromNamespaceAndPath(itemId.getNamespace(), itemId.getPath() + "_flat");
        BakedModel flatModel = modelManager.getModel(ModelResourceLocation.inventory(flatId));
        BakedModel missingModel = modelManager.getMissingModel();
        return flatModel != null && flatModel != missingModel ? flatModel : model;
    }
}
