package mods.cybercat.gigeresque.common.block.animators;

import mod.azure.azurelib.rewrite.animation.AzAnimatorConfig;
import mod.azure.azurelib.rewrite.animation.controller.AzAnimationController;
import mod.azure.azurelib.rewrite.animation.controller.AzAnimationControllerContainer;
import mod.azure.azurelib.rewrite.animation.impl.AzBlockAnimator;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.block.petrifiedblocks.entity.PetrifiedOjbect2Entity;

public class Petrified2Animator extends AzBlockAnimator<PetrifiedOjbect2Entity> {

    private static final ResourceLocation ANIMATIONS = Constants.modResource(
        "animations/entity/chestburster/chestburster.animation.json"
    );

    public Petrified2Animator() {
        super(AzAnimatorConfig.defaultConfig());
    }

    @Override
    public void registerControllers(AzAnimationControllerContainer<PetrifiedOjbect2Entity> animationControllerContainer) {
        animationControllerContainer.add(
            AzAnimationController.builder(this, Constants.BASE_CONTROLLER)
                .build()
        );
    }

    @Override
    public @NotNull ResourceLocation getAnimationLocation(PetrifiedOjbect2Entity animatable) {
        return ANIMATIONS;
    }
}
