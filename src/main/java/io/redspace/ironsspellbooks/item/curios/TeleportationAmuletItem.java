package io.redspace.ironsspellbooks.item.curios;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.compat.TrinketsSlots;
import io.redspace.ironsspellbooks.util.MinecraftInstanceHelper;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import io.redspace.ironsspellbooks.compat.trinkets.TrinketsApi;
import io.redspace.ironsspellbooks.compat.trinkets.TrinketSlotContext;

import java.util.List;

public class TeleportationAmuletItem extends SimpleDescriptiveCurio {
    private static final Component VANITY_DESCRIPTION = Component.translatable("item.irons_spellbooks.teleportation_amulet.desc.alt").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC);

    public TeleportationAmuletItem(Properties properties) {
        super(properties, TrinketsSlots.NECKLACE_SLOT);
    }

    private void handleCurse(TrinketSlotContext TrinketSlotContext, ItemStack stack) {
        var entity = TrinketSlotContext.entity();
        if (entity != null && !TrinketSlotContext.entity().level().isClientSide && !canUse(entity)) {
            TrinketsApi.getTrinketsInventory(TrinketSlotContext.entity()).ifPresent(
                    handler ->
                    {
                        var equippedStack = handler.getEquippedTrinkets().getStackInSlot(TrinketSlotContext.index());
                        if (ItemStack.matches(stack, equippedStack)) {
                            handler.setEquippedTrinket(TrinketsSlots.NECKLACE_SLOT, TrinketSlotContext.index(), ItemStack.EMPTY);
                            createItemEntity(TrinketSlotContext.entity().level(), stack, TrinketSlotContext.entity().position());
                        }
                    }
            );
        }
    }

    @Override
    public List<Component> getAttributesTooltip(List<Component> tooltips, TooltipContext tooltipContext, ItemStack stack) {

        var player = MinecraftInstanceHelper.getPlayer();
        if (player != null) {
            if (canUse(player)) {
                super.getAttributesTooltip(tooltips, tooltipContext, stack);
            }
        }
        tooltips.add(0, VANITY_DESCRIPTION);
        return tooltips;
    }


    public void trinketTick(TrinketSlotContext TrinketSlotContext, ItemStack stack) {
        super.trinketTick(TrinketSlotContext, stack);
        //Using short modulo on tick because #onEquip fires before the living entity's attributes are calculated on relog/entity creation, meaning the curse will erroneously fire
        if (TrinketSlotContext.entity().tickCount % 5 == 0) {
            handleCurse(TrinketSlotContext, stack);
        }
    }

    private boolean canUse(LivingEntity livingEntity) {
        return AttributeRegistry.getValueOrDefault(livingEntity, AttributeRegistry.ENDER_SPELL_POWER, 1.0D) > 1.25;
    }

    private void createItemEntity(Level level, ItemStack stack, Vec3 center) {
        Vec3 target = center.add(new Vec3(Utils.random.nextIntBetweenInclusive(4, 8) + Utils.random.nextFloat(), 0, 0).yRot(Utils.random.nextFloat() * Mth.TWO_PI));
        Vec3 clipped = Utils.raycastForBlock(level, center.add(0, 0.5, 0), target.add(0, 0.5, 0), ClipContext.Fluid.NONE).getLocation();
        Vec3 placement = Utils.moveToRelativeGroundLevel(level, clipped, 5).add(0, 0.75, 0);
        var item = new ItemEntity(level, placement.x, placement.y, placement.z, stack);
        level.addFreshEntity(item);
        MagicManager.spawnParticles(level, ParticleHelper.UNSTABLE_ENDER, placement.x, placement.y, placement.z, 20, 0.2, 0.2, 0.2, 0.2, false);
        level.playSound(null, BlockPos.containing(placement), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1, 1);
        level.playSound(null, BlockPos.containing(center), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1, 1);
    }
}


