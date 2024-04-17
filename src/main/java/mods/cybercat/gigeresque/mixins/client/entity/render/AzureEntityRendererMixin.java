package mods.cybercat.gigeresque.mixins.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import mod.azure.azurelib.common.api.client.model.GeoModel;
import mod.azure.azurelib.common.api.client.renderer.GeoEntityRenderer;
import mod.azure.azurelib.common.api.client.renderer.layer.GeoRenderLayer;
import mod.azure.azurelib.common.api.common.animatable.GeoEntity;
import mod.azure.azurelib.common.internal.client.renderer.GeoRenderer;
import mod.azure.azurelib.common.internal.common.cache.object.BakedGeoModel;
import mod.azure.bettercrawling.entity.mob.IClimberEntity;
import mod.azure.bettercrawling.entity.mob.Orientation;
import mod.azure.bettercrawling.entity.mob.PathingTarget;
import mod.azure.bettercrawling.platform.Services;
import mods.cybercat.gigeresque.client.entity.render.feature.EggmorphGeoFeatureRenderer;
import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/**
 * @author Aelpecyem
 */
@Environment(EnvType.CLIENT)
@Mixin(value = GeoEntityRenderer.class)
public abstract class AzureEntityRendererMixin<T extends Entity & GeoEntity> {

    @Shadow
    public abstract T getAnimatable();

    @Shadow
    public abstract GeoEntityRenderer<T> addRenderLayer(GeoRenderLayer<T> layer);

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void init(EntityRendererProvider.Context ctx, GeoModel<T> modelProvider, CallbackInfo ci) {
        if (this.getAnimatable() instanceof Mob)
            this.addRenderLayer(new EggmorphGeoFeatureRenderer<>((GeoRenderer<T>) this));
    }

    @Inject(method = "actuallyRender*", at = @At("HEAD"))
    private void doPreRender(PoseStack poseStack, T animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha, CallbackInfo ci) {
        if (!animatable.isPassenger() && !animatable.isVehicle() && animatable instanceof LivingEntity livingEntity)
            onPreRenderLiving(livingEntity, partialTick, poseStack);
    }

    @Inject(method = "actuallyRender*", at = @At("TAIL"))
    private void doPostRender(PoseStack poseStack, T animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha, CallbackInfo ci) {
        if (!animatable.isPassenger() && !animatable.isVehicle() && animatable instanceof LivingEntity livingEntity) {
            onPostRenderLiving(livingEntity, partialTick, poseStack, bufferSource);
        }
    }

    private static void onPreRenderLiving(LivingEntity entity, float partialTicks, PoseStack matrixStack) {
        if (!(entity instanceof IClimberEntity climber)) {
            return;
        }
        if (climber instanceof AlienEntity alienEntity && alienEntity.isPassedOut()) {
            return;
        }

        Orientation orientation = climber.getOrientation();
        Orientation renderOrientation = climber.calculateOrientation(partialTicks);
        climber.setRenderOrientation(renderOrientation);
        float verticalOffset = climber.getVerticalOffset(partialTicks);
        float x = climber.getAttachmentOffset(Direction.Axis.X,
                partialTicks) - (float) renderOrientation.normal().x * verticalOffset;
        float y = climber.getAttachmentOffset(Direction.Axis.Y,
                partialTicks) - (float) renderOrientation.normal().y * verticalOffset;
        float z = climber.getAttachmentOffset(Direction.Axis.Z,
                partialTicks) - (float) renderOrientation.normal().z * verticalOffset;
        matrixStack.translate(x, y, z);
        matrixStack.mulPose(Axis.YP.rotationDegrees(renderOrientation.yaw()));
        matrixStack.mulPose(Axis.XP.rotationDegrees(renderOrientation.pitch()));
        matrixStack.mulPose(Axis.YP.rotationDegrees(Math.signum(
                0.5F - orientation.componentY() - orientation.componentZ() - orientation.componentX()) * renderOrientation.yaw()));
    }

