package io.redspace.ironsspellbooks.compat.client;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public final class CuriosScreenOpener {
    private CuriosScreenOpener() {
    }

    public static boolean openScreen(Minecraft minecraft) {
        if (minecraft == null || minecraft.player == null) {
            return false;
        }

        if (tryInvokeStaticNoArgs("dev.emi.trinkets.TrinketsClient", "openTrinketScreen", "openTrinketsScreen")) {
            return true;
        }
        if (tryInvokeStaticWithMinecraft("dev.emi.trinkets.TrinketsClient", minecraft, "openTrinketScreen", "openTrinketsScreen")) {
            return true;
        }
        if (tryInvokeStaticNoArgs("dev.emi.trinkets.client.TrinketScreenManager", "open", "openScreen", "openTrinketScreen")) {
            return true;
        }
        if (tryInvokeStaticWithMinecraft("dev.emi.trinkets.client.TrinketScreenManager", minecraft, "open", "openScreen", "openTrinketScreen")) {
            return true;
        }
        if (tryInvokeStaticNoArgs("io.wispforest.accessories.client.AccessoriesClient", "attemptToOpenScreen", "openAccessoriesScreen", "openScreen")) {
            return true;
        }
        if (tryInvokeStaticWithMinecraft("io.wispforest.accessories.client.AccessoriesClient", minecraft, "attemptToOpenScreen", "openAccessoriesScreen", "openScreen")) {
            return true;
        }

        minecraft.player.displayClientMessage(Component.translatable("message.irons_spellbooks.curios_screen_unavailable"), true);
        return false;
    }

    private static boolean tryInvokeStaticNoArgs(String className, String... methodNames) {
        try {
            Class<?> clazz = Class.forName(className);
            for (String methodName : methodNames) {
                try {
                    Method method = clazz.getMethod(methodName);
                    if (Modifier.isStatic(method.getModifiers())) {
                        method.invoke(null);
                        return true;
                    }
                } catch (NoSuchMethodException ignored) {
                }
            }
        } catch (Throwable ignored) {
        }
        return false;
    }

    private static boolean tryInvokeStaticWithMinecraft(String className, Minecraft minecraft, String... methodNames) {
        try {
            Class<?> clazz = Class.forName(className);
            for (String methodName : methodNames) {
                for (Method method : clazz.getMethods()) {
                    if (!method.getName().equals(methodName) || method.getParameterCount() != 1 || !Modifier.isStatic(method.getModifiers())) {
                        continue;
                    }
                    Class<?> parameter = method.getParameterTypes()[0];
                    if (parameter.isInstance(minecraft)) {
                        method.invoke(null, minecraft);
                        return true;
                    }
                }
            }
        } catch (Throwable ignored) {
        }
        return false;
    }
}
