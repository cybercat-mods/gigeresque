package mods.cybercat.gigeresque.common.block.animators;

import mod.azure.azurelib.rewrite.animation.AzAnimatorConfig;
import mod.azure.azurelib.rewrite.animation.controller.AzAnimationController;
import mod.azure.azurelib.rewrite.animation.controller.AzAnimationControllerContainer;
import mod.azure.azurelib.rewrite.animation.impl.AzBlockAnimator;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.block.petrifiedblocks.entity.PetrifiedOjbect4Entity;

public class Petrified4Animator extends AzBlockAnimator<PetrifiedOjbect4Entity> {

    private static final ResourceLocation ANIMATIONS = Constants.modResource(
        "animations/entity/runnerburster/runnerburster.animation.json"
    );

    public Petrified4Animator() {
        super(AzAnimatorConfig.defaultConfig());
    }

    @Override
    public void registerControllers(AzAnimationControllerContainer<PetrifiedOjbect4Entity> animationControllerContainer) {
        animationControllerContainer.add(
            AzAnimationController.builder(this, Constants.BASE_CONTROLLER)
                .build()
        );
    }

    @Override
    public @NotNull ResourceLocation getAnimationLocation(PetrifiedOjbect4Entity animatable) {
        return ANIMATIONS;
    }
}
