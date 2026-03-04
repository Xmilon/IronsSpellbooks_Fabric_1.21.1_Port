package net.neoforged.fml;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.objectweb.asm.Type;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ModList {
    private static final ModList INSTANCE = new ModList();

    public static ModList get() {
        return INSTANCE;
    }

    public boolean isLoaded(String modid) {
        return FabricLoader.getInstance().isModLoaded(modid);
    }

    public List<ModInfo> getMods() {
        return FabricLoader.getInstance()
                .getAllMods()
                .stream()
                .map(ModInfo::new)
                .collect(Collectors.toList());
    }

    public List<ModFileScanData> getAllScanData() {
        return Collections.emptyList();
    }

    public ModFileInfo getModFileById(String modid) {
        return FabricLoader.getInstance()
                .getModContainer(modid)
                .map(ModFileInfo::new)
                .orElse(null);
    }

    public static class ModInfo {
        private final ModContainer container;
        private final ModMetadata metadata;
        private final ModFileInfo owningFile;
        private final ModConfig config = new ModConfig();

        public ModInfo(ModContainer container) {
            this.container = container;
            this.metadata = container.getMetadata();
            this.owningFile = new ModFileInfo(container);
        }

        public String getModId() {
            return metadata.getId();
        }

        public String getDisplayName() {
            return metadata.getName();
        }

        public String getVersion() {
            return metadata.getVersion().getFriendlyString();
        }

        public ModFileInfo getOwningFile() {
            return owningFile;
        }

        public Optional<String> getModURL() {
            return metadata.getContact().get("homepage");
        }

        public ModConfig getConfig() {
            return config;
        }
    }

    public static class ModFileInfo {
        private final ModContainer container;
        private final ModFile file;
        private final ModConfig config = new ModConfig();

        public ModFileInfo(ModContainer container) {
            this.container = container;
            this.file = new ModFile(container);
        }

        public ModFile getFile() {
            return file;
        }

        public ModConfig getConfig() {
            return config;
        }
    }

    public static class ModFile {
        private final ModContainer container;

        public ModFile(ModContainer container) {
            this.container = container;
        }

        public String getFileName() {
            Path origin = container.getOrigin().getPaths().isEmpty() ? null : container.getOrigin().getPaths().getFirst();
            if (origin == null) {
                return container.getMetadata().getId();
            }
            return origin.getFileName() != null ? origin.getFileName().toString() : origin.toString();
        }

        public Path findResource(String path) {
            return container.findPath(path).orElse(Path.of(path));
        }
    }

    public static class ModConfig {
        public Optional<String> getConfigElement(String key) {
            return Optional.empty();
        }
    }

    public static class ModFileScanData {
        public List<AnnotationData> getAnnotations() { return Collections.emptyList(); }
    }

    public static class AnnotationData {
        public Type annotationType() { return Type.VOID_TYPE; }
        public String memberName() { return ""; }
    }
}
