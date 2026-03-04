package net.neoforged.neoforge.fluids;

public class FluidType {
    public static class Properties {
        public static Properties create() { return new Properties(); }
    }

    public FluidType() {
        this(Properties.create());
    }

    public FluidType(Properties properties) {
    }

    public net.minecraft.network.chat.Component getDescription(net.neoforged.neoforge.fluids.FluidStack stack) {
        return net.minecraft.network.chat.Component.literal("Fluid");
    }

    public String getDescriptionId(net.neoforged.neoforge.fluids.FluidStack stack) {
        return "block.minecraft.water";
    }

    public int getLightLevel(net.neoforged.neoforge.fluids.FluidStack stack) {
        return 0;
    }
}
