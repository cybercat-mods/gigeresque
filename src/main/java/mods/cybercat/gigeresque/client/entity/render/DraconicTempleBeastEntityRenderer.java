package mods.cybercat.gigeresque.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.renderer.GeoEntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.client.entity.model.DraconicTempleBeastEntityModel;
import mods.cybercat.gigeresque.common.entity.impl.templebeast.DraconicTempleBeastEntity;

public class DraconicTempleBeastEntityRenderer extends GeoEntityRenderer<DraconicTempleBeastEntity> {

    public DraconicTempleBeastEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new DraconicTempleBeastEntityModel());
        this.shadowRadius = 1.0f;
    }

    @Override
    public void render(
        DraconicTempleBeastEntity entity,
        float entityYaw,
        float partialTick,
        @NotNull PoseStack poseStack,
        @NotNull MultiBufferSource bufferSource,
        int packedLight
    ) {
        if (!entity.isVehicle()) {
            poseStack.translate(0, -0.35, 0);
        }
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    protected float getDeathMaxRotation(DraconicTempleBeastEntity entityLivingBaseIn) {
        return 0.0F;
    }

    @Override
    public float getMotionAnimThreshold(DraconicTempleBeastEntity animatable) {
        return 0.005f;
    }
}
