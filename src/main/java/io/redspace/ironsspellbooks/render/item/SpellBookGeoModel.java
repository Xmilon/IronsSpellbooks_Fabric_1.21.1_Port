package io.redspace.ironsspellbooks.render.item;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.item.SpellBook;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SpellBookGeoModel extends GeoModel<SpellBook> {
    private static final ResourceLocation MODEL = IronsSpellbooks.id("geo/spell_book.geo.json");
    private static final ResourceLocation ANIMATION = IronsSpellbooks.id("animations/spell_book.animation.json");
    private static final ResourceLocation TEXTURE = IronsSpellbooks.id("textures/item/temp_spellbook.png");

    @Override
    public ResourceLocation getModelResource(SpellBook animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(SpellBook animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(SpellBook animatable) {
        return ANIMATION;
    }
}
