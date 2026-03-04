package net.neoforged.fml.event.lifecycle;

public class FMLCommonSetupEvent extends net.neoforged.bus.api.Event {
    public void enqueueWork(Runnable runnable) {
        runnable.run();
    }
}
