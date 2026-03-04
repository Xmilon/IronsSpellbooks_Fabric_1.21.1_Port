package net.neoforged.neoforge.capabilities;

import java.util.function.BiFunction;

public class RegisterCapabilitiesEvent extends net.neoforged.bus.api.Event {
    public <BE, CTX, CAP> void registerBlockEntity(Object capability, Object blockEntityType, BiFunction<BE, CTX, CAP> provider) {
    }
}
