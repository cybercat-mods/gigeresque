package com.bvanseg.gigeresque.client.entity.render.feature;

import com.bvanseg.gigeresque.client.entity.model.EntityModelsJava;
import com.bvanseg.gigeresque.client.entity.render.AquaticAlienEntityRendererJava;
import com.bvanseg.gigeresque.client.entity.texture.EntityTexturesJava;
import com.bvanseg.gigeresque.common.entity.impl.AquaticAlienEntityJava;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import static com.bvanseg.gigeresque.common.util.MathUtil.clamp;
import static java.lang.Math.max;

@Environment(EnvType.CLIENT)
public class AquaticAlienFeatureRendererJava extends GeoLayerRenderer<AquaticAlienEntityJava> {
    private IGeoRenderer<AquaticAlienEntityJava> entityRenderer;
    public AquaticAlienFeatureRendererJava(IGeoRenderer<AquaticAlienEntityJava> entityRenderer) {
        super(entityRenderer);
        this.entityRenderer = entityRenderer;
    }

    @Override
    public void render(
            MatrixStack matrixStackIn,
            VertexConsumerProvider bufferIn,
            int packedLightIn,
            AquaticAlienEntityJava aquaticAlienEntity,
            float limbSwing,
            float limbSwingAmount,
            float partialTicks,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        var waterColor = aquaticAlienEntity.world.getBiome(aquaticAlienEntity.getBlockPos()).getWaterColor();
        var wr = ((waterColor >> 16) & 0xFF) / 255.0f;
        var wg = ((waterColor >>  8) & 0xFF) / 255.0f;
        var wb = ((waterColor >>  0) & 0xFF) / 255.0f;

        var r = clamp(wr + (wr / 1.2f), 0.0f, 1.0f);
        var g = clamp(wg + (wg / 1.2f), 0.0f, 1.0f);
        var b = clamp(wb + (wb / 1.2f), 0.0f, 1.0f);

        var uv = aquaticAlienEntity.hurtTime > 0 ? OverlayTexture.field_32953 : OverlayTexture.DEFAULT_UV;

        entityRenderer.render(
                getEntityModel().getModel(EntityModelsJava.AQUATIC_ALIEN),
                aquaticAlienEntity,
                partialTicks,
                RenderLayer.getEntityTranslucent(EntityTexturesJava.AQUATIC_ALIEN_YOUNG),
                matrixStackIn,
                bufferIn,
                bufferIn.getBuffer(RenderLayer.getEntityCutout(EntityTexturesJava.AQUATIC_ALIEN_YOUNG)),
                packedLightIn,
                uv,
                1.0f,
                1.0f,
                1.0f,
                ((aquaticAlienEntity.getMaxGrowth() - aquaticAlienEntity.getGrowth()) / aquaticAlienEntity.getMaxGrowth())
        );

        entityRenderer.render(
                getEntityModel().getModel(EntityModelsJava.AQUATIC_ALIEN),
                aquaticAlienEntity,
                partialTicks,
                RenderLayer.getEntityTranslucent(EntityTexturesJava.AQUATIC_ALIEN_TINTABLE),
                matrixStackIn,
                bufferIn,
                bufferIn.getBuffer(RenderLayer.getEntityCutout(EntityTexturesJava.AQUATIC_ALIEN_TINTABLE)),
                packedLightIn,
                uv,
                r, g, b, max((aquaticAlienEntity.getGrowth() / aquaticAlienEntity.getMaxGrowth()) - 0.3f, 0f)
        );
    }
}
