package net.neoforged.neoforge.common.loot;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public abstract class LootModifier implements IGlobalLootModifier {
    protected final LootItemCondition[] conditions;

    protected LootModifier() {
        this(new LootItemCondition[0]);
    }

    protected LootModifier(LootItemCondition[] conditions) {
        this.conditions = conditions;
    }

    @Override
    public ObjectArrayList<ItemStack> apply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        return doApply(generatedLoot, context);
    }

    protected abstract ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context);
}
