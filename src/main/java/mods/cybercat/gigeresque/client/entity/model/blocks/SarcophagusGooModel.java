package mods.cybercat.gigeresque.client.entity.model.blocks;

import mod.azure.azurelib.model.DefaultedBlockGeoModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.block.entity.AlienStorageGooEntity;

public class SarcophagusGooModel extends DefaultedBlockGeoModel<AlienStorageGooEntity> {

    public SarcophagusGooModel() {
        super(Constants.modResource("sarcophagus/sarcophagus"));
    }

    @Override
    public RenderType getRenderType(AlienStorageGooEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }

}
