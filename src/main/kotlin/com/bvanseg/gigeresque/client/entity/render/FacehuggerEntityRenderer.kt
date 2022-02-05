package com.bvanseg.gigeresque.client.entity.render

import com.bvanseg.gigeresque.client.entity.model.FacehuggerEntityModel
import com.bvanseg.gigeresque.common.entity.impl.FacehuggerEntity
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3f
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer

/**
 * @author Boston Vanseghi
 */
@Environment(EnvType.CLIENT)
class FacehuggerEntityRenderer(
    val context: EntityRendererFactory.Context
) : GeoEntityRenderer<FacehuggerEntity>(
    context,
    FacehuggerEntityModel()
) {
    private val headDistances = HashMap<EntityType<*>, (FacehuggerEntity, Entity) -> TransformData>()

    init {
        this.shadowRadius = 0.2f
        headDistances[EntityType.SHEEP] = { facehugger, host ->
            TransformData(
                0.0,
                host.getEyeHeight(host.pose).toDouble() - host.mountedHeightOffset - facehugger.width + 0.4,
                host.width - facehugger.height.toDouble() - 0.1,
                0.385,
                calcStandardOffsetY(facehugger) - 0.05
            )
        }
        headDistances[EntityType.COW] = { facehugger, host ->
            TransformData(
                0.0,
                host.getEyeHeight(host.pose).toDouble() - host.mountedHeightOffset - facehugger.width + 0.4,
                host.width - facehugger.height.toDouble() - 0.1,
                0.41,
                calcStandardOffsetY(facehugger) - 0.05
            )
        }
        headDistances[EntityType.PIG] = { facehugger, host ->
            TransformData(
                0.0,
                host.getEyeHeight(host.pose).toDouble() - host.mountedHeightOffset - facehugger.width + 0.4,
                host.width - facehugger.height.toDouble() - 0.1,
                0.41, // Distance from face
                calcStandardOffsetY(facehugger) - 0.05 // Distance from head
            )
        }
        headDistances[EntityType.WOLF] = { facehugger, host ->
            TransformData(
                0.0,
                host.getEyeHeight(host.pose).toDouble() - host.mountedHeightOffset - facehugger.width + 0.4,
                host.width - facehugger.height.toDouble() - 0.1,
                0.54,
                calcStandardOffsetY(facehugger) - 0.15
            )
        }
        headDistances[EntityType.VILLAGER] = { facehugger, host ->
            TransformData(
                0.0,
                0.0,
                0.0,
                0.36,
                calcStandardOffsetY(facehugger)
            )
        }
    }

    override fun applyRotations(
        facehugger: FacehuggerEntity,
        matrixStackIn: MatrixStack,
        ageInTicks: Float,
        rotationYaw: Float,
        partialTicks: Float
    ) {
        if (facehugger.isAlive && facehugger.isAttachedToHost()) {
            val host = facehugger.vehicle!! as LivingEntity
            val transformData = getTransformData(facehugger, host)
            val bodyYaw = MathHelper.lerpAngleDegrees(partialTicks, host.prevBodyYaw, host.bodyYaw)
            val headYaw = MathHelper.lerpAngleDegrees(partialTicks, host.prevHeadYaw, host.headYaw) - bodyYaw
            val headPitch = MathHelper.lerpAngleDegrees(partialTicks, host.pitch, host.prevPitch)

            //translate head-center
            matrixStackIn.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(bodyYaw))
            matrixStackIn.translate(transformData.originX, transformData.originY, transformData.originZ)
            //yaw
            matrixStackIn.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(headYaw))
            //pitch
            matrixStackIn.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(headPitch))
            matrixStackIn.translate(0.0, transformData.headOffset, transformData.faceOffset) //apply offsets

        } else {
            super.applyRotations(facehugger, matrixStackIn, ageInTicks, rotationYaw, partialTicks)
        }
    }

    private fun getTransformData(facehugger: FacehuggerEntity, host: Entity): TransformData {
        return headDistances.computeIfAbsent(host.type) {
            return@computeIfAbsent { facehugger, host ->
                TransformData(
                    0.0,
                    0.0,
                    0.0,
                    host.getEyeHeight(host.pose).toDouble() - host.mountedHeightOffset - facehugger.width + host.width,
                    calcStandardOffsetY(facehugger)
                )
            }
        }.invoke(facehugger, host)
    }

    private fun calcStandardOffsetY(facehugger: FacehuggerEntity): Double {
        return -facehugger.width.toDouble()
    }

    private data class TransformData(
        val originX: Double,
        val originY: Double,
        val originZ: Double,
        val faceOffset: Double,
        val headOffset: Double
    )
}