package io.redspace.ironsspellbooks.player;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Enemy;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;

@EventBusSubscriber(modid = IronsSpellbooks.MODID)
public final class ServerPlayerEvents {
    private ServerPlayerEvents() {
    }

    @SubscribeEvent
    public static void onLivingChangeTarget(LivingChangeTargetEvent event) {
        var newTarget = event.getNewAboutToBeSetTarget();
        var entity = event.getEntity();
        if (newTarget == null) {
            return;
        }
        // Prevent neutral/ally AI from hard-targeting enemy summons unless already engaged.
        if (newTarget instanceof IMagicSummon summon && summon instanceof Enemy && entity instanceof Mob mob && !entity.equals(mob.getTarget())) {
            event.setCanceled(true);
            return;
        }
        // True invisibility should prevent mobs from acquiring the target.
        if (newTarget.hasEffect(MobEffectRegistry.TRUE_INVISIBILITY)) {
            event.setCanceled(true);
        }
    }
}
