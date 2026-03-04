package net.neoforged.neoforge.client.settings;

public interface IKeyConflictContext {
    default boolean isActive() {
        return true;
    }

    default boolean conflicts(IKeyConflictContext other) {
        return this == other;
    }
}
