package mods.cybercat.gigeresque.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import mod.azure.azurelib.cache.object.BakedGeoModel;
import mod.azure.azurelib.renderer.GeoEntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import mods.cybercat.gigeresque.client.entity.model.FacehuggerEntityModel;
import mods.cybercat.gigeresque.common.entity.impl.classic.FacehuggerEntity;

public class FacehuggerEntityRenderer extends GeoEntityRenderer<FacehuggerEntity> {

    private final HashMap<EntityType<?>, TransformDataGenerator> headDistances = new HashMap<>();

    public FacehuggerEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new FacehuggerEntityModel());
        this.shadowRadius = 0.2f;
        headDistances.put(
            EntityType.SHEEP,
            (facehugger, host) -> new TransformData(
                0.0,
                (host.getEyeHeight(host.getPose())) - host.getPassengersRidingOffset() - facehugger.getBbWidth() + 0.4,
                host.getBbWidth() - (facehugger.getBbHeight()) - 0.1,
                0.385,
                calcStandardOffsetY(facehugger) + 0.85
            )
        );
        headDistances.put(
            EntityType.COW,
            (facehugger, host) -> new TransformData(
                0.0,
                (host.getEyeHeight(host.getPose())) - host.getPassengersRidingOffset() - facehugger.getBbWidth() + 0.4,
                host.getBbWidth() - (facehugger.getBbHeight()) - 0.1,
                0.41,
                calcStandardOffsetY(facehugger) + 0.85
            )
        );
        headDistances.put(
            EntityType.PIG,
            (facehugger, host) -> new TransformData(
                0.0,
                (host.getEyeHeight(host.getPose())) - host.getPassengersRidingOffset() - facehugger.getBbWidth() + 0.4,
                host.getBbWidth() - (facehugger.getBbHeight()) - 0.1,
                0.41,
                calcStandardOffsetY(facehugger) + 0.85
            )
        );
        headDistances.put(
            EntityType.WOLF,
            (facehugger, host) -> new TransformData(
                0.0,
                (host.getEyeHeight(host.getPose())) - host.getPassengersRidingOffset() - facehugger.getBbWidth() + 0.4,
                host.getBbWidth() - (facehugger.getBbHeight()) - 0.2,
                0.6,
                calcStandardOffsetY(facehugger) + 0.95
            )
        );
        headDistances.put(
            EntityType.VILLAGER,
            (facehugger, host) -> new TransformData(0.0, 0.5, 0.0, 0.36, calcStandardOffsetY(facehugger))
        );
        headDistances.put(
            EntityType.WITCH,
            (facehugger, host) -> new TransformData(0.0, 0.5, 0.0, 0.36, calcStandardOffsetY(facehugger))
        );
        headDistances.put(
            EntityType.PILLAGER,
            (facehugger, host) -> new TransformData(0.0, 0.5, 0.0, 0.36, calcStandardOffsetY(facehugger))
        );
        headDistances.put(
            EntityType.VINDICATOR,
            (facehugger, host) -> new TransformData(0.0, 0.5, 0.0, 0.36, calcStandardOffsetY(facehugger))
        );
        headDistances.put(
            EntityType.WANDERING_TRADER,
            (facehugger, host) -> new TransformData(0.0, 0.5, 0.0, 0.36, calcStandardOffsetY(facehugger))
        );
        headDistances.put(
            EntityType.EVOKER,
            (facehugger, host) -> new TransformData(0.0, 0.5, 0.0, 0.36, calcStandardOffsetY(facehugger))
        );
        headDistances.put(
            EntityType.DOLPHIN,
            (facehugger, host) -> new TransformData(0.0, -0.23, 0.0, 0.82, calcStandardOffsetY(facehugger) + 0.33)
        );
        headDistances.put(
            EntityType.PLAYER,
            (facehugger, host) -> new TransformData(0.0, 0.25, 0.0, 0.2, calcStandardOffsetY(facehugger) + 0.4)
        );
        headDistances.put(
            EntityType.DONKEY,
            (facehugger, host) -> new TransformData(
                0.0,
                (host.getEyeHeight(host.getPose())) - host.getPassengersRidingOffset() - facehugger.getBbWidth() + 0.4,
                host.getBbWidth() - (facehugger.getBbHeight()) - 0.25,
                0.36,
                calcStandardOffsetY(facehugger) + 0.98
            )
        );
        headDistances.put(
            EntityType.FOX,
            (facehugger, host) -> new TransformData(
                0.0,
                (host.getEyeHeight(host.getPose())) - host.getPassengersRidingOffset() - facehugger.getBbWidth() + 0.45,
                host.getBbWidth() - (facehugger.getBbHeight()) + 0.0,
                0.36,
                calcStandardOffsetY(facehugger) + 0.88
            )
        );
        headDistances.put(
            EntityType.GOAT,
            (facehugger, host) -> new TransformData(
                0.0,
                (host.getEyeHeight(host.getPose())) - host.getPassengersRidingOffset() - facehugger.getBbWidth() + 0.3,
                host.getBbWidth() - (facehugger.getBbHeight()) - 0.0,
                0.36,
                calcStandardOffsetY(facehugger) + 0.85
            )
        );
        headDistances.put(
            EntityType.HOGLIN,
            (facehugger, host) -> new TransformData(
                0.0,
                (host.getEyeHeight(host.getPose())) - host.getPassengersRidingOffset() - facehugger.getBbWidth() - 0.1,
                host.getBbWidth() - (facehugger.getBbHeight()) + 0.25,
                0.36,
                calcStandardOffsetY(facehugger) + 0.85
            )
        );
        headDistances.put(
            EntityType.HORSE,
            (facehugger, host) -> new TransformData(
                -0.05,
                (host.getEyeHeight(host.getPose())) - host.getPassengersRidingOffset() - facehugger.getBbWidth() + 0.7,
                host.getBbWidth() - (facehugger.getBbHeight()) + 0.15,
                0.36,
                calcStandardOffsetY(facehugger) + 0.98
            )
        );
        headDistances.put(
            EntityType.MOOSHROOM,
            (facehugger, host) -> new TransformData(
                0.0,
                (host.getEyeHeight(host.getPose())) - host.getPassengersRidingOffset() - facehugger.getBbWidth() + 0.4,
                host.getBbWidth() - (facehugger.getBbHeight()) - 0.1,
                0.41,
                calcStandardOffsetY(facehugger) + 0.85
            )
        );
        headDistances.put(
            EntityType.LLAMA,
            (facehugger, host) -> new TransformData(
                0.0,
                (host.getEyeHeight(host.getPose())) - host.getPassengersRidingOffset() - facehugger.getBbWidth() + 0.6,
                host.getBbWidth() - (facehugger.getBbHeight()) + 0.3,
                0.41,
                calcStandardOffsetY(facehugger) + 0.85
            )
        );
        headDistances.put(
            EntityType.TRADER_LLAMA,
            (facehugger, host) -> new TransformData(
                0.0,
                (host.getEyeHeight(host.getPose())) - host.getPassengersRidingOffset() - facehugger.getBbWidth() + 0.6,
                host.getBbWidth() - (facehugger.getBbHeight()) + 0.3,
                0.41,
                calcStandardOffsetY(facehugger) + 0.85
            )
        );
        headDistances.put(
            EntityType.MULE,
            (facehugger, host) -> new TransformData(
                0.0,
                (host.getEyeHeight(host.getPose())) - host.getPassengersRidingOffset() - facehugger.getBbWidth() + 0.49,
                host.getBbWidth() - (facehugger.getBbHeight()) - 0.2,
                0.41,
                calcStandardOffsetY(facehugger) + 0.85
            )
        );
        headDistances.put(
            EntityType.OCELOT,
            (facehugger, host) -> new TransformData(
                0.0,
                (host.getEyeHeight(host.getPose())) - host.getPassengersRidingOffset() - facehugger.getBbWidth() + 0.5,
                host.getBbWidth() - (facehugger.getBbHeight()) + 0.1,
                0.41,
                calcStandardOffsetY(facehugger) + 0.85
            )
        );
        headDistances.put(
            EntityType.PANDA,
            (facehugger, host) -> new TransformData(
                0.0,
                (host.getEyeHeight(host.getPose())) - host.getPassengersRidingOffset() - facehugger.getBbWidth() + 0.2,
                host.getBbWidth() - (facehugger.getBbHeight()) + 0.1,
                0.41,
                calcStandardOffsetY(facehugger) + 0.85
            )
        );
        headDistances.put(
            EntityType.TURTLE,
            (facehugger, host) -> new TransformData(
                0.0,
                (host.getEyeHeight(host.getPose())) - host.getPassengersRidingOffset() - facehugger.getBbWidth() + 0.4,
                host.getBbWidth() - (facehugger.getBbHeight()) - 0.45,
                0.41,
                calcStandardOffsetY(facehugger) + 0.85
            )
        );
        headDistances.put(
            EntityType.PIGLIN,
            (facehugger, host) -> new TransformData(
                0.0,
                (host.getEyeHeight(host.getPose())) - host.getPassengersRidingOffset() - facehugger.getBbWidth() + 0.4,
                host.getBbWidth() - (facehugger.getBbHeight()) - 0.45,
                0.41,
                calcStandardOffsetY(facehugger) + 0.85
            )
        );
        headDistances.put(
            EntityType.PIGLIN_BRUTE,
            (facehugger, host) -> new TransformData(
                0.0,
                (host.getEyeHeight(host.getPose())) - host.getPassengersRidingOffset() - facehugger.getBbWidth() + 0.4,
                host.getBbWidth() - (facehugger.getBbHeight()) - 0.45,
                0.3,
                calcStandardOffsetY(facehugger) + 0.85
            )
        );
        headDistances.put(
            EntityType.RAVAGER,
            (facehugger, host) -> new TransformData(
                0.0,
                (host.getEyeHeight(host.getPose())) - host.getPassengersRidingOffset() - facehugger.getBbWidth() + 0.3,
                host.getBbWidth() - (facehugger.getBbHeight()) + 0.5,
                -0.3,
                calcStandardOffsetY(facehugger) + 0.25
            )
        );
        headDistances.put(
            EntityType.SNIFFER,
            (facehugger, host) -> new TransformData(
                0.0,
                (host.getEyeHeight(host.getPose())) - host.getPassengersRidingOffset() - facehugger.getBbWidth() + 0.3,
                host.getBbWidth() - (facehugger.getBbHeight()) + 0.5,
                0.5,
                calcStandardOffsetY(facehugger) + 0.75
            )
        );
        headDistances.put(
            EntityType.POLAR_BEAR,
            (facehugger, host) -> new TransformData(
                0.0,
                (host.getEyeHeight(host.getPose())) - host.getPassengersRidingOffset() - facehugger.getBbWidth() + 0.3,
                host.getBbWidth() - (facehugger.getBbHeight()) + 0.15,
                0.41,
                calcStandardOffsetY(facehugger) + 0.8
            )
        );
        headDistances.put(
            EntityType.CAMEL,
            (facehugger, host) -> new TransformData(
                0.0,
                (host.getEyeHeight(host.getPose())) - host.getPassengersRidingOffset() - facehugger.getBbWidth() + 0.85,
                host.getBbWidth() - (facehugger.getBbHeight()) - 0.35,
                0.36,
                calcStandardOffsetY(facehugger) + 0.85
            )
        );
    }

    @Override
    public void render(
        @NotNull FacehuggerEntity entity,
        float entityYaw,
        float partialTick,
        @NotNull PoseStack poseStack,
        @NotNull MultiBufferSource bufferSource,
        int packedLight
    ) {
        if (entity.isVehicle()) {
            poseStack.translate(0, -0.18, 0);
        }
        if (entity.isPassenger()) {
            poseStack.translate(0, -0.18, 0);
        }
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public void preRender(
        PoseStack poseStack,
        FacehuggerEntity animatable,
        BakedGeoModel model,
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
        if (animatable.isEggSpawn()) {
            poseStack.pushPose();
            poseStack.scale(
                animatable.tickCount < 5 ? 0 : 1F,
                animatable.tickCount < 5 ? 0 : 1F,
                animatable.tickCount < 5 ? 0 : 1F
            );
            poseStack.popPose();
        }
        super.preRender(
            poseStack,
            animatable,
            model,
            bufferSource,
            buffer,
            isReRender,
            partialTick,
            packedLight,
            packedOverlay,
            red,
            green,
            blue,
            alpha
        );
    }

    @Override
    protected void applyRotations(
        FacehuggerEntity facehugger,
        PoseStack matrixStackIn,
        float ageInTicks,
        float rotationYaw,
        float partialTicks
    ) {
        if (facehugger.isAlive() && facehugger.isAttachedToHost()) {
            var host = (LivingEntity) facehugger.getVehicle();
            if (host == null)
                return;
            var transformData = getTransformData(facehugger, host);
            var bodyYaw = Mth.rotLerp(partialTicks, host.yBodyRotO, host.yBodyRot);
            var headYaw = Mth.rotLerp(partialTicks, host.yHeadRotO, host.yHeadRot) - bodyYaw;
            var headPitch = Mth.rotLerp(partialTicks, host.getXRot(), host.xRotO);

            // translate head-center
            matrixStackIn.mulPose(Axis.YN.rotationDegrees(bodyYaw));
            matrixStackIn.translate(transformData.originX, transformData.originY, transformData.originZ);
            // yaw
            matrixStackIn.mulPose(Axis.YN.rotationDegrees(headYaw));
            // pitch
            matrixStackIn.mulPose(Axis.XP.rotationDegrees(headPitch));
            matrixStackIn.translate(0.0, transformData.headOffset, transformData.faceOffset); // apply offsets

        } else
            super.applyRotations(facehugger, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
    }

    private TransformData getTransformData(FacehuggerEntity facehugger, Entity host) {
        return headDistances.computeIfAbsent(
            host.getType(),
            entityType -> (facehugger1, host1) -> new TransformData(
                0.0,
                0.0,
                0.0,
                (host.getEyeHeight(
                    host.getPose()
                )) - host.getPassengersRidingOffset() - facehugger.getBbWidth() + host.getBbWidth(),
                calcStandardOffsetY(facehugger)
            )
        ).invoke(facehugger, host);
    }

    private double calcStandardOffsetY(FacehuggerEntity facehugger) {
        return -facehugger.getBbWidth();
    }

    @Override
    protected float getDeathMaxRotation(FacehuggerEntity entityLivingBaseIn) {
        return 0;
    }

    private interface TransformDataGenerator {

        TransformData invoke(FacehuggerEntity facehugger, Entity host);
    }

    private record TransformData(
        double originX,
        double originY,
        double originZ,
        double faceOffset,
        double headOffset
    ) {

    }

    @Override
    public float getMotionAnimThreshold(FacehuggerEntity animatable) {
        return 0.005f;
    }
}
