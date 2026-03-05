package io.redspace.ironsspellbooks.item.curios;

import io.redspace.ironsspellbooks.compat.TrinketsSlots;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import io.redspace.ironsspellbooks.compat.trinkets.TrinketSlotContext;

public class VisibilityRing extends SimpleDescriptiveCurio {
    public VisibilityRing() {
        super(ItemPropertiesHelper.equipment().stacksTo(1), TrinketsSlots.RING_SLOT);
    }


    public void trinketTick(TrinketSlotContext TrinketSlotContext, ItemStack stack) {
        super.trinketTick(TrinketSlotContext, stack);
        TrinketSlotContext.entity().removeEffect(MobEffects.BLINDNESS);
        TrinketSlotContext.entity().removeEffect(MobEffects.DARKNESS);
    }

}
