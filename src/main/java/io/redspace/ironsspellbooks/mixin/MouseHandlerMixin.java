package io.redspace.ironsspellbooks.mixin;

import io.redspace.ironsspellbooks.player.ClientInputEvents;
import net.minecraft.client.MouseHandler;
import net.neoforged.neoforge.client.event.InputEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {
    @Inject(method = "onScroll", at = @At("HEAD"), cancellable = true)
    private void irons_spellbooks$handleAltSpellScroll(long windowPointer, double horizontalScroll, double verticalScroll, CallbackInfo ci) {
        InputEvent.MouseScrollingEvent event = new InputEvent.MouseScrollingEvent(verticalScroll);
        ClientInputEvents.clientMouseScrolled(event);
        if (event.isCanceled()) {
            ci.cancel();
        }
    }
}

