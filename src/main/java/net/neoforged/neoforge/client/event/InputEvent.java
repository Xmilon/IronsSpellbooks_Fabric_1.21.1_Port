package net.neoforged.neoforge.client.event;

import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public interface InputEvent {
    class MouseScrollingEvent extends Event implements ICancellableEvent {
        private double scrollDeltaY;
        private boolean canceled;

        public MouseScrollingEvent(double scrollDeltaY) {
            this.scrollDeltaY = scrollDeltaY;
        }

        public double getScrollDeltaY() {
            return scrollDeltaY;
        }

        @Override
        public boolean isCanceled() {
            return canceled;
        }

        @Override
        public void setCanceled(boolean canceled) {
            this.canceled = canceled;
        }
    }

    class InteractionKeyMappingTriggered extends Event implements ICancellableEvent {
        private final boolean useItem;
        private final boolean attack;
        private boolean swingHand = true;
        private boolean canceled;

        public InteractionKeyMappingTriggered(boolean useItem, boolean attack) {
            this.useItem = useItem;
            this.attack = attack;
        }

        public boolean isUseItem() {
            return useItem;
        }

        public boolean isAttack() {
            return attack;
        }

        public void setSwingHand(boolean swingHand) {
            this.swingHand = swingHand;
        }

        public boolean shouldSwingHand() {
            return swingHand;
        }

        @Override
        public boolean isCanceled() {
            return canceled;
        }

        @Override
        public void setCanceled(boolean canceled) {
            this.canceled = canceled;
        }
    }
}
