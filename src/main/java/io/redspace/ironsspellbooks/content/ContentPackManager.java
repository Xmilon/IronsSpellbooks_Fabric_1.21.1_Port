package io.redspace.ironsspellbooks.content;

import io.redspace.ironsspellbooks.api.events.SpellOnCastEvent;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.data.IronsDataStorage;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public final class ContentPackManager {
    public static final ContentPackManager INSTANCE = new ContentPackManager();
    private static final String ENABLED_TAG = "EnabledPacks";

    private final Map<String, ContentPack> packs = new LinkedHashMap<>();
    private final Set<String> enabledPacks = new HashSet<>();

    private ContentPackManager() {
        registerBuiltinPacks();
    }

    private void registerBuiltinPacks() {
        register(new SpellSchoolMasteryPack());
    }

    public void register(ContentPack pack) {
        packs.put(normalize(pack.getId()), pack);
    }

    public Collection<String> getPackIds() {
        return Collections.unmodifiableCollection(packs.keySet());
    }

    public ContentPack getPack(String id) {
        return packs.get(normalize(id));
    }

    public boolean isEnabled(String id) {
        return enabledPacks.contains(normalize(id));
    }

    public boolean setEnabled(String id, boolean enabled) {
        String key = normalize(id);
        if (!packs.containsKey(key)) {
            return false;
        }
        if (enabled) {
            enabledPacks.add(key);
        } else {
            enabledPacks.remove(key);
        }
        ContentPack pack = packs.get(key);
        if (pack != null) {
            pack.onEnabledChanged(enabled);
        }
        if (IronsDataStorage.INSTANCE != null) {
            IronsDataStorage.INSTANCE.setDirty();
        }
        return true;
    }

    public double getBasePowerMultiplier(LivingEntity entity, SchoolType schoolType) {
        if (enabledPacks.isEmpty() || entity == null || schoolType == null) {
            return 1.0d;
        }
        double multiplier = 1.0d;
        for (String id : enabledPacks) {
            ContentPack pack = packs.get(id);
            if (pack != null) {
                multiplier *= pack.getBasePowerMultiplier(entity, schoolType);
            }
        }
        return multiplier;
    }

    public void handleSpellCast(SpellOnCastEvent event) {
        if (enabledPacks.isEmpty()) {
            return;
        }
        for (String id : enabledPacks) {
            ContentPack pack = packs.get(id);
            if (pack != null) {
                pack.onSpellCast(event);
            }
        }
    }

    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        ListTag enabled = new ListTag();
        for (String id : enabledPacks) {
            enabled.add(StringTag.valueOf(id));
        }
        tag.put(ENABLED_TAG, enabled);
        return tag;
    }

    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        enabledPacks.clear();
        if (tag.contains(ENABLED_TAG, Tag.TAG_LIST)) {
            ListTag list = tag.getList(ENABLED_TAG, Tag.TAG_STRING);
            for (Tag entry : list) {
                enabledPacks.add(normalize(entry.getAsString()));
            }
        }
        // Strip unknown ids to keep state clean and consistent.
        enabledPacks.retainAll(packs.keySet());
    }

    private static String normalize(String id) {
        return id == null ? "" : id.trim().toLowerCase(Locale.ROOT);
    }
}
