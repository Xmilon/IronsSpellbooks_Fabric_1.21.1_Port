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
    private static final String SPELLBOOK_MODEL_TEXTURE_PREFIX = "textures/item/spell_book_models/";
    private static final String ITEM_TEXTURE_PREFIX = "textures/item/";

    public SpellBookGeoRenderer() {
        super(new SpellBookGeoModel());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext transformType, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (transformType == ItemDisplayContext.GUI || transformType == ItemDisplayContext.NONE) {
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

        ResourceLocation modelTexture = IronsSpellbooks.id(SPELLBOOK_MODEL_TEXTURE_PREFIX + itemId.getPath() + ".png");
        if (Minecraft.getInstance().getResourceManager().getResource(modelTexture).isPresent()) {
            return modelTexture;
        }

        ResourceLocation itemTexture = IronsSpellbooks.id(ITEM_TEXTURE_PREFIX + itemId.getPath() + ".png");
        if (Minecraft.getInstance().getResourceManager().getResource(itemTexture).isPresent()) {
            return itemTexture;
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
