package io.redspace.ironsspellbooks.api.registry;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.attribute.MagicPercentAttribute;
import io.redspace.ironsspellbooks.api.attribute.MagicRangedAttribute;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.compat.TrinketsSlots;
import io.redspace.ironsspellbooks.compat.trinkets.TrinketsApi;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import io.redspace.ironsspellbooks.compat.trinkets.TrinketSlotContext;
import io.redspace.ironsspellbooks.compat.trinkets.TrinketSlotResult;
import io.redspace.ironsspellbooks.compat.trinkets.ITrinketItem;
import io.redspace.ironsspellbooks.item.SpellBook;


@EventBusSubscriber(modid = IronsSpellbooks.MODID, bus = EventBusSubscriber.Bus.MOD)
public class AttributeRegistry {

    private static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Registries.ATTRIBUTE, IronsSpellbooks.MODID);

    public static void register(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }

    public static final DeferredHolder<Attribute, Attribute> MAX_MANA = ATTRIBUTES.register("max_mana", () -> (new MagicRangedAttribute("attribute.irons_spellbooks.max_mana", 100.0D, 0.0D, 1000000.0D).setSyncable(true)));
    public static final DeferredHolder<Attribute, Attribute> MANA_REGEN = ATTRIBUTES.register("mana_regen", () -> (new MagicPercentAttribute("attribute.irons_spellbooks.mana_regen", 1.0D, 0.0D, 100.0D).setSyncable(true)));
    public static final DeferredHolder<Attribute, Attribute> COOLDOWN_REDUCTION = ATTRIBUTES.register("cooldown_reduction", () -> (new MagicPercentAttribute("attribute.irons_spellbooks.cooldown_reduction", 1.0D, -100.0D, 100.0D).setSyncable(true)));
    public static final DeferredHolder<Attribute, Attribute> SPELL_POWER = ATTRIBUTES.register("spell_power", () -> (new MagicPercentAttribute("attribute.irons_spellbooks.spell_power", 1.0D, -100, 100.0D).setSyncable(true)));
    public static final DeferredHolder<Attribute, Attribute> SPELL_RESIST = ATTRIBUTES.register("spell_resist", () -> (new MagicPercentAttribute("attribute.irons_spellbooks.spell_resist", 1.0D, -100, 100.0D).setSyncable(true)));
    public static final DeferredHolder<Attribute, Attribute> CAST_TIME_REDUCTION = ATTRIBUTES.register("cast_time_reduction", () -> (new MagicPercentAttribute("attribute.irons_spellbooks.cast_time_reduction", 1.0D, -100, 100.0D).setSyncable(true)));
    public static final DeferredHolder<Attribute, Attribute> SUMMON_DAMAGE = ATTRIBUTES.register("summon_damage", () -> (new MagicPercentAttribute("attribute.irons_spellbooks.summon_damage", 1.0D, -100, 100.0D).setSyncable(true)));
    public static final DeferredHolder<Attribute, Attribute> CASTING_MOVESPEED = ATTRIBUTES.register("casting_movespeed", () -> (new MagicPercentAttribute("attribute.irons_spellbooks.casting_movespeed", 1, 0, 100.0D).setSyncable(true)));

    public static final DeferredHolder<Attribute, Attribute> FIRE_MAGIC_RESIST = newResistanceAttribute("fire");
    public static final DeferredHolder<Attribute, Attribute> ICE_MAGIC_RESIST = newResistanceAttribute("ice");
    public static final DeferredHolder<Attribute, Attribute> LIGHTNING_MAGIC_RESIST = newResistanceAttribute("lightning");
    public static final DeferredHolder<Attribute, Attribute> HOLY_MAGIC_RESIST = newResistanceAttribute("holy");
    public static final DeferredHolder<Attribute, Attribute> ENDER_MAGIC_RESIST = newResistanceAttribute("ender");
    public static final DeferredHolder<Attribute, Attribute> BLOOD_MAGIC_RESIST = newResistanceAttribute("blood");
    public static final DeferredHolder<Attribute, Attribute> EVOCATION_MAGIC_RESIST = newResistanceAttribute("evocation");
    public static final DeferredHolder<Attribute, Attribute> NATURE_MAGIC_RESIST = newResistanceAttribute("nature");
    public static final DeferredHolder<Attribute, Attribute> ELDRITCH_MAGIC_RESIST = newResistanceAttribute("eldritch");

    public static final DeferredHolder<Attribute, Attribute> FIRE_SPELL_POWER = newPowerAttribute("fire");
    public static final DeferredHolder<Attribute, Attribute> ICE_SPELL_POWER = newPowerAttribute("ice");
    public static final DeferredHolder<Attribute, Attribute> LIGHTNING_SPELL_POWER = newPowerAttribute("lightning");
    public static final DeferredHolder<Attribute, Attribute> HOLY_SPELL_POWER = newPowerAttribute("holy");
    public static final DeferredHolder<Attribute, Attribute> ENDER_SPELL_POWER = newPowerAttribute("ender");
    public static final DeferredHolder<Attribute, Attribute> BLOOD_SPELL_POWER = newPowerAttribute("blood");
    public static final DeferredHolder<Attribute, Attribute> EVOCATION_SPELL_POWER = newPowerAttribute("evocation");
    public static final DeferredHolder<Attribute, Attribute> NATURE_SPELL_POWER = newPowerAttribute("nature");
    public static final DeferredHolder<Attribute, Attribute> ELDRITCH_SPELL_POWER = newPowerAttribute("eldritch");

    @SubscribeEvent
    public static void modifyEntityAttributes(EntityAttributeModificationEvent e) {
        e.getTypes().forEach(entity -> ATTRIBUTES.getEntries().forEach(attribute -> e.add(entity, (DeferredHolder<Attribute, Attribute>) attribute)));
    }

    private static DeferredHolder<Attribute, Attribute> newResistanceAttribute(String id) {
        return (DeferredHolder<Attribute, Attribute>) ATTRIBUTES.register(id + "_magic_resist", () -> (new MagicPercentAttribute("attribute.irons_spellbooks." + id + "_magic_resist", 1.0D, -100, 100).setSyncable(true)));
    }

    private static DeferredHolder<Attribute, Attribute> newPowerAttribute(String id) {
        return ATTRIBUTES.register(id + "_spell_power", () -> (new MagicPercentAttribute("attribute.irons_spellbooks." + id + "_spell_power", 1.0D, -100, 100).setSyncable(true)));
    }

    public static double getValueOrDefault(LivingEntity entity, DeferredHolder<Attribute, Attribute> attribute, double fallback) {
        return getValueOrDefault(entity, (net.minecraft.core.Holder<Attribute>) attribute, fallback);
    }

    public static double getValueOrDefault(LivingEntity entity, net.minecraft.core.Holder<Attribute> attribute, double fallback) {
        if (entity == null || attribute == null) {
            return fallback;
        }
        AttributeInstance instance = entity.getAttribute(attribute);
        return instance != null ? instance.getValue() : fallback;
    }

    public static double getValueOrDefaultWithSpellbookFallback(LivingEntity entity, DeferredHolder<Attribute, Attribute> attribute, double fallback) {
        return getValueOrDefaultWithSpellbookFallback(entity, (net.minecraft.core.Holder<Attribute>) attribute, fallback);
    }

    public static double getValueOrDefaultWithSpellbookFallback(LivingEntity entity, net.minecraft.core.Holder<Attribute> attribute, double fallback) {
        double value = getValueOrDefault(entity, attribute, fallback);
        if (!(entity instanceof Player player)) {
            return value;
        }
        if (!player.level().isClientSide) {
            return value;
        }
        if (Double.compare(value, fallback) != 0) {
            return value;
        }
        return getSpellbookAttributeFallback(player, attribute, fallback);
    }

    public static double getMaxManaWithFallback(LivingEntity entity) {
        double fromAttributes = getValueOrDefault(entity, MAX_MANA, 100.0D);
        if (!(entity instanceof Player player)) {
            return fromAttributes;
        }

        double fromEquipment = getMaxManaFromEquipment(player);
        double fromSpellbookCurio = getMaxManaFromSpellbookCurio(player);
        return Math.max(fromAttributes, Math.max(fromEquipment, fromSpellbookCurio));
    }

    private static double getMaxManaFromEquipment(Player player) {
        double base = 100.0D;
        double additive = 0.0D;
        double multipliedBase = 0.0D;
        double multipliedTotal = 0.0D;

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND) {
                continue;
            }
            ItemStack stack = player.getItemBySlot(slot);
            if (stack.isEmpty()) {
                continue;
            }
            ItemAttributeModifiers modifiers = stack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);
            for (ItemAttributeModifiers.Entry entry : modifiers.modifiers()) {
                if (entry.attribute().value() != MAX_MANA.get()) {
                    continue;
                }
                if (entry.slot() != EquipmentSlotGroup.bySlot(slot)) {
                    continue;
                }
                switch (entry.modifier().operation()) {
                    case ADD_VALUE -> additive += entry.modifier().amount();
                    case ADD_MULTIPLIED_BASE -> multipliedBase += entry.modifier().amount();
                    case ADD_MULTIPLIED_TOTAL -> multipliedTotal += entry.modifier().amount();
                }
            }
        }

        return Math.max(100.0D, (base + additive) * (1.0D + multipliedBase) * (1.0D + multipliedTotal));
    }

    private static double getMaxManaFromSpellbookCurio(Player player) {
        ItemStack spellbookStack = Utils.getPlayerEquippedSpellbookStack(player);
        if (spellbookStack == null || spellbookStack.isEmpty() || !(spellbookStack.getItem() instanceof ITrinketItem curioItem)) {
            return 100.0D;
        }

        var TrinketSlotContext = new TrinketSlotContext(TrinketsSlots.SPELLBOOK_SLOT, player, 0, false, true);
        var modifiers = curioItem.getAttributeModifiers(TrinketSlotContext, IronsSpellbooks.id("max_mana_fallback"), spellbookStack);
        double base = 100.0D;
        double additive = 0.0D;
        double multipliedBase = 0.0D;
        double multipliedTotal = 0.0D;

        for (var entry : modifiers.entries()) {
            if (entry.getKey().value() != MAX_MANA.get()) {
                continue;
            }
            switch (entry.getValue().operation()) {
                case ADD_VALUE -> additive += entry.getValue().amount();
                case ADD_MULTIPLIED_BASE -> multipliedBase += entry.getValue().amount();
                case ADD_MULTIPLIED_TOTAL -> multipliedTotal += entry.getValue().amount();
            }
        }

        return Math.max(100.0D, (base + additive) * (1.0D + multipliedBase) * (1.0D + multipliedTotal));
    }

    private static double getSpellbookAttributeFallback(Player player, net.minecraft.core.Holder<Attribute> attribute, double fallback) {
        TrinketSlotResult result = TrinketsApi.getTrinketsInventory(player)
                .map(inv -> inv.findTrinkets(stack -> stack.getItem() instanceof SpellBook).stream()
                        .filter(entry -> TrinketsSlots.SPELLBOOK_SLOT.equals(entry.slotContext().identifier()))
                        .findFirst()
                        .orElse(null))
                .orElse(null);
        if (result == null) {
            return fallback;
        }
        ItemStack stack = result.stack();
        if (stack.isEmpty() || !(stack.getItem() instanceof ITrinketItem trinketItem)) {
            return fallback;
        }
        var modifiers = trinketItem.getAttributeModifiers(result.slotContext(), IronsSpellbooks.id("client_spell_power_fallback"), stack);
        var attrModifiers = modifiers.get(attribute);
        if (attrModifiers == null || attrModifiers.isEmpty()) {
            return fallback;
        }
        return applyAttributeModifiers(fallback, attrModifiers);
    }

    private static double applyAttributeModifiers(double base, Iterable<AttributeModifier> modifiers) {
        double additive = 0.0D;
        double multipliedBase = 0.0D;
        double multipliedTotal = 0.0D;

        for (AttributeModifier modifier : modifiers) {
            switch (modifier.operation()) {
                case ADD_VALUE -> additive += modifier.amount();
                case ADD_MULTIPLIED_BASE -> multipliedBase += modifier.amount();
                case ADD_MULTIPLIED_TOTAL -> multipliedTotal += modifier.amount();
            }
        }
        return (base + additive) * (1.0D + multipliedBase) * (1.0D + multipliedTotal);
    }
}
