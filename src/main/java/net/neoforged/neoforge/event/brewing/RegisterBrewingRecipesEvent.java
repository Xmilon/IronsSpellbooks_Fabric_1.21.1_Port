package net.neoforged.neoforge.event.brewing;

public class RegisterBrewingRecipesEvent extends net.neoforged.bus.api.Event {
    public Builder getBuilder() {
        return new Builder();
    }

    public static class Builder {
        public void addMix(net.minecraft.core.Holder<net.minecraft.world.item.alchemy.Potion> input, net.minecraft.world.item.Item ingredient, net.minecraft.core.Holder<net.minecraft.world.item.alchemy.Potion> output) {
        }

        public void addMix(net.minecraft.core.Holder<net.minecraft.world.item.alchemy.Potion> input, net.minecraft.world.level.ItemLike ingredient, net.minecraft.core.Holder<net.minecraft.world.item.alchemy.Potion> output) {
        }
    }
}
