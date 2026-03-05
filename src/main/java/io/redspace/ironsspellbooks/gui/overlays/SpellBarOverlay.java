package io.redspace.ironsspellbooks.gui.overlays;

import com.mojang.blaze3d.systems.RenderSystem;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.compat.TrinketsSlots;
import io.redspace.ironsspellbooks.config.ClientConfigs;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import io.redspace.ironsspellbooks.player.ClientRenderCache;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec2;

import java.util.List;

public class SpellBarOverlay implements LayeredDraw.Layer {
    public static final SpellBarOverlay instance = new SpellBarOverlay();

    public final static ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, "textures/gui/icons.png");
    static final int IMAGE_HEIGHT = 21;
    static final int IMAGE_WIDTH = 21;
    static final int boxSize = 20;

    public enum Anchor {
        Hotbar(0, 0),
        TopLeft(0, 0),
        TopRight(0, 1),
        BottomLeft(0, 1),
        BottomRight(1, 1);
        final int m1, m2;

        Anchor(int mx, int my) {
            this.m1 = mx;
            this.m2 = my;
        }
    }

    static final int CONTEXTUAL_FADE_WAIT = 80;
    public static int fadeoutDelay;
    static int lastTick;
    static float alpha;
    static int lastSpellCount;

    public void render(GuiGraphics guiHelper, DeltaTracker deltaTracker) {
        var minecraft = Minecraft.getInstance();
        var player = minecraft.player;
        if (minecraft.options.hideGui || player == null || player.isSpectator()) {
            return;
        }
        var screenWidth = guiHelper.guiWidth();
        var screenHeight = guiHelper.guiHeight();
        ManaBarOverlay.Display displayMode = ClientConfigs.SPELL_BAR_DISPLAY.get();
        if (displayMode == ManaBarOverlay.Display.Never) {
            return;
        } else if (displayMode == ManaBarOverlay.Display.Contextual) {
            handleFading(player);
            if (fadeoutDelay <= 0) {
                return;
            }
        } else {
            alpha = 1f;
        }

        var ssm = ClientMagicData.getSpellSelectionManager();
        if (ssm == null) {
            return;
        }
        if (ssm.getSpellCount() != lastSpellCount) {
            lastSpellCount = ssm.getSpellCount();
            ClientRenderCache.generateRelativeLocations(ssm, 20, 22);
            if (displayMode == ManaBarOverlay.Display.Contextual) {
                fadeoutDelay = CONTEXTUAL_FADE_WAIT;
            }
        }
        if (ssm.getSpellCount() <= 0) {
            return;
        }
        //System.out.println("SpellBarDisplay: Holding Spellbook");

        int centerX, centerY;
        int configOffsetY = ClientConfigs.SPELL_BAR_Y_OFFSET.get();
        int configOffsetX = ClientConfigs.SPELL_BAR_X_OFFSET.get();
        Anchor anchor = ClientConfigs.SPELL_BAR_ANCHOR.get();
        if (anchor == Anchor.Hotbar) {
            // Align from the first hotbar slot and shift right to stay clear of chat.
            centerX = (screenWidth / 2 - 91) - 120;
            centerY = screenHeight - 45;
        } else {
            centerX = screenWidth * anchor.m1;
            centerY = screenHeight * anchor.m2;
        }
        centerX += configOffsetX;
        centerY += configOffsetY;

        //
        //  Render Spells
        //
        List<SpellData> spells = ssm.getAllSpells().stream().map((slot) -> slot.spellData).toList();
        var locations = ClientRenderCache.relativeSpellBarSlotLocations;
        //var spellSelection = ClientMagicData.getSyncedSpellData(player).getSpellSelection();
        int selectedSpellIndex = ssm.getGlobalSelectionIndex();

        //Slot Border
        prepTranslucency();
        for (Vec2 location : locations) {
            guiHelper.blit(TEXTURE, centerX + (int) location.x, centerY + (int) location.y, 66, 84, 22, 22);
        }
        //Spell Icons
        for (int i = 0; i < locations.size(); i++) {
            guiHelper.blit(spells.get(i).getSpell().getSpellIconResource(), centerX + (int) locations.get(i).x + 3, centerY + (int) locations.get(i).y + 3, 0, 0, 16, 16, 16, 16);
        }
        //Border + Cooldowns
        for (int i = 0; i < locations.size(); i++) {
            if (i != selectedSpellIndex) {
                guiHelper.blit(TEXTURE, centerX + (int) locations.get(i).x, centerY + (int) locations.get(i).y, 22 + (!ssm.getAllSpells().get(i).slot.equals(TrinketsSlots.SPELLBOOK_SLOT) ? 110 : 0), 84, 22, 22);
            }
            float f = ClientMagicData.getCooldownPercent(spells.get(i).getSpell());
            if (f > 0) {
                int pixels = (int) (16 * f + 1f);
                guiHelper.blit(TEXTURE, centerX + (int) locations.get(i).x + 3, centerY + (int) locations.get(i).y + 19 - pixels, 47, 87, 16, pixels);
            }
        }
        //Selected Outline
        for (int i = 0; i < locations.size(); i++) {
            //setTranslucentTexture(TEXTURE);
            if (i == selectedSpellIndex) {
                guiHelper.blit(TEXTURE, centerX + (int) locations.get(i).x, centerY + (int) locations.get(i).y, 0, 84, 22, 22);
            }
        }
        flushTranslucency();
    }

    private static void handleFading(Player player) {
        if (lastTick != player.tickCount) {
            lastTick = player.tickCount;
            if (ClientMagicData.isCasting() || ClientMagicData.getCooldowns().hasCooldownsActive() || ClientMagicData.getRecasts().hasRecastsActive()) {
                fadeoutDelay = CONTEXTUAL_FADE_WAIT;
            }
            if (fadeoutDelay > 0) {
                fadeoutDelay--;
            }
        }
        alpha = Mth.clamp(fadeoutDelay / 20f, 0, 1);
        if (fadeoutDelay <= 0) {
            return;
        }
    }

    private static void prepTranslucency() {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getRendertypeTranslucentShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, alpha);
    }

    private static void flushTranslucency() {
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    }
}
