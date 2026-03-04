package io.redspace.ironsspellbooks.effect;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;


public class PlanarSightEffect extends MagicMobEffect implements ISyncedMobEffect {
    public PlanarSightEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }

    public boolean shouldApplyEffectTickThisTick(int pDuration, int pAmplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int pAmplifier) {
        if (livingEntity.level().isClientSide && livingEntity == Minecraft.getInstance().player) {
            for (int i = 0; i < 3; i++) {
                Vec3 pos = new Vec3(Utils.getRandomScaled(16), Utils.getRandomScaled(5f) + 5, Utils.getRandomScaled(16)).add(livingEntity.position());
                Vec3 random = new Vec3(Utils.getRandomScaled(.08f), Utils.getRandomScaled(.08f), Utils.getRandomScaled(.08f));
                livingEntity.level().addParticle(ParticleTypes.WHITE_ASH, pos.x, pos.y, pos.z, random.x, random.y, random.z);
            }
        }
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    public static class EcholocationBlindnessFogFunction {
        public Holder<MobEffect> getMobEffect() {
            return MobEffectRegistry.PLANAR_SIGHT;
        }
    }
}

