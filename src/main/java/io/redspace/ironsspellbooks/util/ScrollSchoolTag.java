package io.redspace.ironsspellbooks.util;

import io.redspace.ironsspellbooks.api.spells.SchoolType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public final class ScrollSchoolTag {
    public static final String SCHOOL_TAG = "school";

    private ScrollSchoolTag() {
    }

    public static void setSchool(ItemStack stack, SchoolType schoolType) {
        if (schoolType == null || schoolType.getId() == null) {
            return;
        }
        var schoolId = schoolType.getId().toString();
        if (schoolId == null || schoolId.isBlank()) {
            return;
        }
        setSchool(stack, schoolId);
    }

    public static void setSchool(ItemStack stack, String schoolPath) {
        if (stack == null || stack.isEmpty() || schoolPath == null || schoolPath.isBlank()) {
            return;
        }
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        tag.putString(SCHOOL_TAG, schoolPath);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        syncModelData(stack);
    }

    public static Optional<String> getSchool(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return Optional.empty();
        }
        var customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) {
            return Optional.empty();
        }
        var tag = customData.copyTag();
        var school = tag.getString(SCHOOL_TAG);
        if (school == null || school.isBlank()) {
            return Optional.empty();
        }
        return Optional.of(school);
    }

    public static void syncModelData(ItemStack stack) {
        var school = getSchool(stack);
        if (school.isEmpty()) {
            stack.remove(DataComponents.CUSTOM_MODEL_DATA);
            return;
        }
        int modelValue = modelValueForSchool(school.get());
        if (modelValue <= 0) {
            stack.remove(DataComponents.CUSTOM_MODEL_DATA);
            return;
        }
        stack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(modelValue));
    }

    private static int modelValueForSchool(String schoolIdOrPath) {
        var parsed = ResourceLocation.tryParse(schoolIdOrPath);
        var path = parsed != null ? parsed.getPath() : schoolIdOrPath;
        return Math.round(ScrollSchoolModels.predicateValueForSchool(path));
    }
}
