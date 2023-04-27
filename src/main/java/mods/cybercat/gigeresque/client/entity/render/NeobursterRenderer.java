package mods.cybercat.gigeresque.client.entity.render;

import mod.azure.azurelib.renderer.GeoEntityRenderer;
import mods.cybercat.gigeresque.client.entity.model.NeobursterModel;
import mods.cybercat.gigeresque.common.entity.impl.neo.NeobursterEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

@Environment(EnvType.CLIENT)
public class NeobursterRenderer extends GeoEntityRenderer<NeobursterEntity> {
	public NeobursterRenderer(EntityRendererProvider.Context context) {
		super(context, new NeobursterModel());
		this.shadowRadius = 0.5f;
	}

	@Override
	protected float getDeathMaxRotation(NeobursterEntity entityLivingBaseIn) {
		return 0.0F;
	}
}
