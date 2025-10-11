package mods.cybercat.gigeresque.common.entity.animators.runner;

import mod.azure.azurelib.common.animation.controller.AzAnimationController;
import mod.azure.azurelib.common.animation.controller.AzAnimationControllerContainer;
import mod.azure.azurelib.common.animation.impl.AzEntityAnimator;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.impl.runner.RunnerbursterEntity;

public class RunnerbursterAnimator extends AzEntityAnimator<RunnerbursterEntity> {

    private static final ResourceLocation ANIMATIONS = Constants.modResource(
        "animations/entity/runnerburster/runnerburster.animation.json"
    );

    @Override
    public void registerControllers(AzAnimationControllerContainer<RunnerbursterEntity> animationControllerContainer) {
        animationControllerContainer.add(
            AzAnimationController.builder(this, Constants.BASE_CONTROLLER)
                .setTransitionLength(5)
                .build()
        );
        animationControllerContainer.add(
            AzAnimationController.builder(this, Constants.ATTACK_CONTROLLER)
                .setTransitionLength(5)
                .build()
        );
    }

    @Override
    public @NotNull ResourceLocation getAnimationLocation(RunnerbursterEntity animatable) {
        return ANIMATIONS;
    }
}
