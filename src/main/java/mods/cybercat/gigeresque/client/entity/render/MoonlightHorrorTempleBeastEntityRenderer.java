package mods.cybercat.gigeresque.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.renderer.GeoEntityRenderer;
import mods.cybercat.gigeresque.client.entity.model.MoonlightHorrorTempleBeastEntityModel;
import mods.cybercat.gigeresque.common.entity.impl.templebeast.MoonlightHorrorTempleBeastEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;

public class MoonlightHorrorTempleBeastEntityRenderer extends GeoEntityRenderer<MoonlightHorrorTempleBeastEntity> {
    public MoonlightHorrorTempleBeastEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new MoonlightHorrorTempleBeastEntityModel());
        this.shadowRadius = 1.0f;
    }

    @Override
    public void render(MoonlightHorrorTempleBeastEntity entity, float entityYaw, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        if (!entity.isVehicle()) {
            poseStack.translate(0, 0, 0);
        }
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    protected float getDeathMaxRotation(MoonlightHorrorTempleBeastEntity entityLivingBaseIn) {
        return 0.0F;
    }

    @Override
    public float getMotionAnimThreshold(MoonlightHorrorTempleBeastEntity animatable) {
        return 0.005f;
    }
}
