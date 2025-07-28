package mods.cybercat.gigeresque.datagen.lang;

import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.entity.GigEntities;
import mods.cybercat.gigeresque.common.item.GigItems;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class EnglishLanguageProvider extends FabricLanguageProvider {

    public EnglishLanguageProvider(
        FabricDataOutput dataOutput,
        CompletableFuture<HolderLookup.Provider> registryLookup
    ) {
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider registryLookup, TranslationBuilder builder) {
        // Mob
        builder.add(GigEntities.ALIEN.get(), "Entombed Interloper");
        builder.add(GigEntities.AQUATIC_ALIEN.get(), "Threshing Interloper");
        builder.add(GigEntities.AQUATIC_CHESTBURSTER.get(), "Threshing Burster");
        builder.add(GigEntities.CHESTBURSTER.get(), "Chestburster");
        builder.add(GigEntities.EGG.get(), "Strange Egg");
        builder.add(GigEntities.FACEHUGGER.get(), "Facehugger");
        builder.add(GigEntities.RUNNER_ALIEN.get(), "Lithe Interloper");
        builder.add(GigEntities.RUNNERBURSTER.get(), "Lithe Burster");
        builder.add(GigEntities.MUTANT_POPPER.get(), "Mutant Popper");
        builder.add(GigEntities.MUTANT_HAMMERPEDE.get(), "Mutant Hammerpede");
        builder.add(GigEntities.MUTANT_STALKER.get(), "Mutant Stalker");
        builder.add(GigEntities.NEOBURSTER.get(), "Pale Burster");
        builder.add(GigEntities.NEOMORPH_ADOLESCENT.get(), "Maiming Pale Adolescent");
        builder.add(GigEntities.NEOMORPH.get(), "Maiming Pale");
        builder.add(GigEntities.SPITTER.get(), "Melting Interloper");
        builder.add(GigEntities.RAVENOUSTEMPLEBEAST.get(), "Ravenous Templebeast");
        builder.add(GigEntities.DRACONICTEMPLEBEAST.get(), "Draconic Templebeast");
        builder.add(GigEntities.MOONLIGHTHORRORTEMPLEBEAST.get(), "Moonlight Templebeast");
        builder.add(GigEntities.BAPHOMORPH.get(), "Charnel Templebeast");
        builder.add(GigEntities.HELLMORPH_RUNNER.get(), "Infernal Interloper");
        builder.add(GigEntities.HELL_BURSTER.get(), "Infernal Burster");
        builder.add(GigEntities.BLOOD.get(), "Blood");
        builder.add(GigEntities.ACID.get(), "Acid");
        builder.add(GigEntities.ACID_PROJECTILE.get(), "Acid");
        builder.add(GigEntities.GOO.get(), "Black Goo");
        builder.add(GigEntities.ENGINEER_HOLOGRAM.get(), "Hologram");
        builder.add(GigEntities.AQUA_EGG.get(), "Threshing Egg");

        // Block Entities
        builder.add("entity.gigeresque.petrified_object", "Petrified Object");
        builder.add("entity.gigeresque.petrified_object_1", "Petrified Object");
        builder.add("entity.gigeresque.petrified_object_2", "Petrified Object");
        builder.add("entity.gigeresque.petrified_object_3", "Petrified Object");
        builder.add("entity.gigeresque.petrified_object_4", "Petrified Object");
        builder.add("entity.gigeresque.petrified_object_5", "Petrified Object");
        builder.add("entity.gigeresque.neomorph_spore_pods", "Spore Pods");
        builder.add("entity.gigeresque.sitting_idol_entity", "Sitting Idol");
        builder.add("entity.gigeresque.alien_storage_jar_entity", "Bonelike Jar");
        builder.add("entity.gigeresque.alien_storage_block_entity", "Catacomb Sarcophagus");
        builder.add("entity.gigeresque.alien_storage_block_entity_hugger", "Catacomb Sarcophagus");
        builder.add("entity.gigeresque.alien_storage_block_entity_goo", "Catacomb Sarcophagus");
        builder.add("entity.gigeresque.alien_storage_block_entity_spore", "Catacomb Sarcophagus");

        // Items
        builder.add(GigItems.BLACK_FLUID_BUCKET.get(), "Black Fluid Bucket");
        builder.add(GigItems.ALIEN_SPAWN_EGG.get(), "Entombed Interloper Spawn Egg");
        builder.add(GigItems.AQUATIC_ALIEN_SPAWN_EGG.get(), "Threshing Interloper Spawn Egg");
        builder.add(GigItems.AQUATIC_CHESTBURSTER_SPAWN_EGG.get(), "Threshing Burster Spawn Egg");
        builder.add(GigItems.CHESTBURSTER_SPAWN_EGG.get(), "Chestburster Spawn Egg");
        builder.add(GigItems.EGG_SPAWN_EGG.get(), "Strange Egg Spawn Egg");
        builder.add(GigItems.FACEHUGGER_SPAWN_EGG.get(), "Facehugger Spawn Egg");
        builder.add(GigItems.RUNNER_ALIEN_SPAWN_EGG.get(), "Lithe Interloper Spawn Egg");
        builder.add(GigItems.RUNNERBURSTER_SPAWN_EGG.get(), "Lithe Burster Spawn Egg");
        builder.add(GigItems.MUTANT_POPPER_SPAWN_EGG.get(), "Mutant Popper Spawn Egg");
        builder.add(GigItems.MUTANT_HAMMERPEDE_SPAWN_EGG.get(), "Mutant Hammerpede Spawn Egg");
        builder.add(GigItems.MUTANT_STALKER_SPAWN_EGG.get(), "Mutant Stalker Spawn Egg");
        builder.add(GigItems.NEOBURSTER_SPAWN_EGG.get(), "Pale Burster Spawn Egg");
        builder.add(GigItems.NEOMORPH_ADOLESCENT_SPAWN_EGG.get(), "Adolescent Maiming Pale Spawn Egg");
        builder.add(GigItems.NEOMORPH_SPAWN_EGG.get(), "Maiming Pale Spawn Egg");
        builder.add(GigItems.SPITTER_SPAWN_EGG.get(), "Melting Interloper Spawn Egg");
        builder.add(GigItems.RAVENOUSTEMPLEBEAST_SPAWN_EGG.get(), "Ravenous Templebeast Spawn Egg");
        builder.add(GigItems.DRACONICTEMPLEBEAST_SPAWN_EGG.get(), "Draconic Templebeast Spawn Egg");
        builder.add(GigItems.MOONLIGHTHORRORTEMPLEBEAST_SPAWN_EGG.get(), "Moonlight Horror Templebeast Spawn Egg");
        builder.add(GigItems.HELLMORPH_RUNNER_SPAWN_EGG.get(), "Infernal Interloper Spawn Egg");
        builder.add(GigItems.BAPHOMORPH_SPAWN_EGG.get(), "Charnel Templebeast Spawn Egg");
        builder.add(GigItems.HELL_BURSTER_SPAWN_EGG.get(), "Infernal Burster Spawn Egg");
        builder.add(GigItems.SURGERY_KIT.get(), "Surgery Kit");
        builder.add(GigItems.TRACKER.get(), "Memo Stone");
        builder.add(GigItems.DEV_DEBUG_STICK.get(), "You sleep now");

        // Blocks
        builder.add(GigBlocks.BLACK_FLUID.get(), "Black Fluid");
        builder.add(GigBlocks.BEACON_BLOCK.get(), "Shroudlight");
        builder.add(GigBlocks.PETRIFIED_OBJECT_BLOCK.get(), "Petrified Object");
        builder.add(GigBlocks.PETRIFIED_OBJECT_1_BLOCK.get(), "Petrified Object");
        builder.add(GigBlocks.PETRIFIED_OBJECT_2_BLOCK.get(), "Petrified Object");
        builder.add(GigBlocks.PETRIFIED_OBJECT_3_BLOCK.get(), "Petrified Object");
        builder.add(GigBlocks.PETRIFIED_OBJECT_4_BLOCK.get(), "Petrified Object");
        builder.add(GigBlocks.PETRIFIED_OBJECT_5_BLOCK.get(), "Petrified Object");
        builder.add(GigBlocks.NEST_RESIN.get(), "Nest Resin");
        builder.add(GigBlocks.NEST_RESIN_BLOCK.get(), "Nest Resin Block");
        builder.add(GigBlocks.NEST_RESIN_WEB.get(), "Nest Resin Web");
        builder.add(GigBlocks.NEST_RESIN_WEB_CROSS.get(), "Nest Resin Web (Cross)");
        builder.add(GigBlocks.ORGANIC_ALIEN_BLOCK.get(), "Organic Catacomb Block");
        builder.add(GigBlocks.ORGANIC_FRAGILE_ALIEN_BLOCK.get(), "Organic Fragile Catacomb Block");
        builder.add(GigBlocks.ORGANIC_ALIEN_SLAB.get(), "Organic Catacomb Slab");
        builder.add(GigBlocks.ORGANIC_ALIEN_STAIRS.get(), "Organic Catacomb Stairs");
        builder.add(GigBlocks.ORGANIC_ALIEN_WALL.get(), "Organic Catacomb Wall");
        builder.add(GigBlocks.RESINOUS_ALIEN_BLOCK.get(), "Resinous Catacomb Block");
        builder.add(GigBlocks.RESINOUS_ALIEN_PILLAR.get(), "Resinous Catacomb Pillar");
        builder.add(GigBlocks.RESINOUS_FRAGILE_ALIEN_BLOCK.get(), "Resinous Fragile Catacomb Block");
        builder.add(GigBlocks.RESINOUS_ALIEN_SLAB.get(), "Resinous Catacomb Slab");
        builder.add(GigBlocks.RESINOUS_ALIEN_STAIRS.get(), "Resinous Catacomb Stairs");
        builder.add(GigBlocks.RESINOUS_ALIEN_WALL.get(), "Resinous Catacomb Wall");
        builder.add(GigBlocks.RIBBED_ALIEN_BLOCK.get(), "Ribbed Catacomb Block");
        builder.add(GigBlocks.RIBBED_FRAGILE_ALIEN_BLOCK.get(), "Ribbed Fragile Catacomb Block");
        builder.add(GigBlocks.RIBBED_ALIEN_SLAB.get(), "Ribbed Catacomb Slab");
        builder.add(GigBlocks.RIBBED_ALIEN_STAIRS.get(), "Ribbed Catacomb Stairs");
        builder.add(GigBlocks.RIBBED_ALIEN_WALL.get(), "Ribbed Catacomb Wall");
        builder.add(GigBlocks.RIBBED_ALIEN_PILLAR.get(), "Ribbed Catacomb Pillar");
        builder.add(GigBlocks.ROUGH_ALIEN_BLOCK.get(), "Rough Catacomb Block");
        builder.add(GigBlocks.ROUGH_FRAGILE_ALIEN_BLOCK.get(), "Rough Fragile Catacomb Block");
        builder.add(GigBlocks.ROUGH_ALIEN_WALL.get(), "Rough Catacomb Wall");
        builder.add(GigBlocks.ROUGH_ALIEN_STAIRS.get(), "Rough Catacomb Stairs");
        builder.add(GigBlocks.ROUGH_ALIEN_SLAB.get(), "Rough Catacomb Slab");
        builder.add(GigBlocks.SINOUS_ALIEN_BLOCK.get(), "Sinuous Catacomb Block");
        builder.add(GigBlocks.SINOUS_FRAGILE_ALIEN_BLOCK.get(), "Sinuous Fragile Catacomb Block");
        builder.add(GigBlocks.SINOUS_ALIEN_SLAB.get(), "Sinuous Catacomb Slab");
        builder.add(GigBlocks.SINOUS_ALIEN_STAIRS.get(), "Sinuous Catacomb Stairs");
        builder.add(GigBlocks.SINOUS_ALIEN_WALL.get(), "Sinuous Catacomb Wall");
        builder.add(GigBlocks.SMOOTH_ALIEN_PILLAR.get(), "Smooth Catacomb Pillar");
        builder.add(GigBlocks.SMOOTH_ALIEN_STAIRS.get(), "Smooth Catacomb Stairs");
        builder.add(GigBlocks.MURAL_ALIEN_BLOCK_1.get(), "Creature Mural Block 1");
        builder.add(GigBlocks.MURAL_ALIEN_BLOCK_2.get(), "Creature Mural Block 2");
        builder.add(GigBlocks.MURAL_ALIEN_BLOCK_3.get(), "Creature Mural Block 3");
        builder.add(GigBlocks.MURAL_ALIEN_BLOCK_4.get(), "Creature Mural Block 4");
        builder.add(GigBlocks.MURAL_ALIEN_BLOCK_5.get(), "Creature Mural Block 5");
        builder.add(GigBlocks.MURAL_ALIEN_BLOCK_6.get(), "Creature Mural Block 6");
        builder.add(GigBlocks.MURAL_ALIEN_BLOCK_7.get(), "Creature Mural Block 7");
        builder.add(GigBlocks.MURAL_ALIEN_BLOCK_8.get(), "Creature Mural Block 8");
        builder.add(GigBlocks.MURAL_ALIEN_BLOCK_9.get(), "Creature Mural Block 9");
        builder.add(GigBlocks.MURAL_ALIEN_BLOCK_10.get(), "Creature Mural Block 10");
        builder.add(GigBlocks.MURAL_ALIEN_BLOCK_11.get(), "Creature Mural Block 11");
        builder.add(GigBlocks.MURAL_ALIEN_BLOCK_12.get(), "Creature Mural Block 12");
        builder.add(GigBlocks.MURAL_ALIEN_BLOCK_13.get(), "Creature Mural Block 13");
        builder.add(GigBlocks.MURAL_ALIEN_BLOCK_14.get(), "Creature Mural Block 14");
        builder.add(GigBlocks.MURAL_ALIEN_BLOCK_15.get(), "Creature Mural Block 15");
        builder.add(GigBlocks.MURAL_ALIEN_BLOCK_16.get(), "Creature Mural Block 16");
        builder.add(GigBlocks.MURAL_ALIEN_BLOCK_17.get(), "Creature Mural Block 17");
        builder.add(GigBlocks.MURAL_ALIEN_BLOCK_18.get(), "Creature Mural Block 18");
        builder.add(GigBlocks.MURAL_ALIEN_BLOCK_19.get(), "Creature Mural Block 19");
        builder.add(GigBlocks.MURAL_ALIEN_BLOCK_20.get(), "Creature Mural Block 20");
        builder.add(GigBlocks.MURAL_ALIEN_BLOCK_21.get(), "Creature Mural Block 21");
        builder.add(GigBlocks.MURAL_ALIEN_BLOCK_22.get(), "Creature Mural Block 22");
        builder.add(GigBlocks.MURAL_ALIEN_BLOCK_23.get(), "Creature Mural Block 23");
        builder.add(GigBlocks.MURAL_ALIEN_BLOCK_24.get(), "Creature Mural Block 24");
        builder.add(GigBlocks.SPORE_BLOCK.get(), "Spores Puff");
        builder.add(GigBlocks.ALIEN_STORAGE_BLOCK_1.get(), "Catacomb Sarcophagus");
        builder.add(GigBlocks.ALIEN_STORAGE_BLOCK_1_HUGGER.get(), "Catacomb Sarcophagus (Hugger)");
        builder.add(GigBlocks.ALIEN_STORAGE_BLOCK_1_GOO.get(), "Catacomb Sarcophagus (Goo)");
        builder.add(GigBlocks.ALIEN_STORAGE_BLOCK_1_SPORE.get(), "Catacomb Sarcophagus (Spore)");
        builder.add(GigBlocks.ALIEN_STORAGE_BLOCK_INVIS.get(), "Bonelike Sarcophagus");
        builder.add(GigBlocks.ALIEN_STORAGE_BLOCK_2.get(), "Bonelike Jar");
        builder.add(GigBlocks.ALIEN_STORAGE_BLOCK_3.get(), "Sitting Idol");
        builder.add(GigBlocks.ALIEN_STORAGE_BLOCK_INVIS2.get(), "Sitting Idol");
        builder.add(GigBlocks.SURFACE_VENT_BLOCK.get(), "Surface Vent");
        builder.add(GigBlocks.DUNGEON_VENT_BLOCK.get(), "Dungeon Vent");

        // Effects
        builder.add(GigStatusEffects.ACID.value(), "Acid Burn");
        builder.add("effect.gigeresque.acid.description", "");
        builder.add(GigStatusEffects.TRAUMA.value(), "Trauma");
        builder.add("effect.gigeresque.trauma.description", "");
        builder.add(GigStatusEffects.DNA.value(), "DNA Disintegration");
        builder.add("effect.gigeresque.dna_disintegration.description", "");
        builder.add(GigStatusEffects.SPORE.value(), "Mycosis");
        builder.add("effect.gigeresque.neo_spore.description", "");
        builder.add(GigStatusEffects.IMPREGNATION.value(), "Impregnation");
        builder.add("effect.gigeresque.impregnation.description", "");
        builder.add(GigStatusEffects.EGGMORPHING.value(), "Eggmorphing");
        builder.add("effect.gigeresque.eggmorphing.description", "");

        // Maps
        builder.add("filled_map.gig_dungeon", "Otherworldly Structure Map");

        // Creative Tabs
        builder.add("itemGroup." + CommonMod.MOD_ID + ".items", "Gigeresque Items");
        builder.add("itemGroup." + CommonMod.MOD_ID + ".blocks", "Gigeresque Blocks");

        // Tool Tips
        builder.add("item.gigeresque.creativeonly.tooltip", "Creative Mode Only Item");
        builder.add("block.gigeresque.unfinished.tooltip", "Unfinished Block");

        // Death
        builder.add("death.attack.acid", "%s dissolved in acid.");
        builder.add("death.attack.failed_surgery", "%s's hand slipped.");
        builder.add("death.attack.failed_surgery.player", "%s's hand slipped.");
        builder.add("death.attack.chestburst", "A parasite ruptured %s's chest!");
        builder.add("death.attack.eggmorph", "%s perpetuates the cycle.");
        builder.add("death.attack.dna", "%s's genetic code was rearranged.");
        builder.add("death.attack.goo", "%s is no longer what they used to be.");
        builder.add("death.attack.xeno", "So %s um, we think we should discuss the bonus situation...");
        builder.add("death.attack.execution1", "%s suffered a splitting headache.");
        builder.add("death.attack.execution2", "%s had a brain blast!");
        builder.add("death.attack.spore", "%s suffered from a massive dose of Tetanospasmin!");

        // Subtitles
        builder.add("subtitles.gigeresque.dungeon_idle", "Something strange plays");
        builder.add("subtitles.gigeresque.tracker_summon", "A ghostly figure appears");
        builder.add("subtitles.gigeresque.alien_hiss", "");
        builder.add("subtitles.gigeresque.alien_claw", "");
        builder.add("subtitles.gigeresque.alien_tail", "");
        builder.add("subtitles.gigeresque.alien_death", "");
        builder.add("subtitles.gigeresque.alien_hurt", "");
        builder.add("subtitles.gigeresque.alien_headbite", "");
        builder.add("subtitles.gigeresque.alien_death_thud", "");
        builder.add("subtitles.gigeresque.alien_crunch", "");
        builder.add("subtitles.gigeresque.egg_notice", "");
        builder.add("subtitles.gigeresque.hugger_ambient", "");
        builder.add("subtitles.gigeresque.hugger_death", "");
        builder.add("subtitles.gigeresque.hugger_hurt", "");
        builder.add("subtitles.gigeresque.hugger_implant", "");
        builder.add("subtitles.gigeresque.chestbursting", "");
        builder.add("subtitles.gigeresque.alien_footstep", "");
        builder.add("subtitles.gigeresque.alien_handstep", "");
        builder.add("subtitles.gigeresque.aqua_landmove", "");
        builder.add("subtitles.gigeresque.burster_crawl", "");
        builder.add("subtitles.gigeresque.aqua_landclaw", "");

        // Configs
        builder.add("config.gigeresque.option.classicXenoConfigs", "Entombed Interloper Configs");
        builder.add("config.gigeresque.option.aquaticXenoConfigs", "Threshing Interloper Configs");
        builder.add("config.gigeresque.option.hammerpedeConfigs", "Hammerpede Configs");
        builder.add("config.gigeresque.option.popperConfigs", "Popper Configs");
        builder.add("config.gigeresque.option.runnerbusterConfigs", "Lithe Burster Configs");
        builder.add("config.gigeresque.option.stalkerConfigs", "Stalker Configs");
        builder.add("config.gigeresque.option.runnerConfigs", "Lithe Interloper Configs");
        builder.add("config.gigeresque.option.eggConfigs", "Strange Egg Configs");
        builder.add("config.gigeresque.option.spitterConfigs", "Melting Interloper Configs");
        builder.add("config.gigeresque.option.neobursterConfigs", "Pale Burster Configs");
        builder.add("config.gigeresque.option.neomorphAdolescentConfigs", "Maiming Pale Adolescent Configs");
        builder.add("config.gigeresque.option.neomorphConfigs", "Maiming Pale Configs");
        builder.add("config.gigeresque.option.draconicTempleBeastConfigs", "Draconic Templebeast Configs");
        builder.add("config.gigeresque.option.moonlightHorrorTempleBeastConfigs", "Moonlight Horror Templebeast Configs");
        builder.add("config.gigeresque.option.ravenousTempleBeastConfigs", "Ravenous Templebeast Configs");
        builder.add("config.gigeresque.option.baphomorphConfigs", "Charnel Templebeast Configs");
        builder.add("config.gigeresque.option.hellmorphrunnerConfigs", "Infernal Interloper Configs");
        builder.add("config.gigeresque.option.facehuggerConfigs", "Facehugger Configs");
        builder.add("config.gigeresque.option.alienblockConfigs", "Block Configs");
        builder.add("config.gigeresque.option.bursterConfigs", "Burster Configs");
        builder.add("config.gigeresque.option.isolationMode", "Isolation Mode");
        builder.add("config.gigeresque.option.surgeryKit", "Surgery Kit");
        builder.add("config.gigeresque.option.acidResistantBlocks", "Acid-Resistant Blocks");
        builder.add("config.gigeresque.option.acidResistantBlocks.@Tooltip", "Blocks that will not be destroyed by acid.");
        builder.add("config.gigeresque.option.alienGrowthMultiplier", "Catacomb Growth Multiplier");
        builder.add("config.gigeresque.option.alienGrowthMultiplier.@Tooltip", "The rate at which aliens mature.");
        builder.add("config.gigeresque.option.aquaticAlienGrowthMultiplier", "Threshing Interloper Growth Multiplier");
        builder.add("config.gigeresque.option.aquaticChestbursterGrowthMultiplier", "Threshing Burster Growth Multiplier");
        builder.add("config.gigeresque.option.chestbursterGrowthMultiplier", "Chestburster Growth Multiplier");
        builder.add("config.gigeresque.option.eggmorphTickTimer", "Eggmorph Tick Timer");
        builder.add("config.gigeresque.option.facehuggerAttachTickTimer", "Facehugger Attach Tick Timer");
        builder.add("config.gigeresque.option.impregnationTickTimer", "Impregnation Tick Timer");
        builder.add("config.gigeresque.option.runnerAlienGrowthMultiplier", "Lithe Interloper Growth Multiplier");
        builder.add("config.gigeresque.option.runnerbursterGrowthMultiplier", "Lithe Burster Growth Multiplier");
        builder.add("config.gigeresque.option.gooEffectTickTimer", "Goo Effect Tick Timer");
        builder.add("config.gigeresque.option.maxSurgeryKitUses", "Max Uses of Surgery Kits");
        builder.add("config.gigeresque.option.classicXenoHealth", "Entombed Interloper Health");
        builder.add("config.gigeresque.option.classicXenoArmor", "Entombed Interloper Armor");
        builder.add("config.gigeresque.option.classicXenoAttackDamage", "Entombed Interloper Attack Damage");
        builder.add("config.gigeresque.option.aquaticXenoHealth", "Threshing Interloper Health");
        builder.add("config.gigeresque.option.aquaticXenoArmor", "Threshing Interloper Armor");
        builder.add("config.gigeresque.option.aquaticXenoAttackDamage", "Threshing Interloper Attack Damage");
        builder.add("config.gigeresque.option.hammerpedeHealth", "Hammerpede Health");
        builder.add("config.gigeresque.option.hammerpedeAttackDamage", "Hammerpede Attack Damage");
        builder.add("config.gigeresque.option.popperHealth", "Popper Health");
        builder.add("config.gigeresque.option.popperAttackDamage", "Popper Attack Damage");
        builder.add("config.gigeresque.option.runnerbusterHealth", "Lithe Burster Health");
        builder.add("config.gigeresque.option.runnerbusterAttackDamage", "Lithe Burster Attack Damage");
        builder.add("config.gigeresque.option.stalkerXenoHealth", "Stalker Health");
        builder.add("config.gigeresque.option.stalkerXenoArmor", "Stalker Armor");
        builder.add("config.gigeresque.option.stalkerAttackDamage", "Stalker Attack Damage");
        builder.add("config.gigeresque.option.runnerXenoHealth", "Lithe Interloper Health");
        builder.add("config.gigeresque.option.runnerXenoArmor", "Lithe Interloper Armor");
        builder.add("config.gigeresque.option.runnerXenoAttackDamage", "Lithe Interloper Attack Damage");
        builder.add("config.gigeresque.option.alieneggHealth", "Strange Egg Health");
        builder.add("config.gigeresque.option.chestbursterHealth", "Chestburster Health");
        builder.add("config.gigeresque.option.facehuggerHealth", "Facehugger Health");
        builder.add("config.gigeresque.option.alienegg_spawn_weight", "Strange Egg Spawn Weight");
        builder.add("config.gigeresque.option.alienegg_min_group", "Strange Egg Spawn Min Group Size");
        builder.add("config.gigeresque.option.alienegg_max_group", "Strange Egg Spawn Max Group Size");
        builder.add("config.gigeresque.option.classicXenoAttackSpeed", "Entombed Interloper Attack Movement Speed");
        builder.add("config.gigeresque.option.stalkerAttackSpeed", "Stalker Attack Movement Speed");
        builder.add("config.gigeresque.option.runnerXenoAttackSpeed", "Lithe Interloper Attack Movement Speed");
        builder.add("config.gigeresque.option.classicXenoTailAttackDamage", "Entombed Interloper Tail Extra Damage");
        builder.add("config.gigeresque.option.aquaticXenoTailAttackDamage", "Threshing Interloper Tail Extra Damage");
        builder.add("config.gigeresque.option.stalkerTailAttackDamage", "Stalker Tail Extra Damage");
        builder.add("config.gigeresque.option.runnerXenoTailAttackDamage", "Lithe Interloper Tail Extra Damage");
        builder.add("config.gigeresque.option.alieneggHatchRange", "Egg Hatch Range");
        builder.add("config.gigeresque.option.acidDamage", "Acid Damage Per Tick");
        builder.add("config.gigeresque.option.xenoMaxSoundRange", "Xeno Audio Range");
        builder.add("config.gigeresque.option.surgeryKitCooldownTicks", "Surgery Kit Cooldown Ticks");
        builder.add("config.gigeresque.option.facehuggerGivesBlindness", "Facehuggers Give Blindness");
        builder.add("config.gigeresque.option.spitterXenoHealth", "Melting Interloper Health");
        builder.add("config.gigeresque.option.spitterXenoArmor", "Melting Interloper Armor");
        builder.add("config.gigeresque.option.spitterAttackDamage", "Melting Interloper Attack Damage");
        builder.add("config.gigeresque.option.spitterXenoTailAttackDamage", "Melting Interloper Tail Extra Damage");
        builder.add("config.gigeresque.option.spitterRangeAttackDamage", "Melting Interloper Ranged Attack Damage");
        builder.add("config.gigeresque.option.neobursterXenoHealth", "Pale Burster Health");
        builder.add("config.gigeresque.option.neobursterAttackDamage", "Pale Burster Attack Damage");
        builder.add("config.gigeresque.option.neomorph_adolescentXenoHealth", "Maiming Pale Adolescent Health");
        builder.add("config.gigeresque.option.neomorph_adolescentAttackDamage", "Maiming Pale Adolescent Attack Damage");
        builder.add("config.gigeresque.option.neomorph_adolescentXenoTailAttackDamage", "Maiming Pale Adolescent Tail Extra Damage");
        builder.add("config.gigeresque.option.neomorphXenoHealth", "Maiming Pale Health");
        builder.add("config.gigeresque.option.neomorphXenoArmor", "Maiming Pale Armor");
        builder.add("config.gigeresque.option.neomorphAttackDamage", "Maiming Pale Attack Damage");
        builder.add("config.gigeresque.option.neomorphXenoTailAttackDamage", "Maiming Pale Tail Extra Damage");
        builder.add("config.gigeresque.option.draconicTempleBeastXenoHealth", "Draconic Templebeast Health");
        builder.add("config.gigeresque.option.draconicTempleBeastXenoArmor", "Draconic Templebeast Armor");
        builder.add("config.gigeresque.option.draconicTempleBeastAttackDamage", "Draconic Templebeast Attack Damage");
        builder.add("config.gigeresque.option.ravenousTempleBeastXenoHealth", "Ravenous Templebeast Health");
        builder.add("config.gigeresque.option.ravenousTempleBeastXenoArmor", "Ravenous Templebeast Armor");
        builder.add("config.gigeresque.option.ravenousTempleBeastAttackDamage", "Ravenous Templebeast Attack Damage");
        builder.add("config.gigeresque.option.moonlightHorrorTempleBeastXenoHealth", "Moonlight Horror Templebeast Health");
        builder.add("config.gigeresque.option.moonlightHorrorTempleBeastXenoArmor", "Moonlight Horror Templebeast Armor");
        builder.add("config.gigeresque.option.moonlightHorrorTempleBeastAttackDamage", "Moonlight Horror Templebeast Attack Damage");
        builder.add("config.gigeresque.option.baphomorphXenoHealth", "Charnel Templebeast Health");
        builder.add("config.gigeresque.option.baphomorphXenoArmor", "Charnel Templebeast Armor");
        builder.add("config.gigeresque.option.baphomorphAttackDamage", "Charnel Templebeast Attack Damage");
        builder.add("config.gigeresque.option.hellmorph_runnerXenoHealth", "Infernal Interloper Health");
        builder.add("config.gigeresque.option.hellmorph_runnerXenoArmor", "Infernal Interloper Armor");
        builder.add("config.gigeresque.option.hellmorph_runnerAttackDamage", "Infernal Interloper Attack Damage");
        builder.add("config.gigeresque.option.sporeTickTimer", "Spore Tick Timer");
        builder.add("config.gigeresque.option.alienblockHardness", "Catacomb Block Hardness");
        builder.add("config.gigeresque.option.alienblockResistance", "Catacomb Block Resistance");
        builder.add("config.gigeresque.option.enableDevparticles", "Enable Dev Particles");
        builder.add("config.gigeresque.option.enableDevEntites", "Enable Dev Entities");
        builder.add("config.gigeresque.option.blackfuildNonrepacle", "Makes Black Fluid Nonreplacable");
        builder.add("config.gigeresque.option.facehuggerStunTickTimer", "Facehugger Stun Timer");
        builder.add("config.gigeresque.option.enableFacehuggerAttachmentTimer", "Enable Facehugger On Screen Timer");
        builder.add("config.gigeresque.option.enableFacehuggerTimerTicks", "Make Facehugger Timer Screen Use Ticks");
        builder.add("config.gigeresque.option.enabledCreativeBootAcidProtection", "Make Acid Not Damage Boots of Creative Players");
        builder.add("config.gigeresque.option.enableAcidLavaRemoval", "Make Lava be able to remove Acid/Black Goo bleeding");
        builder.add("config.gigeresque.option.enableLogging", "Enable Extra Logging");
        builder.add("config.gigeresque.option.enablePandoraEffects", "Enable Pandora Effect");
        builder.add("config.gigeresque.option.hellbusterConfigs", "Infernal Burster Configs");
        builder.add("config.gigeresque.option.hellbusterGrowthMultiplier", "Infernal Burster Growth Multiplier");
        builder.add("config.gigeresque.option.hellbusterHealth", "Infernal Burster Health");
        builder.add("config.gigeresque.option.hellbusterAttackDamage", "Infernal Burster Attack Damage");
        builder.add("config.screen.gigeresque", "Gigeresque Options");

        // Advancements
        builder.add("advancements.gigeresque.find_dungeon.title", "Within These Twisted Halls...");
        builder.add("advancements.gigeresque.find_dungeon.description", "Enter the unknown");
        builder.add("advancements.gigeresque.surgery_kit.title", "Cut It Out");
        builder.add("advancements.gigeresque.surgery_kit.description", "Remove a parasite before it removes you");
        builder.add("advancements.gigeresque.dna_cure.title", "All I Am is Me");
        builder.add("advancements.gigeresque.dna_cure.description", "Prevent total genetic collapse by consuming wither suspicious stew and a golden apple");
        builder.add("advancements.gigeresque.all_gig_effects.title", "This Has to Be On Spore-pose");
        builder.add("advancements.gigeresque.all_gig_effects.description", "Have three separate infections at once");
        builder.add("advancements.gigeresque.doubletrouble.title", "Now You're Really In the Spit");
        builder.add("advancements.gigeresque.doubletrouble.description", "Have two things go horribly wrong at the same time");
        builder.add("advancements.gigeresque.facehugged.title", "...We Are Not Alone");
        builder.add("advancements.gigeresque.facehugged.description", "Have the unknown enter you");
        builder.add("advancements.gigeresque.firstspawnfromeffect.title", "Something Followed Us Back");
        builder.add("advancements.gigeresque.firstspawnfromeffect.description", "Realise you're no longer safe");
        builder.add("advancements.gigeresque.dontacidbottle.title", "Yeah, Like That's Ever Gonna Happen");
        builder.add("advancements.gigeresque.dontacidbottle.description", "Fail to exploit acid");
        builder.add("advancements.gigeresque.dontdothat.title", "Yeah, Like That's Ever Gonna Happen");
        builder.add("advancements.gigeresque.dontdothat.description", "Fail to exploit the unknown");
        builder.add("advancements.gigeresque.dontgoobottle.title", "Yeah, Like That's Ever Gonna Happen");
        builder.add("advancements.gigeresque.dontgoobottle.description", "Fail to exploit black fluid blood");
    }
}
