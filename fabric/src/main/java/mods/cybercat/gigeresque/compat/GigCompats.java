package mods.cybercat.gigeresque.compat;

import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.Constants;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;

public record GigCompats() {

    public static void initialize() {
        if (FabricLoader.getInstance().isModLoaded("alexsmobs"))
            FabricLoader.getInstance().getModContainer(CommonMod.MOD_ID).ifPresent(
                    (modContainer -> ResourceManagerHelper.registerBuiltinResourcePack(
                            // Mod Compat datapack found in resources/resourcepacks
                            Constants.modResource("alexsmobscompat"), modContainer,
                            Component.literal("alexsmobscompat"), ResourcePackActivationType.DEFAULT_ENABLED)));

        if (FabricLoader.getInstance().isModLoaded("caracal"))
            FabricLoader.getInstance().getModContainer(CommonMod.MOD_ID).ifPresent(
                    (modContainer -> ResourceManagerHelper.registerBuiltinResourcePack(
                            // Mod Compat datapack found in resources/resourcepacks
                            Constants.modResource("caracalcompat"), modContainer, Component.literal("caracalcompat"),
                            ResourcePackActivationType.DEFAULT_ENABLED)));

        if (FabricLoader.getInstance().isModLoaded("friendsandfoes"))
            FabricLoader.getInstance().getModContainer(CommonMod.MOD_ID).ifPresent(
                    (modContainer -> ResourceManagerHelper.registerBuiltinResourcePack(
                            // Mod Compat datapack found in resources/resourcepacks
                            Constants.modResource("friendsandfoescompat"), modContainer,
                            Component.literal("friendsandfoescompat"), ResourcePackActivationType.DEFAULT_ENABLED)));

        if (FabricLoader.getInstance().isModLoaded("graveyard"))
            FabricLoader.getInstance().getModContainer(CommonMod.MOD_ID).ifPresent(
                    (modContainer -> ResourceManagerHelper.registerBuiltinResourcePack(
                            // Mod Compat datapack found in resources/resourcepacks
                            Constants.modResource("graveyardcompat"), modContainer,
                            Component.literal("graveyardcompat"), ResourcePackActivationType.DEFAULT_ENABLED)));

        if (FabricLoader.getInstance().isModLoaded("illagerinvasion"))
            FabricLoader.getInstance().getModContainer(CommonMod.MOD_ID).ifPresent(
                    (modContainer -> ResourceManagerHelper.registerBuiltinResourcePack(
                            // Mod Compat datapack found in resources/resourcepacks
                            Constants.modResource("illagerinvasioncompat"), modContainer,
                            Component.literal("illagerinvasioncompat"), ResourcePackActivationType.DEFAULT_ENABLED)));

        if (FabricLoader.getInstance().isModLoaded("twilightforest"))
            FabricLoader.getInstance().getModContainer(CommonMod.MOD_ID).ifPresent(
                    (modContainer -> ResourceManagerHelper.registerBuiltinResourcePack(
                            // Mod Compat datapack found in resources/resourcepacks
                            Constants.modResource("twilightforestcompat"), modContainer,
                            Component.literal("twilightforestcompat"), ResourcePackActivationType.DEFAULT_ENABLED)));

        if (FabricLoader.getInstance().isModLoaded("jarjarbinks"))
            FabricLoader.getInstance().getModContainer(CommonMod.MOD_ID).ifPresent(
                    (modContainer -> ResourceManagerHelper.registerBuiltinResourcePack(
                            // Mod Compat datapack found in resources/resourcepacks
                            Constants.modResource("jarjarcompat"), modContainer, Component.literal("jarjarcompat"),
                            ResourcePackActivationType.DEFAULT_ENABLED)));

        if (FabricLoader.getInstance().isModLoaded("arachnids"))
            FabricLoader.getInstance().getModContainer(CommonMod.MOD_ID).ifPresent(
                    (modContainer -> ResourceManagerHelper.registerBuiltinResourcePack(
                            // Mod Compat datapack found in resources/resourcepacks
                            Constants.modResource("arachnidscompat"), modContainer,
                            Component.literal("arachnidscompat"), ResourcePackActivationType.DEFAULT_ENABLED)));

        if (FabricLoader.getInstance().isModLoaded("aftershock"))
            FabricLoader.getInstance().getModContainer(CommonMod.MOD_ID).ifPresent(
                    (modContainer -> ResourceManagerHelper.registerBuiltinResourcePack(
                            // Mod Compat datapack found in resources/resourcepacks
                            Constants.modResource("aftershockcompat"), modContainer,
                            Component.literal("aftershockcompat"), ResourcePackActivationType.DEFAULT_ENABLED)));

        if (FabricLoader.getInstance().isModLoaded("livingthings"))
            FabricLoader.getInstance().getModContainer(CommonMod.MOD_ID).ifPresent(
                    (modContainer -> ResourceManagerHelper.registerBuiltinResourcePack(
                            // Mod Compat datapack found in resources/resourcepacks
                            Constants.modResource("livingthingscompat"), modContainer,
                            Component.literal("livingthingscompat"), ResourcePackActivationType.DEFAULT_ENABLED)));

        if (FabricLoader.getInstance().isModLoaded("ydms_redpanda"))
            FabricLoader.getInstance().getModContainer(CommonMod.MOD_ID).ifPresent(
                    (modContainer -> ResourceManagerHelper.registerBuiltinResourcePack(
                            // Mod Compat datapack found in resources/resourcepacks
                            Constants.modResource("ydms_redpandacompat"), modContainer,
                            Component.literal("ydms_redpandacompat"), ResourcePackActivationType.DEFAULT_ENABLED)));

        if (FabricLoader.getInstance().isModLoaded("mca"))
            FabricLoader.getInstance().getModContainer(CommonMod.MOD_ID).ifPresent(
                    (modContainer -> ResourceManagerHelper.registerBuiltinResourcePack(
                            // Mod Compat datapack found in resources/resourcepacks
                            Constants.modResource("mcacompat"), modContainer, Component.literal("mcacompat"),
                            ResourcePackActivationType.DEFAULT_ENABLED)));

        if (FabricLoader.getInstance().isModLoaded("cave_dweller"))
            FabricLoader.getInstance().getModContainer(CommonMod.MOD_ID).ifPresent(
                    (modContainer -> ResourceManagerHelper.registerBuiltinResourcePack(
                            // Mod Compat datapack found in resources/resourcepacks
                            Constants.modResource("cavedwellercompat"), modContainer,
                            Component.literal("cavedwellercompat"), ResourcePackActivationType.DEFAULT_ENABLED)));

        if (FabricLoader.getInstance().isModLoaded("betterend"))
            FabricLoader.getInstance().getModContainer(CommonMod.MOD_ID).ifPresent(
                    (modContainer -> ResourceManagerHelper.registerBuiltinResourcePack(
                            // Mod Compat datapack found in resources/resourcepacks
                            Constants.modResource("betterendcompat"), modContainer,
                            Component.literal("betterendcompat"), ResourcePackActivationType.DEFAULT_ENABLED)));

        if (FabricLoader.getInstance().isModLoaded("ad_astra"))
            FabricLoader.getInstance().getModContainer(CommonMod.MOD_ID).ifPresent(
                    (modContainer -> ResourceManagerHelper.registerBuiltinResourcePack(
                            // Mod Compat datapack found in resources/resourcepacks
                            Constants.modResource("adastracompat"), modContainer, Component.literal("adastracompat"),
                            ResourcePackActivationType.DEFAULT_ENABLED)));

        if (FabricLoader.getInstance().isModLoaded("promenade"))
            FabricLoader.getInstance().getModContainer(CommonMod.MOD_ID).ifPresent(
                    (modContainer -> ResourceManagerHelper.registerBuiltinResourcePack(
                            // Mod Compat datapack found in resources/resourcepacks
                            Constants.modResource("promenadecompat"), modContainer,
                            Component.literal("promenadecompat"), ResourcePackActivationType.DEFAULT_ENABLED)));

        if (FabricLoader.getInstance().isModLoaded("hwg"))
            FabricLoader.getInstance().getModContainer(CommonMod.MOD_ID).ifPresent(
                    (modContainer -> ResourceManagerHelper.registerBuiltinResourcePack(
                            // Mod Compat datapack found in resources/resourcepacks
                            Constants.modResource("hwgcompat"), modContainer, Component.literal("hwgcompat"),
                            ResourcePackActivationType.DEFAULT_ENABLED)));

        if (FabricLoader.getInstance().isModLoaded("aether"))
            FabricLoader.getInstance().getModContainer(CommonMod.MOD_ID).ifPresent(
                    (modContainer -> ResourceManagerHelper.registerBuiltinResourcePack(
                            // Mod Compat datapack found in resources/resourcepacks
                            Constants.modResource("aethercompat"), modContainer, Component.literal("aethercompat"),
                            ResourcePackActivationType.DEFAULT_ENABLED)));

        if (FabricLoader.getInstance().isModLoaded("alexscaves"))
            FabricLoader.getInstance().getModContainer(CommonMod.MOD_ID).ifPresent(
                    (modContainer -> ResourceManagerHelper.registerBuiltinResourcePack(
                            // Mod Compat datapack found in resources/resourcepacks
                            Constants.modResource("alexscavescompat"), modContainer,
                            Component.literal("alexscavescompat"), ResourcePackActivationType.DEFAULT_ENABLED)));

        if (FabricLoader.getInstance().isModLoaded("avp"))
            FabricLoader.getInstance().getModContainer(CommonMod.MOD_ID).ifPresent(
                    (modContainer -> ResourceManagerHelper.registerBuiltinResourcePack(
                            // Mod Compat datapack found in resources/resourcepacks
                            Constants.modResource("avpcompat"), modContainer,
                            Component.literal("avpcompat"), ResourcePackActivationType.DEFAULT_ENABLED)));

        if (FabricLoader.getInstance().isModLoaded("stellaris"))
            FabricLoader.getInstance().getModContainer(CommonMod.MOD_ID).ifPresent(
                    (modContainer -> ResourceManagerHelper.registerBuiltinResourcePack(
                            // Mod Compat datapack found in resources/resourcepacks
                            Constants.modResource("stellariscompat"), modContainer,
                            Component.literal("stellariscompat"), ResourcePackActivationType.DEFAULT_ENABLED)));
    }
}