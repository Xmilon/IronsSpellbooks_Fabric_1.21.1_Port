package net.neoforged.neoforge.client.event;

import net.neoforged.bus.api.Event;

public class CustomizeGuiOverlayEvent extends Event {
    public static class BossEventProgress extends CustomizeGuiOverlayEvent {
        private int increment;
        private int y;
        private net.minecraft.client.gui.GuiGraphics guiGraphics;
        private net.minecraft.world.BossEvent bossEvent;
        public int getIncrement(){ return increment; }
        public void setIncrement(int increment){ this.increment = increment; }
        public int getY() { return y; }
        public net.minecraft.client.gui.GuiGraphics getGuiGraphics() { return guiGraphics; }
        public net.minecraft.world.BossEvent getBossEvent() { return bossEvent; }
    }
}
