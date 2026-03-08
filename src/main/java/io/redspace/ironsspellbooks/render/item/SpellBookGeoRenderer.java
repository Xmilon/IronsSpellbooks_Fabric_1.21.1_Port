package io.redspace.ironsspellbooks.render.item;

import com.mojang.blaze3d.vertex.PoseStack;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.item.SpellBook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class SpellBookGeoRenderer extends GeoItemRenderer<SpellBook> {
    private static final ResourceLocation DEFAULT_TEXTURE = IronsSpellbooks.id("textures/item/temp_spellbook.png");

    public SpellBookGeoRenderer() {
        super(new SpellBookGeoModel());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext transformType, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (transformType == ItemDisplayContext.GUI) {
            renderFlatInGui(stack, transformType, poseStack, bufferSource, packedLight, packedOverlay);
            return;
        }

        super.renderByItem(stack, transformType, poseStack, bufferSource, packedLight, packedOverlay);
    }

    @Override
    public ResourceLocation getTextureLocation(SpellBook animatable) {
        ItemStack currentStack = getCurrentItemStack();
        if (currentStack == null || currentStack.isEmpty()) {
            return DEFAULT_TEXTURE;
        }

        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(currentStack.getItem());
        if (itemId == null || !IronsSpellbooks.MODID.equals(itemId.getNamespace())) {
            return DEFAULT_TEXTURE;
        }

        ResourceLocation texture = IronsSpellbooks.id("textures/item/" + itemId.getPath() + ".png");
        if (Minecraft.getInstance().getResourceManager().getResource(texture).isPresent()) {
            return texture;
        }

        return DEFAULT_TEXTURE;
    }

    private void renderFlatInGui(ItemStack stack, ItemDisplayContext transformType, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
        if (itemId == null) {
            return;
        }

        var modelManager = Minecraft.getInstance().getModelManager();
        ResourceLocation flatId = ResourceLocation.fromNamespaceAndPath(itemId.getNamespace(), itemId.getPath() + "_flat");
        BakedModel flatModel = modelManager.getModel(ModelResourceLocation.inventory(flatId));
        BakedModel missingModel = modelManager.getMissingModel();

        if (flatModel == null || flatModel == missingModel) {
            return;
        }

        Minecraft.getInstance().getItemRenderer().render(stack, transformType, false, poseStack, bufferSource, packedLight, packedOverlay, flatModel);
    }
}
