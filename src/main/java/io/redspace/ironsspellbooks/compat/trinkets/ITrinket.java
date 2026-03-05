package io.redspace.ironsspellbooks.compat.trinkets;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;

public interface ITrinket {
    class SoundInfo {
        public final SoundEvent soundEvent;
        public final float volume;
        public final float pitch;

        public SoundInfo(SoundEvent soundEvent, float volume, float pitch) {
            this.soundEvent = soundEvent;
            this.volume = volume;
            this.pitch = pitch;
        }
    }

    default SoundInfo getEquipSound(TrinketSlotContext TrinketSlotContext, ItemStack stack) {
        return null;
    }
}