    private static void onPostRenderLiving(LivingEntity entity, float partialTicks, PoseStack matrixStack, MultiBufferSource bufferIn) {
        if (entity instanceof IClimberEntity climber) {
            Orientation orientation = climber.getOrientation();
            Orientation renderOrientation = climber.getRenderOrientation();
            if (renderOrientation != null) {
                float verticalOffset = climber.getVerticalOffset(partialTicks);
                float x = climber.getAttachmentOffset(Direction.Axis.X,
                        partialTicks) - (float) renderOrientation.normal().x * verticalOffset;
                float y = climber.getAttachmentOffset(Direction.Axis.Y,
                        partialTicks) - (float) renderOrientation.normal().y * verticalOffset;
                float z = climber.getAttachmentOffset(Direction.Axis.Z,
                        partialTicks) - (float) renderOrientation.normal().z * verticalOffset;
                matrixStack.mulPose(Axis.YP.rotationDegrees(-Math.signum(
                        0.5F - orientation.componentY() - orientation.componentZ() - orientation.componentX()) * renderOrientation.yaw()));
                matrixStack.mulPose(Axis.XP.rotationDegrees(-renderOrientation.pitch()));
                matrixStack.mulPose(Axis.YP.rotationDegrees(-renderOrientation.yaw()));
                if (Minecraft.getInstance().getEntityRenderDispatcher().shouldRenderHitBoxes() && (Services.PLATFORM.isDevelopmentEnvironment() || Gigeresque.config.enableDevparticles)) {
                    LevelRenderer.renderLineBox(matrixStack, bufferIn.getBuffer(RenderType.LINES),
                            (new AABB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)).inflate(0.20000000298023224), 1.0F, 1.0F, 1.0F,
                            1.0F);
                    double rx = entity.xo + (entity.getX() - entity.xo) * partialTicks;
                    double ry = entity.yo + (entity.getY() - entity.yo) * partialTicks;
                    double rz = entity.zo + (entity.getZ() - entity.zo) * partialTicks;
                    Vec3 movementTarget = climber.getTrackedMovementTarget();
                    if (movementTarget != null) {
                        LevelRenderer.renderLineBox(matrixStack, bufferIn.getBuffer(RenderType.LINES),
                                (new AABB(movementTarget.x() - 0.25, movementTarget.y() - 0.25,
                                        movementTarget.z() - 0.25, movementTarget.x() + 0.25, movementTarget.y() + 0.25,
                                        movementTarget.z() + 0.25)).move(-rx - x, -ry - y,
                                        -rz - z), 0.0F, 1.0F, 1.0F, 1.0F);
                    }

                    List<PathingTarget> pathingTargets = climber.getTrackedPathingTargets();
                    if (pathingTargets != null) {
                        int i = 0;

                        for (var var20 = pathingTargets.iterator(); var20.hasNext(); ++i) {
                            PathingTarget pathingTarget = var20.next();
                            BlockPos pos = pathingTarget.pos();
                            LevelRenderer.renderLineBox(matrixStack, bufferIn.getBuffer(RenderType.LINES),
                                    (new AABB(pos)).move(-rx - x, -ry - y, -rz - z), 1.0F,
                                    (float) i / (float) (pathingTargets.size() - 1), 0.0F, 0.15F);
                            matrixStack.pushPose();
                            matrixStack.translate(pos.getX() + 0.5 - rx - x,
                                    pos.getY() + 0.5 - ry - y,
                                    pos.getZ() + 0.5 - rz - z);
                            matrixStack.mulPose(pathingTarget.side().getOpposite().getRotation());
                            LevelRenderer.renderLineBox(matrixStack, bufferIn.getBuffer(RenderType.LINES),
                                    new AABB(-0.501, -0.501, -0.501, 0.501, -0.45, 0.501), 1.0F,
                                    (float) i / (float) (pathingTargets.size() - 1), 0.0F, 1.0F);
                            matrixStack.popPose();
                        }
                    }
                }

                matrixStack.translate(-x, -y, -z);
            }
        }

    }
}
