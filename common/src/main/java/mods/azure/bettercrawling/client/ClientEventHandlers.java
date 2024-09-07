package mods.azure.bettercrawling.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import mods.azure.bettercrawling.entity.mob.IClimberEntity;
import mods.azure.bettercrawling.entity.mob.Orientation;
import mods.azure.bettercrawling.entity.mob.PathingTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import java.util.List;

public record ClientEventHandlers() {
	
	public static void onPreRenderLiving(LivingEntity entity, float partialTicks, PoseStack matrixStack) {

		if(entity instanceof IClimberEntity climber) {
			Orientation orientation = climber.getOrientation();
			Orientation renderOrientation = climber.calculateOrientation(partialTicks);
			climber.setRenderOrientation(renderOrientation);

			float verticalOffset = climber.getVerticalOffset(partialTicks);

			float x = climber.getAttachmentOffset(Direction.Axis.X, partialTicks) - (float) renderOrientation.normal().x * verticalOffset;
			float y = climber.getAttachmentOffset(Direction.Axis.Y, partialTicks) - (float) renderOrientation.normal().y * verticalOffset;
			float z = climber.getAttachmentOffset(Direction.Axis.Z, partialTicks) - (float) renderOrientation.normal().z * verticalOffset;

			matrixStack.translate(x, y, z);

			matrixStack.mulPose(Axis.YP.rotationDegrees(renderOrientation.yaw()));
			matrixStack.mulPose(Axis.XP.rotationDegrees(renderOrientation.pitch()));
			matrixStack.mulPose(Axis.YP.rotationDegrees(
                    Math.signum(0.5f - orientation.componentY() - orientation.componentZ() - orientation.componentX()) * renderOrientation.yaw()));
		}
	}

