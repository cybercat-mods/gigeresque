package mods.cybercat.gigeresque.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererConfig;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.rom.RomAlienAnimator;
import mods.cybercat.gigeresque.common.entity.impl.rom.RomAlienEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class RomAlienRenderer extends AzEntityRenderer<RomAlienEntity> {

    private static final ResourceLocation MODEL = Constants.modResource("geo/entity/rom_alien/rom_alien.geo.json");

    protected RomAlienRenderer(EntityRendererProvider.Context context) {
        super(
                AzEntityRendererConfig.<RomAlienEntity>builder($ -> MODEL, xeno -> {
                            var progress = Math.max(0, Math.min(1 - (xeno.getGrowth() / xeno.getMaxGrowth()), 1));

                            if (xeno.isPassedOut()) {
                                return EntityTextures.ROM_ALIEN_STATIS;
                            }
                            if (progress > 0) {
                                return EntityTextures.ROM_ALIEN_YOUNG;
                            }
                            return EntityTextures.ROM_ALIEN;
                        })
                        .setAnimatorProvider(RomAlienAnimator::new)
                        .setDeathMaxRotation(0.0F)
                        .build(),
                context
        );
    }

    @Override
    public void render(
            RomAlienEntity entity,
            float entityYaw,
            float partialTick,
            PoseStack stack,
            @NotNull MultiBufferSource bufferSource,
            int packedLightIn
    ) {
        var scaleFactor = 0.8f + ((entity.getGrowth() / entity.getMaxGrowth()) / 5f);
        stack.scale(scaleFactor, scaleFactor, scaleFactor);
        super.render(entity, entityYaw, partialTick, stack, bufferSource, packedLightIn);
    }
}
