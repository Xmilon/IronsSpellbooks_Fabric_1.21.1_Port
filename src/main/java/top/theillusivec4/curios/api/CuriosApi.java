package top.theillusivec4.curios.api;

import io.redspace.ironsspellbooks.compat.Curios;
import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.item.curios.CurioBaseItem;
import io.redspace.ironsspellbooks.item.curios.SimpleDescriptiveCurio;
import io.redspace.ironsspellbooks.util.ModTags;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.Container;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class CuriosApi {
    private static final CuriosHelper HELPER = new CuriosHelper();
    private static final boolean TRINKETS_LOADED = FabricLoader.getInstance().isModLoaded("trinkets");

    public static Optional<ICuriosItemHandler> getCuriosInventory(LivingEntity entity) {
        return Optional.of(new FallbackCuriosItemHandler(entity));
    }

    public static CuriosHelper getCuriosHelper() {
        return HELPER;
    }

    public interface ICuriosItemHandler {
        interface EquippedCurios {
            default ItemStack getStackInSlot(int index) {
                return ItemStack.EMPTY;
            }
        }

        default List<SlotResult> findCurios(Predicate<ItemStack> predicate) {
            return List.of();
        }

        default List<SlotResult> findCurios(Item item) {
            return List.of();
        }

        default Optional<SlotResult> findCurio(String identifier, int index) {
            return Optional.empty();
        }

        default void setEquippedCurio(String identifier, int index, ItemStack stack) {
        }

        default Optional<SlotResult> findFirstCurio(Item item) {
            return Optional.empty();
        }

        default EquippedCurios getEquippedCurios() {
            return new EquippedCurios() {};
        }
    }

    public static class CuriosHelper {
        public List<String> getCurioTags(Item item) {
            if (item instanceof SpellBook) {
                return List.of(Curios.SPELLBOOK_SLOT);
            }
            if (item instanceof SimpleDescriptiveCurio simpleDescriptiveCurio && simpleDescriptiveCurio.getSlotIdentifier() != null) {
                return List.of(simpleDescriptiveCurio.getSlotIdentifier());
            }
            if (item instanceof CurioBaseItem curioBaseItem && !curioBaseItem.getCurioSlotId().isBlank()) {
                return List.of(curioBaseItem.getCurioSlotId());
            }
            return Collections.emptyList();
        }
    }

    private static final class FallbackCuriosItemHandler implements ICuriosItemHandler {
        private final LivingEntity entity;

        private FallbackCuriosItemHandler(LivingEntity entity) {
            this.entity = entity;
        }

        @Override
        public List<SlotResult> findCurios(Predicate<ItemStack> predicate) {
            return scanCurios().stream().map(CurioEntry::asResult).filter(result -> predicate.test(result.stack())).toList();
        }

        @Override
        public List<SlotResult> findCurios(Item item) {
            return scanCurios().stream().map(CurioEntry::asResult).filter(result -> result.stack().is(item)).toList();
        }

        @Override
        public Optional<SlotResult> findCurio(String identifier, int index) {
            return scanCurios().stream().filter(entry -> entry.slotContext.identifier().equals(identifier) && entry.slotContext.index() == index).map(CurioEntry::asResult).findFirst();
        }

        @Override
        public void setEquippedCurio(String identifier, int index, ItemStack stack) {
            scanCurios().stream()
                    .filter(entry -> entry.slotContext.identifier().equals(identifier) && entry.slotContext.index() == index)
                    .findFirst()
                    .ifPresent(entry -> entry.writeToInventory(stack));
        }

        @Override
        public Optional<SlotResult> findFirstCurio(Item item) {
            return scanCurios().stream().map(CurioEntry::asResult).filter(result -> result.stack().is(item)).findFirst();
        }

        @Override
        public EquippedCurios getEquippedCurios() {
            Map<Integer, ItemStack> byIndex = new HashMap<>();
            for (CurioEntry curioEntry : scanCurios()) {
                byIndex.put(curioEntry.slotContext.index(), curioEntry.stack);
            }
            return new EquippedCurios() {
                @Override
                public ItemStack getStackInSlot(int index) {
                    return byIndex.getOrDefault(index, ItemStack.EMPTY);
                }
            };
        }

        private List<CurioEntry> scanCurios() {
            if (!(entity instanceof Player player)) {
                return List.of();
            }
            var entries = new java.util.ArrayList<CurioEntry>();
            entries.addAll(scanTrinketCurios(player));
            Map<String, Integer> nextIndexBySlot = countSlotIndexes(entries);

            List<ItemStack> allStacks = new java.util.ArrayList<>();
            allStacks.addAll(player.getInventory().items);
            allStacks.addAll(player.getInventory().armor);
            allStacks.addAll(player.getInventory().offhand);

            for (int rawIndex = 0; rawIndex < allStacks.size(); rawIndex++) {
                ItemStack stack = allStacks.get(rawIndex);
                if (stack.isEmpty()) {
                    continue;
                }
                String slotId = inferSlotId(stack);
                if (slotId == null) {
                    continue;
                }
                int slotIndex = nextIndexBySlot.getOrDefault(slotId, 0);
                nextIndexBySlot.put(slotId, slotIndex + 1);
                SlotContext slotContext = new SlotContext(slotId, entity, slotIndex, false, true);
                final int inventoryIndex = rawIndex;
                entries.add(new CurioEntry(rawIndex, stack, slotContext, newStack -> writeToPlayerInventory(player, inventoryIndex, newStack)));
            }
            entries.sort(Comparator.comparingInt(entry -> entry.rawInventoryIndex));
            return entries;
        }

        private List<CurioEntry> scanTrinketCurios(Player player) {
            if (!TRINKETS_LOADED) {
                return List.of();
            }
            try {
                Class<?> trinketsApiClass = Class.forName("dev.emi.trinkets.api.TrinketsApi");
                Method getTrinketComponent = trinketsApiClass.getMethod("getTrinketComponent", LivingEntity.class);
                Object result = getTrinketComponent.invoke(null, player);
                if (!(result instanceof Optional<?> optionalComponent) || optionalComponent.isEmpty()) {
                    return List.of();
                }
                Object component = optionalComponent.get();
                Method getInventory = component.getClass().getMethod("getInventory");
                Object inventoriesObj = getInventory.invoke(component);
                if (!(inventoriesObj instanceof Map<?, ?> groups)) {
                    return List.of();
                }

                var entries = new java.util.ArrayList<CurioEntry>();
                Map<String, Integer> nextIndexBySlot = new HashMap<>();
                int syntheticIndex = -1_000_000;
                for (Map.Entry<?, ?> groupEntry : groups.entrySet()) {
                    String groupName = String.valueOf(groupEntry.getKey());
                    if (!(groupEntry.getValue() instanceof Map<?, ?> slotMap)) {
                        continue;
                    }
                    for (Map.Entry<?, ?> slotEntry : slotMap.entrySet()) {
                        String slotName = String.valueOf(slotEntry.getKey());
                        if (!(slotEntry.getValue() instanceof Container trinketInventory)) {
                            continue;
                        }

                        int size = trinketInventory.getContainerSize();
                        for (int index = 0; index < size; index++) {
                            ItemStack stack = trinketInventory.getItem(index);
                            String slotId = mapTrinketSlotToCurioSlot(groupName, slotName);
                            if (slotId == null && !stack.isEmpty()) {
                                slotId = inferSlotId(stack);
                            }
                            if (slotId == null) {
                                continue;
                            }
                            int slotIndex = nextIndexBySlot.getOrDefault(slotId, 0);
                            nextIndexBySlot.put(slotId, slotIndex + 1);
                            SlotContext slotContext = new SlotContext(slotId, entity, slotIndex, false, true);
                            Consumer<ItemStack> writer = createTrinketWriter(trinketInventory, index);
                            entries.add(new CurioEntry(syntheticIndex++, stack, slotContext, writer));
                        }
                    }
                }
                return entries;
            } catch (Throwable ignored) {
                return List.of();
            }
        }

        private static Consumer<ItemStack> createTrinketWriter(Container trinketInventory, int index) {
            return stack -> trinketInventory.setItem(index, stack);
        }

        private static String mapTrinketSlotToCurioSlot(String groupName, String slotName) {
            String normalized = (groupName + "_" + slotName).toLowerCase();
            if (normalized.contains("spellbook")) {
                return Curios.SPELLBOOK_SLOT;
            }
            if (normalized.contains("ring")) {
                return Curios.RING_SLOT;
            }
            if (normalized.contains("necklace") || normalized.contains("charm") || normalized.contains("amulet")) {
                return Curios.NECKLACE_SLOT;
            }
            return null;
        }

        private static Map<String, Integer> countSlotIndexes(List<CurioEntry> entries) {
            Map<String, Integer> counts = new HashMap<>();
            for (CurioEntry entry : entries) {
                counts.put(entry.slotContext.identifier(), counts.getOrDefault(entry.slotContext.identifier(), 0) + 1);
            }
            return counts;
        }

        private static void writeToPlayerInventory(Player player, int rawInventoryIndex, ItemStack newStack) {
            int itemsSize = player.getInventory().items.size();
            int armorSize = player.getInventory().armor.size();
            if (rawInventoryIndex < itemsSize) {
                player.getInventory().items.set(rawInventoryIndex, newStack);
            } else if (rawInventoryIndex < itemsSize + armorSize) {
                player.getInventory().armor.set(rawInventoryIndex - itemsSize, newStack);
            } else {
                int offhandIndex = rawInventoryIndex - itemsSize - armorSize;
                if (offhandIndex >= 0 && offhandIndex < player.getInventory().offhand.size()) {
                    player.getInventory().offhand.set(offhandIndex, newStack);
                }
            }
        }

        private String inferSlotId(ItemStack stack) {
            Item item = stack.getItem();
            if (stack.is(ModTags.SPELLBOOK_CURIO) || item instanceof SpellBook) {
                return Curios.SPELLBOOK_SLOT;
            }
            List<String> tags = HELPER.getCurioTags(item);
            if (!tags.isEmpty()) {
                return tags.getFirst();
            }
            if (item instanceof CurioBaseItem) {
                return "curio";
            }
            return null;
        }
    }

    private static final class CurioEntry {
        private final int rawInventoryIndex;
        private final ItemStack stack;
        private final SlotContext slotContext;
        private final Consumer<ItemStack> writer;

        private CurioEntry(int rawInventoryIndex, ItemStack stack, SlotContext slotContext, Consumer<ItemStack> writer) {
            this.rawInventoryIndex = rawInventoryIndex;
            this.stack = stack;
            this.slotContext = slotContext;
            this.writer = writer;
        }

        private SlotResult asResult() {
            return new SlotResult(slotContext, stack);
        }

        private void writeToInventory(ItemStack newStack) {
            writer.accept(newStack);
        }
    }
}
