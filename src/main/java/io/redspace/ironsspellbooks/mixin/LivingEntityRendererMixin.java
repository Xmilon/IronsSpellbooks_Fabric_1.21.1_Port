package io.redspace.ironsspellbooks.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import io.redspace.ironsspellbooks.render.AngelWingsLayer;
import io.redspace.ironsspellbooks.player.ClientPlayerEvents;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity> {
    @Unique
    private boolean irons_spellbooks$angelWingsLayerAdded = false;

    @Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("HEAD"), cancellable = true)
    private void irons_spellbooks$beforeRender(T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, CallbackInfo ci) {
        if (!irons_spellbooks$angelWingsLayerAdded && (Object) this instanceof PlayerRenderer playerRenderer) {
            ((LivingEntityRendererAccessor) this).irons_spellbooks$addLayer(new AngelWingsLayer<>(playerRenderer));
            irons_spellbooks$angelWingsLayerAdded = true;
        }
        var event = new net.neoforged.neoforge.client.event.RenderLivingEvent.Pre<>(entity, poseStack, bufferSource, partialTick);
        ClientPlayerEvents.beforeLivingRender(event);
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("TAIL"))
    private void irons_spellbooks$afterRender(T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, CallbackInfo ci) {
        var event = new net.neoforged.neoforge.client.event.RenderLivingEvent.Post<>(entity, poseStack, bufferSource, partialTick);
        ClientPlayerEvents.afterLivingRender(event);
    }
}
