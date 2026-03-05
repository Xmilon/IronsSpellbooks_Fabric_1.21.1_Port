package io.redspace.ironsspellbooks.item.curios;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import io.redspace.ironsspellbooks.compat.TrinketsSlots;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import io.redspace.ironsspellbooks.compat.trinkets.TrinketsApi;
import io.redspace.ironsspellbooks.compat.trinkets.TrinketSlotContext;
import io.redspace.ironsspellbooks.compat.trinkets.TrinketSlotResult;
import io.redspace.ironsspellbooks.compat.trinkets.ITrinket;
import io.redspace.ironsspellbooks.compat.trinkets.ITrinketItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class CurioBaseItem extends Item implements ITrinketItem {
    String attributeSlot = "";
    Function<Integer, Multimap<Holder<Attribute>, AttributeModifier>> attributes = null;

    public CurioBaseItem(Item.Properties properties) {
        super(properties);
    }

    public boolean isEquippedBy(@Nullable LivingEntity entity) {
        return entity != null && TrinketsApi.getTrinketsInventory(entity).map(inv -> inv.findFirstTrinket(this).isPresent()).orElse(false);
    }

    @NotNull

    public ITrinket.SoundInfo getEquipSound(TrinketSlotContext TrinketSlotContext, ItemStack stack) {
        return new ITrinket.SoundInfo(SoundEvents.ARMOR_EQUIP_CHAIN.value(), 1.0f, 1.0f);
    }


    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(TrinketSlotContext TrinketSlotContext, ResourceLocation id, ItemStack stack) {
        return TrinketSlotContext.identifier().equals(this.attributeSlot) ? attributes.apply(TrinketSlotContext.index()) : ITrinketItem.super.getAttributeModifiers(TrinketSlotContext, id, stack);
    }

    public CurioBaseItem withAttributes(String slot, AttributeContainer... attributes) {
        this.attributeSlot = slot;
        this.attributes = (index) -> {
            ImmutableMultimap.Builder<Holder<Attribute>, AttributeModifier> builder = ImmutableMultimap.builder();
            for (AttributeContainer holder : attributes) {
                String id = String.format("%s_%s", attributeSlot, index);
                builder.put(holder.attribute(), holder.createModifier(id));
            }
            return builder.build();
        };
        return this;
    }

    public CurioBaseItem withSpellbookAttributes(AttributeContainer... attributes) {
        return withAttributes(TrinketsSlots.SPELLBOOK_SLOT, attributes);
    }

    public String getCurioSlotId() {
        return attributeSlot == null ? "" : attributeSlot;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack heldStack = player.getItemInHand(hand);
        if (heldStack.isEmpty()) {
            return super.use(level, player, hand);
        }

        List<String> slotIds = new ArrayList<>(TrinketsApi.getTrinketsHelper().getTrinketTags(this));
        if (slotIds.isEmpty() && !getCurioSlotId().isBlank()) {
            slotIds.add(getCurioSlotId());
        }
        if (slotIds.isEmpty()) {
            return super.use(level, player, hand);
        }

        for (String slotId : slotIds) {
            Optional<SlotChangeTarget> target = findSlotForUse(player, slotId);
            if (target.isEmpty()) {
                continue;
            }

            if (!level.isClientSide) {
                ItemStack equippedItem = target.get().result.stack().copy();
                if (equippedItem.isEmpty()) {
                    ItemStack stackToEquip = heldStack.copy();
                    stackToEquip.setCount(1);
                    target.get().handler.setEquippedTrinket(slotId, target.get().result.slotContext().index(), stackToEquip);
                    playEquipSound(player, target.get().result.slotContext(), stackToEquip);
                    if (!player.getAbilities().instabuild) {
                        heldStack.shrink(1);
                    }
                } else {
                    ItemStack stackToEquip = heldStack.copy();
                    if (player.getAbilities().instabuild) {
                        stackToEquip.setCount(1);
                    }
                    target.get().handler.setEquippedTrinket(slotId, target.get().result.slotContext().index(), stackToEquip);
                    player.setItemInHand(hand, equippedItem);
                    playEquipSound(player, target.get().result.slotContext(), stackToEquip);
                }
            }

            return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide);
        }

        return super.use(level, player, hand);
    }

    private Optional<SlotChangeTarget> findSlotForUse(Player player, String slotId) {
        return TrinketsApi.getTrinketsInventory(player).flatMap(handler -> {
            TrinketSlotResult firstOccupied = null;
            for (int index = 0; index < 16; index++) {
                Optional<TrinketSlotResult> slot = handler.findTrinket(slotId, index);
                if (slot.isEmpty()) {
                    if (index == 0) {
                        return Optional.empty();
                    }
                    break;
                }
                if (slot.get().stack().isEmpty()) {
                    return Optional.of(new SlotChangeTarget(handler, slot.get()));
                }
                if (firstOccupied == null) {
                    firstOccupied = slot.get();
                }
            }
            return firstOccupied == null ? Optional.empty() : Optional.of(new SlotChangeTarget(handler, firstOccupied));
        });
    }

    private void playEquipSound(Player player, TrinketSlotContext TrinketSlotContext, ItemStack stack) {
        ITrinket.SoundInfo soundInfo = getEquipSound(TrinketSlotContext, stack);
        if (soundInfo == null || soundInfo.soundEvent == null) {
            soundInfo = new ITrinket.SoundInfo(SoundEvents.ARMOR_EQUIP_CHAIN.value(), 1.0f, 1.0f);
        }
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), soundInfo.soundEvent, SoundSource.PLAYERS, soundInfo.volume, soundInfo.pitch);
    }

    private record SlotChangeTarget(TrinketsApi.ITrinketsItemHandler handler, TrinketSlotResult result) {
    }
}
