package net.neoforged.neoforge.registries;

public class DataPackRegistryEvent extends net.neoforged.bus.api.Event {
    public static class NewRegistry extends DataPackRegistryEvent {
        public <T> void dataPackRegistry(Object key, Object networkCodec, Object codec) {
        }
    }
}
