package net.neoforged.neoforge.data.event;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class GatherDataEvent extends net.neoforged.bus.api.Event {
    public DataGenerator getGenerator() { return null; }
    public ExistingFileHelper getExistingFileHelper() { return null; }
    public CompletableFuture<HolderLookup.Provider> getLookupProvider() { return null; }
    public boolean includeServer() { return true; }
}
