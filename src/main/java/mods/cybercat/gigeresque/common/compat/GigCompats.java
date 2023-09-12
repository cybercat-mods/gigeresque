package mods.cybercat.gigeresque.common.compat;

import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.util.GigeresqueInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public record GigCompats() implements GigeresqueInitializer {

	private static GigCompats instance;

	synchronized public static GigCompats getInstance() {
		if (instance == null) {
			instance = new GigCompats();
		}
		return instance;
	}

	@Override
	public void initialize() {
		if (FabricLoader.getInstance().isModLoaded("alexsmobs"))
			FabricLoader.getInstance().getModContainer(Gigeresque.MOD_ID).ifPresent((modContainer -> {
				ResourceManagerHelper.registerBuiltinResourcePack(
						// Mod Compat datapack found in resources/resourcepacks
						new ResourceLocation(Gigeresque.MOD_ID, "alexsmobscompat"), modContainer, Component.literal("alexsmobscompat"), ResourcePackActivationType.DEFAULT_ENABLED);
			}));

		if (FabricLoader.getInstance().isModLoaded("caracal"))
			FabricLoader.getInstance().getModContainer(Gigeresque.MOD_ID).ifPresent((modContainer -> {
				ResourceManagerHelper.registerBuiltinResourcePack(
						// Mod Compat datapack found in resources/resourcepacks
						new ResourceLocation(Gigeresque.MOD_ID, "caracalcompat"), modContainer, Component.literal("caracalcompat"), ResourcePackActivationType.DEFAULT_ENABLED);
			}));

		if (FabricLoader.getInstance().isModLoaded("friendsandfoes"))
			FabricLoader.getInstance().getModContainer(Gigeresque.MOD_ID).ifPresent((modContainer -> {
				ResourceManagerHelper.registerBuiltinResourcePack(
						// Mod Compat datapack found in resources/resourcepacks
						new ResourceLocation(Gigeresque.MOD_ID, "friendsandfoescompat"), modContainer, Component.literal("friendsandfoescompat"), ResourcePackActivationType.DEFAULT_ENABLED);
			}));

		if (FabricLoader.getInstance().isModLoaded("graveyard"))
			FabricLoader.getInstance().getModContainer(Gigeresque.MOD_ID).ifPresent((modContainer -> {
				ResourceManagerHelper.registerBuiltinResourcePack(
						// Mod Compat datapack found in resources/resourcepacks
						new ResourceLocation(Gigeresque.MOD_ID, "graveyardcompat"), modContainer, Component.literal("graveyardcompat"), ResourcePackActivationType.DEFAULT_ENABLED);
			}));

		if (FabricLoader.getInstance().isModLoaded("illagerinvasion"))
			FabricLoader.getInstance().getModContainer(Gigeresque.MOD_ID).ifPresent((modContainer -> {
				ResourceManagerHelper.registerBuiltinResourcePack(
						// Mod Compat datapack found in resources/resourcepacks
						new ResourceLocation(Gigeresque.MOD_ID, "illagerinvasioncompat"), modContainer, Component.literal("illagerinvasioncompat"), ResourcePackActivationType.DEFAULT_ENABLED);
			}));

		if (FabricLoader.getInstance().isModLoaded("twilightforest"))
			FabricLoader.getInstance().getModContainer(Gigeresque.MOD_ID).ifPresent((modContainer -> {
				ResourceManagerHelper.registerBuiltinResourcePack(
						// Mod Compat datapack found in resources/resourcepacks
						new ResourceLocation(Gigeresque.MOD_ID, "twilightforestcompat"), modContainer, Component.literal("twilightforestcompat"), ResourcePackActivationType.DEFAULT_ENABLED);
			}));

		if (FabricLoader.getInstance().isModLoaded("jarjarbinks"))
			FabricLoader.getInstance().getModContainer(Gigeresque.MOD_ID).ifPresent((modContainer -> {
				ResourceManagerHelper.registerBuiltinResourcePack(
						// Mod Compat datapack found in resources/resourcepacks
						new ResourceLocation(Gigeresque.MOD_ID, "jarjarcompat"), modContainer, Component.literal("jarjarcompat"), ResourcePackActivationType.DEFAULT_ENABLED);
			}));

		if (FabricLoader.getInstance().isModLoaded("arachnids"))
			FabricLoader.getInstance().getModContainer(Gigeresque.MOD_ID).ifPresent((modContainer -> {
				ResourceManagerHelper.registerBuiltinResourcePack(
						// Mod Compat datapack found in resources/resourcepacks
						new ResourceLocation(Gigeresque.MOD_ID, "arachnidscompat"), modContainer, Component.literal("arachnidscompat"), ResourcePackActivationType.DEFAULT_ENABLED);
			}));

		if (FabricLoader.getInstance().isModLoaded("aftershock"))
			FabricLoader.getInstance().getModContainer(Gigeresque.MOD_ID).ifPresent((modContainer -> {
				ResourceManagerHelper.registerBuiltinResourcePack(
						// Mod Compat datapack found in resources/resourcepacks
						new ResourceLocation(Gigeresque.MOD_ID, "aftershockcompat"), modContainer, Component.literal("aftershockcompat"), ResourcePackActivationType.DEFAULT_ENABLED);
			}));

		if (FabricLoader.getInstance().isModLoaded("livingthings"))
			FabricLoader.getInstance().getModContainer(Gigeresque.MOD_ID).ifPresent((modContainer -> {
				ResourceManagerHelper.registerBuiltinResourcePack(
						// Mod Compat datapack found in resources/resourcepacks
						new ResourceLocation(Gigeresque.MOD_ID, "livingthingscompat"), modContainer, Component.literal("livingthingscompat"), ResourcePackActivationType.DEFAULT_ENABLED);
			}));

		if (FabricLoader.getInstance().isModLoaded("ydms_redpanda"))
			FabricLoader.getInstance().getModContainer(Gigeresque.MOD_ID).ifPresent((modContainer -> {
				ResourceManagerHelper.registerBuiltinResourcePack(
						// Mod Compat datapack found in resources/resourcepacks
						new ResourceLocation(Gigeresque.MOD_ID, "ydms_redpandacompat"), modContainer, Component.literal("ydms_redpandacompat"), ResourcePackActivationType.DEFAULT_ENABLED);
			}));

		if (FabricLoader.getInstance().isModLoaded("mca"))
			FabricLoader.getInstance().getModContainer(Gigeresque.MOD_ID).ifPresent((modContainer -> {
				ResourceManagerHelper.registerBuiltinResourcePack(
						// Mod Compat datapack found in resources/resourcepacks
						new ResourceLocation(Gigeresque.MOD_ID, "mcacompat"), modContainer, Component.literal("mcacompat"), ResourcePackActivationType.DEFAULT_ENABLED);
			}));

		if (FabricLoader.getInstance().isModLoaded("cave_dweller"))
			FabricLoader.getInstance().getModContainer(Gigeresque.MOD_ID).ifPresent((modContainer -> {
				ResourceManagerHelper.registerBuiltinResourcePack(
						// Mod Compat datapack found in resources/resourcepacks
						new ResourceLocation(Gigeresque.MOD_ID, "cavedwellercompat"), modContainer, Component.literal("cavedwellercompat"), ResourcePackActivationType.DEFAULT_ENABLED);
			}));

		if (FabricLoader.getInstance().isModLoaded("betterend"))
			FabricLoader.getInstance().getModContainer(Gigeresque.MOD_ID).ifPresent((modContainer -> {
				ResourceManagerHelper.registerBuiltinResourcePack(
						// Mod Compat datapack found in resources/resourcepacks
						new ResourceLocation(Gigeresque.MOD_ID, "betterendcompat"), modContainer, Component.literal("betterendcompat"), ResourcePackActivationType.DEFAULT_ENABLED);
			}));
	}
}