package mods.cybercat.gigeresque.client.entity.texture;

import mods.cybercat.gigeresque.common.Gigeresque;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

public class EggmorphLayerTexture implements AutoCloseable {
	private static final Identifier layerTexture = new Identifier(Gigeresque.MOD_ID,
			"textures/misc/eggmorph_overlay.png");
	public RenderLayer renderLayer;
	private NativeImageBackedTexture texture;

	public EggmorphLayerTexture(TextureManager textureManager, ResourceManager resourceManager, Identifier base) {
		texture = new NativeImageBackedTexture(new NativeImage(128, 128, true));
		MinecraftClient.getInstance().execute(new Runnable() {
			@Override
			public void run() {
				try {
					var baseImage = NativeImage.read(resourceManager.getResource(base).getInputStream());
					var layerImage = NativeImage.read(resourceManager.getResource(layerTexture).getInputStream());
					texture = new NativeImageBackedTexture(
							new NativeImage(layerImage.getWidth(), layerImage.getHeight(), true));
					var height = layerImage.getHeight();
					var width = layerImage.getWidth();
					var heightRatio = height / ((float) baseImage.getHeight());
					var widthRatio = width / ((float) baseImage.getWidth());
					for (int y = 0; y < height; y++) {
						for (int x = 0; x < width; x++) {
							var color = baseImage.getColor(((int) (x / widthRatio)), ((int) (y / heightRatio)));
							var alpha = color >> 24 & 255;
							if (alpha > 25) {
								NativeImage image = texture.getImage();
								if (image != null) {
									image.setColor(x, y, layerImage.getColor(x, y));
								}
							}
						}
					}
					texture.upload();
					baseImage.close();
				} catch (Exception e) {
				}
			}
		});
		Identifier id = textureManager.registerDynamicTexture("eggmorph_layer/" + base.getPath(), texture);
		renderLayer = RenderLayer.getEntityTranslucent(id);
	}

	@Override
	public void close() throws Exception {
		texture.close();
	}
}
