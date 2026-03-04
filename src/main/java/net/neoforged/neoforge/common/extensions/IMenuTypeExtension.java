package net.neoforged.neoforge.common.extensions;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.network.IContainerFactory;

public interface IMenuTypeExtension {
    static <T extends AbstractContainerMenu> MenuType<T> create(IContainerFactory<T> factory) {
        return new MenuType<>((containerId, inventory) -> factory.create(containerId, inventory, new FriendlyByteBuf(Unpooled.buffer())), FeatureFlags.DEFAULT_FLAGS);
    }
}
