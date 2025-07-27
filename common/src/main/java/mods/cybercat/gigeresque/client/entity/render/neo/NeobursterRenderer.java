package mods.cybercat.gigeresque.client.entity.render.neo;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererConfig;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.render.feature.BloodLayer;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.neo.NeobursterAnimator;
import mods.cybercat.gigeresque.common.entity.helper.managers.animations.neo.NeobursterAnimManager;
import mods.cybercat.gigeresque.common.entity.impl.neo.NeobursterEntity;

public class NeobursterRenderer extends AzEntityRenderer<NeobursterEntity> {

    public NeobursterRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<NeobursterEntity>builder(EntityModels.NEOBURSTER, EntityTextures.NEOBURSTER)
                .setAnimatorProvider(NeobursterAnimator::new)
                .setDeathMaxRotation(0.0F)
                .addRenderLayer(new BloodLayer<>())
                .setScale(bursterEntity -> 1.0f + ((bursterEntity.getGrowth() / bursterEntity.getMaxGrowth()) / 5.0f))
                .setPrerenderEntry(rendererPipelineContext -> {
                    if (rendererPipelineContext.bakedModel().getBone("sac").isPresent()) {
                        rendererPipelineContext.bakedModel().getBone("sac").get().setHidden(true);
                    }
                    return rendererPipelineContext;
                })
                .build(),
            context
        );
        this.shadowRadius = 0.25f;
    }

    @Override
    public void render(
        @NotNull NeobursterEntity entity,
        float entityYaw,
        float partialTick,
        @NotNull PoseStack poseStack,
        @NotNull MultiBufferSource bufferSource,
        int packedLight
    ) {
        NeobursterAnimManager.handleAnimations(entity);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
