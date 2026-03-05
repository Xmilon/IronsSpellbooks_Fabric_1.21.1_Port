package io.redspace.ironsspellbooks.item.curios;

import io.redspace.ironsspellbooks.compat.TrinketsSlots;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import io.redspace.ironsspellbooks.compat.trinkets.TrinketSlotContext;

public class FrostwardRing extends SimpleDescriptiveCurio {
    public FrostwardRing() {
        super(ItemPropertiesHelper.equipment().stacksTo(1), TrinketsSlots.RING_SLOT);
    }


    public void trinketTick(TrinketSlotContext TrinketSlotContext, ItemStack stack) {
        super.trinketTick(TrinketSlotContext, stack);
        TrinketSlotContext.entity().setTicksFrozen(0);
    }


    public boolean canWalkOnPowderedSnow(ItemStack stack, LivingEntity wearer) {
        return true;
    }
}
