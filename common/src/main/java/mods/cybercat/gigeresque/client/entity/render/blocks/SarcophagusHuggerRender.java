package mods.cybercat.gigeresque.client.entity.render.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mod.azure.azurelib.common.api.client.renderer.GeoBlockRenderer;
import mod.azure.azurelib.common.api.client.renderer.layer.BlockAndItemGeoLayer;
import mod.azure.azurelib.common.internal.common.cache.object.GeoBone;
import mods.cybercat.gigeresque.client.entity.model.blocks.SarcophagusHuggerModel;
import mods.cybercat.gigeresque.common.block.entity.AlienStorageHuggerEntity;
import mods.cybercat.gigeresque.common.entity.GigEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class SarcophagusHuggerRender extends GeoBlockRenderer<AlienStorageHuggerEntity> {
    public SarcophagusHuggerRender() {
        super(new SarcophagusHuggerModel());
        this.addRenderLayer(new BlockAndItemGeoLayer<>(this) {
            @Nullable
            @Override
            protected ItemStack getStackForBone(GeoBone bone, AlienStorageHuggerEntity animatable) {
                return bone.getName().equalsIgnoreCase("heldItem") ? new ItemStack(Items.AIR) : null;
            }

            @Override
            protected ItemDisplayContext getTransformTypeForStack(GeoBone bone, ItemStack stack, AlienStorageHuggerEntity animatable) {
                return ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
            }

            @Override
            protected void renderStackForBone(PoseStack poseStack, GeoBone bone, ItemStack stack, AlienStorageHuggerEntity animatable, MultiBufferSource bufferSource, float partialTick, int packedLight, int packedOverlay) {
                poseStack.mulPose(Axis.XP.rotationDegrees(-90));
                poseStack.mulPose(Axis.YP.rotationDegrees(180));
                poseStack.mulPose(Axis.ZP.rotationDegrees(0));
                poseStack.translate(0.0D, 0.0D, -2.0D);
                poseStack.scale(0.7F, 0.7F, 0.7F);
                if (animatable.checkHuggerstatus())
                    Minecraft.getInstance().getEntityRenderDispatcher().render(
                            Objects.requireNonNull(GigEntities.FACEHUGGER.get().create(
                                    Objects.requireNonNull(animatable.getLevel()))), 0.0, 0.0, 0.0, 0.0f, partialTick,
                            poseStack, bufferSource, packedLight);
                super.renderStackForBone(poseStack, bone, stack, animatable, bufferSource, partialTick, packedLight,
                        packedOverlay);
            }
        });
    }

}