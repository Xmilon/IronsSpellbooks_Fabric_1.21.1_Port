package io.redspace.ironsspellbooks.item.armor;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.entity.armor.*;
import io.redspace.ironsspellbooks.entity.armor.netherite.NetheriteMageArmorModel;
import io.redspace.ironsspellbooks.entity.armor.priest.PriestArmorModel;
import io.redspace.ironsspellbooks.entity.armor.priest.PriestArmorRenderer;
import io.redspace.ironsspellbooks.entity.armor.pumpkin.PumpkinArmorModel;
import io.redspace.ironsspellbooks.entity.armor.pumpkin.PumpkinArmorRenderer;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

import java.util.Map;

public final class ArmorClientRenderers {
    private ArmorClientRenderers() {
    }

    public static GeoArmorRenderer<?> create(ExtendedArmorItem item) {
        return switch (item.getClass().getSimpleName()) {
            case "ArchevokerArmorItem" -> new GenericCustomArmorRenderer<>(new ArchevokerArmorModel());
            case "BootsOfSpeedArmorItem" -> new GenericCustomArmorRenderer<>(new BootsOfSpeedArmorModel());
            case "CryomancerArmorItem" -> new GenericCustomArmorRenderer<>(new CryomancerArmorModel());
            case "CultistArmorItem" -> new GenericCustomArmorRenderer<>(new CultistArmorModel());
            case "ElectromancerArmorItem" -> new GenericCustomArmorRenderer<>(new ElectromancerArmorModel());
            case "GoldCrownArmorItem" -> new GenericCustomArmorRenderer<>(new GoldCrownModel());
            case "InfernalSorcererArmorItem" -> new GenericCustomArmorRenderer<>(new InfernalSorcererArmorModel());
            case "NetheriteMageArmorItem" -> new DyeableArmorRenderer<>(new NetheriteMageArmorModel());
            case "PaladinArmorItem" -> new GenericCustomArmorRenderer<>(new PaladinArmorModel());
            case "PlaguedArmorItem" -> new GenericCustomArmorRenderer<>(new PlaguedArmorModel());
            case "PriestArmorItem" -> new PriestArmorRenderer(new PriestArmorModel());
            case "PumpkinArmorItem" -> new PumpkinArmorRenderer(new PumpkinArmorModel());
            case "PyromancerArmorItem" -> new GenericCustomArmorRenderer<>(new PyromancerArmorModel());
            case "ShadowwalkerArmorItem" -> new GenericCustomArmorRenderer<>(new ShadowwalkerArmorModel());
            case "TarnishedCrownArmorItem" -> new GenericCustomArmorRenderer<>(new TarnishedCrownModel());
            case "WanderingMagicianArmorItem" -> new GenericCustomArmorRenderer<>(new WanderingMagicianModel());
            case "WizardArmorItem" -> new DyeableArmorRenderer<>(new GenericArmorModel<WizardArmorItem>("wizard")
                    .variants(Map.of("hat", IronsSpellbooks.id("geo/wizard_armor_hat.geo.json"))));
            default -> throw new IllegalStateException("No client armor renderer registered for " + item.getClass().getName());
        };
    }
}
