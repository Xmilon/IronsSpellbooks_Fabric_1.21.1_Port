package io.redspace.ironsspellbooks.entity.mobs.wizards;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.NeutralWizard;
import io.redspace.ironsspellbooks.util.ModTags;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

import java.util.List;

@EventBusSubscriber
public class WizardAIEvents {

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        // NeoForge-only block event API is not wired in the Fabric port shim yet.
    }

    @SubscribeEvent
    public static void onBlockUsed(PlayerInteractEvent.RightClickBlock event) {
        // NeoForge-only block interaction event API is not wired in the Fabric port shim yet.
    }

    public static void angerNearbyWizards(Player player, int angerLevel, boolean requireLineOfSight, boolean blockRelated) {
        if (player.getAbilities().instabuild) {
            return;
        }
        List<NeutralWizard> list = player.level().getEntitiesOfClass(NeutralWizard.class, player.getBoundingBox().inflate(16.0D));
        list.stream().filter((neutralWizard) -> (neutralWizard.guardsBlocks() || !blockRelated) && (!requireLineOfSight || BehaviorUtils.canSee(neutralWizard, player))).forEach((neutralWizard) -> {
            neutralWizard.increaseAngerLevel(player, angerLevel, true);
            neutralWizard.setPersistentAngerTarget(player.getUUID());
            if (blockRelated && player instanceof ServerPlayer serverPlayer) {
                var advancement = serverPlayer.serverLevel().getServer().getAdvancements().get(IronsSpellbooks.id("irons_spellbooks/steal_from_wizard"));
                if (advancement != null) {
                    serverPlayer.getAdvancements().award(advancement, "anger_wizard");
                }
            }
        });
    }

}



