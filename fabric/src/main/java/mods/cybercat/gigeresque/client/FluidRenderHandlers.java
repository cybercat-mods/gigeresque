package mods.cybercat.gigeresque.client;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.fluid.GigFluids;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record FluidRenderHandlers() {

    public static void initialize() {
        setupFluidRendering(Constants.modResource("black_fluid"));
        BlockRenderLayerMap.INSTANCE.putFluids(RenderType.solid(), GigFluids.BLACK_FLUID_STILL.get(), GigFluids.BLACK_FLUID_FLOWING.get());
    }

    private static void setupFluidRendering(ResourceLocation textureFluidId) {
        var stillSpriteId = ResourceLocation.fromNamespaceAndPath(textureFluidId.getNamespace(), "block/" + textureFluidId.getPath() + "_still");
        var flowingSpriteId = ResourceLocation.fromNamespaceAndPath(textureFluidId.getNamespace(), "block/" + textureFluidId.getPath() + "_flow");

        var fluidId = BuiltInRegistries.FLUID.getKey(GigFluids.BLACK_FLUID_STILL.get());
        var listenerId = ResourceLocation.fromNamespaceAndPath(fluidId.getNamespace(), fluidId.getPath() + "_reload_listener");
        var fluidSprites = new TextureAtlasSprite[2];

        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public void onResourceManagerReload(@NotNull ResourceManager manager) {
                var atlas = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS);
                fluidSprites[0] = atlas.apply(stillSpriteId);
                fluidSprites[1] = atlas.apply(flowingSpriteId);
            }

            @Override
            public ResourceLocation getFabricId() {
                return listenerId;
            }
        });

        var renderHandler = new FluidRenderHandler() {
            @Override
            public TextureAtlasSprite[] getFluidSprites(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, FluidState state) {
                return fluidSprites;
            }
        };

        FluidRenderHandlerRegistry.INSTANCE.register(GigFluids.BLACK_FLUID_STILL.get(), renderHandler);
        FluidRenderHandlerRegistry.INSTANCE.register(GigFluids.BLACK_FLUID_FLOWING.get(), renderHandler);
    }
}
