package mods.cybercat.gigeresque.client.entity.model.blocks;

import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.block.entity.AlienStorageEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import mod.azure.azurelib.model.DefaultedBlockGeoModel;

public class SarcophagusModel extends DefaultedBlockGeoModel<AlienStorageEntity> {
	
	public SarcophagusModel() {
		super(new ResourceLocation(Gigeresque.MOD_ID, "sarcophagus/sarcophagus"));
	}

	@Override
	public RenderType getRenderType(AlienStorageEntity animatable, ResourceLocation texture) {
		return RenderType.entityTranslucent(getTextureResource(animatable));
	}

}
