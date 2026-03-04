package net.neoforged.neoforge.client.event;

import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.neoforged.bus.api.Event;

public class RegisterParticleProvidersEvent extends Event {
    public <T extends ParticleOptions> void registerSpriteSet(ParticleType<T> type, ParticleProvider.Sprite<T> provider) {
    }

    public <T extends ParticleOptions> void registerSpecial(ParticleType<T> type, ParticleProvider<T> provider) {
    }
}
