package mods.cybercat.gigeresque.client.entity.render.blocks;

import com.mojang.math.Axis;
import mod.azure.azurelib.rewrite.model.AzBone;
import mod.azure.azurelib.rewrite.render.AzRendererPipelineContext;
import mod.azure.azurelib.rewrite.render.block.AzBlockEntityRenderer;
import mod.azure.azurelib.rewrite.render.block.AzBlockEntityRendererConfig;
import mod.azure.azurelib.rewrite.render.layer.AzBlockAndItemLayer;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.block.animators.StatueHuggerAnimator;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Objects;

import mods.cybercat.gigeresque.common.block.entity.AlienStorageHuggerEntity;
import mods.cybercat.gigeresque.common.entity.GigEntities;

public class SarcophagusHuggerRender<T extends AlienStorageHuggerEntity> extends AzBlockEntityRenderer<T> {

    private static final ResourceLocation MODEL = Constants.modResource("geo/block/sarcophagus/sarcophagus.geo.json");

    private static final ResourceLocation TEXTURE = Constants.modResource("textures/block/sarcophagus/sarcophagus.png");

    public SarcophagusHuggerRender() {
        super(
                AzBlockEntityRendererConfig.<T>builder(MODEL, TEXTURE)
                        .setAnimatorProvider(StatueHuggerAnimator::new)
                        .addRenderLayer(new AzBlockAndItemLayer<T>() {

                            @Override
                            public ItemStack itemStackForBone(AzBone bone) {
                                return bone.getName().equalsIgnoreCase("heldItem") ? new ItemStack(Items.AIR) : null;
                            }

                            @Override
                            protected ItemDisplayContext getTransformTypeForStack(AzBone bone, ItemStack stack) {
                                return ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
                            }

                            @Override
                            protected void renderItemForBone(AzRendererPipelineContext<T> context, AzBone bone, ItemStack itemStack) {
                                context.poseStack().mulPose(Axis.XP.rotationDegrees(-90));
                                context.poseStack().mulPose(Axis.YP.rotationDegrees(180));
                                context.poseStack().mulPose(Axis.ZP.rotationDegrees(0));
                                context.poseStack().translate(0.0D, 0.0D, -2.0D);
                                context.poseStack().scale(0.7F, 0.7F, 0.7F);
                                if (context.animatable().checkHuggerstatus())
                                    Minecraft.getInstance()
                                            .getEntityRenderDispatcher()
                                            .render(
                                                    Objects.requireNonNull(
                                                            GigEntities.FACEHUGGER.get()
                                                                    .create(
                                                                            Objects.requireNonNull(
                                                                                    context.animatable().getLevel())
                                                                    )
                                                    ),
                                                    0.0,
                                                    0.0,
                                                    0.0,
                                                    0.0f,
                                                    context.partialTick(),
                                                    context.poseStack(),
                                                    context.multiBufferSource(),
                                                    context.packedLight()
                                            );
                                super.renderItemForBone(context, bone, itemStack);
                            }
                        })
                        .build()
        );
    }

}
