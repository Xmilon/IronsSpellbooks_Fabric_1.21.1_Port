package net.neoforged.neoforge.common;

import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

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

    @Override
    public void register(Object target) {
        if (target == null) {
            return;
        }

        final Class<?> clazz;
        final Object instance;
        final boolean staticOnly;
        if (target instanceof Class<?> type) {
            clazz = type;
            instance = null;
            staticOnly = true;
        } else {
            clazz = target.getClass();
            instance = target;
            staticOnly = false;
        }

        for (Method method : clazz.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(SubscribeEvent.class)) {
                continue;
            }
            Class<?>[] params = method.getParameterTypes();
            if (params.length != 1 || !Event.class.isAssignableFrom(params[0])) {
                continue;
            }
            boolean isStatic = Modifier.isStatic(method.getModifiers());
            if (staticOnly && !isStatic) {
                continue;
            }
            if (!isStatic && instance == null) {
                continue;
            }
            method.setAccessible(true);
            Class<?> paramType = params[0];
            Object targetInstance = isStatic ? null : instance;
            addListener((Consumer<Event>) event -> {
                if (!paramType.isInstance(event)) {
                    return;
                }
                try {
                    method.invoke(targetInstance, event);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to dispatch event " + event.getClass().getName() + " to " + method, e);
                }
            });
        }
    }
}
