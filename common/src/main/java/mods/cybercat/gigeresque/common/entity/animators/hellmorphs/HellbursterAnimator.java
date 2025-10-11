package mods.cybercat.gigeresque.common.entity.animators.hellmorphs;

import mod.azure.azurelib.common.animation.controller.AzAnimationController;
import mod.azure.azurelib.common.animation.controller.AzAnimationControllerContainer;
import mod.azure.azurelib.common.animation.impl.AzEntityAnimator;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.impl.hellmorphs.HellbursterEntity;

public class HellbursterAnimator extends AzEntityAnimator<HellbursterEntity> {

    private static final ResourceLocation ANIMATIONS = Constants.modResource(
        "animations/entity/hell_burster/hell_burster.animation.json"
    );

    @Override
    public void registerControllers(AzAnimationControllerContainer<HellbursterEntity> animationControllerContainer) {
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
    public @NotNull ResourceLocation getAnimationLocation(HellbursterEntity animatable) {
        return ANIMATIONS;
    }
}
