package io.redspace.ironsspellbooks.api.magic;

import io.redspace.ironsspellbooks.api.events.ChangeManaEvent;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.capabilities.magic.PlayerCooldowns;
import io.redspace.ironsspellbooks.capabilities.magic.PlayerRecasts;
import io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public class MagicData {

    private boolean isMob = false;

    public MagicData(boolean isMob) {
        this.isMob = isMob;
    }

    public MagicData() {
        this(false);
    }

    public MagicData(ServerPlayer serverPlayer) {
        this(false);
        this.serverPlayer = serverPlayer;
        this.playerRecasts = new PlayerRecasts(serverPlayer);
    }

    public void setServerPlayer(ServerPlayer serverPlayer) {
        if (this.serverPlayer == null && serverPlayer != null) {
            this.serverPlayer = serverPlayer;
            this.playerRecasts = new PlayerRecasts(serverPlayer);
        }
    }

    private ServerPlayer serverPlayer = null;
    public static final String MANA = "mana";
    public static final String COOLDOWNS = "cooldowns";
    public static final String RECASTS = "recasts";
    public static final String SCHOOL_CAST_COUNTS = "school_cast_counts";
    public static final String SCHOOL_POWER_BONUSES = "school_power_bonuses";

    /********* MANA *******************************************************/

    private float mana;

    public float getMana() {
        return mana;
    }

    public void setMana(float mana) {
        //Event will not get posted if the server player is null
        ChangeManaEvent e = new ChangeManaEvent(this.serverPlayer, this, this.mana, mana);
        if (this.serverPlayer == null || !NeoForge.EVENT_BUS.post(e).isCanceled()) {
            this.mana = e.getNewMana();
        }
        if (this.serverPlayer != null) {
            float maxMana = (float) AttributeRegistry.getMaxManaWithFallback(serverPlayer);
            if (this.mana > maxMana) {
                this.mana = maxMana;
            }
        }
    }

    /**
     * Client sync path: trust server-sent value and avoid local attribute clamping.
     */
    public void setSyncedMana(float mana) {
        this.mana = mana;
    }

    public void addMana(float mana) {
        setMana(this.mana + mana);
    }

    /********* SYNC DATA *******************************************************/

    private SyncedSpellData syncedSpellData;

    public SyncedSpellData getSyncedData() {
        if (syncedSpellData == null) {
            syncedSpellData = new SyncedSpellData(serverPlayer);
        }

        return syncedSpellData;
    }

    public void setSyncedData(SyncedSpellData syncedSpellData) {
        this.syncedSpellData = syncedSpellData;
    }

    /********* CASTING *******************************************************/

    private int castingSpellLevel = 0;
    private int castDuration = 0;
    private int castDurationRemaining = 0;
    private CastSource castSource;
    private CastType castType;
    private @Nullable ICastData additionalCastData;
    private int poisonedTimestamp; //Poison does not have a damage source, so we mark when we are poisoned to ignore if instead of cancelling our long cast

    private ItemStack castingItemStack = ItemStack.EMPTY;


    public void resetCastingState() {
        //Ironsspellbooks.logger.debug("PlayerMagicData.resetCastingState: serverPlayer:{}", serverPlayer);
        this.castingSpellLevel = 0;
        this.castDuration = 0;
        this.castDurationRemaining = 0;
        this.castSource = CastSource.NONE;
        this.castType = CastType.NONE;
        this.getSyncedData().setIsCasting(false, "", 0, getCastingEquipmentSlot());
        resetAdditionalCastData();

        if (serverPlayer != null) {
            serverPlayer.stopUsingItem();
        }
    }

    public void initiateCast(AbstractSpell spell, int spellLevel, int castDuration, CastSource castSource, String castingEquipmentSlot) {
        this.castingSpellLevel = spellLevel;
        this.castDuration = castDuration;
        this.castDurationRemaining = castDuration;
        this.castSource = castSource;
        this.castType = spell.getCastType();
        this.syncedSpellData.setIsCasting(true, spell.getSpellId(), spellLevel, castingEquipmentSlot);
    }

    public ICastData getAdditionalCastData() {
        return additionalCastData;
    }

    public void setAdditionalCastData(ICastData newCastData) {
        additionalCastData = newCastData;
    }

    public void resetAdditionalCastData() {
        if (additionalCastData != null) {
            additionalCastData.reset();
            additionalCastData = null;
        }
    }

    public boolean isCasting() {
        return getSyncedData().isCasting();
    }

    public String getCastingEquipmentSlot() {
        return getSyncedData().getCastingEquipmentSlot();
    }

    public String getCastingSpellId() {
        return getSyncedData().getCastingSpellId();
    }

    public SpellData getCastingSpell() {
        return new SpellData(SpellRegistry.getSpell(getSyncedData().getCastingSpellId()), castingSpellLevel);
    }

    public int getCastingSpellLevel() {
        return castingSpellLevel;
    }

    public CastSource getCastSource() {
        if (castSource == null) {
            return CastSource.NONE;
        }

        return castSource;
    }

    public CastType getCastType() {
        return castType;
    }

    public float getCastCompletionPercent() {
        if (castDuration == 0) {
            return 1;
        }

        return 1 - (castDurationRemaining / (float) castDuration);
    }

    public int getCastDurationRemaining() {
        return castDurationRemaining;
    }

    public int getCastDuration() {
        return castDuration;
    }

    public void handleCastDuration() {
        castDurationRemaining--;

        if (castDurationRemaining <= 0) {
            castDurationRemaining = 0;
        }
    }

    public void setPlayerCastingItem(ItemStack itemStack) {
        this.castingItemStack = itemStack;
    }

    public ItemStack getPlayerCastingItem() {
        return this.castingItemStack;
    }

    public void markPoisoned() {
        if (this.serverPlayer != null) {
            this.poisonedTimestamp = serverPlayer.tickCount;
        }
    }

    public boolean popMarkedPoison() {
        if (this.serverPlayer != null) {
            boolean poisoned = this.serverPlayer.tickCount - poisonedTimestamp <= 1;
            //reset so magic damage on the same tick does not get marked as poison
            poisonedTimestamp = 0;
            return poisoned;
        }
        return false;
    }

    /********* COOLDOWNS *******************************************************/

    private final PlayerCooldowns playerCooldowns = new PlayerCooldowns();

    public PlayerCooldowns getPlayerCooldowns() {
        return this.playerCooldowns;
    }

    /********* RECASTS *******************************************************/

    private PlayerRecasts playerRecasts = new PlayerRecasts();

    public PlayerRecasts getPlayerRecasts() {
        // mobs cannot support the more advanced state tracking of recasts, provide no-op data holder instead
        // preserves maximum functionality
        return isMob ? new PlayerRecasts() : this.playerRecasts;
    }

    /********* SPELL SCHOOL MASTERY *******************************************************/

    private final Map<String, Integer> schoolCastCounts = new HashMap<>();
    private final Map<String, Double> schoolPowerBonuses = new HashMap<>();

    public int getSchoolCastCount(SchoolType schoolType) {
        if (schoolType == null || schoolType.getId() == null) {
            return 0;
        }
        return schoolCastCounts.getOrDefault(schoolType.getId().toString(), 0);
    }

    public double getSchoolPowerBonus(SchoolType schoolType) {
        if (schoolType == null || schoolType.getId() == null) {
            return 0d;
        }
        return schoolPowerBonuses.getOrDefault(schoolType.getId().toString(), 0d);
    }

    public int incrementSchoolCastCount(SchoolType schoolType) {
        if (schoolType == null || schoolType.getId() == null) {
            return 0;
        }
        String key = schoolType.getId().toString();
        int next = schoolCastCounts.getOrDefault(key, 0) + 1;
        schoolCastCounts.put(key, next);
        return next;
    }

    public double addSchoolPowerBonus(SchoolType schoolType, double bonusDelta) {
        if (schoolType == null || schoolType.getId() == null) {
            return 0d;
        }
        String key = schoolType.getId().toString();
        double next = schoolPowerBonuses.getOrDefault(key, 0d) + bonusDelta;
        schoolPowerBonuses.put(key, next);
        return next;
    }

    @OnlyIn(Dist.CLIENT)
    public void setPlayerRecasts(PlayerRecasts playerRecasts) {
        this.playerRecasts = playerRecasts;
    }

    /********* SYSTEM *******************************************************/

    private static final Map<LivingEntity, MagicData> ENTITY_MAGIC_DATA = new WeakHashMap<>();

    public static MagicData getPlayerMagicData(LivingEntity livingEntity) {
        return ENTITY_MAGIC_DATA.computeIfAbsent(livingEntity, entity -> {
            if (entity instanceof ServerPlayer serverPlayer) {
                return new MagicData(serverPlayer);
            }
            return new MagicData(entity instanceof net.minecraft.world.entity.Mob);
        });
    }

    public void copyFrom(MagicData oldData, HolderLookup.Provider provider, boolean wasDeath) {
        CompoundTag tag = new CompoundTag();
        oldData.saveNBTData(tag, provider);
        this.loadNBTData(tag, provider);

        if (wasDeath && this.serverPlayer != null) {
            this.setSyncedData(oldData.getSyncedData().getPersistentData(this.serverPlayer));
        }
    }

    public void saveNBTData(CompoundTag compound, HolderLookup.Provider provider) {
        compound.putInt(MANA, (int) mana);

        if (playerCooldowns.hasCooldownsActive()) {
            compound.put(COOLDOWNS, playerCooldowns.saveNBTData());
        }

        if (playerRecasts.hasRecastsActive()) {
            compound.put(RECASTS, playerRecasts.saveNBTData(provider));
        }

        if (!schoolCastCounts.isEmpty()) {
            CompoundTag castTag = new CompoundTag();
            for (var entry : schoolCastCounts.entrySet()) {
                castTag.putInt(entry.getKey(), entry.getValue());
            }
            compound.put(SCHOOL_CAST_COUNTS, castTag);
        }

        if (!schoolPowerBonuses.isEmpty()) {
            CompoundTag bonusTag = new CompoundTag();
            for (var entry : schoolPowerBonuses.entrySet()) {
                bonusTag.putDouble(entry.getKey(), entry.getValue());
            }
            compound.put(SCHOOL_POWER_BONUSES, bonusTag);
        }

        getSyncedData().saveNBTData(compound, provider);
    }

    public void loadNBTData(CompoundTag compound, HolderLookup.Provider provider) {
        mana = compound.getInt(MANA);

        var listTag = (ListTag) compound.get(COOLDOWNS);
        if (listTag != null && !listTag.isEmpty()) {
            playerCooldowns.loadNBTData(listTag);
        }

        listTag = (ListTag) compound.get(RECASTS);
        if (listTag != null && !listTag.isEmpty()) {
            playerRecasts.loadNBTData(listTag, provider);
        }

        if (compound.contains(SCHOOL_CAST_COUNTS, Tag.TAG_COMPOUND)) {
            CompoundTag castTag = compound.getCompound(SCHOOL_CAST_COUNTS);
            schoolCastCounts.clear();
            for (String key : castTag.getAllKeys()) {
                schoolCastCounts.put(key, castTag.getInt(key));
            }
        }

        if (compound.contains(SCHOOL_POWER_BONUSES, Tag.TAG_COMPOUND)) {
            CompoundTag bonusTag = compound.getCompound(SCHOOL_POWER_BONUSES);
            schoolPowerBonuses.clear();
            for (String key : bonusTag.getAllKeys()) {
                schoolPowerBonuses.put(key, bonusTag.getDouble(key));
            }
        }

        getSyncedData().loadNBTData(compound, provider);
    }

    @Override
    public String toString() {
        return String.format("isCasting:%s, spellID:%s], spellLevel:%s, duration:%s, durationRemaining:%s, source:%s, type:%s",
                getSyncedData().isCasting(),
                getSyncedData().getCastingSpellId(),
                castingSpellLevel,
                castDuration,
                castDurationRemaining,
                castSource,
                castType);
    }
}


