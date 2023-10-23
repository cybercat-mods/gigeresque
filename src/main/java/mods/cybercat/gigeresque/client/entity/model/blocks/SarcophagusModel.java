package mods.cybercat.gigeresque.client.entity.model.blocks;

import mod.azure.azurelib.model.DefaultedBlockGeoModel;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.block.entity.AlienStorageEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class SarcophagusModel extends DefaultedBlockGeoModel<AlienStorageEntity> {

    public SarcophagusModel() {
        super(Constants.modResource("sarcophagus/sarcophagus"));
    }

    @Override
    public RenderType getRenderType(AlienStorageEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }

}
