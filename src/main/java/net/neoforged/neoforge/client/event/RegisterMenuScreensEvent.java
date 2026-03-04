package net.neoforged.neoforge.client.event;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.Event;

public class RegisterMenuScreensEvent extends Event {
    @FunctionalInterface
    public interface ScreenFactory<T extends AbstractContainerMenu> {
        Object create(T menu, Inventory inventory, Component title);
    }

    public <T extends AbstractContainerMenu> void register(MenuType<T> menuType, ScreenFactory<T> factory) {
    }
}
