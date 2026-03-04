package net.neoforged.neoforge.common;

import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.IEventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings({"rawtypes", "unchecked"})
public class SimpleEventBus implements IEventBus {
    private final List<Consumer> listeners = new ArrayList<>();

    @Override
    public <T> void addListener(Consumer<T> listener) {
        listeners.add((Consumer) listener);
    }

    @Override
    public <T extends Event> T post(T event) {
        for (Consumer listener : listeners) {
            try {
                listener.accept(event);
            } catch (ClassCastException ignored) {
            }
        }
        return event;
    }
}
