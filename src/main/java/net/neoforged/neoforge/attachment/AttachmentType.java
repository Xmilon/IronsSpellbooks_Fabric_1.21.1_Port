package net.neoforged.neoforge.attachment;

import java.util.function.Function;

public class AttachmentType<T> {
    public static <T> Builder<T> builder(Function<Object, T> factory) {
        return new Builder<>();
    }

    public static class Builder<T> {
        public Builder<T> serialize(Object serializer) {
            return this;
        }

        public AttachmentType<T> build() {
            return new AttachmentType<>();
        }
    }
}
