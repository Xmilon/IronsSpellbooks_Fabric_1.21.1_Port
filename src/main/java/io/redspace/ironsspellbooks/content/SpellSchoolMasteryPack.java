package io.redspace.ironsspellbooks.content;

import io.redspace.ironsspellbooks.api.events.SpellOnCastEvent;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class SpellSchoolMasteryPack implements ContentPack {
    public static final String ID = "spell_school_mastery";
    private static final int CASTS_PER_BONUS = 15;
    private static final double BONUS_STEP = 0.015d;

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Spell School Mastery");
    }

    @Override
    public void onSpellCast(SpellOnCastEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        SchoolType schoolType = event.getSchoolType();
        if (schoolType == null || schoolType.getId() == null) {
            return;
        }
        String schoolId = schoolType.getId().toString();
        int newCount = SpellSchoolMasteryStore.INSTANCE.incrementCast(player.getUUID(), player.getGameProfile().getName(), schoolId);
        if (newCount % CASTS_PER_BONUS == 0) {
            double newBonus = SpellSchoolMasteryStore.INSTANCE.addBonus(player.getUUID(), player.getGameProfile().getName(), schoolId, BONUS_STEP);
            applyMasteryBonus(player, schoolType, newBonus);
            player.displayClientMessage(buildIncreaseMessage(schoolType, newBonus), false);
        }
    }

    @Override
    public double getBasePowerMultiplier(LivingEntity entity, SchoolType schoolType) {
        return 1.0d;
    }

    @Override
    public void onEnabledChanged(boolean enabled) {
        if (IronsSpellbooks.MCS == null) {
            return;
        }
        for (ServerPlayer player : IronsSpellbooks.MCS.getPlayerList().getPlayers()) {
            if (enabled) {
                applyAllBonuses(player);
            } else {
                removeAllBonuses(player);
            }
        }
    }

    @Override
    public void appendPackData(CommandSourceStack source, List<Component> output, UUID target) {
        if (target != null) {
            appendPlayerData(source, output, target);
            return;
        }
        Map<UUID, SpellSchoolMasteryStore.PlayerMasteryData> allData = SpellSchoolMasteryStore.INSTANCE.getAll();
        if (allData.isEmpty()) {
            output.add(Component.literal("No mastery data saved."));
            return;
        }
        for (UUID uuid : allData.keySet()) {
            appendPlayerData(source, output, uuid);
        }
    }

    private static Component buildIncreaseMessage(SchoolType schoolType, double totalBonus) {
        return Component.literal("School mastery increased: ")
                .append(schoolType.getDisplayName().copy())
                .append(Component.literal(String.format(Locale.ROOT, " +%.1f%% (total %.1f%%)", BONUS_STEP * 100d, totalBonus * 100d)));
    }

    private void appendPlayerData(CommandSourceStack source, List<Component> output, UUID uuid) {
        var data = SpellSchoolMasteryStore.INSTANCE.get(uuid);
        if (data == null) {
            output.add(Component.literal("No mastery data for " + uuid + "."));
            return;
        }
        String name = data.getLastKnownName();
        Component header = Component.literal("Player: ").append(Component.literal(name == null || name.isBlank() ? uuid.toString() : name));
        output.add(header);
        for (SchoolType school : SchoolRegistry.REGISTRY) {
            String schoolId = school.getId().toString();
            int count = SpellSchoolMasteryStore.INSTANCE.getCastCount(uuid, schoolId);
            double bonus = SpellSchoolMasteryStore.INSTANCE.getBonus(uuid, schoolId);
            int next = CASTS_PER_BONUS - (count % CASTS_PER_BONUS);
            String line = String.format(
                    Locale.ROOT,
                    "  %s - casts: %d, bonus: %.1f%%, next in: %d",
                    school.getDisplayName().getString(),
                    count,
                    bonus * 100d,
                    next == 0 ? CASTS_PER_BONUS : next
            );
            output.add(Component.literal(line).withStyle(school.getDisplayName().getStyle()));
        }
    }

    public static void applyAllBonuses(ServerPlayer player) {
        for (SchoolType school : SchoolRegistry.REGISTRY) {
            if (school.getId() == null) {
                continue;
            }
            SpellSchoolMasteryStore.INSTANCE.ensureSchoolInitialized(
                    player.getUUID(),
                    player.getGameProfile().getName(),
                    school.getId().toString()
            );
            double bonus = SpellSchoolMasteryStore.INSTANCE.getBonus(player.getUUID(), school.getId().toString());
            applyMasteryBonus(player, school, bonus);
        }
    }

    public static void removeAllBonuses(ServerPlayer player) {
        for (SchoolType school : SchoolRegistry.REGISTRY) {
            removeMasteryBonus(player, school);
        }
    }

    public static void applyMasteryBonus(ServerPlayer player, SchoolType school, double bonus) {
        if (school == null || school.getId() == null) {
            return;
        }
        AttributeInstance instance = player.getAttribute(school.getPowerAttribute());
        if (instance == null) {
            return;
        }
        ResourceLocation id = masteryModifierId(school.getId());
        instance.removeModifier(id);
        if (bonus > 0d) {
            AttributeModifier modifier = new AttributeModifier(id, bonus, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
            instance.addPermanentModifier(modifier);
        }
    }

    private static void removeMasteryBonus(ServerPlayer player, SchoolType school) {
        if (school == null || school.getId() == null) {
            return;
        }
        AttributeInstance instance = player.getAttribute(school.getPowerAttribute());
        if (instance == null) {
            return;
        }
        instance.removeModifier(masteryModifierId(school.getId()));
    }

    private static ResourceLocation masteryModifierId(ResourceLocation schoolId) {
        return ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, "mastery/" + schoolId.getNamespace() + "/" + schoolId.getPath());
    }
}
