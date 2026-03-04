package net.neoforged.fml.loading;

import java.nio.file.Path;

public class FMLPaths {
    public static final FMLPaths CONFIGDIR = new FMLPaths();

    public Path get() {
        return Path.of("config");
    }
}