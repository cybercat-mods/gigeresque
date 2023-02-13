package mods.cybercat.gigeresque.client.entity.model.blocks;

import mod.azure.azurelib.model.DefaultedBlockGeoModel;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.block.entity.JarStorageEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class JarModel extends DefaultedBlockGeoModel<JarStorageEntity> {
	
	public JarModel() {
		super(Constants.modResource("jar/jar"));
	}

	@Override
	public RenderType getRenderType(JarStorageEntity animatable, ResourceLocation texture) {
		return RenderType.entityTranslucent(getTextureResource(animatable));
	}

}
