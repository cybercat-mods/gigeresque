package mods.cybercat.gigeresque.compat;

import mods.cybercat.gigeresque.Constants;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.event.AddPackFindersEvent;

public record GigCompats() {
    
    @SubscribeEvent
    public static void onConstructMod(final AddPackFindersEvent event) {
        if (ModList.get().isLoaded("alexsmobs"))
            event.addPackFinders(Constants.modResource("resourcepacks/alexsmobscompat"),
                    PackType.SERVER_DATA,
                    Component.literal("alexsmobscompat"),
                    PackSource.FEATURE,
                    true,
                    Pack.Position.TOP);

        if (ModList.get().isLoaded("caracal"))
            event.addPackFinders(Constants.modResource("resourcepacks/caracalcompat"),
                    PackType.SERVER_DATA,
                    Component.literal("caracalcompat"),
                    PackSource.FEATURE,
                    true,
                    Pack.Position.TOP);

        if (ModList.get().isLoaded("friendsandfoes"))
            event.addPackFinders(Constants.modResource("resourcepacks/friendsandfoescompat"),
                    PackType.SERVER_DATA,
                    Component.literal("friendsandfoescompat"),
                    PackSource.FEATURE,
                    true,
                    Pack.Position.TOP);

        if (ModList.get().isLoaded("graveyard"))
            event.addPackFinders(Constants.modResource("resourcepacks/graveyardcompat"),
                    PackType.SERVER_DATA,
                    Component.literal("graveyardcompat"),
                    PackSource.FEATURE,
                    true,
                    Pack.Position.TOP);

        if (ModList.get().isLoaded("illagerinvasion"))
            event.addPackFinders(Constants.modResource("resourcepacks/illagerinvasioncompat"),
                    PackType.SERVER_DATA,
                    Component.literal("illagerinvasioncompat"),
                    PackSource.FEATURE,
                    true,
                    Pack.Position.TOP);

        if (ModList.get().isLoaded("twilightforest"))
            event.addPackFinders(Constants.modResource("resourcepacks/twilightforestcompat"),
                    PackType.SERVER_DATA,
                    Component.literal("twilightforestcompat"),
                    PackSource.FEATURE,
                    true,
                    Pack.Position.TOP);

        if (ModList.get().isLoaded("jarjarbinks"))
            event.addPackFinders(Constants.modResource("resourcepacks/jarjarcompat"),
                    PackType.SERVER_DATA,
                    Component.literal("jarjarcompat"),
                    PackSource.FEATURE,
                    true,
                    Pack.Position.TOP);

        if (ModList.get().isLoaded("arachnids"))
            event.addPackFinders(Constants.modResource("resourcepacks/arachnidscompat"),
                    PackType.SERVER_DATA,
                    Component.literal("arachnidscompat"),
                    PackSource.FEATURE,
                    true,
                    Pack.Position.TOP);

        if (ModList.get().isLoaded("aftershock"))
            event.addPackFinders(Constants.modResource("resourcepacks/aftershockcompat"),
                    PackType.SERVER_DATA,
                    Component.literal("aftershockcompat"),
                    PackSource.FEATURE,
                    true,
                    Pack.Position.TOP);

        if (ModList.get().isLoaded("livingthings"))
            event.addPackFinders(Constants.modResource("resourcepacks/livingthingscompat"),
                    PackType.SERVER_DATA,
                    Component.literal("livingthingscompat"),
                    PackSource.FEATURE,
                    true,
                    Pack.Position.TOP);

        if (ModList.get().isLoaded("ydms_redpanda"))
            event.addPackFinders(Constants.modResource("resourcepacks/ydms_redpandacompat"),
                    PackType.SERVER_DATA,
                    Component.literal("ydms_redpandacompat"),
                    PackSource.FEATURE,
                    true,
                    Pack.Position.TOP);

        if (ModList.get().isLoaded("mca"))
            event.addPackFinders(Constants.modResource("resourcepacks/mcacompat"),
                    PackType.SERVER_DATA,
                    Component.literal("mcacompat"),
                    PackSource.FEATURE,
                    true,
                    Pack.Position.TOP);

        if (ModList.get().isLoaded("cave_dweller"))
            event.addPackFinders(Constants.modResource("resourcepacks/cavedwellercompat"),
                    PackType.SERVER_DATA,
                    Component.literal("cavedwellercompat"),
                    PackSource.FEATURE,
                    true,
                    Pack.Position.TOP);

        if (ModList.get().isLoaded("betterend"))
            event.addPackFinders(Constants.modResource("resourcepacks/betterendcompat"),
                    PackType.SERVER_DATA,
                    Component.literal("betterendcompat"),
                    PackSource.FEATURE,
                    true,
                    Pack.Position.TOP);

        if (ModList.get().isLoaded("ad_astra"))
            event.addPackFinders(Constants.modResource("resourcepacks/adastracompat"),
                    PackType.SERVER_DATA,
                    Component.literal("adastracompat"),
                    PackSource.FEATURE,
                    true,
                    Pack.Position.TOP);

        if (ModList.get().isLoaded("promenade"))
            event.addPackFinders(Constants.modResource("resourcepacks/promenadecompat"),
                    PackType.SERVER_DATA,
                    Component.literal("promenadecompat"),
                    PackSource.FEATURE,
                    true,
                    Pack.Position.TOP);

        if (ModList.get().isLoaded("hwg"))
            event.addPackFinders(Constants.modResource("resourcepacks/hwgcompat"),
                    PackType.SERVER_DATA,
                    Component.literal("hwgcompat"),
                    PackSource.FEATURE,
                    true,
                    Pack.Position.TOP);

        if (ModList.get().isLoaded("aethercompat"))
            event.addPackFinders(Constants.modResource("resourcepacks/aethercompat"),
                    PackType.SERVER_DATA,
                    Component.literal("alexsmobscompat"),
                    PackSource.FEATURE,
                    true,
                    Pack.Position.TOP);

        if (ModList.get().isLoaded("alexscaves"))
            event.addPackFinders(Constants.modResource("resourcepacks/alexscavescompat"),
                    PackType.SERVER_DATA,
                    Component.literal("alexscavescompat"),
                    PackSource.FEATURE,
                    true,
                    Pack.Position.TOP);

        if (ModList.get().isLoaded("avp"))
            event.addPackFinders(Constants.modResource("resourcepacks/avpcompat"),
                    PackType.SERVER_DATA,
                    Component.literal("avpcompat"),
                    PackSource.FEATURE,
                    true,
                    Pack.Position.TOP);

        if (ModList.get().isLoaded("stellaris"))
            event.addPackFinders(Constants.modResource("resourcepacks/stellariscompat"),
                    PackType.SERVER_DATA,
                    Component.literal("stellariscompat"),
                    PackSource.FEATURE,
                    true,
                    Pack.Position.TOP);
    }
}
