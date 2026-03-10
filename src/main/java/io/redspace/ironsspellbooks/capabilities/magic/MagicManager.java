package io.redspace.ironsspellbooks.capabilities.magic;

import io.redspace.ironsspellbooks.api.events.SpellCooldownAddedEvent;
import io.redspace.ironsspellbooks.api.magic.IMagicManager;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.item.Scroll;
import io.redspace.ironsspellbooks.network.EquipmentChangedPacket;
import io.redspace.ironsspellbooks.network.SyncManaPacket;
import io.redspace.ironsspellbooks.network.casting.SyncCooldownPacket;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static io.redspace.ironsspellbooks.api.registry.AttributeRegistry.*;

public class MagicManager implements IMagicManager {
    public static final int MANA_REGEN_TICKS = 10;
    public static final int CONTINUOUS_CAST_TICK_INTERVAL = 10;
    private static final float MANA_SYNC_EPSILON = 0.001f;
    private final Map<UUID, Integer> lastKnownMaxMana = new HashMap<>();
    private final Map<UUID, Float> lastKnownMana = new HashMap<>();
    private final Map<UUID, Integer> lastKnownSpellListHash = new HashMap<>();

    public boolean regenPlayerMana(ServerPlayer serverPlayer, MagicData playerMagicData, int playerMaxMana) {
        var mana = playerMagicData.getMana();
        if (mana != playerMaxMana) {
            float playerManaRegenMultiplier = (float) io.redspace.ironsspellbooks.api.registry.AttributeRegistry.getValueOrDefault(serverPlayer, MANA_REGEN, 1.0D);
            var increment = playerMaxMana * playerManaRegenMultiplier * .01f * ServerConfigs.safeGet(ServerConfigs.MANA_REGEN_MULTIPLIER).floatValue();
            playerMagicData.setMana(Mth.clamp(playerMagicData.getMana() + increment, 0, playerMaxMana));
            return true;
        } else {
            return false;
        }
    }


    public void tick(Level level) {
        boolean doManaRegen = level.getServer().getTickCount() % MANA_REGEN_TICKS == 0;
        var activePlayers = level.players().stream()
                .filter(ServerPlayer.class::isInstance)
                .map(player -> player.getUUID())
                .collect(java.util.stream.Collectors.toSet());
        lastKnownMaxMana.keySet().retainAll(activePlayers);
        lastKnownMana.keySet().retainAll(activePlayers);
        lastKnownSpellListHash.keySet().retainAll(activePlayers);

        level.players().stream().toList().forEach(player -> {
            if (player instanceof ServerPlayer serverPlayer) {
                MagicData playerMagicData = MagicData.getPlayerMagicData(serverPlayer);
                int playerMaxMana = (int) io.redspace.ironsspellbooks.api.registry.AttributeRegistry.getMaxManaWithFallback(serverPlayer);
                playerMagicData.getPlayerCooldowns().tick(1);
                playerMagicData.getPlayerRecasts().tick(2);
                boolean shouldSyncMana = false;

                int previousMaxMana = lastKnownMaxMana.getOrDefault(serverPlayer.getUUID(), -1);
                if (previousMaxMana != playerMaxMana) {
                    lastKnownMaxMana.put(serverPlayer.getUUID(), playerMaxMana);
                    playerMagicData.setMana(Mth.clamp(playerMagicData.getMana(), 0, playerMaxMana));
                    shouldSyncMana = true;
                }

                if (playerMagicData.isCasting()) {
                    playerMagicData.handleCastDuration();
                    var spell = SpellRegistry.getSpell(playerMagicData.getCastingSpellId());
                    if ((spell.getCastType() == CastType.LONG && !serverPlayer.isUsingItem()) || spell.getCastType() == CastType.INSTANT) {
                        if (playerMagicData.getCastDurationRemaining() <= 0) {
                            spell.castSpell(serverPlayer.level(), playerMagicData.getCastingSpellLevel(), serverPlayer, playerMagicData.getCastSource(), true);
                            if (playerMagicData.getCastSource() == CastSource.SCROLL) {
                                Scroll.attemptRemoveScrollAfterCast(serverPlayer);
                            }
                            spell.onServerCastComplete(serverPlayer.level(), playerMagicData.getCastingSpellLevel(), serverPlayer, playerMagicData, false);
                        }
                    } else if (spell.getCastType() == CastType.CONTINUOUS) {
                        if ((playerMagicData.getCastDurationRemaining() + 1) % CONTINUOUS_CAST_TICK_INTERVAL == 0) {
                            if (playerMagicData.getCastDurationRemaining() < CONTINUOUS_CAST_TICK_INTERVAL || (playerMagicData.getCastSource().consumesMana() && playerMagicData.getMana() - spell.getManaCost(playerMagicData.getCastingSpellLevel()) * 2 < 0)) {
                                spell.castSpell(serverPlayer.level(), playerMagicData.getCastingSpellLevel(), serverPlayer, playerMagicData.getCastSource(), true);

                                if (playerMagicData.getCastSource() == CastSource.SCROLL) {
                                    Scroll.attemptRemoveScrollAfterCast(serverPlayer);
                                }

                                spell.onServerCastComplete(serverPlayer.level(), playerMagicData.getCastingSpellLevel(), serverPlayer, playerMagicData, false);

                            } else {
                                spell.castSpell(serverPlayer.level(), playerMagicData.getCastingSpellLevel(), serverPlayer, playerMagicData.getCastSource(), false);
                            }
                        }
                    }

                    if (playerMagicData.isCasting()) {
                        spell.onServerCastTick(serverPlayer.level(), playerMagicData.getCastingSpellLevel(), serverPlayer, playerMagicData);
                    }
                }

                if (doManaRegen) {
                    if (regenPlayerMana(serverPlayer, playerMagicData, playerMaxMana)) {
                        shouldSyncMana = true;
                    }

                    int currentSpellListHash = hashSpellList(serverPlayer);
                    int previousSpellListHash = lastKnownSpellListHash.getOrDefault(serverPlayer.getUUID(), Integer.MIN_VALUE);
                    if (currentSpellListHash != previousSpellListHash) {
                        lastKnownSpellListHash.put(serverPlayer.getUUID(), currentSpellListHash);
                        playerMagicData.getSyncedData().syncToPlayer(serverPlayer);
                        PacketDistributor.sendToPlayer(serverPlayer, new EquipmentChangedPacket());
                    }
                }

                float currentMana = playerMagicData.getMana();
                float previousMana = lastKnownMana.getOrDefault(serverPlayer.getUUID(), Float.NaN);
                if (Float.isNaN(previousMana) || Math.abs(previousMana - currentMana) > MANA_SYNC_EPSILON) {
                    shouldSyncMana = true;
                }
                lastKnownMana.put(serverPlayer.getUUID(), currentMana);

                if (shouldSyncMana) {
                    PacketDistributor.sendToPlayer(serverPlayer, new SyncManaPacket(playerMagicData, serverPlayer));
                }
            }
        });
    }

