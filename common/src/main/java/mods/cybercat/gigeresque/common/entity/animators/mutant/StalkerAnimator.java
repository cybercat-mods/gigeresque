package mods.cybercat.gigeresque.common.entity.animators.mutant;

import mod.azure.azurelib.rewrite.animation.controller.AzAnimationController;
import mod.azure.azurelib.rewrite.animation.controller.AzAnimationControllerContainer;
import mod.azure.azurelib.rewrite.animation.impl.AzEntityAnimator;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.impl.mutant.StalkerEntity;

public class StalkerAnimator extends AzEntityAnimator<StalkerEntity> {

    private static final ResourceLocation ANIMATIONS = Constants.modResource(
        "animations/entity/stalker/stalker.animation.json"
    );

    @Override
    public void registerControllers(AzAnimationControllerContainer<StalkerEntity> animationControllerContainer) {
        animationControllerContainer.add(
            AzAnimationController.builder(this, Constants.BASE_CONTROLLER)
                .setTransitionLength(5)
                .build()
        );
    }

    @Override
    public @NotNull ResourceLocation getAnimationLocation(StalkerEntity animatable) {
        return ANIMATIONS;
    }
}
