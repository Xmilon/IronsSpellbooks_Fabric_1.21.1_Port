package net.neoforged.fml.loading;

import net.fabricmc.loader.api.FabricLoader;
import net.neoforged.fml.ModList;

public class FMLLoader {
    private static final LoadingModList LOADING_MOD_LIST = new LoadingModList();

    public static LoadingModList getLoadingModList() {
        return LOADING_MOD_LIST;
    }

    public static boolean isProduction() {
        return !FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    public static class LoadingModList {
        public ModList.ModFileInfo getModFileById(String modid) {
            return ModList.get().getModFileById(modid);
        }
    }
}