    public void addCooldown(ServerPlayer serverPlayer, AbstractSpell spell, CastSource castSource) {
        int effectiveCooldown = getEffectiveSpellCooldown(spell, serverPlayer, castSource);
        var pre = NeoForge.EVENT_BUS.post(new SpellCooldownAddedEvent.Pre(effectiveCooldown, spell, serverPlayer, castSource));

        if (castSource == CastSource.SCROLL || pre.isCanceled()) {
            return;
        }

        effectiveCooldown = pre.getEffectiveCooldown();

        MagicData.getPlayerMagicData(serverPlayer).getPlayerCooldowns().addCooldown(spell, effectiveCooldown);
        PacketDistributor.sendToPlayer(serverPlayer, new SyncCooldownPacket(spell.getSpellId(), effectiveCooldown));

        NeoForge.EVENT_BUS.post(new SpellCooldownAddedEvent.Post(effectiveCooldown, spell, serverPlayer, castSource));
    }

    public void clearCooldowns(ServerPlayer serverPlayer) {
        MagicData.getPlayerMagicData(serverPlayer).getPlayerCooldowns().clearCooldowns();
        MagicData.getPlayerMagicData(serverPlayer).getPlayerCooldowns().syncToPlayer(serverPlayer);
    }

    public static int getEffectiveSpellCooldown(AbstractSpell spell, Player player, CastSource castSource) {
        double playerCooldownModifier = io.redspace.ironsspellbooks.api.registry.AttributeRegistry.getValueOrDefault(player, COOLDOWN_REDUCTION, 1.0D);

        float itemCoolDownModifer = 1;
        if (castSource == CastSource.SWORD) {
            itemCoolDownModifer = ServerConfigs.safeGet(ServerConfigs.SWORDS_CD_MULTIPLIER).floatValue();
        }
        return (int) (spell.getSpellCooldown() * (2 - Utils.softCapFormula(playerCooldownModifier)) * itemCoolDownModifer);
    }

    public static void spawnParticles(Level level, ParticleOptions particle, double x, double y, double z, int count, double deltaX, double deltaY, double deltaZ, double speed, boolean force) {
        level.getServer().getPlayerList().getPlayers().forEach(player -> ((ServerLevel) level).sendParticles(player, particle, force, x, y, z, count, deltaX, deltaY, deltaZ, speed));
    }

    private static int hashSpellList(ServerPlayer player) {
        SpellSelectionManager selectionManager = new SpellSelectionManager(player);
        int hash = 1;
        for (SpellSelectionManager.SelectionOption option : selectionManager.getAllSpells()) {
            hash = 31 * hash + option.slot.hashCode();
            hash = 31 * hash + option.slotIndex;
            hash = 31 * hash + option.spellData.getSpell().getSpellId().hashCode();
            hash = 31 * hash + option.spellData.getLevel();
        }
        return hash;
    }
}


