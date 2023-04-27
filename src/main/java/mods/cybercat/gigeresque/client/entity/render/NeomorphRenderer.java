package mods.cybercat.gigeresque.client.entity.render;

import mod.azure.azurelib.renderer.GeoEntityRenderer;
import mods.cybercat.gigeresque.client.entity.model.NeomorphModel;
import mods.cybercat.gigeresque.common.entity.impl.neo.NeomorphEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

@Environment(EnvType.CLIENT)
public class NeomorphRenderer extends GeoEntityRenderer<NeomorphEntity> {
	public NeomorphRenderer(EntityRendererProvider.Context context) {
		super(context, new NeomorphModel());
		this.shadowRadius = 0.5f;
	}

	@Override
	protected float getDeathMaxRotation(NeomorphEntity entityLivingBaseIn) {
		return 0.0F;
	}
}
