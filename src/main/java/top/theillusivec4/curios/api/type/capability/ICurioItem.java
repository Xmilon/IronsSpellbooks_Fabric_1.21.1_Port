package top.theillusivec4.curios.api.type.capability;

import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public interface ICurioItem {
    default Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        return com.google.common.collect.ImmutableMultimap.of();
    }

    default List<Component> getAttributesTooltip(List<Component> tooltips, Item.TooltipContext tooltipContext, ItemStack stack) {
        return tooltips;
    }

    default void curioTick(SlotContext slotContext, ItemStack stack) {
    }
}
