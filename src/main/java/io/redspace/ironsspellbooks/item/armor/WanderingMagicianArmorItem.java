package io.redspace.ironsspellbooks.item.armor;

import io.redspace.ironsspellbooks.registries.ArmorMaterialRegistry;
import net.minecraft.world.item.ArmorItem;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class WanderingMagicianArmorItem extends ExtendedArmorItem {
    public WanderingMagicianArmorItem(ArmorItem.Type slot, Properties settings) {
        super(ArmorMaterialRegistry.WANDERING_MAGICIAN, slot, settings, withManaAttribute(25));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GeoArmorRenderer<?> supplyRenderer() {
        return ArmorClientRenderers.create(this);
    }
}