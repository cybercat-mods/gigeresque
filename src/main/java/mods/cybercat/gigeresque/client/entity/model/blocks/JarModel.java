package mods.cybercat.gigeresque.client.entity.model.blocks;

import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.block.entity.JarStorageEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;

public class JarModel extends DefaultedBlockGeoModel<JarStorageEntity> {
	
	public JarModel() {
		super(new ResourceLocation(Gigeresque.MOD_ID, "jar/jar"));
	}

	@Override
	public RenderType getRenderType(JarStorageEntity animatable, ResourceLocation texture) {
		return RenderType.entityTranslucent(getTextureResource(animatable));
	}

}
