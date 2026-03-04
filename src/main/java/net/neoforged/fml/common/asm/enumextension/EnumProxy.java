package net.neoforged.fml.common.asm.enumextension;

public class EnumProxy<T> {
    private final Class<T> enumClass;
    private final Object[] args;

    public EnumProxy(Class<T> enumClass, Object... args) {
        this.enumClass = enumClass;
        this.args = args;
    }

    public T getValue() {
        T[] values = enumClass.getEnumConstants();
        return values != null && values.length > 0 ? values[0] : null;
    }

    public Object[] getArgs() {
        return args;
    }
}
