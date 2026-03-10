package io.redspace.ironsspellbooks.content;

import io.redspace.ironsspellbooks.api.events.SpellOnCastEvent;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;
import java.util.UUID;

public interface ContentPack {
    String getId();

    default Component getDisplayName() {
        return Component.literal(getId());
    }

    default void onEnabledChanged(boolean enabled) {
    }

    default void onSpellCast(SpellOnCastEvent event) {
    }

    /**
     * @return A multiplier applied to the spell's base power for the given school.
     */
    default double getBasePowerMultiplier(LivingEntity entity, SchoolType schoolType) {
        return 1.0d;
    }

    default void appendPackData(CommandSourceStack source, List<Component> output, UUID target) {
    }
}
