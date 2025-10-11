package mods.cybercat.gigeresque.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mod.azure.azurelib.common.render.AzLayerRenderer;
import mod.azure.azurelib.common.render.entity.AzEntityModelRenderer;
import mod.azure.azurelib.common.render.entity.AzEntityRendererPipeline;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

import java.util.UUID;

import mods.cybercat.gigeresque.client.entity.render.helper.EntityHeadData;
import mods.cybercat.gigeresque.client.entity.render.helper.EntityHeadOffsetData;
import mods.cybercat.gigeresque.common.entity.impl.classic.FacehuggerEntity;

/**
 * Credit to Boston for this code
 */
public class FacehuggerModelRenderer extends AzEntityModelRenderer<FacehuggerEntity> {

    public FacehuggerModelRenderer(
        AzEntityRendererPipeline<FacehuggerEntity> entityRendererPipeline,
        AzLayerRenderer<UUID, FacehuggerEntity> layerRenderer
    ) {
        super(entityRendererPipeline, layerRenderer);
    }

    @Override
    protected void applyRotations(
        FacehuggerEntity animatable,
        PoseStack poseStack,
        float ageInTicks,
        float rotationYaw,
        float partialTick,
        float nativeScale
    ) {
        if (!animatable.isPassenger()) {
            super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick, nativeScale);
            return;
        }

        var host = (LivingEntity) animatable.getVehicle();

        if (host == null) {
            return;
        }

        var data = EntityHeadData.ENTITY_HEAD_DATA_BY_TYPE.get(host.getType());

        if (data == null) {
            return;
        }

        applyFaceRotations(animatable, poseStack, partialTick, host, data);
    }

    private void applyFaceRotations(
        FacehuggerEntity facehuggerEntity,
        PoseStack poseStack,
        float partialTick,
        LivingEntity host,
        EntityHeadData data
    ) {
        var bodyYaw = Mth.rotLerp(partialTick, host.yBodyRotO, host.yBodyRot);
        var headYaw = Mth.rotLerp(partialTick, host.yHeadRotO, host.yHeadRot) - bodyYaw;
        var headPitch = Mth.rotLerp(partialTick, host.getXRot(), host.xRotO);

        var xPivot = data.pivot().x;
        var yPivot = data.pivot().y;
        var zPivot = data.pivot().z;
        var ySize = data.size().y;
        var zSize = data.size().z;

        poseStack.mulPose(Axis.YN.rotationDegrees(bodyYaw));

        poseStack.translate(xPivot, yPivot - host.getBbHeight(), -zPivot);
        poseStack.mulPose(Axis.YN.rotationDegrees(headYaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(headPitch));
        poseStack.translate(-xPivot, -yPivot + host.getBbHeight(), zPivot);

        var offsetSuppilers = EntityHeadOffsetData.ENTITY_HEAD_OFFSET_DATA_BY_TYPE.get(host.getType());

        if (offsetSuppilers != null) {
            var yOffset = offsetSuppilers.verticalOffsetSupplier().apply(data, facehuggerEntity);
            var zOffset = offsetSuppilers.faceOffsetSupplier().apply(data, facehuggerEntity);
            poseStack.translate(0, yOffset, zOffset);
        } else {
            poseStack.translate(0, -ySize, zSize);
        }
    }
}
