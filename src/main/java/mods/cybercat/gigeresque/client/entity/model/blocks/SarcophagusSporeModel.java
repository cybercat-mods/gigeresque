package mods.cybercat.gigeresque.client.entity.model.blocks;

import mod.azure.azurelib.common.api.client.model.DefaultedBlockGeoModel;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.block.entity.AlienStorageSporeEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class SarcophagusSporeModel extends DefaultedBlockGeoModel<AlienStorageSporeEntity> {

    public SarcophagusSporeModel() {
        super(Constants.modResource("sarcophagus/sarcophagus"));
    }

    @Override
    public RenderType getRenderType(AlienStorageSporeEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }

}
