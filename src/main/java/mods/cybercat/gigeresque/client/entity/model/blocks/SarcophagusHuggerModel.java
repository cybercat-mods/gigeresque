package mods.cybercat.gigeresque.client.entity.model.blocks;

import mod.azure.azurelib.model.DefaultedBlockGeoModel;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.block.entity.AlienStorageHuggerEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class SarcophagusHuggerModel extends DefaultedBlockGeoModel<AlienStorageHuggerEntity> {

	public SarcophagusHuggerModel() {
		super(Constants.modResource("sarcophagus/sarcophagus"));
	}

	@Override
	public RenderType getRenderType(AlienStorageHuggerEntity animatable, ResourceLocation texture) {
		return RenderType.entityTranslucent(getTextureResource(animatable));
	}

}
