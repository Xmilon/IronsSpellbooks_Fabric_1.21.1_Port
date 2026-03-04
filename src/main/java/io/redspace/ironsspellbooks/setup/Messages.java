package io.redspace.ironsspellbooks.setup;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class Messages {
    private static final List<ReceiverRegistration<?>> PLAY_TO_CLIENT = new ArrayList<>();
    private static final List<ReceiverRegistration<?>> PLAY_TO_SERVER = new ArrayList<>();
    private static final Map<CustomPacketPayload.Type<?>, Boolean> REGISTERED_S2C_TYPES = new IdentityHashMap<>();
    private static final Map<CustomPacketPayload.Type<?>, Boolean> REGISTERED_C2S_TYPES = new IdentityHashMap<>();
    private static boolean initialized = false;
    private static boolean serverReceiversRegistered = false;

    public static void register() {
        if (initialized) {
            return;
        }
        initialized = true;
        PayloadHandler.register(new net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent());
        registerServerReceivers();
    }

    public static void sendToServer(Object message) {
        if (!(message instanceof CustomPacketPayload payload)) {
            return;
        }
        // Avoid linking client-only networking classes on dedicated servers.
        try {
            Class<?> helperClass = Class.forName("io.redspace.ironsspellbooks.setup.ClientMessages");
            Method sendMethod = helperClass.getDeclaredMethod("sendToServer", CustomPacketPayload.class);
            sendMethod.invoke(null, payload);
        } catch (ReflectiveOperationException ignored) {
        }
    }

    public static void sendToPlayer(Object message, ServerPlayer player) {
        if (message instanceof CustomPacketPayload payload) {
            ServerPlayNetworking.send(player, payload);
        }
    }

    public static void sendToAll(Object message) {
        if (!(message instanceof CustomPacketPayload payload)) {
            return;
        }
        if (IronsSpellbooks.MCS == null) {
            return;
        }
        for (ServerPlayer player : PlayerLookup.all(IronsSpellbooks.MCS)) {
            ServerPlayNetworking.send(player, payload);
        }
    }

    public static void sendToTrackingEntity(Object message, Entity entity) {
        if (!(message instanceof CustomPacketPayload payload)) {
            return;
        }
        for (ServerPlayer player : PlayerLookup.tracking(entity)) {
            ServerPlayNetworking.send(player, payload);
        }
    }

    public static void sendToTrackingEntityAndSelf(Object message, Entity entity) {
        if (!(message instanceof CustomPacketPayload payload)) {
            return;
        }
        for (ServerPlayer player : PlayerLookup.tracking(entity)) {
            ServerPlayNetworking.send(player, payload);
        }
        if (entity instanceof ServerPlayer serverPlayer) {
            ServerPlayNetworking.send(serverPlayer, payload);
        }
    }

    public static List<ReceiverRegistration<?>> getPlayToClientRegistrations() {
        return PLAY_TO_CLIENT;
    }

    public static <T extends CustomPacketPayload> void registerPlayToClient(CustomPacketPayload.Type<T> type,
                                                                            StreamCodec<?, T> codec,
                                                                            BiConsumer<T, IPayloadContext> handler) {
        if (!REGISTERED_S2C_TYPES.containsKey(type)) {
            @SuppressWarnings("unchecked")
            StreamCodec<RegistryFriendlyByteBuf, T> castCodec = (StreamCodec<RegistryFriendlyByteBuf, T>) codec;
            PayloadTypeRegistry.playS2C().register(type, castCodec);
            REGISTERED_S2C_TYPES.put(type, true);
        }
        PLAY_TO_CLIENT.add(new ReceiverRegistration<>(type, handler));
    }

    public static <T extends CustomPacketPayload> void registerPlayToServer(CustomPacketPayload.Type<T> type,
                                                                            StreamCodec<?, T> codec,
                                                                            BiConsumer<T, IPayloadContext> handler) {
        if (!REGISTERED_C2S_TYPES.containsKey(type)) {
            @SuppressWarnings("unchecked")
            StreamCodec<RegistryFriendlyByteBuf, T> castCodec = (StreamCodec<RegistryFriendlyByteBuf, T>) codec;
            PayloadTypeRegistry.playC2S().register(type, castCodec);
            REGISTERED_C2S_TYPES.put(type, true);
        }
        PLAY_TO_SERVER.add(new ReceiverRegistration<>(type, handler));
    }

    private static void registerServerReceivers() {
        if (serverReceiversRegistered) {
            return;
        }
        serverReceiversRegistered = true;
        for (ReceiverRegistration<?> registration : PLAY_TO_SERVER) {
            registerServerReceiver(registration);
        }
    }

    private static <T extends CustomPacketPayload> void registerServerReceiver(ReceiverRegistration<T> registration) {
        ServerPlayNetworking.registerGlobalReceiver(registration.type(), (payload, context) ->
                registration.handler().accept(payload, new ServerPayloadContext(context.player())));
    }

    public record ReceiverRegistration<T extends CustomPacketPayload>(CustomPacketPayload.Type<T> type,
                                                                      BiConsumer<T, IPayloadContext> handler) {
    }

    private static final class ServerPayloadContext implements IPayloadContext {
        private final ServerPlayer player;

        private ServerPayloadContext(ServerPlayer player) {
            this.player = player;
        }

        @Override
        public void enqueueWork(Runnable runnable) {
            if (player.getServer() != null) {
                player.getServer().execute(runnable);
            } else {
                runnable.run();
            }
        }

        @Override
        public ServerPlayer player() {
            return player;
        }
    }
}


