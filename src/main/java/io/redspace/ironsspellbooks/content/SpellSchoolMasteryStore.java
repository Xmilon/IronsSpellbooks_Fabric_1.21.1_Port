package io.redspace.ironsspellbooks.content;

import io.redspace.ironsspellbooks.data.IronsDataStorage;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class SpellSchoolMasteryStore {
    public static final SpellSchoolMasteryStore INSTANCE = new SpellSchoolMasteryStore();

    private static final String PLAYERS_TAG = "Players";
    private static final String PLAYER_UUID = "Uuid";
    private static final String PLAYER_NAME = "Name";
    private static final String CASTS_TAG = "Casts";
    private static final String BONUSES_TAG = "Bonuses";

    private final Map<UUID, PlayerMasteryData> dataByPlayer = new HashMap<>();

    private SpellSchoolMasteryStore() {
    }

    public PlayerMasteryData getOrCreate(UUID uuid, String name) {
        PlayerMasteryData data = dataByPlayer.computeIfAbsent(uuid, ignored -> new PlayerMasteryData());
        if (name != null && !name.isBlank()) {
            data.lastKnownName = name;
        }
        return data;
    }

    public PlayerMasteryData get(UUID uuid) {
        return dataByPlayer.get(uuid);
    }

    public int incrementCast(UUID uuid, String name, String schoolId) {
        PlayerMasteryData data = getOrCreate(uuid, name);
        int next = data.castCounts.getOrDefault(schoolId, 0) + 1;
        data.castCounts.put(schoolId, next);
        markDirty();
        return next;
    }

    public double addBonus(UUID uuid, String name, String schoolId, double delta) {
        PlayerMasteryData data = getOrCreate(uuid, name);
        double next = data.powerBonuses.getOrDefault(schoolId, 0d) + delta;
        if (next < 0d) {
            next = 0d;
        }
        data.powerBonuses.put(schoolId, next);
        markDirty();
        return next;
    }

    public int getCastCount(UUID uuid, String schoolId) {
        PlayerMasteryData data = dataByPlayer.get(uuid);
        if (data == null) {
            return 0;
        }
        return data.castCounts.getOrDefault(schoolId, 0);
    }

    public double getBonus(UUID uuid, String schoolId) {
        PlayerMasteryData data = dataByPlayer.get(uuid);
        if (data == null) {
            return 0d;
        }
        return data.powerBonuses.getOrDefault(schoolId, 0d);
    }

    public Map<UUID, PlayerMasteryData> getAll() {
        return dataByPlayer;
    }

    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag root = new CompoundTag();
        ListTag players = new ListTag();
        for (var entry : dataByPlayer.entrySet()) {
            CompoundTag playerTag = new CompoundTag();
            playerTag.put(PLAYER_UUID, NbtUtils.createUUID(entry.getKey()));
            PlayerMasteryData data = entry.getValue();
            if (data.lastKnownName != null && !data.lastKnownName.isBlank()) {
                playerTag.putString(PLAYER_NAME, data.lastKnownName);
            }
            CompoundTag casts = new CompoundTag();
            for (var castEntry : data.castCounts.entrySet()) {
                casts.putInt(castEntry.getKey(), castEntry.getValue());
            }
            playerTag.put(CASTS_TAG, casts);

            CompoundTag bonuses = new CompoundTag();
            for (var bonusEntry : data.powerBonuses.entrySet()) {
                bonuses.putDouble(bonusEntry.getKey(), bonusEntry.getValue());
            }
            playerTag.put(BONUSES_TAG, bonuses);

            players.add(playerTag);
        }
        root.put(PLAYERS_TAG, players);
        return root;
    }

    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag root) {
        dataByPlayer.clear();
        if (!root.contains(PLAYERS_TAG, Tag.TAG_LIST)) {
            return;
        }
        ListTag players = root.getList(PLAYERS_TAG, Tag.TAG_COMPOUND);
        for (Tag entry : players) {
            CompoundTag playerTag = (CompoundTag) entry;
            if (!playerTag.contains(PLAYER_UUID, Tag.TAG_INT_ARRAY)) {
                continue;
            }
            UUID uuid = NbtUtils.loadUUID(playerTag.get(PLAYER_UUID));
            PlayerMasteryData data = new PlayerMasteryData();
            if (playerTag.contains(PLAYER_NAME, Tag.TAG_STRING)) {
                data.lastKnownName = playerTag.getString(PLAYER_NAME);
            }
            if (playerTag.contains(CASTS_TAG, Tag.TAG_COMPOUND)) {
                CompoundTag casts = playerTag.getCompound(CASTS_TAG);
                for (String key : casts.getAllKeys()) {
                    data.castCounts.put(key, casts.getInt(key));
                }
            }
            if (playerTag.contains(BONUSES_TAG, Tag.TAG_COMPOUND)) {
                CompoundTag bonuses = playerTag.getCompound(BONUSES_TAG);
                for (String key : bonuses.getAllKeys()) {
                    data.powerBonuses.put(key, bonuses.getDouble(key));
                }
            }
            dataByPlayer.put(uuid, data);
        }
    }

    private static void markDirty() {
        if (IronsDataStorage.INSTANCE != null) {
            IronsDataStorage.INSTANCE.setDirty();
        }
    }

    public static class PlayerMasteryData {
        private String lastKnownName;
        private final Map<String, Integer> castCounts = new HashMap<>();
        private final Map<String, Double> powerBonuses = new HashMap<>();

        public String getLastKnownName() {
            return lastKnownName;
        }
    }
}
