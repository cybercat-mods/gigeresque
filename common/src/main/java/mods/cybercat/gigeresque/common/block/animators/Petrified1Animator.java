package mods.cybercat.gigeresque.common.block.animators;

import mod.azure.azurelib.rewrite.animation.AzAnimatorConfig;
import mod.azure.azurelib.rewrite.animation.controller.AzAnimationController;
import mod.azure.azurelib.rewrite.animation.controller.AzAnimationControllerContainer;
import mod.azure.azurelib.rewrite.animation.impl.AzBlockAnimator;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.block.entity.SporeBlockEntity;
import mods.cybercat.gigeresque.common.block.petrifiedblocks.entity.PetrifiedOjbect1Entity;
import mods.cybercat.gigeresque.common.block.petrifiedblocks.entity.PetrifiedOjbect4Entity;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class Petrified1Animator extends AzBlockAnimator<PetrifiedOjbect1Entity> {

    private static final ResourceLocation ANIMATIONS = Constants.modResource(
            "animations/entity/aquatic_chestburster/aquatic_chestburster.animation.json"
    );

    public Petrified1Animator() {
        super(AzAnimatorConfig.defaultConfig());
    }

    @Override
    public void registerControllers(AzAnimationControllerContainer<PetrifiedOjbect1Entity> animationControllerContainer) {
        animationControllerContainer.add(
                AzAnimationController.builder(this, Constants.BASE_CONTROLLER)
                        .build()
        );
    }

    @Override
    public @NotNull ResourceLocation getAnimationLocation(PetrifiedOjbect1Entity animatable) {
        return ANIMATIONS;
    }
}
