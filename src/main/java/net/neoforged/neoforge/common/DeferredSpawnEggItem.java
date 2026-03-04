package net.neoforged.neoforge.common;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

public class DeferredSpawnEggItem extends SpawnEggItem {
    public DeferredSpawnEggItem(Supplier<? extends EntityType<?>> type, int primaryColor, int secondaryColor, Item.Properties properties) {
        super((EntityType<? extends Mob>) type.get(), primaryColor, secondaryColor, properties);
    }

    public DeferredSpawnEggItem(DeferredHolder<EntityType<?>, ? extends EntityType<?>> type, int primaryColor, int secondaryColor, Item.Properties properties) {
        this(type::get, primaryColor, secondaryColor, properties);
    }
}
