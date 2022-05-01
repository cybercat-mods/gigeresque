package mods.cybercat.gigeresque.client.fluid.render;

import org.jetbrains.annotations.Nullable;

import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.fluid.GigFluids;
import mods.cybercat.gigeresque.common.util.GigeresqueInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockRenderView;

@Environment(EnvType.CLIENT)
public class FluidRenderHandlers implements GigeresqueInitializer {

	@Override
	public void initialize() {
		setupFluidRendering(GigFluids.BLACK_FLUID_STILL, GigFluids.BLACK_FLUID_FLOWING,
				new Identifier(Gigeresque.MOD_ID, "black_fluid"));
		BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getSolid(), GigFluids.BLACK_FLUID_STILL,
				GigFluids.BLACK_FLUID_FLOWING);
	}

	@SuppressWarnings("deprecation")
	private void setupFluidRendering(Fluid still, Fluid flowing, Identifier textureFluidId) {
		var stillSpriteId = new Identifier(textureFluidId.getNamespace(),
				"block/" + textureFluidId.getPath() + "_still");
		var flowingSpriteId = new Identifier(textureFluidId.getNamespace(),
				"block/" + textureFluidId.getPath() + "_flow");

		// If they're not already present, add the sprites to the block atlas
		ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE)
				.register((atlasTexture, registry) -> {
					registry.register(stillSpriteId);
					registry.register(flowingSpriteId);
				});
		var fluidId = Registry.FLUID.getId(still);
		var listenerId = new Identifier(fluidId.getNamespace(), fluidId.getPath() + "_reload_listener");
		Sprite[] fluidSprites = new Sprite[2];

		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES)
				.registerReloadListener(new SimpleSynchronousResourceReloadListener() {
					@Override
					public void reload(ResourceManager manager) {
						var atlas = MinecraftClient.getInstance()
								.getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
						fluidSprites[0] = atlas.apply(stillSpriteId);
						fluidSprites[1] = atlas.apply(flowingSpriteId);
					}

					@Override
					public Identifier getFabricId() {
						return listenerId;
					}
				});

		// The FluidRenderer gets the sprites and color from a FluidRenderHandler during
		// rendering
		var renderHandler = new FluidRenderHandler() {
			@Override
			public Sprite[] getFluidSprites(@Nullable BlockRenderView view, @Nullable BlockPos pos, FluidState state) {
				return fluidSprites;
			}
		};

		FluidRenderHandlerRegistry.INSTANCE.register(still, renderHandler);
		FluidRenderHandlerRegistry.INSTANCE.register(flowing, renderHandler);
	}
}
