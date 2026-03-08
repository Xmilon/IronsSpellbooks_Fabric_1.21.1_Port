package io.redspace.ironsspellbooks.item;

import io.redspace.ironsspellbooks.render.item.SpellBookGeoRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;

import java.util.function.Consumer;

public final class SpellBookClientHooks {
    private SpellBookClientHooks() {
    }

    public static void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private BlockEntityWithoutLevelRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                if (this.renderer == null) {
                    this.renderer = new SpellBookGeoRenderer();
                }

                return this.renderer;
            }
        });
    }
}
