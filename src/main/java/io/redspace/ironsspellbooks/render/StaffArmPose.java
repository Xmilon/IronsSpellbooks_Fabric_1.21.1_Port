package io.redspace.ironsspellbooks.render;

import net.minecraft.client.model.HumanoidModel;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

public final class StaffArmPose {
    private StaffArmPose() {
    }

    public static final EnumProxy<HumanoidModel.ArmPose> STAFF_ARM_POSE =
            new EnumProxy<>(HumanoidModel.ArmPose.class);
}
