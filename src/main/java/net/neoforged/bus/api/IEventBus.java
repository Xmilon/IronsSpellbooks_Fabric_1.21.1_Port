package net.neoforged.bus.api;

import java.util.function.Consumer;

public interface IEventBus {
    <T> void addListener(Consumer<T> listener);
    <T extends Event> T post(T event);
    default void register(Object ignored) {
    }
}
