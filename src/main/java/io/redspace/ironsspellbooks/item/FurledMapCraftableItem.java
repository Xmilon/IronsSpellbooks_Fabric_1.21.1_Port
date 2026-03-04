package io.redspace.ironsspellbooks.item;

import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;

@EventBusSubscriber
public class FurledMapCraftableItem extends FurledMapItem {
    final boolean ancient;
    final FurledMapData mapData;

    public FurledMapCraftableItem(boolean ancient, FurledMapData mapData) {
        this.ancient = ancient;
        this.mapData = mapData;
    }

    @Override
    public String getDescriptionId() {
        return ancient ? ItemRegistry.ANCIENT_FURLED_MAP.get().getDescriptionId() : ItemRegistry.FURLED_MAP.get().getDescriptionId();
    }


    @SubscribeEvent
    public static void setMapData(ModifyDefaultComponentsEvent event) {
        // Default component mutation hooks are NeoForge-specific; item instances already
        // carry their map data in this Fabric port path.
        for (Item item : BuiltInRegistries.ITEM) {
            if (item instanceof FurledMapCraftableItem map) {
                // no-op
            }
        }
    }
}
