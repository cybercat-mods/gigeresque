package mods.cybercat.gigeresque.common.item;

import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.common.entity.GigEntities;
import mods.cybercat.gigeresque.common.fluid.GigFluids;
import mods.cybercat.gigeresque.platform.GigServices;
import net.minecraft.world.item.*;

import java.util.function.Supplier;

public record GigItems() implements CommonItemRegistryInterface {

    public static final Supplier<Item> BLACK_FLUID_BUCKET = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "black_fluid_bucket", () ->
                    new BucketItem(GigFluids.BLACK_FLUID_STILL.get(),
                            new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
    public static final Supplier<Item> SURGERY_KIT = CommonItemRegistryInterface.registerItem(CommonMod.MOD_ID,
            "surgery_kit", SurgeryKitItem::new);
    public static final Supplier<SpawnEggItem> ALIEN_SPAWN_EGG = CommonItemRegistryInterface.registerItem(
            CommonMod.MOD_ID,
            "alien_spawn_egg",
            GigServices.COMMON_REGISTRY.makeSpawnEggFor(
                    GigEntities.ALIEN,
                    0x404345,
                    0x949597,
                    new Item.Properties()));
    public static final Supplier<SpawnEggItem> AQUATIC_ALIEN_SPAWN_EGG = CommonItemRegistryInterface.registerItem(
            CommonMod.MOD_ID, "aquatic_alien_spawn_egg",
            GigServices.COMMON_REGISTRY.makeSpawnEggFor(GigEntities.AQUATIC_ALIEN, 0x404345, 0x949597,
                    new Item.Properties()));
    public static final Supplier<SpawnEggItem> AQUATIC_CHESTBURSTER_SPAWN_EGG = CommonItemRegistryInterface.registerItem(
            CommonMod.MOD_ID, "aquatic_chestburster_spawn_egg",
            GigServices.COMMON_REGISTRY.makeSpawnEggFor(GigEntities.AQUATIC_CHESTBURSTER, 0xDED29D, 0x2C2B27,
                    new Item.Properties()));
    public static final Supplier<SpawnEggItem> CHESTBURSTER_SPAWN_EGG = CommonItemRegistryInterface.registerItem(
            CommonMod.MOD_ID, "chestburster_spawn_egg",
            GigServices.COMMON_REGISTRY.makeSpawnEggFor(GigEntities.CHESTBURSTER, 0xDED29D, 0x2C2B27, new Item.Properties()));
    public static final Supplier<SpawnEggItem> EGG_SPAWN_EGG = CommonItemRegistryInterface.registerItem(
            CommonMod.MOD_ID, "egg_spawn_egg",
            GigServices.COMMON_REGISTRY.makeSpawnEggFor(GigEntities.EGG, 0x554E45, 0x4D4932, new Item.Properties()));
    public static final Supplier<SpawnEggItem> FACEHUGGER_SPAWN_EGG = CommonItemRegistryInterface.registerItem(
            CommonMod.MOD_ID, "facehugger_spawn_egg",
            GigServices.COMMON_REGISTRY.makeSpawnEggFor(GigEntities.FACEHUGGER, 0xC7B986, 0x516B21, new Item.Properties()));
    public static final Supplier<SpawnEggItem> RUNNER_ALIEN_SPAWN_EGG = CommonItemRegistryInterface.registerItem(
            CommonMod.MOD_ID, "runner_alien_spawn_egg",
            GigServices.COMMON_REGISTRY.makeSpawnEggFor(GigEntities.RUNNER_ALIEN, 0x3E230B, 0x623C25, new Item.Properties()));
    public static final Supplier<SpawnEggItem> RUNNERBURSTER_SPAWN_EGG = CommonItemRegistryInterface.registerItem(
            CommonMod.MOD_ID, "runnerburster_spawn_egg",
            GigServices.COMMON_REGISTRY.makeSpawnEggFor(GigEntities.RUNNERBURSTER, 0xDED29D, 0x2C2B27,
                    new Item.Properties()));
    public static final Supplier<SpawnEggItem> MUTANT_POPPER_SPAWN_EGG = CommonItemRegistryInterface.registerItem(
            CommonMod.MOD_ID, "popper_spawn_egg",
            GigServices.COMMON_REGISTRY.makeSpawnEggFor(GigEntities.MUTANT_POPPER, 0xdeeae9, 0x816d66,
                    new Item.Properties()));
    public static final Supplier<SpawnEggItem> MUTANT_HAMMERPEDE_SPAWN_EGG = CommonItemRegistryInterface.registerItem(
            CommonMod.MOD_ID, "hammerpede_spawn_egg",
            GigServices.COMMON_REGISTRY.makeSpawnEggFor(GigEntities.MUTANT_HAMMERPEDE, 0xe3e1d5, 0x826e66,
                    new Item.Properties()));
    public static final Supplier<SpawnEggItem> MUTANT_STALKER_SPAWN_EGG = CommonItemRegistryInterface.registerItem(
            CommonMod.MOD_ID, "stalker_spawn_egg",
            GigServices.COMMON_REGISTRY.makeSpawnEggFor(GigEntities.MUTANT_STALKER, 0xcdd7d8, 0x816d66,
                    new Item.Properties()));
    public static final Supplier<SpawnEggItem> NEOBURSTER_SPAWN_EGG = CommonItemRegistryInterface.registerItem(
            CommonMod.MOD_ID, "neoburster_spawn_egg",
            GigServices.COMMON_REGISTRY.makeSpawnEggFor(GigEntities.NEOBURSTER, 0xdccfca, 0xcbbcb6, new Item.Properties()));
    public static final Supplier<SpawnEggItem> NEOMORPH_ADOLESCENT_SPAWN_EGG = CommonItemRegistryInterface.registerItem(
            CommonMod.MOD_ID, "neomorph_adolescent_spawn_egg",
            GigServices.COMMON_REGISTRY.makeSpawnEggFor(GigEntities.NEOMORPH_ADOLESCENT, 0xe6ddd9, 0xada1a2,
                    new Item.Properties()));
    public static final Supplier<SpawnEggItem> NEOMORPH_SPAWN_EGG = CommonItemRegistryInterface.registerItem(
            CommonMod.MOD_ID, "neomorph_spawn_egg",
            GigServices.COMMON_REGISTRY.makeSpawnEggFor(GigEntities.NEOMORPH, 0xfaf8f5, 0xa587a4, new Item.Properties()));
    public static final Supplier<SpawnEggItem> SPITTER_SPAWN_EGG = CommonItemRegistryInterface.registerItem(
            CommonMod.MOD_ID, "spitter_spawn_egg",
            GigServices.COMMON_REGISTRY.makeSpawnEggFor(GigEntities.SPITTER, 0xccc737, 0x383a33, new Item.Properties()));
    public static final Supplier<SpawnEggItem> DRACONICTEMPLEBEAST_SPAWN_EGG = CommonItemRegistryInterface.registerItem(
            CommonMod.MOD_ID, "draconictemplebeast_spawn_egg",
            GigServices.COMMON_REGISTRY.makeSpawnEggFor(GigEntities.DRACONICTEMPLEBEAST, 0x7d7060, 0x2d2f2c,
                    new Item.Properties()));
    public static final Supplier<SpawnEggItem> RAVENOUSTEMPLEBEAST_SPAWN_EGG = CommonItemRegistryInterface.registerItem(
            CommonMod.MOD_ID, "ravenoustemplebeast_spawn_egg",
            GigServices.COMMON_REGISTRY.makeSpawnEggFor(GigEntities.RAVENOUSTEMPLEBEAST, 0x9d917b, 0x1e1d1c,
                    new Item.Properties()));
    public static final Supplier<SpawnEggItem> MOONLIGHTHORRORTEMPLEBEAST_SPAWN_EGG = CommonItemRegistryInterface.registerItem(
            CommonMod.MOD_ID, "moonlighthorrortemplebeast_spawn_egg",
            GigServices.COMMON_REGISTRY.makeSpawnEggFor(GigEntities.MOONLIGHTHORRORTEMPLEBEAST, 0xDEE0E2, 0x5f6d7f,
                    new Item.Properties()));
    public static final Supplier<SpawnEggItem> HELLMORPH_RUNNER_SPAWN_EGG = CommonItemRegistryInterface.registerItem(
            CommonMod.MOD_ID, "hellmorph_runner_spawn_egg",
            GigServices.COMMON_REGISTRY.makeSpawnEggFor(GigEntities.HELLMORPH_RUNNER, 0x2b2c24, 0x514939,
                    new Item.Properties()));
    public static final Supplier<SpawnEggItem> BAPHOMORPH_SPAWN_EGG = CommonItemRegistryInterface.registerItem(
            CommonMod.MOD_ID, "baphomorph_spawn_egg",
            GigServices.COMMON_REGISTRY.makeSpawnEggFor(GigEntities.BAPHOMORPH, 0x52361e, 0x887a56,
                    new Item.Properties()));

    public static void initialize() {
    }
}
