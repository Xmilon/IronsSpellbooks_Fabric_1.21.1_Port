package io.redspace.ironsspellbooks.mixin;

import io.redspace.ironsspellbooks.render.AngelWingsLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public class PlayerRendererMixin {

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Inject(method = "<init>", at = @At("TAIL"))
    private void irons_spellbooks$addAngelWingsLayer(EntityRendererProvider.Context context, boolean useSlimModel, CallbackInfo ci) {
        ((LivingEntityRendererAccessor) this).irons_spellbooks$addLayer(new AngelWingsLayer((PlayerRenderer) (Object) this));
    }
}