	public static void onPostRenderLiving(LivingEntity entity, float partialTicks, PoseStack matrixStack, MultiBufferSource bufferIn) {

		if(entity instanceof IClimberEntity climber) {
			Orientation orientation = climber.getOrientation();
			Orientation renderOrientation = climber.getRenderOrientation();

			if(renderOrientation != null) {
				float verticalOffset = climber.getVerticalOffset(partialTicks);

				float x = climber.getAttachmentOffset(Direction.Axis.X, partialTicks) - (float) renderOrientation.normal().x * verticalOffset;
				float y = climber.getAttachmentOffset(Direction.Axis.Y, partialTicks) - (float) renderOrientation.normal().y * verticalOffset;
				float z = climber.getAttachmentOffset(Direction.Axis.Z, partialTicks) - (float) renderOrientation.normal().z * verticalOffset;

				matrixStack.mulPose(Axis.YP.rotationDegrees(-(float) Math.signum(0.5f - orientation.componentY() - orientation.componentZ() - orientation.componentX()) * renderOrientation.yaw()));
				matrixStack.mulPose(Axis.XP.rotationDegrees(-renderOrientation.pitch()));
				matrixStack.mulPose(Axis.YP.rotationDegrees(-renderOrientation.yaw()));

				if(Minecraft.getInstance().getEntityRenderDispatcher().shouldRenderHitBoxes()) {
					LevelRenderer.renderLineBox(matrixStack, bufferIn.getBuffer(RenderType.LINES), new AABB(0, 0, 0, 0, 0, 0).inflate(0.2f), 1.0f, 1.0f, 1.0f, 1.0f);

					double rx = entity.xo + (entity.getX() - entity.xo) * partialTicks;
					double ry = entity.yo + (entity.getY() - entity.yo) * partialTicks;
					double rz = entity.zo + (entity.getZ() - entity.zo) * partialTicks;

					Vec3 movementTarget = climber.getTrackedMovementTarget();

					if(movementTarget != null) {
						LevelRenderer.renderLineBox(matrixStack, bufferIn.getBuffer(RenderType.LINES), new AABB(movementTarget.x() - 0.25f, movementTarget.y() - 0.25f, movementTarget.z() - 0.25f, movementTarget.x() + 0.25f, movementTarget.y() + 0.25f, movementTarget.z() + 0.25f).move(-rx - x, -ry - y, -rz - z), 0.0f, 1.0f, 1.0f, 1.0f);
					}

					List<PathingTarget> pathingTargets = climber.getTrackedPathingTargets();

					if(pathingTargets != null) {
						int i = 0;

						for(PathingTarget pathingTarget : pathingTargets) {
							BlockPos pos = pathingTarget.pos();

                            LevelRenderer.renderLineBox(matrixStack, bufferIn.getBuffer(RenderType.LINES), new AABB(pos).move(-rx - x, -ry - y, -rz - z), 1.0f, i / (float) (pathingTargets.size() - 1), 0.0f, 0.15f);
							
							matrixStack.pushPose();
							matrixStack.translate(pos.getX() + 0.5D - rx - x, pos.getY() + 0.5D - ry - y, pos.getZ() + 0.5D - rz - z);

							matrixStack.mulPose(pathingTarget.side().getOpposite().getRotation());

							LevelRenderer.renderLineBox(matrixStack, bufferIn.getBuffer(RenderType.LINES), new AABB(-0.501D, -0.501D, -0.501D, 0.501D, -0.45D, 0.501D), 1.0f, i / (float) (pathingTargets.size() - 1), 0.0f, 1.0f);

							Matrix4f matrix4f = matrixStack.last().pose();
							VertexConsumer builder = bufferIn.getBuffer(RenderType.LINES);

							builder.addVertex(matrix4f, -0.501f, -0.45f, -0.501f).setColor(1.0f, i / (float) (pathingTargets.size() - 1), 0.0f, 1.0f);
							builder.addVertex(matrix4f, 0.501f, -0.45f, 0.501f).setColor(1.0f, i / (float) (pathingTargets.size() - 1), 0.0f, 1.0f);
							builder.addVertex(matrix4f, -0.501f, -0.45f, 0.501f).setColor(1.0f, i / (float) (pathingTargets.size() - 1), 0.0f, 1.0f);
							builder.addVertex(matrix4f, 0.501f, -0.45f, -0.501f).setColor(1.0f, i / (float) (pathingTargets.size() - 1), 0.0f, 1.0f);

							matrixStack.popPose();

							i++;
						}
					}

					Matrix4f matrix4f = matrixStack.last().pose();
					VertexConsumer builder = bufferIn.getBuffer(RenderType.LINES);

					builder.addVertex(matrix4f, 0, 0, 0).setColor(0, 1, 1, 1).setNormal(0,0,0);
					builder.addVertex(matrix4f, (float) orientation.normal().x * 2, (float) orientation.normal().y * 2, (float) orientation.normal().z * 2).setColor(1.0f, 0.0f, 1.0f, 1.0f).setNormal(0,0,0);

					LevelRenderer.renderLineBox(matrixStack, bufferIn.getBuffer(RenderType.LINES), new AABB(0, 0, 0, 0, 0, 0).move((float) orientation.normal().x * 2, (float) orientation.normal().y * 2, (float) orientation.normal().z * 2).inflate(0.025f), 1.0f, 0.0f, 1.0f, 1.0f);

					matrixStack.pushPose();

					matrixStack.translate(-x, -y, -z);

					matrix4f = matrixStack.last().pose();

					builder.addVertex(matrix4f, 0, entity.getBbHeight() * 0.5f, 0).setColor(0, 1, 1, 1).setNormal(0,0,0);
					builder.addVertex(matrix4f, (float) orientation.localX().x, entity.getBbHeight() * 0.5f + (float) orientation.localX().y, (float) orientation.localX().z).setColor(1.0f, 0.0f, 0.0f, 1.0f).setNormal(0,0,0);

					LevelRenderer.renderLineBox(matrixStack, bufferIn.getBuffer(RenderType.LINES), new AABB(0, 0, 0, 0, 0, 0).move((float) orientation.localX().x, entity.getBbHeight() * 0.5f + (float) orientation.localX().y, (float) orientation.localX().z).inflate(0.025f), 1.0f, 0.0f, 0.0f, 1.0f);

					builder.addVertex(matrix4f, 0, entity.getBbHeight() * 0.5f, 0).setColor(0, 1, 1, 1).setNormal(0,0,0);
					builder.addVertex(matrix4f, (float) orientation.localY().x, entity.getBbHeight() * 0.5f + (float) orientation.localY().y, (float) orientation.localY().z).setColor(0.0f, 1.0f, 0.0f, 1.0f).setNormal(0,0,0);

					LevelRenderer.renderLineBox(matrixStack, bufferIn.getBuffer(RenderType.LINES), new AABB(0, 0, 0, 0, 0, 0).move((float) orientation.localY().x, entity.getBbHeight() * 0.5f + (float) orientation.localY().y, (float) orientation.localY().z).inflate(0.025f), 0.0f, 1.0f, 0.0f, 1.0f);

					builder.addVertex(matrix4f, 0, entity.getBbHeight() * 0.5f, 0).setColor(0, 1, 1, 1).setNormal(0,0,0);
					builder.addVertex(matrix4f, (float) orientation.localZ().x, entity.getBbHeight() * 0.5f + (float) orientation.localZ().y, (float) orientation.localZ().z).setColor(0.0f, 0.0f, 1.0f, 1.0f).setNormal(0,0,0);

					LevelRenderer.renderLineBox(matrixStack, bufferIn.getBuffer(RenderType.LINES), new AABB(0, 0, 0, 0, 0, 0).move((float) orientation.localZ().x, entity.getBbHeight() * 0.5f + (float) orientation.localZ().y, (float) orientation.localZ().z).inflate(0.025f), 0.0f, 0.0f, 1.0f, 1.0f);

					matrixStack.popPose();
				}

				matrixStack.translate(-x, -y, -z);
			}
		}
	}
}
