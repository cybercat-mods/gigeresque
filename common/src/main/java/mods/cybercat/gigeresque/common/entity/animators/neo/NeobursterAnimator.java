package mods.cybercat.gigeresque.common.entity.animators.neo;

import mod.azure.azurelib.rewrite.animation.controller.AzAnimationController;
import mod.azure.azurelib.rewrite.animation.controller.AzAnimationControllerContainer;
import mod.azure.azurelib.rewrite.animation.controller.keyframe.AzKeyframeCallbacks;
import mod.azure.azurelib.rewrite.animation.impl.AzEntityAnimator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.impl.neo.NeobursterEntity;
import mods.cybercat.gigeresque.common.sound.GigSounds;

public class NeobursterAnimator extends AzEntityAnimator<NeobursterEntity> {

    private static final ResourceLocation ANIMATIONS = Constants.modResource(
        "animations/entity/neoburster/neoburster.animation.json"
    );

    @Override
    public void registerControllers(AzAnimationControllerContainer<NeobursterEntity> animationControllerContainer) {
        animationControllerContainer.add(
            AzAnimationController.builder(this, Constants.BASE_CONTROLLER)
                .setTransitionLength(5)
                .setKeyframeCallbacks(
                    AzKeyframeCallbacks.<NeobursterEntity>builder()
                        .setSoundKeyframeHandler(
                            event -> {
                                if (event.getKeyframeData().getSound().equals("thudSoundkey")) {
                                    event.getAnimatable()
                                        .level()
                                        .playLocalSound(
                                            event.getAnimatable().getX(),
                                            event.getAnimatable().getY(),
                                            event.getAnimatable().getZ(),
                                            GigSounds.ALIEN_DEATH_THUD.get(),
                                            SoundSource.HOSTILE,
                                            0.5F,
                                            1.0F,
                                            true
                                        );
                                }
                                if (event.getKeyframeData().getSound().equals("stepSoundkey")) {
                                    event.getAnimatable()
                                        .level()
                                        .playLocalSound(
                                            event.getAnimatable().getX(),
                                            event.getAnimatable().getY(),
                                            event.getAnimatable().getZ(),
                                            GigSounds.ALIEN_HANDSTEP.get(),
                                            SoundSource.HOSTILE,
                                            0.5F,
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
            AzAnimationController.builder(this, Constants.ATTACK_CONTROLLER)
                .setTransitionLength(5)
                .build()
        );
    }

    @Override
    public @NotNull ResourceLocation getAnimationLocation(NeobursterEntity animatable) {
        return ANIMATIONS;
    }
}
