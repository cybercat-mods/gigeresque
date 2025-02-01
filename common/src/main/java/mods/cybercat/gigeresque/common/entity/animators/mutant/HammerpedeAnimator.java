package mods.cybercat.gigeresque.common.entity.animators.mutant;

import mod.azure.azurelib.rewrite.animation.controller.AzAnimationController;
import mod.azure.azurelib.rewrite.animation.controller.AzAnimationControllerContainer;
import mod.azure.azurelib.rewrite.animation.impl.AzEntityAnimator;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.impl.mutant.HammerpedeEntity;

public class HammerpedeAnimator extends AzEntityAnimator<HammerpedeEntity> {

    private static final ResourceLocation ANIMATIONS = Constants.modResource(
        "animations/entity/hammerpede/hammerpede.animation.json"
    );

    @Override
    public void registerControllers(AzAnimationControllerContainer<HammerpedeEntity> animationControllerContainer) {
        animationControllerContainer.add(
            AzAnimationController.builder(this, Constants.BASE_CONTROLLER)
                .setTransitionLength(5)
                .build()
        );
        animationControllerContainer.add(
            AzAnimationController.builder(this, Constants.HOSTILE_CONTROLLER)
                .setTransitionLength(0)
                .build()
        );
        animationControllerContainer.add(
            AzAnimationController.builder(this, Constants.ATTACK_CONTROLLER)
                .setTransitionLength(0)
                .build()
        );
    }

    @Override
    public @NotNull ResourceLocation getAnimationLocation(HammerpedeEntity animatable) {
        return ANIMATIONS;
    }
}
