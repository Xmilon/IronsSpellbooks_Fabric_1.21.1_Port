package io.redspace.ironsspellbooks.damage;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

public final class DamageTypeTagsCompat {
    private DamageTypeTagsCompat() {}

    private static TagKey<DamageType> create(String name) {
        return TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(IronsSpellbooks.MODID, name));
    }

    public static final TagKey<DamageType> BYPASS_EVASION = create("bypass_evasion");
    public static final TagKey<DamageType> LONG_CAST_IGNORE = create("long_cast_ignore");
}
