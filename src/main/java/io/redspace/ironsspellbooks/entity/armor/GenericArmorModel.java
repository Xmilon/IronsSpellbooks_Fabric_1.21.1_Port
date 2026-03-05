package io.redspace.ironsspellbooks.entity.armor;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.item.armor.ExtendedArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import java.util.Map;

public class GenericArmorModel<T extends ExtendedArmorItem> extends DefaultedItemGeoModel<T> {
    private final ResourceLocation model; //(IronsSpellbooks.MODID, "geo/shadowwalker_armor.geo.json");

    private final ResourceLocation texture; //(IronsSpellbooks.MODID, "textures/models/armor/shadowwalker.png");

    private static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, "animations/wizard_armor_animation.json");

    public GenericArmorModel(String modid, String name) {
        super(ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, ""));
        this.model = ResourceLocation.fromNamespaceAndPath(modid, String.format("geo/%s_armor.geo.json", name));
        this.texture = ResourceLocation.fromNamespaceAndPath(modid, String.format("textures/models/armor/%s.png", name));
    }

    public GenericArmorModel(String name) {
        this(IronsSpellbooks.MODID, name);
    }

    public GenericArmorModel<T> variants(Map<String, ResourceLocation> modelVariants) {
        // Variant support is currently disabled on this loader path until per-stack renderer context is wired.
        return this;
    }

    @Override
    public ResourceLocation getModelResource(T animatable) {
        return model;
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        return texture;
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        return ANIMATION;
    }
}
