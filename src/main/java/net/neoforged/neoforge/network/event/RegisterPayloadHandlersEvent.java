package net.neoforged.neoforge.network.event;

import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class RegisterPayloadHandlersEvent extends net.neoforged.bus.api.Event {
    public PayloadRegistrar registrar(String modId) {
        return new PayloadRegistrar();
    }
}
