package mods.cybercat.gigeresque;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.azure.azurelib.common.internal.common.AzureLib;
import mods.cybercat.gigeresque.common.entity.GigEntities;
import mods.cybercat.gigeresque.common.entity.impl.aqua.AquaticAlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.classic.AlienEggEntity;
import mods.cybercat.gigeresque.common.entity.impl.classic.ChestbursterEntity;
import mods.cybercat.gigeresque.common.entity.impl.classic.ClassicAlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.classic.FacehuggerEntity;
import mods.cybercat.gigeresque.common.entity.impl.extra.SpitterEntity;
import mods.cybercat.gigeresque.common.entity.impl.mutant.HammerpedeEntity;
import mods.cybercat.gigeresque.common.entity.impl.mutant.PopperEntity;
import mods.cybercat.gigeresque.common.entity.impl.mutant.StalkerEntity;
import mods.cybercat.gigeresque.common.entity.impl.neo.NeobursterEntity;
import mods.cybercat.gigeresque.common.entity.impl.neo.NeomorphAdolescentEntity;
import mods.cybercat.gigeresque.common.entity.impl.neo.NeomorphEntity;
import mods.cybercat.gigeresque.common.entity.impl.runner.RunnerAlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.runner.RunnerbursterEntity;
import mods.cybercat.gigeresque.common.entity.impl.templebeast.DraconicTempleBeastEntity;
import mods.cybercat.gigeresque.common.entity.impl.templebeast.MoonlightHorrorTempleBeastEntity;
import mods.cybercat.gigeresque.common.entity.impl.templebeast.RavenousTempleBeastEntity;
import mods.cybercat.gigeresque.common.util.GigVillagerTrades;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@Mod(CommonMod.MOD_ID)
public final class NeoForgeMod {
    public static DeferredRegister<BlockEntityType<?>> blockEntityTypeDeferredRegister = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, CommonMod.MOD_ID);
    public static DeferredRegister<Block> blockDeferredRegister = DeferredRegister.create(Registries.BLOCK, CommonMod.MOD_ID);
    public static DeferredRegister<EntityType<?>> entityTypeDeferredRegister = DeferredRegister.create(Registries.ENTITY_TYPE, CommonMod.MOD_ID);
    public static DeferredRegister<ArmorMaterial> armorMaterialDeferredRegister = DeferredRegister.create(Registries.ARMOR_MATERIAL, CommonMod.MOD_ID);
    public static DeferredRegister<Item> itemDeferredRegister = DeferredRegister.create(Registries.ITEM, CommonMod.MOD_ID);
    public static DeferredRegister<SoundEvent> soundEventDeferredRegister= DeferredRegister.create(Registries.SOUND_EVENT, CommonMod.MOD_ID);
    public static DeferredRegister<MenuType<?>> menuTypeDeferredRegister= DeferredRegister.create(Registries.MENU, CommonMod.MOD_ID);
    public static DeferredRegister<StructureType<?>> structureTypeDeferredRegister = DeferredRegister.create(Registries.STRUCTURE_TYPE, CommonMod.MOD_ID);
    public static DeferredRegister<ParticleType<?>> particleTypeDeferredRegister = DeferredRegister.create(Registries.PARTICLE_TYPE, CommonMod.MOD_ID);
    public static DeferredRegister<CreativeModeTab> creativeModeTabDeferredRegister = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CommonMod.MOD_ID);
    public static DeferredRegister<MobEffect> statusEffectDeferredRegister = DeferredRegister.create(Registries.MOB_EFFECT, CommonMod.MOD_ID);
    public static DeferredRegister<Fluid> fluidDeferredRegister = DeferredRegister.create(Registries.FLUID, CommonMod.MOD_ID);

    public NeoForgeMod(IEventBus modEventBus) {
        AzureLib.initialize();
        CommonMod.initRegistries();
        NeoForgeMod.blockEntityTypeDeferredRegister.register(modEventBus);
        NeoForgeMod.blockDeferredRegister.register(modEventBus);
        NeoForgeMod.entityTypeDeferredRegister.register(modEventBus);
        if (NeoForgeMod.armorMaterialDeferredRegister != null)
            NeoForgeMod.armorMaterialDeferredRegister.register(modEventBus);
        NeoForgeMod.itemDeferredRegister.register(modEventBus);
        NeoForgeMod.soundEventDeferredRegister.register(modEventBus);
        if (NeoForgeMod.menuTypeDeferredRegister != null)
            NeoForgeMod.menuTypeDeferredRegister.register(modEventBus);
        NeoForgeMod.structureTypeDeferredRegister.register(modEventBus);
        NeoForgeMod.particleTypeDeferredRegister.register(modEventBus);
        NeoForgeMod.creativeModeTabDeferredRegister.register(modEventBus);
        NeoForgeMod.statusEffectDeferredRegister.register(modEventBus);
        NeoForgeMod.fluidDeferredRegister.register(modEventBus);
        modEventBus.addListener(this::createEntityAttributes);
        modEventBus.addListener(this::onRegisterEvent);
        ModEntitySpawn.SERIALIZER.register(modEventBus);
    }

    public void onRegisterEvent(RegisterSpawnPlacementsEvent event) {
        event.register(GigEntities.EGG.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AlienEggEntity::checkMonsterSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.AND);
    }

    public void createEntityAttributes(final EntityAttributeCreationEvent event) {
        event.put(GigEntities.ALIEN.get(), ClassicAlienEntity.createAttributes().build());
        event.put(GigEntities.AQUATIC_ALIEN.get(), AquaticAlienEntity.createAttributes().build());
        event.put(GigEntities.AQUATIC_CHESTBURSTER.get(), ChestbursterEntity.createAttributes().build());
        event.put(GigEntities.CHESTBURSTER.get(), ChestbursterEntity.createAttributes().build());
        event.put(GigEntities.EGG.get(), AlienEggEntity.createAttributes().build());
        event.put(GigEntities.FACEHUGGER.get(), FacehuggerEntity.createAttributes().build());
        event.put(GigEntities.RUNNER_ALIEN.get(), RunnerAlienEntity.createAttributes().build());
        event.put(GigEntities.RUNNERBURSTER.get(), RunnerbursterEntity.createAttributes().build());
        event.put(GigEntities.MUTANT_POPPER.get(), PopperEntity.createAttributes().build());
        event.put(GigEntities.MUTANT_HAMMERPEDE.get(), HammerpedeEntity.createAttributes().build());
        event.put(GigEntities.MUTANT_STALKER.get(), StalkerEntity.createAttributes().build());
        event.put(GigEntities.NEOBURSTER.get(), NeobursterEntity.createAttributes().build());
        event.put(GigEntities.NEOMORPH_ADOLESCENT.get(), NeomorphAdolescentEntity.createAttributes().build());
        event.put(GigEntities.NEOMORPH.get(), NeomorphEntity.createAttributes().build());
        event.put(GigEntities.SPITTER.get(), SpitterEntity.createAttributes().build());
        event.put(GigEntities.DRACONICTEMPLEBEAST.get(), DraconicTempleBeastEntity.createAttributes().build());
        event.put(GigEntities.RAVENOUSTEMPLEBEAST.get(), RavenousTempleBeastEntity.createAttributes().build());
        event.put(GigEntities.MOONLIGHTHORRORTEMPLEBEAST.get(), MoonlightHorrorTempleBeastEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        GigVillagerTrades.addTrades();
    }

    record ModEntitySpawn(HolderSet<Biome> biomes, MobSpawnSettings.SpawnerData spawn) implements BiomeModifier {

        public static DeferredRegister<MapCodec<? extends BiomeModifier>> SERIALIZER = DeferredRegister.create(
                NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, CommonMod.MOD_ID);

        static Supplier<MapCodec<ModEntitySpawn>> JARJAR_SPAWN_CODEC = SERIALIZER.register("mobspawns",
                () -> RecordCodecBuilder.mapCodec(
                        builder -> builder.group(Biome.LIST_CODEC.fieldOf("biomes").forGetter(ModEntitySpawn::biomes),
                                MobSpawnSettings.SpawnerData.CODEC.fieldOf("spawn").forGetter(
                                        ModEntitySpawn::spawn)).apply(builder, ModEntitySpawn::new)));


        @Override
        public void modify(@NotNull Holder<Biome> biome, @NotNull Phase phase, ModifiableBiomeInfo.BiomeInfo.@NotNull Builder builder) {
            if (phase == Phase.ADD && this.biomes.contains(biome)) {
                builder.getMobSpawnSettings().addSpawn(MobCategory.MONSTER, this.spawn);
            }
        }

        @Override
        public @NotNull MapCodec<? extends BiomeModifier> codec() {
            return JARJAR_SPAWN_CODEC.get();
        }
    }
}
