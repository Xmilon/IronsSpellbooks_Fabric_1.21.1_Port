package io.redspace.ironsspellbooks;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.CompletableFuture;

public class FabricDatagenEntrypoint implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        var pack = generator.createPack();
        pack.addProvider(MithrilLootTablesProvider::new);
        pack.addProvider(MithrilWorldgenProvider::new);
    }

    private static class MithrilLootTablesProvider implements DataProvider {
        private final PackOutput.PathProvider blockLootPathProvider;

        private MithrilLootTablesProvider(FabricDataOutput output) {
            this.blockLootPathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "loot_table/blocks");
        }

        @Override
        public CompletableFuture<?> run(CachedOutput cachedOutput) {
            CompletableFuture<?> mithril = DataProvider.saveStable(cachedOutput, selfDrop("irons_spellbooks:mithril_ore"), blockLootPathProvider.json(ResourceLocation.parse("irons_spellbooks:mithril_ore")));
            CompletableFuture<?> deepslateMithril = DataProvider.saveStable(cachedOutput, selfDrop("irons_spellbooks:deepslate_mithril_ore"), blockLootPathProvider.json(ResourceLocation.parse("irons_spellbooks:deepslate_mithril_ore")));
            return CompletableFuture.allOf(mithril, deepslateMithril);
        }

        @Override
        public String getName() {
            return "Iron's Spellbooks Mithril Loot Tables";
        }

        private static JsonObject selfDrop(String blockId) {
            JsonObject root = new JsonObject();
            root.addProperty("type", "minecraft:block");

            JsonArray pools = new JsonArray();
            JsonObject pool = new JsonObject();
            pool.addProperty("rolls", 1);
            JsonArray entries = new JsonArray();
            JsonObject entry = new JsonObject();
            entry.addProperty("type", "minecraft:item");
            entry.addProperty("name", blockId);
            entries.add(entry);
            pool.add("entries", entries);
            pools.add(pool);

            root.add("pools", pools);
            return root;
        }
    }

    private static class MithrilWorldgenProvider implements DataProvider {
        private final PackOutput.PathProvider configuredFeaturePathProvider;
        private final PackOutput.PathProvider placedFeaturePathProvider;

        private MithrilWorldgenProvider(FabricDataOutput output) {
            this.configuredFeaturePathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "worldgen/configured_feature");
            this.placedFeaturePathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "worldgen/placed_feature");
        }

        @Override
        public CompletableFuture<?> run(CachedOutput cachedOutput) {
            CompletableFuture<?> configured = DataProvider.saveStable(cachedOutput, mithrilConfiguredFeature(), configuredFeaturePathProvider.json(ResourceLocation.parse("irons_spellbooks:ore_mithril_feature")));
            CompletableFuture<?> placed = DataProvider.saveStable(cachedOutput, mithrilPlacedFeature(), placedFeaturePathProvider.json(ResourceLocation.parse("irons_spellbooks:ore_mithril_placement")));
            return CompletableFuture.allOf(configured, placed);
        }

        @Override
        public String getName() {
            return "Iron's Spellbooks Mithril Worldgen";
        }

        private static JsonObject mithrilConfiguredFeature() {
            JsonObject root = new JsonObject();
            root.addProperty("type", "minecraft:ore");

            JsonObject config = new JsonObject();
            config.addProperty("size", 3);
            config.addProperty("discard_chance_on_air_exposure", 1.0f);

            JsonArray targets = new JsonArray();
            targets.add(target("minecraft:stone_ore_replaceables", "irons_spellbooks:mithril_ore"));
            targets.add(target("minecraft:deepslate_ore_replaceables", "irons_spellbooks:deepslate_mithril_ore"));
            config.add("targets", targets);

            root.add("config", config);
            return root;
        }

        private static JsonObject mithrilPlacedFeature() {
            JsonObject root = new JsonObject();
            root.addProperty("feature", "irons_spellbooks:ore_mithril_feature");

            JsonArray placement = new JsonArray();

            JsonObject count = new JsonObject();
            count.addProperty("type", "minecraft:count");
            count.addProperty("count", 7);
            placement.add(count);

            JsonObject inSquare = new JsonObject();
            inSquare.addProperty("type", "minecraft:in_square");
            placement.add(inSquare);

            JsonObject heightRange = new JsonObject();
            heightRange.addProperty("type", "minecraft:height_range");
            JsonObject height = new JsonObject();
            height.addProperty("type", "minecraft:uniform");
            JsonObject minInclusive = new JsonObject();
            minInclusive.addProperty("absolute", -63);
            JsonObject maxInclusive = new JsonObject();
            maxInclusive.addProperty("absolute", -38);
            height.add("min_inclusive", minInclusive);
            height.add("max_inclusive", maxInclusive);
            heightRange.add("height", height);
            placement.add(heightRange);

            JsonObject biome = new JsonObject();
            biome.addProperty("type", "minecraft:biome");
            placement.add(biome);

            root.add("placement", placement);
            return root;
        }

        private static JsonObject target(String replaceableTag, String blockId) {
            JsonObject target = new JsonObject();

            JsonObject targetPredicate = new JsonObject();
            targetPredicate.addProperty("predicate_type", "minecraft:tag_match");
            targetPredicate.addProperty("tag", replaceableTag);
            target.add("target", targetPredicate);

            JsonObject state = new JsonObject();
            state.addProperty("Name", blockId);
            target.add("state", state);

            return target;
        }
    }
}
