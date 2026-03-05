package io.redspace.ironsspellbooks.item.curios;

import io.redspace.ironsspellbooks.compat.TrinketsSlots;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import io.redspace.ironsspellbooks.compat.trinkets.TrinketSlotContext;

public class PoisonwardRing extends SimpleDescriptiveCurio {
    public PoisonwardRing() {
        super(ItemPropertiesHelper.equipment().stacksTo(1), TrinketsSlots.RING_SLOT);
    }


    public void trinketTick(TrinketSlotContext TrinketSlotContext, ItemStack stack) {
        super.trinketTick(TrinketSlotContext, stack);
        TrinketSlotContext.entity().removeEffect(MobEffects.POISON);
    }

}
