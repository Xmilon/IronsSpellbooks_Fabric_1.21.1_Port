package net.neoforged.neoforge.network.handling;

import net.minecraft.server.level.ServerPlayer;

public interface IPayloadContext {
    default void enqueueWork(Runnable runnable) {
        runnable.run();
    }

    default <T> T enqueueWork(java.util.concurrent.Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    default ServerPlayer player() {
        return null;
    }
}
