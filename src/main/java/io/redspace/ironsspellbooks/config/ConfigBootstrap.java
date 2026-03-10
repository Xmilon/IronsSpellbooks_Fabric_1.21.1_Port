package io.redspace.ironsspellbooks.config;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.lang.reflect.Method;

public final class ConfigBootstrap {
    private static final String[] PREFIXES = new String[] {
            "net.neoforged",
            "net.minecraftforge"
    };

    private ConfigBootstrap() {
    }

    public static void registerConfigs() {
        boolean registered = false;
        for (String prefix : PREFIXES) {
            if (tryRegister(prefix)) {
                registered = true;
                break;
            }
        }
        if (!registered) {
            IronsSpellbooks.LOGGER.warn("Failed to register configs via config API port; defaults will be used.");
        }
    }

    private static boolean tryRegister(String prefix) {
        try {
            Class<?> contextClass = Class.forName(prefix + ".fml.ModLoadingContext");
            Method getMethod = contextClass.getMethod("get");
            Object context = getMethod.invoke(null);

            Class<?> typeClass = Class.forName(prefix + ".fml.config.ModConfig$Type");
            Object serverType = Enum.valueOf(typeClass.asSubclass(Enum.class), "SERVER");
            Object clientType = Enum.valueOf(typeClass.asSubclass(Enum.class), "CLIENT");

            Method registerMethod = findRegisterMethod(contextClass, typeClass);
            if (registerMethod == null) {
                return false;
            }

            if (registerMethod.getParameterCount() == 2) {
                registerMethod.invoke(context, serverType, ServerConfigs.SPEC);
                registerMethod.invoke(context, clientType, ClientConfigs.SPEC);
            } else {
                registerMethod.invoke(context, serverType, ServerConfigs.SPEC, "irons_spellbooks-server.toml");
                registerMethod.invoke(context, clientType, ClientConfigs.SPEC, "irons_spellbooks-client.toml");
            }
            IronsSpellbooks.LOGGER.info("Registered configs via {}.fml.ModLoadingContext", prefix);
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    private static Method findRegisterMethod(Class<?> contextClass, Class<?> typeClass) {
        for (Method method : contextClass.getMethods()) {
            if (!method.getName().equals("registerConfig")) {
                continue;
            }
            Class<?>[] params = method.getParameterTypes();
            if (params.length < 2 || params.length > 3) {
                continue;
            }
            if (!params[0].equals(typeClass)) {
                continue;
            }
            if (!params[1].isAssignableFrom(ModConfigSpec.class)) {
                continue;
            }
            if (params.length == 3 && !params[2].equals(String.class)) {
                continue;
            }
            return method;
        }
        return null;
    }
}
