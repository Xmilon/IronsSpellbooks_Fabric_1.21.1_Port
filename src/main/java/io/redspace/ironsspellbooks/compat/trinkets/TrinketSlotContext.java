package io.redspace.ironsspellbooks.compat.trinkets;

import net.minecraft.world.entity.LivingEntity;

public class TrinketSlotContext {
    private final String identifier;
    private final LivingEntity entity;
    private final int index;
    private final boolean cosmetic;
    private final boolean visible;

    public TrinketSlotContext(String identifier, LivingEntity entity, int index, boolean cosmetic, boolean visible) {
        this.identifier = identifier;
        this.entity = entity;
        this.index = index;
        this.cosmetic = cosmetic;
        this.visible = visible;
    }

    public String identifier() {
        return identifier;
    }

    public LivingEntity entity() {
        return entity;
    }

    public int index() {
        return index;
    }

    public boolean cosmetic() {
        return cosmetic;
    }

    public boolean visible() {
        return visible;
    }
}
