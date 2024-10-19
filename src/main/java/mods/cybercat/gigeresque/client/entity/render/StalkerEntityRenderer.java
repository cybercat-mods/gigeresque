package mods.cybercat.gigeresque.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.azure.azurelib.cache.object.BakedGeoModel;
import mod.azure.azurelib.renderer.GeoEntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.client.entity.model.StalkerEntityModel;
import mods.cybercat.gigeresque.common.entity.impl.mutant.StalkerEntity;

public class StalkerEntityRenderer extends GeoEntityRenderer<StalkerEntity> {

    public StalkerEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new StalkerEntityModel());
        this.shadowRadius = 1.0f;
    }

    @Override
    public void actuallyRender(
        PoseStack poseStack,
        StalkerEntity animatable,
        BakedGeoModel model,
        RenderType renderType,
        MultiBufferSource bufferSource,
        VertexConsumer buffer,
        boolean isReRender,
        float partialTick,
        int packedLight,
        int packedOverlay,
        float red,
        float green,
        float blue,
        float alpha
    ) {
        super.actuallyRender(
            poseStack,
            animatable,
            model,
            renderType,
            bufferSource,
            buffer,
            isReRender,
            partialTick,
            packedLight,
            packedOverlay,
            red,
            green,
            blue,
            1.0F
        );
    }

    @Override
    public void render(
        @NotNull StalkerEntity entity,
        float entityYaw,
        float partialTick,
        @NotNull PoseStack poseStack,
        @NotNull MultiBufferSource bufferSource,
        int packedLight
    ) {
        if (!entity.isVehicle()) {
            poseStack.translate(0, -0.18, 0);
        }
        if (entity.isNoAi()) {
            poseStack.translate(0, 0.15, 0);
        }
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    protected float getDeathMaxRotation(StalkerEntity entityLivingBaseIn) {
        return 0.0F;
    }

    @Override
    public float getMotionAnimThreshold(StalkerEntity animatable) {
        return 0.005f;
    }
}
