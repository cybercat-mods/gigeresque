package mods.cybercat.gigeresque.client.entity.render.rom;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRenderer;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRendererConfig;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.render.feature.ClassicAgingOverLay;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.rom.RomAlienAnimator;
import mods.cybercat.gigeresque.common.entity.helper.managers.animations.rom.RomAlienAnimManager;
import mods.cybercat.gigeresque.common.entity.impl.rom.RomAlienEntity;

public class RomAlienRenderer extends AzEntityRenderer<RomAlienEntity> {

    private static final ResourceLocation MODEL = Constants.modResource("geo/entity/rom_alien/rom_alien.geo.json");

    protected RomAlienRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<RomAlienEntity>builder($ -> EntityModels.ROM_ALIEN, xeno -> {
                var progress = Math.max(0, Math.min(1 - (xeno.getGrowth() / xeno.getMaxGrowth()), 1));

                if (xeno.stasisManager.isStasis()) {
                    return EntityTextures.ROM_ALIEN_STASIS;
                }
                if (progress > 0) {
                    return EntityTextures.ROM_ALIEN_YOUNG;
                }
                return EntityTextures.ROM_ALIEN;
            })
                .setAnimatorProvider(RomAlienAnimator::new)
                .setDeathMaxRotation(0.0F)
                .setShadowRadius(0.5F)
                .addRenderLayer(new ClassicAgingOverLay<>(EntityTextures.ROM_ALIEN_YOUNG))
                .setScale(romAlienEntity -> {
                    var scaleFactor = 0.8f + ((romAlienEntity.getGrowth() / romAlienEntity.getMaxGrowth()) / 5f);
                    return Math.min(scaleFactor, 1.0F);
                })
                .build(),
            context
        );
    }

    @Override
    public void render(
        @NotNull RomAlienEntity entity,
        float entityYaw,
        float partialTick,
        @NotNull PoseStack stack,
        @NotNull MultiBufferSource bufferSource,
        int packedLightIn
    ) {
        RomAlienAnimManager.handleAnimations(entity);
        super.render(entity, entityYaw, partialTick, stack, bufferSource, packedLightIn);
    }
}
