package io.redspace.ironsspellbooks.item.weapons;


import io.redspace.ironsspellbooks.IronsSpellbooks;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public record AttributeContainer(Holder<Attribute> attribute, double value, AttributeModifier.Operation operation) {
    public AttributeModifier createModifier(String slot) {
        String attributeName;
        try {
            attributeName = ResourceLocation.parse(attribute.getRegisteredName()).getPath();
        } catch (RuntimeException ignored) {
            attributeName = attribute.getRegisteredName().toLowerCase().replaceAll("[^a-z0-9/._-]", "_");
            if (attributeName.isBlank()) {
                attributeName = "attribute";
            }
        }
        return new AttributeModifier(IronsSpellbooks.id(String.format("%s_%s_modifier", slot, attributeName)), value, operation);
    }
}
