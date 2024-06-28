package mods.cybercat.gigeresque.common.sound;

import mods.cybercat.gigeresque.CommonMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

public record GigSounds() implements CommonSoundRegistryInterface {
    public static Supplier<SoundEvent> ALIEN_HISS = CommonSoundRegistryInterface.registerSound(
            CommonMod.MOD_ID,
            "alien_hiss",
            () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(CommonMod.MOD_ID,
                            "alien_hiss")));
    public static Supplier<SoundEvent> ALIEN_AMBIENT = CommonSoundRegistryInterface.registerSound(
            CommonMod.MOD_ID,
            "alien_ambient",
            () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(CommonMod.MOD_ID,
                            "alien_ambient")));
    public static Supplier<SoundEvent> ALIEN_HURT = CommonSoundRegistryInterface.registerSound(
            CommonMod.MOD_ID,
            "alien_hurt",
            () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(CommonMod.MOD_ID,
                            "alien_hurt")));
    public static Supplier<SoundEvent> ALIEN_DEATH = CommonSoundRegistryInterface.registerSound(
            CommonMod.MOD_ID,
            "alien_death",
            () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(CommonMod.MOD_ID,
                            "alien_death")));
    public static Supplier<SoundEvent> ALIEN_DEATH_THUD = CommonSoundRegistryInterface.registerSound(
            CommonMod.MOD_ID,
            "alien_death_thud",
            () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(CommonMod.MOD_ID,
                            "alien_death_thud")));
    public static Supplier<SoundEvent> ALIEN_CLAW = CommonSoundRegistryInterface.registerSound(
            CommonMod.MOD_ID,
            "alien_claw",
            () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(CommonMod.MOD_ID,
                            "alien_claw")));
    public static Supplier<SoundEvent> ALIEN_TAIL = CommonSoundRegistryInterface.registerSound(
            CommonMod.MOD_ID,
            "alien_tail",
            () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(CommonMod.MOD_ID,
                            "alien_tail")));
    public static Supplier<SoundEvent> ALIEN_HEADBITE = CommonSoundRegistryInterface.registerSound(
            CommonMod.MOD_ID,
            "alien_headbite",
            () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(CommonMod.MOD_ID,
                            "alien_headbite")));
    public static Supplier<SoundEvent> ALIEN_CRUNCH = CommonSoundRegistryInterface.registerSound(
            CommonMod.MOD_ID,
            "alien_crunch",
            () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(CommonMod.MOD_ID,
                            "alien_crunch")));
    public static Supplier<SoundEvent> ALIEN_FOOTSTEP = CommonSoundRegistryInterface.registerSound(
            CommonMod.MOD_ID,
            "alien_footstep",
            () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(CommonMod.MOD_ID,
                            "alien_footstep")));
    public static Supplier<SoundEvent> ALIEN_HANDSTEP = CommonSoundRegistryInterface.registerSound(
            CommonMod.MOD_ID,
            "alien_handstep",
            () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(CommonMod.MOD_ID,
                            "alien_handstep")));
    public static Supplier<SoundEvent> EGG_NOTICE = CommonSoundRegistryInterface.registerSound(
            CommonMod.MOD_ID,
            "egg_notice",
            () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(CommonMod.MOD_ID,
                            "egg_notice")));
    public static Supplier<SoundEvent> EGG_OPEN = CommonSoundRegistryInterface.registerSound(
            CommonMod.MOD_ID,
            "egg_open",
            () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(CommonMod.MOD_ID,
                            "egg_open")));
    public static Supplier<SoundEvent> AQUA_LANDMOVE = CommonSoundRegistryInterface.registerSound(
            CommonMod.MOD_ID,
            "aqua_landmove",
            () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(CommonMod.MOD_ID,
                            "aqua_landmove")));
    public static Supplier<SoundEvent> AQUA_LANDCLAW = CommonSoundRegistryInterface.registerSound(
            CommonMod.MOD_ID,
            "aqua_landclaw",
            () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(CommonMod.MOD_ID,
                            "aqua_landclaw")));
    public static Supplier<SoundEvent> BURSTER_CRAWL = CommonSoundRegistryInterface.registerSound(
            CommonMod.MOD_ID,
            "burster_crawl",
            () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(CommonMod.MOD_ID,
                            "burster_crawl")));
    public static Supplier<SoundEvent> HUGGER_AMBIENT = CommonSoundRegistryInterface.registerSound(
            CommonMod.MOD_ID,
            "hugger_ambient",
            () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(CommonMod.MOD_ID,
                            "hugger_ambient")));
    public static Supplier<SoundEvent> HUGGER_DEATH = CommonSoundRegistryInterface.registerSound(
            CommonMod.MOD_ID,
            "hugger_death",
            () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(CommonMod.MOD_ID,
                            "hugger_death")));
    public static Supplier<SoundEvent> HUGGER_HURT = CommonSoundRegistryInterface.registerSound(
            CommonMod.MOD_ID,
            "hugger_hurt",
            () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(CommonMod.MOD_ID,
                            "hugger_hurt")));
    public static Supplier<SoundEvent> HUGGER_IMPLANT = CommonSoundRegistryInterface.registerSound(
            CommonMod.MOD_ID,
            "hugger_implant",
            () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(CommonMod.MOD_ID,
                            "hugger_implant")));
    public static Supplier<SoundEvent> CHESTBURSTING = CommonSoundRegistryInterface.registerSound(
            CommonMod.MOD_ID,
            "chestbursting",
            () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(CommonMod.MOD_ID,
                            "chestbursting")));

    public static void initialize() {
    }
}
