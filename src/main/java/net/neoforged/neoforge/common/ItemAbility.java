package net.neoforged.neoforge.common;

public class ItemAbility {
    private final String id;

    public ItemAbility(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }
}
