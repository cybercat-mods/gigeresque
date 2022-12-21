package mods.cybercat.gigeresque.client.entity.model.blocks;

import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.block.entity.IdolStorageEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;

public class SittingIdolModel extends DefaultedBlockGeoModel<IdolStorageEntity> {
	
	public SittingIdolModel() {
		super(new ResourceLocation(Gigeresque.MOD_ID, "sittingidol/sittingidol"));
	}

	@Override
	public RenderType getRenderType(IdolStorageEntity animatable, ResourceLocation texture) {
		return RenderType.entityTranslucent(getTextureResource(animatable));
	}

}
