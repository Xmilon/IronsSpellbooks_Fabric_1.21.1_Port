package io.redspace.ironsspellbooks.content;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.events.SpellOnCastEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = IronsSpellbooks.MODID)
public final class ContentPackEvents {
    private ContentPackEvents() {
    }

    @SubscribeEvent
    public static void onSpellCast(SpellOnCastEvent event) {
        ContentPackManager.INSTANCE.handleSpellCast(event);
    }
}
