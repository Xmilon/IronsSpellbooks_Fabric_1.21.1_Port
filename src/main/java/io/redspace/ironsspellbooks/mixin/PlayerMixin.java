package io.redspace.ironsspellbooks.mixin;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.item.armor.IDisableHat;
import io.redspace.ironsspellbooks.item.armor.IDisableJacket;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerMixin {
    @Inject(method = "createAttributes", at = @At("RETURN"))
    private static void irons_spellbooks$addMagicAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        AttributeSupplier.Builder builder = cir.getReturnValue();
        if (builder == null) {
            return;
        }
        builder.add(AttributeRegistry.MAX_MANA)
                .add(AttributeRegistry.MANA_REGEN)
                .add(AttributeRegistry.COOLDOWN_REDUCTION)
                .add(AttributeRegistry.SPELL_POWER)
                .add(AttributeRegistry.SPELL_RESIST)
                .add(AttributeRegistry.CAST_TIME_REDUCTION)
                .add(AttributeRegistry.SUMMON_DAMAGE)
                .add(AttributeRegistry.CASTING_MOVESPEED)
                .add(AttributeRegistry.FIRE_MAGIC_RESIST)
                .add(AttributeRegistry.ICE_MAGIC_RESIST)
                .add(AttributeRegistry.LIGHTNING_MAGIC_RESIST)
                .add(AttributeRegistry.HOLY_MAGIC_RESIST)
                .add(AttributeRegistry.ENDER_MAGIC_RESIST)
                .add(AttributeRegistry.BLOOD_MAGIC_RESIST)
                .add(AttributeRegistry.EVOCATION_MAGIC_RESIST)
                .add(AttributeRegistry.NATURE_MAGIC_RESIST)
                .add(AttributeRegistry.ELDRITCH_MAGIC_RESIST)
                .add(AttributeRegistry.FIRE_SPELL_POWER)
                .add(AttributeRegistry.ICE_SPELL_POWER)
                .add(AttributeRegistry.LIGHTNING_SPELL_POWER)
                .add(AttributeRegistry.HOLY_SPELL_POWER)
                .add(AttributeRegistry.ENDER_SPELL_POWER)
                .add(AttributeRegistry.BLOOD_SPELL_POWER)
                .add(AttributeRegistry.EVOCATION_SPELL_POWER)
                .add(AttributeRegistry.NATURE_SPELL_POWER)
                .add(AttributeRegistry.ELDRITCH_SPELL_POWER);
    }

    @Inject(method = "canEat", at = @At(value = "RETURN"), cancellable = true)
    void canEatForGluttony(boolean pCanAlwaysEat, CallbackInfoReturnable<Boolean> cir) {
        if (((Player) (Object) this).hasEffect(MobEffectRegistry.GLUTTONY)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "isModelPartShown", at = @At(value = "RETURN"), cancellable = true)
    void irons_spellbooks$hideJacketLayers(PlayerModelPart part, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            var self = (Player) (Object) this;
            switch (part) {
                case PlayerModelPart.HAT:
                    cir.setReturnValue(!(self.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof IDisableHat));
                    break;
                case JACKET:
                case LEFT_SLEEVE:
                case RIGHT_SLEEVE:
                    if (self.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof IDisableJacket chestplate && chestplate.disableForSlot(EquipmentSlot.CHEST)) {
                        cir.setReturnValue(false);
                    }
                    break;
                case LEFT_PANTS_LEG:
                case RIGHT_PANTS_LEG:
                    if ((self.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof IDisableJacket leggings && leggings.disableForSlot(EquipmentSlot.LEGS))
                            || (self.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof IDisableJacket boots && boots.disableForSlot(EquipmentSlot.FEET))) {
                        cir.setReturnValue(false);
                    }
                    break;
            }
        }
    }
}
