package net.neoforged.neoforge.event;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.ArrayList;
import java.util.List;

public class ItemAttributeModifierEvent extends net.neoforged.bus.api.Event {
    private final ItemStack itemStack;
    private final List<ItemAttributeModifiers.Entry> modifiers;

    public ItemAttributeModifierEvent(ItemStack itemStack, List<ItemAttributeModifiers.Entry> modifiers) {
        this.itemStack = itemStack;
        this.modifiers = modifiers;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public List<ItemAttributeModifiers.Entry> getModifiers() {
        return modifiers;
    }

    public void addModifier(Holder<Attribute> attribute, AttributeModifier modifier, EquipmentSlotGroup group) {
        modifiers.add(new ItemAttributeModifiers.Entry(attribute, modifier, group));
    }

    public void removeModifier(Holder<Attribute> attribute, net.minecraft.resources.ResourceLocation id) {
        modifiers.removeIf(entry -> entry.attribute().equals(attribute) && entry.modifier().id().equals(id));
    }

    public ItemAttributeModifierEvent() {
        this(ItemStack.EMPTY, new ArrayList<>());
    }
}
