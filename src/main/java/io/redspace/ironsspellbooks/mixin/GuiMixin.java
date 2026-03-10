package io.redspace.ironsspellbooks.mixin;

import io.redspace.ironsspellbooks.config.ClientConfigs;
import io.redspace.ironsspellbooks.gui.overlays.ScreenEffectsOverlay;
import io.redspace.ironsspellbooks.gui.overlays.ManaBarOverlay;
import io.redspace.ironsspellbooks.gui.overlays.SpellBarOverlay;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Gui.class)
public class GuiMixin {

    @Inject(method = "render", at = @At("HEAD"))
    public void irons_spellbooks$renderScreenEffectsFirstLayer(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        ScreenEffectsOverlay.instance.render(guiGraphics, deltaTracker);
        SpellBarOverlay.instance.render(guiGraphics, deltaTracker);
    }

    //TODO: can't this be an event?
    @Inject(method = "isExperienceBarVisible", at = @At(value = "HEAD"), cancellable = true)
    public void irons_spellbooks$disableXpBar(CallbackInfoReturnable<Boolean> cir) {
        if (ClientConfigs.safeGet(ClientConfigs.MANA_BAR_ANCHOR) == ManaBarOverlay.Anchor.XP && Minecraft.getInstance().player != null && ManaBarOverlay.shouldShowManaBar(Minecraft.getInstance().player)) {
            cir.setReturnValue(false);
        }
    }
}
