package mods.cybercat.gigeresque.client.entity.texture;

import com.mojang.blaze3d.platform.NativeImage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

public class EggmorphLayerTexture implements AutoCloseable {
	public RenderType renderLayer;
	private DynamicTexture texture;

	public EggmorphLayerTexture(TextureManager textureManager, ResourceManager resourceManager, ResourceLocation base) {
		texture = new DynamicTexture(new NativeImage(128, 128, true));
		Minecraft.getInstance().execute(new Runnable() {
			@Override
			public void run() {
				try {
					var baseImage = NativeImage.read(resourceManager.getResourceOrThrow(base).open());
					var layerImage = NativeImage
							.read(resourceManager.getResourceOrThrow(EntityTextures.EGGMORPH_OVERLAY).open());
					texture = new DynamicTexture(new NativeImage(layerImage.getWidth(), layerImage.getHeight(), true));
					var height = layerImage.getHeight();
					var width = layerImage.getWidth();
					var heightRatio = height / ((float) baseImage.getHeight());
					var widthRatio = width / ((float) baseImage.getWidth());
					for (int y = 0; y < height; y++) {
						for (int x = 0; x < width; x++) {
							var color = baseImage.getPixelRGBA(((int) (x / widthRatio)), ((int) (y / heightRatio)));
							var alpha = color >> 24 & 255;
							if (alpha > 25) {
								NativeImage image = texture.getPixels();
								if (image != null)
									image.setPixelRGBA(x, y, layerImage.getPixelRGBA(x, y));
							}
						}
					}
					texture.upload();
					baseImage.close();
				} catch (Exception e) {
				}
			}
		});
		ResourceLocation id = textureManager.register("eggmorph_layer/" + base.getPath(), texture);
		renderLayer = RenderType.entityTranslucent(id);
	}

	@Override
	public void close() throws Exception {
		texture.close();
	}
}
