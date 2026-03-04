package net.neoforged.neoforge.common.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class DatapackBuiltinEntriesProvider implements DataProvider {
    private final CompletableFuture<HolderLookup.Provider> provider;

    public DatapackBuiltinEntriesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, RegistrySetBuilder builder, Set<String> modIds) {
        this.provider = provider;
    }

    public CompletableFuture<HolderLookup.Provider> getRegistryProvider() {
        return provider;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public String getName() {
        return "DatapackBuiltinEntriesProvider";
    }
}
