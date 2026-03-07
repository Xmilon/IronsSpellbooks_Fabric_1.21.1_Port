package io.redspace.ironsspellbooks.render;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class ScrollModel extends NBTOverrideItemModel {
    public ScrollModel(BakedModel original) {
        super(original);
    }

    @Override
    Optional<ResourceLocation> getModelFromStack(ItemStack itemStack) {
        if (ISpellContainer.isSpellContainer(itemStack)) {
            var spell = ISpellContainer.get(itemStack).getSpellAtIndex(0).getSpell();
            if (spell != SpellRegistry.none()) {
                return Optional.of(getScrollModelLocation(spell.getSchoolType()));
            }
        }
        return Optional.empty();
    }

    public static ResourceLocation getScrollModelLocation(SchoolType schoolType) {
        return ResourceLocation.fromNamespaceAndPath(schoolType.getId().getNamespace(), String.format("item/scroll_%s", schoolType.getId().getPath()));
    }
}
