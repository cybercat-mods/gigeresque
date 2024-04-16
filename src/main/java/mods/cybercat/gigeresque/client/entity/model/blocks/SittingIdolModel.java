package mods.cybercat.gigeresque.client.entity.model.blocks;

import mod.azure.azurelib.common.api.client.model.DefaultedBlockGeoModel;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.block.entity.IdolStorageEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class SittingIdolModel extends DefaultedBlockGeoModel<IdolStorageEntity> {

    public SittingIdolModel() {
        super(Constants.modResource("sittingidol/sittingidol"));
    }

    @Override
    public RenderType getRenderType(IdolStorageEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }

}
