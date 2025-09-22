package mods.cybercat.gigeresque.client.entity.render.classic;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.rewrite.render.AzLayerRenderer;
import mod.azure.azurelib.rewrite.render.AzModelRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererConfig;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererPipeline;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.client.entity.model.EggModelRenderer;
import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.classic.AlienEggAnimator;
import mods.cybercat.gigeresque.common.entity.helper.GigCommonMethods;
import mods.cybercat.gigeresque.common.entity.helper.states.EggStates;
import mods.cybercat.gigeresque.common.entity.impl.classic.AlienEggEntity;

public class EggEntityRenderer extends AzEntityRenderer<AlienEggEntity> {

    private static final RenderType EGG_RENDER_TYPE = RenderType.entityCutoutNoCull(EntityTextures.EGG);

    private static final RenderType EGG_ACTIVE_RENDER_TYPE = RenderType.entityTranslucentCull(EntityTextures.EGG_ACTIVE);

    public EggEntityRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<AlienEggEntity>builder(
                alienEggEntity -> EntityModels.EGG,
                alienEggEntity -> alienEggEntity.getEggState() == EggStates.HATCHING.ordinal() || alienEggEntity
                    .getEggState() == EggStates.HATCHED.ordinal() ? EntityTextures.EGG_ACTIVE : EntityTextures.EGG
            )
                .setAnimatorProvider(AlienEggAnimator::new)
                .setDeathMaxRotation(0.0F)
                .setShadowRadius(0.5F)
                .setRenderType(
                    alienEggEntity -> alienEggEntity.getEggState() == EggStates.HATCHING.ordinal() || alienEggEntity
                        .getEggState() == EggStates.HATCHED.ordinal() && alienEggEntity.isAlive()
                            ? EGG_ACTIVE_RENDER_TYPE
                            : EGG_RENDER_TYPE
                )
                .build(),
            context
        );
    }

    @Override
    public void render(
        @NotNull AlienEggEntity entity,
        float entityYaw,
        float partialTick,
        @NotNull PoseStack poseStack,
        @NotNull MultiBufferSource bufferSource,
        int packedLight
    ) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        if (entity.isDeadOrDying()) {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendDeath);
        } else if (entity.getEggState() == EggStates.HATCHING.ordinal()) {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendHatching);
        } else if (entity.getEggState() == EggStates.HATCHED.ordinal()) {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendHatchEmpty);
        } else {
            GigCommonMethods.setAnimation(entity.animationDispatcher::sendIdle);
        }
    }

    @Override
    public AzEntityRendererPipeline<AlienEggEntity> createPipeline(AzEntityRendererConfig<AlienEggEntity> config) {
        return new AzEntityRendererPipeline<>(config, this) {

            @Override
            protected AzModelRenderer<AlienEggEntity> createModelRenderer(AzLayerRenderer<AlienEggEntity> layerRenderer) {
                return new EggModelRenderer(this, layerRenderer);
            }
        };
    }
}
