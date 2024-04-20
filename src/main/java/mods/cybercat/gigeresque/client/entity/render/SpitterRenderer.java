package mods.cybercat.gigeresque.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.azure.azurelib.cache.object.BakedGeoModel;
import mod.azure.azurelib.renderer.GeoEntityRenderer;
import mod.azure.azurelib.renderer.layer.AutoGlowingGeoLayer;
import mods.cybercat.gigeresque.client.entity.model.SpitterModel;
import mods.cybercat.gigeresque.common.entity.impl.extra.SpitterEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;

public class SpitterRenderer extends GeoEntityRenderer<SpitterEntity> {
    public SpitterRenderer(EntityRendererProvider.Context context) {
        super(context, new SpitterModel());
        this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
        this.shadowRadius = 0.5f;
    }

    @Override
    public void render(SpitterEntity entity, float entityYaw, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        if (!entity.isVehicle()) {
            poseStack.translate(0, -0.86, 0);
        }
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    protected float getDeathMaxRotation(SpitterEntity entityLivingBaseIn) {
        return 0.0F;
    }

    @Override
    public float getMotionAnimThreshold(SpitterEntity animatable) {
        return 0.005f;
    }
}
