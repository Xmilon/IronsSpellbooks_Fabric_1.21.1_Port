package io.redspace.ironsspellbooks.worldgen;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;

@EventBusSubscriber()
public class VillageAddition {
    @SubscribeEvent
    public static void addNewVillageBuilding(final ServerAboutToStartEvent event) {
        // NeoForge template-pool mutation path is not used in this Fabric-targeted port.
    }
}


