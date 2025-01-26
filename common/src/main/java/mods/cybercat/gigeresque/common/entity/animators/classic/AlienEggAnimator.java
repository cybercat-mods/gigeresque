package mods.cybercat.gigeresque.common.entity.animators.classic;

import mod.azure.azurelib.rewrite.animation.AzAnimatorConfig;
import mod.azure.azurelib.rewrite.animation.controller.AzAnimationController;
import mod.azure.azurelib.rewrite.animation.controller.AzAnimationControllerContainer;
import mod.azure.azurelib.rewrite.animation.controller.keyframe.AzKeyframeCallbacks;
import mod.azure.azurelib.rewrite.animation.impl.AzEntityAnimator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.impl.classic.AlienEggEntity;
import mods.cybercat.gigeresque.common.sound.GigSounds;

public class AlienEggAnimator extends AzEntityAnimator<AlienEggEntity> {

    private static final ResourceLocation ANIMATIONS = Constants.modResource(
        "animations/entity/egg/egg.animation.json"
    );

    public AlienEggAnimator() {
        super(AzAnimatorConfig.defaultConfig());
    }

    @Override
    public void registerControllers(AzAnimationControllerContainer<AlienEggEntity> animationControllerContainer) {
        animationControllerContainer.add(
            AzAnimationController.builder(this, Constants.BASE_CONTROLLER)
                .setTransitionLength(5)
                .setKeyframeCallbacks(
                    AzKeyframeCallbacks.<AlienEggEntity>builder()
                        .setSoundKeyframeHandler(
                            event -> {
                                if (event.getKeyframeData().getSound().equals("hatching")) {
                                    event.getAnimatable()
                                        .level()
                                        .playLocalSound(
                                            event.getAnimatable().getX(),
                                            event.getAnimatable().getY(),
                                            event.getAnimatable().getZ(),
                                            GigSounds.EGG_OPEN.get(),
                                            SoundSource.HOSTILE,
                                            0.75F,
                                            0.1F,
                                            true
                                        );
                                }
                            }
                        )
                        .build()
                )
                .build()
        );
    }

    @Override
    public @NotNull ResourceLocation getAnimationLocation(AlienEggEntity animatable) {
        return ANIMATIONS;
    }
}
