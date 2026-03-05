package io.redspace.ironsspellbooks.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import io.redspace.ironsspellbooks.compat.trinkets.TrinketSlotContext;
import io.redspace.ironsspellbooks.compat.trinkets.client.ITrinketRenderer;

@OnlyIn(Dist.CLIENT)
public class SpellBookCurioRenderer implements ITrinketRenderer {
    ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack itemStack, TrinketSlotContext TrinketSlotContext, PoseStack poseStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (renderLayerParent.getModel() instanceof HumanoidModel<?>) {
            var humanoidModel = (HumanoidModel<LivingEntity>) renderLayerParent.getModel();
            //humanoidModel.setupAnim(TrinketSlotContext.entity(), limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            //humanoidModel.prepareMobModel(TrinketSlotContext.entity(), limbSwing, limbSwingAmount, partialTicks);

            poseStack.pushPose();
            humanoidModel.body.translateAndRotate(poseStack);
            //Negative X is right, Negative Z is Forward
            //Scale by 1/16th, we are now dealing with units of pixels
            poseStack.translate((TrinketSlotContext.entity() != null && !TrinketSlotContext.entity().getItemBySlot(EquipmentSlot.CHEST).isEmpty() ? -5.5 : -4.5) * .0625f, 9 * .0625f, 0);
            //poseStack.mulPose(Vector3f.YP.rotation(Mth.PI * .5f));
            poseStack.mulPose(Axis.YP.rotation(Mth.PI));
            poseStack.mulPose(Axis.ZP.rotation(Mth.PI - 5 * Mth.DEG_TO_RAD));
            poseStack.scale(.625f, .625f, .625f);
            itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, light, OverlayTexture.NO_OVERLAY, poseStack, renderTypeBuffer, null, 0);
            poseStack.popPose();
        }
    }
}
