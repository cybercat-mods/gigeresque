package mods.cybercat.gigeresque.client.entity.model.blocks;

import mod.azure.azurelib.common.api.client.model.DefaultedBlockGeoModel;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.block.entity.SporeBlockEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class SporeBlockModel extends DefaultedBlockGeoModel<SporeBlockEntity> {

    public SporeBlockModel() {
        super(Constants.modResource("neomorph_spore_pods/neomorph_spore_pods"));
    }

    @Override
    public RenderType getRenderType(SporeBlockEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }

}
