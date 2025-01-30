package mods.cybercat.gigeresque.common.entity.animators.classic;

import mod.azure.azurelib.rewrite.animation.controller.AzAnimationController;
import mod.azure.azurelib.rewrite.animation.controller.AzAnimationControllerContainer;
import mod.azure.azurelib.rewrite.animation.controller.keyframe.AzKeyframeCallbacks;
import mod.azure.azurelib.rewrite.animation.impl.AzEntityAnimator;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.impl.classic.ChestbursterEntity;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import org.jetbrains.annotations.NotNull;

public class ChestbursterAnimator extends AzEntityAnimator<ChestbursterEntity> {

    private static final ResourceLocation ANIMATIONS = Constants.modResource(
            "animations/entity/chestburster/chestburster.animation.json"
    );

    @Override
    public void registerControllers(AzAnimationControllerContainer<ChestbursterEntity> animationControllerContainer) {
        animationControllerContainer.add(
                AzAnimationController.builder(this, Constants.BASE_CONTROLLER)
                        .setTransitionLength(5)
                        .setKeyframeCallbacks(
                                AzKeyframeCallbacks.<ChestbursterEntity>builder()
                                        .setSoundKeyframeHandler(
                                                event -> {
                                                    if (event.getKeyframeData().getSound().equals("stepSoundkey")) {
                                                        event.getAnimatable()
                                                                .level()
                                                                .playLocalSound(
                                                                        event.getAnimatable().getX(),
                                                                        event.getAnimatable().getY(),
                                                                        event.getAnimatable().getZ(),
                                                                        GigSounds.BURSTER_CRAWL.get(),
                                                                        SoundSource.HOSTILE,
                                                                        0.25F,
                                                                        1.0F,
                                                                        true
                                                                );
                                                    }
                                                }
                                        )
                                        .build()
                        )
                        .build()
        );
        animationControllerContainer.add(
                AzAnimationController.builder(this, Constants.LIVING_CONTROLLER)
                        .setTransitionLength(0)
                        .build()
        );
    }

    @Override
    public @NotNull ResourceLocation getAnimationLocation(ChestbursterEntity animatable) {
        return ANIMATIONS;
    }
}
