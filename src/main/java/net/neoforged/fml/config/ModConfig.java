package net.neoforged.fml.config;

public class ModConfig {
    public enum Type { CLIENT, COMMON, SERVER }

    private final Type type;

    public ModConfig(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}