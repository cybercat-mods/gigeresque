package mods.cybercat.gigeresque.client.entity.render.rom;

import mod.azure.azurelib.common.render.entity.AzEntityRenderer;
import mod.azure.azurelib.common.render.entity.AzEntityRendererConfig;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.entity.model.AlienModelRenderer;
import mods.cybercat.gigeresque.client.entity.model.EntityModels;
import mods.cybercat.gigeresque.client.entity.render.feature.ClassicAgingAzLayer;
import mods.cybercat.gigeresque.client.entity.texture.EntityTextures;
import mods.cybercat.gigeresque.common.entity.animators.rom.RomAlienAnimator;
import mods.cybercat.gigeresque.common.entity.helper.managers.animations.rom.RomAlienAnimManager;
import mods.cybercat.gigeresque.common.entity.impl.rom.RomAlienEntity;

public class RomAlienRenderer extends AzEntityRenderer<RomAlienEntity> {

    private static final ResourceLocation MODEL = Constants.modResource("geo/entity/rom_alien/rom_alien.geo.json");

    protected RomAlienRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<RomAlienEntity>builder((nullEntity, animatable) -> EntityModels.ROM_ALIEN, (nullEntity, animatable) -> {
                var progress = Math.max(0, Math.min(1 - (animatable.getGrowth() / animatable.getMaxGrowth()), 1));

                if (animatable.stasisManager.isStasis()) {
                    return EntityTextures.ROM_ALIEN_STASIS;
                }
                if (progress > 0) {
                    return EntityTextures.ROM_ALIEN_YOUNG;
                }
                return EntityTextures.ROM_ALIEN;
            })
                .setModelRenderer(AlienModelRenderer::new)
                .setAnimatorProvider(RomAlienAnimator::new)
                .setRenderEntry(renderEntry -> {
                    RomAlienAnimManager.handleAnimations(renderEntry.animatable());
                    return renderEntry;
                })
                .setDeathMaxRotation(0.0F)
                .setShadowRadius(0.5F)
                .addRenderLayer(new ClassicAgingAzLayer<>(EntityTextures.ROM_ALIEN_YOUNG))
                .setScale(romAlienEntity -> {
                    var scaleFactor = 0.8f + ((romAlienEntity.getGrowth() / romAlienEntity.getMaxGrowth()) / 5f);
                    return Math.min(scaleFactor, 1.0F);
                })
                .build(),
            context
        );
    }
}
