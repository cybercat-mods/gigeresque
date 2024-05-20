package mods.cybercat.gigeresque.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.common.api.client.renderer.GeoEntityRenderer;
import mods.cybercat.gigeresque.client.entity.model.AlienEntityModel;
import mods.cybercat.gigeresque.common.entity.impl.classic.ClassicAlienEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;

public class AlienEntityRenderer extends GeoEntityRenderer<ClassicAlienEntity> {

    public AlienEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new AlienEntityModel());
        this.shadowRadius = 0.5f;
    }

    @Override
    public void render(ClassicAlienEntity entity, float entityYaw, float partialTick, PoseStack stack, @NotNull MultiBufferSource bufferSource, int packedLightIn) {
        var scaleFactor = 0.8f + ((entity.getGrowth() / entity.getMaxGrowth()) / 5f);
        stack.scale(scaleFactor, scaleFactor, scaleFactor);
        super.render(entity, entityYaw, partialTick, stack, bufferSource, packedLightIn);
    }

    @Override
    protected float getDeathMaxRotation(ClassicAlienEntity entityLivingBaseIn) {
        return 0;
    }

    @Override
    public float getMotionAnimThreshold(ClassicAlienEntity animatable) {
        return !animatable.isExecuting() && animatable.isVehicle() ? 0.000f : animatable.isPassedOut() ? 0.5f : 0.005f;
    }
}
