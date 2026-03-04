package net.neoforged.neoforge.common;

import java.util.List;
import java.util.function.Predicate;

public class ModConfigSpec {
    public boolean isLoaded() {
        return true;
    }

    public static class Builder {
        public Builder push(String name) { return this; }
        public Builder pop() { return this; }
        public Builder comment(String... comment) { return this; }
        public Builder translation(String key) { return this; }
        public Builder worldRestart() { return this; }
        public <T> ConfigValue<T> define(String name, T defaultValue) { return new ConfigValue<>(defaultValue); }
        public BooleanValue define(String name, boolean defaultValue) { return new BooleanValue(defaultValue); }
        public IntValue defineInRange(String name, int defaultValue, int min, int max) { return new IntValue(defaultValue); }
        public DoubleValue defineInRange(String name, double defaultValue, double min, double max) { return new DoubleValue(defaultValue); }
        public <T extends Enum<T>> EnumValue<T> defineEnum(String name, T defaultValue) { return new EnumValue<>(defaultValue); }
        public <T> ConfigValue<List<? extends T>> defineList(String name, List<? extends T> def, Predicate<Object> validator) { return new ConfigValue<>(def); }
        public <T> ConfigValue<List<? extends T>> defineListAllowEmpty(String name, java.util.function.Supplier<List<? extends T>> def, Predicate<Object> validator) {
            return new ConfigValue<>(def.get());
        }
        public ModConfigSpec build() { return new ModConfigSpec(); }
    }

    public static class ConfigValue<T> {
        protected T value;
        protected final T defaultValue;
        public ConfigValue(T value) {
            this.value = value;
            this.defaultValue = value;
        }
        public T get() { return value; }
        public void set(T value) { this.value = value; }
        public T getDefault() { return defaultValue; }
    }

    public static class BooleanValue extends ConfigValue<Boolean> { public BooleanValue(Boolean v) { super(v); } }
    public static class IntValue extends ConfigValue<Integer> { public IntValue(Integer v) { super(v); } }
    public static class DoubleValue extends ConfigValue<Double> { public DoubleValue(Double v) { super(v); } }
    public static class EnumValue<T extends Enum<T>> extends ConfigValue<T> { public EnumValue(T v) { super(v); } }
}
