package com.bvanseg.gigeresque.client.entity.render.feature;

import com.bvanseg.gigeresque.client.entity.model.EntityModelsJava;
import com.bvanseg.gigeresque.client.entity.texture.EntityTexturesJava;
import com.bvanseg.gigeresque.common.entity.impl.RunnerAlienEntityJava;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

@Environment(EnvType.CLIENT)
public class RunnerAlienFeatureRendererJava extends GeoLayerRenderer<RunnerAlienEntityJava> {
    IGeoRenderer<RunnerAlienEntityJava> entityRenderer;
    public RunnerAlienFeatureRendererJava(IGeoRenderer<RunnerAlienEntityJava> entityRenderer) {
        super(entityRenderer);
        this.entityRenderer = entityRenderer;
    }

    @Override
    public void render(
            MatrixStack matrixStackIn,
            VertexConsumerProvider bufferIn,
            int packedLightIn,
            RunnerAlienEntityJava runnerAlienEntity,
            float limbSwing,
            float limbSwingAmount,
            float partialTicks,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        var uv = runnerAlienEntity.hurtTime > 0 ? OverlayTexture.field_32953 : OverlayTexture.DEFAULT_UV;

        entityRenderer.render(
                getEntityModel().getModel(EntityModelsJava.RUNNER_ALIEN),
                runnerAlienEntity,
                partialTicks,
                RenderLayer.getEntityTranslucent(EntityTexturesJava.RUNNER_ALIEN_YOUNG),
                matrixStackIn,
                bufferIn,
                bufferIn.getBuffer(RenderLayer.getEntityCutout(EntityTexturesJava.RUNNER_ALIEN_YOUNG)),
                packedLightIn,
                uv,
                1.0f,
                1.0f,
                1.0f,
                ((runnerAlienEntity.getMaxGrowth() - runnerAlienEntity.getGrowth()) / runnerAlienEntity.getMaxGrowth())
        );
    }

    @Override
    protected Identifier getEntityTexture(RunnerAlienEntityJava entityIn) {
        return EntityTexturesJava.RUNNER_ALIEN_YOUNG;
    }
}
