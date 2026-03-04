package io.redspace.ironsspellbooks.setup;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class ClientMessages {
    private static boolean clientReceiversRegistered = false;

    private ClientMessages() {
    }

    public static void registerClientReceivers() {
        if (clientReceiversRegistered) {
            return;
        }
        clientReceiversRegistered = true;
        for (Messages.ReceiverRegistration<?> registration : Messages.getPlayToClientRegistrations()) {
            registerClientReceiver(registration);
        }
    }

    public static void sendToServer(CustomPacketPayload payload) {
        ClientPlayNetworking.send(payload);
    }

    private static <T extends CustomPacketPayload> void registerClientReceiver(Messages.ReceiverRegistration<T> registration) {
        ClientPlayNetworking.registerGlobalReceiver(registration.type(), (payload, context) ->
                registration.handler().accept(payload, new ClientPayloadContext()));
    }

    private static final class ClientPayloadContext implements IPayloadContext {
        @Override
        public void enqueueWork(Runnable runnable) {
            net.minecraft.client.Minecraft.getInstance().execute(runnable);
        }

        @Override
        public ServerPlayer player() {
            return null;
        }
    }
}
