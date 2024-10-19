package mods.cybercat.gigeresque.client.entity.model.blocks;

import mod.azure.azurelib.model.DefaultedBlockGeoModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.block.entity.SporeBlockEntity;

public class SporeBlockModel extends DefaultedBlockGeoModel<SporeBlockEntity> {

    public SporeBlockModel() {
        super(Constants.modResource("neomorph_spore_pods/neomorph_spore_pods"));
    }

    @Override
    public RenderType getRenderType(SporeBlockEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }

}
