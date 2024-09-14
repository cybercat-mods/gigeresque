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
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import mods.cybercat.gigeresque.common.util.GigVillagerTrades;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
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
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, CommonMod.MOD_ID);
    public static final Supplier<FluidType> BLACKFLUID_TYPE = FLUID_TYPES.register("black_fluid_type", () -> new FluidType(FluidType.Properties.create()
            .descriptionId("block.gigeresque.black_fuild_block").canSwim(false)
            .canDrown(false).pathType(PathType.LAVA).adjacentPathType(null)
            .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_EMPTY)
            .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
            .lightLevel(15).density(3000).viscosity(6000)) {
        @Override
        public boolean canConvertToSource(@NotNull FluidState state, @NotNull LevelReader reader, @NotNull BlockPos pos) {
            if (reader instanceof Level level) {
                return level.getGameRules().getBoolean(GameRules.RULE_WATER_SOURCE_CONVERSION);
            } else {
                return super.canConvertToSource(state, reader, pos);
            }
        }

        @Override
        public double motionScale(@NotNull Entity entity) {
            return 0.0023333333333333335;
        }

        @Override
        public void setItemMovement(@NotNull ItemEntity entity) {
            Vec3 vec3 = entity.getDeltaMovement();
            entity.setDeltaMovement(vec3.x * 0.949999988079071, vec3.y + (vec3.y < 0.05999999865889549 ? 5.0E-4F : 0.0F), vec3.z * 0.949999988079071);
        }
    });

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
        FLUID_TYPES.register(modEventBus);
        NeoForge.EVENT_BUS.addListener(this::onServerStarted);
        NeoForge.EVENT_BUS.addListener(EventPriority.HIGH, this::onWorldTick);
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

    public void onServerStarted(final ServerStartedEvent event) {
        GigVillagerTrades.addTrades();
    }

    public void onWorldTick(final LevelTickEvent.Post event) {
        // Ensure we are on the server side
        if (event.getLevel().isClientSide) return;

        boolean hasAdvancement = false;

        // Check if any player has the advancement
        for (var player : event.getLevel().players()) {
            if (player instanceof ServerPlayer serverPlayer) {
                var advancement = serverPlayer.getServer().getAdvancements().get(Constants.modResource("xeno_dungeon"));

                if (advancement != null) {

                    if (serverPlayer.getAdvancements().getOrStartProgress(advancement).isDone()) {
                        hasAdvancement = true;
                        break;
                    }
                }
            }
        }

        // If at least one player has the advancement, apply the effect to all players
        if (hasAdvancement) {
            for (var player : event.getLevel().players()) {
                if (!player.hasEffect(GigStatusEffects.DUNGEON_EFFECT)) {
                    player.addEffect(new MobEffectInstance(GigStatusEffects.DUNGEON_EFFECT, -1, 1, true, false,false, null));
                }
            }
        }
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
