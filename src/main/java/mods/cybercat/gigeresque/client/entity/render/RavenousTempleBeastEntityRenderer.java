package mods.cybercat.gigeresque.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.common.api.client.renderer.GeoEntityRenderer;
import mods.cybercat.gigeresque.client.entity.model.RavenousTempleBeastEntityModel;
import mods.cybercat.gigeresque.common.entity.impl.templebeast.RavenousTempleBeastEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;

public class RavenousTempleBeastEntityRenderer extends GeoEntityRenderer<RavenousTempleBeastEntity> {
    public RavenousTempleBeastEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new RavenousTempleBeastEntityModel());
        this.shadowRadius = 1.0f;
    }

    @Override
    public void render(RavenousTempleBeastEntity entity, float entityYaw, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        if (entity.onGround() && !entity.isVehicle()) {
            poseStack.translate(0, -0.15, 0);
        }
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    protected float getDeathMaxRotation(RavenousTempleBeastEntity entityLivingBaseIn) {
        return 0.0F;
    }

    @Override
    public float getMotionAnimThreshold(RavenousTempleBeastEntity animatable) {
        return 0.005f;
    }
}
