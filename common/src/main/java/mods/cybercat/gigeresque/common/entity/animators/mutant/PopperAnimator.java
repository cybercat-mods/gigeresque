package mods.cybercat.gigeresque.common.entity.animators.mutant;

import mod.azure.azurelib.rewrite.animation.controller.AzAnimationController;
import mod.azure.azurelib.rewrite.animation.controller.AzAnimationControllerContainer;
import mod.azure.azurelib.rewrite.animation.controller.keyframe.AzKeyframeCallbacks;
import mod.azure.azurelib.rewrite.animation.impl.AzEntityAnimator;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.impl.classic.ChestbursterEntity;
import mods.cybercat.gigeresque.common.entity.impl.mutant.PopperEntity;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import org.jetbrains.annotations.NotNull;

public class PopperAnimator extends AzEntityAnimator<PopperEntity> {

    private static final ResourceLocation ANIMATIONS = Constants.modResource(
        "animations/entity/popper/popper.animation.json"
    );

    @Override
    public void registerControllers(AzAnimationControllerContainer<PopperEntity> animationControllerContainer) {
        animationControllerContainer.add(
            AzAnimationController.builder(this, Constants.BASE_CONTROLLER)
                .setTransitionLength(5)
                .build()
        );
    }

    @Override
    public @NotNull ResourceLocation getAnimationLocation(PopperEntity animatable) {
        return ANIMATIONS;
    }
}
