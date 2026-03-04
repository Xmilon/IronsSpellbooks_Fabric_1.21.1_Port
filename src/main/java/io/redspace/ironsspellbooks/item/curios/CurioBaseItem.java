package io.redspace.ironsspellbooks.item.curios;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import io.redspace.ironsspellbooks.compat.Curios;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
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
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class CurioBaseItem extends Item implements ICurioItem {
    String attributeSlot = "";
    Function<Integer, Multimap<Holder<Attribute>, AttributeModifier>> attributes = null;

    public CurioBaseItem(Item.Properties properties) {
        super(properties);
    }

    public boolean isEquippedBy(@Nullable LivingEntity entity) {
        return entity != null && CuriosApi.getCuriosInventory(entity).map(inv -> inv.findFirstCurio(this).isPresent()).orElse(false);
    }

    @NotNull

    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.ARMOR_EQUIP_CHAIN.value(), 1.0f, 1.0f);
    }


    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        return slotContext.identifier().equals(this.attributeSlot) ? attributes.apply(slotContext.index()) : ICurioItem.super.getAttributeModifiers(slotContext, id, stack);
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
        return withAttributes(Curios.SPELLBOOK_SLOT, attributes);
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

        List<String> slotIds = new ArrayList<>(CuriosApi.getCuriosHelper().getCurioTags(this));
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
                    target.get().handler.setEquippedCurio(slotId, target.get().result.slotContext().index(), stackToEquip);
                    if (!player.getAbilities().instabuild) {
                        heldStack.shrink(1);
                    }
                } else {
                    ItemStack stackToEquip = heldStack.copy();
                    if (player.getAbilities().instabuild) {
                        stackToEquip.setCount(1);
                    }
                    target.get().handler.setEquippedCurio(slotId, target.get().result.slotContext().index(), stackToEquip);
                    player.setItemInHand(hand, equippedItem);
                }
            }

            return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide);
        }

        return super.use(level, player, hand);
    }

    private Optional<SlotChangeTarget> findSlotForUse(Player player, String slotId) {
        return CuriosApi.getCuriosInventory(player).flatMap(handler -> {
            SlotResult firstOccupied = null;
            for (int index = 0; index < 16; index++) {
                Optional<SlotResult> slot = handler.findCurio(slotId, index);
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

    private record SlotChangeTarget(CuriosApi.ICuriosItemHandler handler, SlotResult result) {
    }
}
