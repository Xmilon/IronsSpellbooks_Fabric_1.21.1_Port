package io.redspace.ironsspellbooks.compat.trinkets;

import io.redspace.ironsspellbooks.compat.TrinketsSlots;
import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.item.curios.CurioBaseItem;
import io.redspace.ironsspellbooks.item.curios.SimpleDescriptiveCurio;
import io.redspace.ironsspellbooks.util.ModTags;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class TrinketsApi {
    private static final TrinketsHelper HELPER = new TrinketsHelper();
    private static final boolean ACCESSORIES_LOADED = FabricLoader.getInstance().isModLoaded("accessories");
    private static final boolean TRINKETS_LOADED = FabricLoader.getInstance().isModLoaded("trinkets");

    public static Optional<ITrinketsItemHandler> getTrinketsInventory(LivingEntity entity) {
        return Optional.of(new FallbackTrinketsItemHandler(entity));
    }

    public static TrinketsHelper getTrinketsHelper() {
        return HELPER;
    }

    public interface ITrinketsItemHandler {
        interface EquippedTrinkets {
            default ItemStack getStackInSlot(int index) {
                return ItemStack.EMPTY;
            }
        }

        default List<TrinketSlotResult> findTrinkets(Predicate<ItemStack> predicate) {
            return List.of();
        }

        default List<TrinketSlotResult> findTrinkets(Item item) {
            return List.of();
        }

        default Optional<TrinketSlotResult> findTrinket(String identifier, int index) {
            return Optional.empty();
        }

        default void setEquippedTrinket(String identifier, int index, ItemStack stack) {
        }

        default Optional<TrinketSlotResult> findFirstTrinket(Item item) {
            return Optional.empty();
        }

        default EquippedTrinkets getEquippedTrinkets() {
            return new EquippedTrinkets() {
            };
        }
    }

    public static class TrinketsHelper {
        public List<String> getTrinketTags(Item item) {
            if (item instanceof SpellBook) {
                return List.of(TrinketsSlots.SPELLBOOK_SLOT);
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

    private static final class FallbackTrinketsItemHandler implements ITrinketsItemHandler {
        private final LivingEntity entity;

        private FallbackTrinketsItemHandler(LivingEntity entity) {
            this.entity = entity;
        }

        @Override
        public List<TrinketSlotResult> findTrinkets(Predicate<ItemStack> predicate) {
            return scanTrinkets().stream().map(TrinketEntry::asResult).filter(result -> predicate.test(result.stack())).toList();
        }

        @Override
        public List<TrinketSlotResult> findTrinkets(Item item) {
            return scanTrinkets().stream().map(TrinketEntry::asResult).filter(result -> result.stack().is(item)).toList();
        }

        @Override
        public Optional<TrinketSlotResult> findTrinket(String identifier, int index) {
            return scanTrinkets().stream().filter(entry -> entry.TrinketSlotContext.identifier().equals(identifier) && entry.TrinketSlotContext.index() == index).map(TrinketEntry::asResult).findFirst();
        }

        @Override
        public void setEquippedTrinket(String identifier, int index, ItemStack stack) {
            scanTrinkets().stream()
                    .filter(entry -> entry.TrinketSlotContext.identifier().equals(identifier) && entry.TrinketSlotContext.index() == index)
                    .findFirst()
                    .ifPresent(entry -> entry.writeToInventory(stack));
        }

        @Override
        public Optional<TrinketSlotResult> findFirstTrinket(Item item) {
            return scanTrinkets().stream().map(TrinketEntry::asResult).filter(result -> result.stack().is(item)).findFirst();
        }

        @Override
        public EquippedTrinkets getEquippedTrinkets() {
            Map<Integer, ItemStack> byIndex = new HashMap<>();
            for (TrinketEntry trinketEntry : scanTrinkets()) {
                byIndex.put(trinketEntry.TrinketSlotContext.index(), trinketEntry.stack);
            }
            return new EquippedTrinkets() {
                @Override
                public ItemStack getStackInSlot(int index) {
                    return byIndex.getOrDefault(index, ItemStack.EMPTY);
                }
            };
        }

        private List<TrinketEntry> scanTrinkets() {
            if (!(entity instanceof Player player)) {
                return List.of();
            }
            List<TrinketEntry> accessoriesEntries = scanAccessories(player);
            if (!accessoriesEntries.isEmpty()) {
                return accessoriesEntries;
            }
            if (!TRINKETS_LOADED) {
                return List.of();
            }
            return scanNativeTrinkets(player);
        }

        private List<TrinketEntry> scanAccessories(Player player) {
            if (!ACCESSORIES_LOADED) {
                return List.of();
            }
            try {
                Class<?> accessoriesCapabilityClass = Class.forName("io.wispforest.accessories.api.AccessoriesCapability");
                Method getOptionally = accessoriesCapabilityClass.getMethod("getOptionally", LivingEntity.class);
                Object capabilityResult = getOptionally.invoke(null, player);
                if (!(capabilityResult instanceof Optional<?> optionalCapability) || optionalCapability.isEmpty()) {
                    return List.of();
                }

                Object capability = optionalCapability.get();
                Method getContainers = capability.getClass().getMethod("getContainers");
                Object containersObj = getContainers.invoke(capability);
                if (!(containersObj instanceof Map<?, ?> containers)) {
                    return List.of();
                }

                var entries = new java.util.ArrayList<TrinketEntry>();
                for (Map.Entry<?, ?> entry : containers.entrySet()) {
                    String slotName = String.valueOf(entry.getKey());
                    String slotId = mapAccessorySlot(slotName);
                    if (slotId == null) {
                        continue;
                    }

                    Object container = entry.getValue();
                    int size = invokeNoArgsInt(container, "getSize", 0);
                    if (size <= 0) {
                        continue;
                    }

                    Object accessoryInventory = invokeNoArgs(container, "getAccessories");
                    if (accessoryInventory == null) {
                        continue;
                    }

                    for (int index = 0; index < size; index++) {
                        ItemStack stack = getInventoryStack(accessoryInventory, index);
                        String resolvedSlotId = slotId;
                        if (resolvedSlotId == null && !stack.isEmpty()) {
                            resolvedSlotId = inferSlotId(stack);
                        }
                        if (resolvedSlotId == null) {
                            continue;
                        }
                        TrinketSlotContext TrinketSlotContext = new TrinketSlotContext(resolvedSlotId, entity, index, false, true);
                        Consumer<ItemStack> writer = createTrinketWriter(accessoryInventory, index);
                        entries.add(new TrinketEntry(index, stack, TrinketSlotContext, writer));
                    }
                }
                return entries;
            } catch (Throwable ignored) {
                return List.of();
            }
        }

        private List<TrinketEntry> scanNativeTrinkets(Player player) {
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

                var entries = new java.util.ArrayList<TrinketEntry>();
                Map<String, Integer> nextIndexBySlot = new HashMap<>();
                int syntheticIndex = -1_000_000;
                for (Map.Entry<?, ?> groupEntry : groups.entrySet()) {
                    String groupName = String.valueOf(groupEntry.getKey());
                    if (!(groupEntry.getValue() instanceof Map<?, ?> slotMap)) {
                        continue;
                    }
                    for (Map.Entry<?, ?> slotEntry : slotMap.entrySet()) {
                        String slotName = String.valueOf(slotEntry.getKey());
                        Object trinketInventory = slotEntry.getValue();

                        int size = getInventorySize(trinketInventory);
                        if (size <= 0) {
                            continue;
                        }
                        for (int index = 0; index < size; index++) {
                            ItemStack stack = getInventoryStack(trinketInventory, index);
                            String slotId = mapTrinketSlot(groupName, slotName);
                            if (slotId == null && !stack.isEmpty()) {
                                slotId = inferSlotId(stack);
                            }
                            if (slotId == null) {
                                continue;
                            }
                            int slotIndex = nextIndexBySlot.getOrDefault(slotId, 0);
                            nextIndexBySlot.put(slotId, slotIndex + 1);
                            TrinketSlotContext TrinketSlotContext = new TrinketSlotContext(slotId, entity, slotIndex, false, true);
                            Consumer<ItemStack> writer = createTrinketWriter(trinketInventory, index);
                            entries.add(new TrinketEntry(syntheticIndex++, stack, TrinketSlotContext, writer));
                        }
                    }
                }
                return entries;
            } catch (Throwable ignored) {
                return List.of();
            }
        }

        private static Consumer<ItemStack> createTrinketWriter(Object trinketInventory, int index) {
            return stack -> setInventoryStack(trinketInventory, index, stack);
        }

        private static Object invokeNoArgs(Object target, String methodName) {
            try {
                Method method = target.getClass().getMethod(methodName);
                return method.invoke(target);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
                return null;
            }
        }

        private static <T> T invokeNoArgsTyped(Object target, String methodName, Class<T> expected) {
            Object result = invokeNoArgs(target, methodName);
            if (expected.isInstance(result)) {
                return expected.cast(result);
            }
            return null;
        }

        private static int invokeNoArgsInt(Object target, String methodName, int fallback) {
            Object result = invokeNoArgs(target, methodName);
            return result instanceof Number n ? n.intValue() : fallback;
        }

        private static int getInventorySize(Object inventory) {
            Object result = invoke(inventory, "getContainerSize");
            if (result instanceof Integer value) {
                return value;
            }
            result = invoke(inventory, "size");
            return result instanceof Integer value ? value : 0;
        }

        private static ItemStack getInventoryStack(Object inventory, int index) {
            Object result = invoke(inventory, "getItem", int.class, index);
            if (result instanceof ItemStack stack) {
                return stack;
            }
            result = invoke(inventory, "getStack", int.class, index);
            return result instanceof ItemStack stack ? stack : ItemStack.EMPTY;
        }

        private static void setInventoryStack(Object inventory, int index, ItemStack stack) {
            boolean wrote = invokeVoid(inventory, "setItem", int.class, index, ItemStack.class, stack);
            if (!wrote) {
                wrote = invokeVoid(inventory, "setStack", int.class, index, ItemStack.class, stack);
            }
            if (wrote) {
                invokeVoid(inventory, "markDirty");
            }
        }

        private static Object invoke(Object target, String methodName, Object... args) {
            Class<?>[] signature = new Class<?>[args.length / 2];
            Object[] values = new Object[args.length / 2];
            for (int i = 0; i < args.length; i += 2) {
                signature[i / 2] = (Class<?>) args[i];
                values[i / 2] = args[i + 1];
            }
            try {
                Method method = target.getClass().getMethod(methodName, signature);
                return method.invoke(target, values);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
                return null;
            }
        }

        private static boolean invokeVoid(Object target, String methodName, Object... args) {
            Class<?>[] signature = new Class<?>[args.length / 2];
            Object[] values = new Object[args.length / 2];
            for (int i = 0; i < args.length; i += 2) {
                signature[i / 2] = (Class<?>) args[i];
                values[i / 2] = args[i + 1];
            }
            try {
                Method method = target.getClass().getMethod(methodName, signature);
                method.invoke(target, values);
                return true;
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
                return false;
            }
        }

        private static String mapTrinketSlot(String groupName, String slotName) {
            String normalized = (groupName + "_" + slotName).toLowerCase();
            if (normalized.contains("spellbook")) {
                return TrinketsSlots.SPELLBOOK_SLOT;
            }
            if (normalized.contains("ring")) {
                return TrinketsSlots.RING_SLOT;
            }
            if (normalized.contains("necklace") || normalized.contains("charm") || normalized.contains("amulet")) {
                return TrinketsSlots.NECKLACE_SLOT;
            }
            return null;
        }

        private static String mapAccessorySlot(String slotName) {
            String normalized = slotName == null ? "" : slotName.toLowerCase();
            if (normalized.contains("spellbook")) {
                return TrinketsSlots.SPELLBOOK_SLOT;
            }
            if (normalized.contains("ring")) {
                return TrinketsSlots.RING_SLOT;
            }
            if (normalized.contains("necklace") || normalized.contains("charm") || normalized.contains("amulet")) {
                return TrinketsSlots.NECKLACE_SLOT;
            }
            if (TrinketsSlots.SPELLBOOK_SLOT.equals(normalized)
                    || TrinketsSlots.RING_SLOT.equals(normalized)
                    || TrinketsSlots.NECKLACE_SLOT.equals(normalized)) {
                return normalized;
            }
            return null;
        }

        private String inferSlotId(ItemStack stack) {
            Item item = stack.getItem();
            if (stack.is(ModTags.SPELLBOOK_CURIO) || item instanceof SpellBook) {
                return TrinketsSlots.SPELLBOOK_SLOT;
            }
            List<String> tags = HELPER.getTrinketTags(item);
            if (!tags.isEmpty()) {
                return tags.getFirst();
            }
            if (item instanceof CurioBaseItem) {
                return "trinket";
            }
            return null;
        }
    }

    private static final class TrinketEntry {
        private final int rawInventoryIndex;
        private final ItemStack stack;
        private final TrinketSlotContext TrinketSlotContext;
        private final Consumer<ItemStack> writer;

        private TrinketEntry(int rawInventoryIndex, ItemStack stack, TrinketSlotContext TrinketSlotContext, Consumer<ItemStack> writer) {
            this.rawInventoryIndex = rawInventoryIndex;
            this.stack = stack;
            this.TrinketSlotContext = TrinketSlotContext;
            this.writer = writer;
        }

        private TrinketSlotResult asResult() {
            return new TrinketSlotResult(TrinketSlotContext, stack);
        }

        private void writeToInventory(ItemStack newStack) {
            writer.accept(newStack);
        }
    }
}
