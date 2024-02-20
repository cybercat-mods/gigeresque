package mods.cybercat.gigeresque.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import mod.azure.azurelib.cache.object.BakedGeoModel;
import mod.azure.azurelib.renderer.GeoEntityRenderer;
import mods.cybercat.gigeresque.client.entity.model.AlienEntityModel;
import mods.cybercat.gigeresque.client.entity.render.feature.ClassicAlienFeatureRenderer;
import mods.cybercat.gigeresque.common.entity.impl.classic.ClassicAlienEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class AlienEntityRenderer extends GeoEntityRenderer<ClassicAlienEntity> {

    public AlienEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new AlienEntityModel());
        this.shadowRadius = 0.5f;
        this.addRenderLayer(new ClassicAlienFeatureRenderer(this));
    }

    @Override
    public void render(ClassicAlienEntity entity, float entityYaw, float partialTicks, PoseStack stack, @NotNull MultiBufferSource bufferIn, int packedLightIn) {
        var scaleFactor = 0.8f + ((entity.getGrowth() / entity.getMaxGrowth()) / 5f);
        stack.scale(scaleFactor, scaleFactor, scaleFactor);
//        if (entity.isCrawling()) {
//            if (entity.isColliding(entity.blockPosition(), entity.level().getBlockState(entity.blockPosition().west()))) {
//                stack.mulPose(Axis.ZP.rotationDegrees(-90));
//            }
//            if (entity.isColliding(entity.blockPosition(), entity.level().getBlockState(entity.blockPosition().above()))) {
//                stack.mulPose(Axis.XP.rotationDegrees(180));
//                stack.translate(0, -0.75, 0);
//            }
//            if (entity.isColliding(entity.blockPosition(), entity.level().getBlockState(entity.blockPosition().north()))) {
//                stack.mulPose(Axis.XP.rotationDegrees(90));
//            }
//            if (entity.isColliding(entity.blockPosition(), entity.level().getBlockState(entity.blockPosition().south()))) {
//                stack.mulPose(Axis.XP.rotationDegrees(-90));
//            }
//            if (entity.isColliding(entity.blockPosition(), entity.level().getBlockState(entity.blockPosition().east()))) {
//                stack.mulPose(Axis.ZP.rotationDegrees(90));
//            }
//        }
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
    }

    @Override
    protected float getDeathMaxRotation(ClassicAlienEntity entityLivingBaseIn) {
        return 0;
    }

    @Override
    public void preRender(PoseStack poseStack, ClassicAlienEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (!animatable.isPassenger() && !isReRender && animatable.getGrowth() < animatable.getMaxGrowth()) {
            var orientation = animatable.getOrientation();
            var renderOrientation = animatable.calculateOrientation(partialTick);
            animatable.setRenderOrientation(renderOrientation);
            var above = animatable.level().getBlockState(animatable.blockPosition().above()).isSolid();
            var below = animatable.level().getBlockState(animatable.blockPosition().below()).isSolid();
            if (above) poseStack.translate(0, -0.25, 0);
            if (animatable.level().getBlockState(animatable.blockPosition().west()).isSolid() && !above && !below)
                poseStack.translate(-0.45, 0, 0);
            if (animatable.level().getBlockState(animatable.blockPosition().east()).isSolid() && !above && !below)
                poseStack.translate(0.45, 0, 0);
            if (animatable.level().getBlockState(animatable.blockPosition().north()).isSolid() && !above && !below)
                poseStack.translate(0, 0, -0.4);
            if (animatable.level().getBlockState(animatable.blockPosition().south()).isSolid() && !above && !below)
                poseStack.translate(0, 0, 0.49);
            poseStack.mulPose(Axis.YP.rotationDegrees(renderOrientation.yaw()));
            poseStack.mulPose(Axis.XP.rotationDegrees(renderOrientation.pitch()));
            poseStack.mulPose(Axis.YP.rotationDegrees(Math.signum(0.5f - orientation.componentY() - orientation.componentZ() - orientation.componentX()) * renderOrientation.yaw()));
        }
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public float getMotionAnimThreshold(ClassicAlienEntity animatable) {
        return !animatable.isExecuting() && animatable.isVehicle() ? 0.000f : 0.005f;
    }
}
