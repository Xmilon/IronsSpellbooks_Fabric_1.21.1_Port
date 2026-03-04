package net.neoforged.neoforge.network.registration;

import io.redspace.ironsspellbooks.setup.Messages;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.function.BiConsumer;

public class PayloadRegistrar {
    public PayloadRegistrar versioned(String version) {
        return this;
    }

    public PayloadRegistrar optional() {
        return this;
    }

    public <B, T extends CustomPacketPayload> void playToClient(CustomPacketPayload.Type<T> type, StreamCodec<B, T> codec, BiConsumer<T, IPayloadContext> handler) {
        Messages.registerPlayToClient(type, codec, handler);
    }

    public <B, T extends CustomPacketPayload> void playToServer(CustomPacketPayload.Type<T> type, StreamCodec<B, T> codec, BiConsumer<T, IPayloadContext> handler) {
        Messages.registerPlayToServer(type, codec, handler);
    }
}
