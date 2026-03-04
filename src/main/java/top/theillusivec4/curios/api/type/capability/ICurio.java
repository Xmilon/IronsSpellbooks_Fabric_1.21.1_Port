package top.theillusivec4.curios.api.type.capability;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

public interface ICurio {
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

    default SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) { return null; }
}