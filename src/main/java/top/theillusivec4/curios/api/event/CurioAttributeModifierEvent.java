package top.theillusivec4.curios.api.event;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

public class CurioAttributeModifierEvent {
    private final ItemStack itemStack;
    private final SlotContext slotContext;
    private final Multimap<Holder<Attribute>, AttributeModifier> modifiers;

    public CurioAttributeModifierEvent(ItemStack itemStack, SlotContext slotContext, Multimap<Holder<Attribute>, AttributeModifier> modifiers) {
        this.itemStack = itemStack;
        this.slotContext = slotContext;
        this.modifiers = modifiers;
    }

    public CurioAttributeModifierEvent() {
        this(ItemStack.EMPTY, new SlotContext("", null, 0, false, false), HashMultimap.create());
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public SlotContext getSlotContext() {
        return slotContext;
    }

    public Multimap<Holder<Attribute>, AttributeModifier> getModifiers() {
        return modifiers;
    }

    public void addModifier(Holder<Attribute> attribute, AttributeModifier modifier) {
        modifiers.put(attribute, modifier);
    }

    public void removeModifier(Holder<Attribute> attribute, net.minecraft.resources.ResourceLocation id) {
        modifiers.entries().removeIf(entry -> entry.getKey().equals(attribute) && entry.getValue().id().equals(id));
    }
}
