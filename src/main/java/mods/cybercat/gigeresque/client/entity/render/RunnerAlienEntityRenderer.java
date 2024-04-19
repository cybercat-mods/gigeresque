package mods.cybercat.gigeresque.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.common.api.client.renderer.GeoEntityRenderer;
import mods.cybercat.gigeresque.client.entity.model.RunnerAlienEntityModel;
import mods.cybercat.gigeresque.client.entity.render.feature.RunnerAlienFeatureRenderer;
import mods.cybercat.gigeresque.common.entity.impl.runner.RunnerAlienEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;

public class RunnerAlienEntityRenderer extends GeoEntityRenderer<RunnerAlienEntity> {
    public RunnerAlienEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new RunnerAlienEntityModel());
        this.shadowRadius = 0.5f;
        this.addRenderLayer(new RunnerAlienFeatureRenderer(this));
    }

    @Override
    public void render(RunnerAlienEntity entity, float entityYaw, float partialTicks, PoseStack stack, @NotNull MultiBufferSource bufferIn, int packedLightIn) {
        float scaleFactor = 0.5f + ((entity.getGrowth() / entity.getMaxGrowth()) / 5f);
        stack.scale(scaleFactor, scaleFactor, scaleFactor);
        if (!entity.isVehicle()) {
            stack.translate(0, -0.16, 0);
        }
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
    }

    @Override
    protected float getDeathMaxRotation(RunnerAlienEntity entityLivingBaseIn) {
        return 0;
    }

    @Override
    public float getMotionAnimThreshold(RunnerAlienEntity animatable) {
        return 0.005f;
    }
}
