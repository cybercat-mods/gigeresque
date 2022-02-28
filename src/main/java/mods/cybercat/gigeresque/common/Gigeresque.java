package mods.cybercat.gigeresque.common;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mods.cybercat.gigeresque.common.block.Blocks;
import mods.cybercat.gigeresque.common.config.GigeresqueConfig;
import mods.cybercat.gigeresque.common.data.handler.TrackedDataHandlers;
import mods.cybercat.gigeresque.common.entity.Entities;
import mods.cybercat.gigeresque.common.fluid.Fluids;
import mods.cybercat.gigeresque.common.item.Items;
import mods.cybercat.gigeresque.common.sound.Sounds;
import mods.cybercat.gigeresque.common.status.effect.StatusEffects;
import mods.cybercat.gigeresque.common.structures.Structures;
import mods.cybercat.gigeresque.common.structures.StructuresConfigured;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.impl.structure.FabricStructureImpl;
import net.fabricmc.fabric.mixin.structure.StructuresConfigAccessor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.FlatChunkGenerator;
import net.minecraft.world.gen.chunk.StructureConfig;
import net.minecraft.world.gen.chunk.StructuresConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import software.bernie.geckolib3.GeckoLib;

public class Gigeresque implements ModInitializer {
	public static GigeresqueConfig config;
	public static final Logger LOGGER = LoggerFactory.getLogger(Gigeresque.class);
	public static final String MOD_ID = "gigeresque";

	@Override
	public void onInitialize() {
		AutoConfig.register(GigeresqueConfig.class, GsonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(GigeresqueConfig.class).getConfig();

		GeckoLib.initialize();

		Items.getInstance().initialize();
		Blocks.getInstance().initialize();
		Fluids.getInstance().initialize();
		Sounds.getInstance().initialize();
		StatusEffects.getInstance().initialize();
		TrackedDataHandlers.getInstance().initialize();
		Entities.getInstance().initialize();
		Structures.setupAndRegisterStructureFeatures();
		StructuresConfigured.registerConfiguredStructures();
		addStructureSpawningToDimensionsAndBiomes();
	}

	public static void addStructureSpawningToDimensionsAndBiomes() {
		BiomeModifications.addStructure(BiomeSelectors.categories(Biome.Category.UNDERGROUND), RegistryKey.of(
				Registry.CONFIGURED_STRUCTURE_FEATURE_KEY,
				BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE.getId(StructuresConfigured.CONFIGURED_GIG_DUNGEON)));

		Identifier runAfterFabricAPIPhase = new Identifier(MOD_ID, "run_after_fabric_api");
		ServerWorldEvents.LOAD.addPhaseOrdering(Event.DEFAULT_PHASE, runAfterFabricAPIPhase);
		ServerWorldEvents.LOAD.register(runAfterFabricAPIPhase,
				(MinecraftServer minecraftServer, ServerWorld serverWorld) -> {
					if (serverWorld.getChunkManager().getChunkGenerator() instanceof FlatChunkGenerator
							&& serverWorld.getRegistryKey().equals(World.OVERWORLD)) {
						return;
					}
					StructuresConfig worldStructureConfig = serverWorld.getChunkManager().getChunkGenerator()
							.getStructuresConfig();
					Map<StructureFeature<?>, StructureConfig> tempMap = new HashMap<>(
							worldStructureConfig.getStructures());
					if (serverWorld.getRegistryKey().equals(World.OVERWORLD)) {
						tempMap.put(Structures.GIG_DUNGEON,
								FabricStructureImpl.STRUCTURE_TO_CONFIG_MAP.get(Structures.GIG_DUNGEON));
					} else {
						tempMap.remove(Structures.GIG_DUNGEON);
					}
					((StructuresConfigAccessor) worldStructureConfig).setStructures(tempMap);
				});
	}
}
