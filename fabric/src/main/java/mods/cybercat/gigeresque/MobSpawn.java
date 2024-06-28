package mods.cybercat.gigeresque;

import mods.cybercat.gigeresque.common.entity.GigEntities;
import mods.cybercat.gigeresque.common.entity.impl.classic.AlienEggEntity;
import mods.cybercat.gigeresque.common.tags.GigTags;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;

public record MobSpawn() {

    public static void initialize() {
        BiomeModifications.addSpawn(BiomeSelectors.tag(
                GigTags.EGGSPAWN_BIOMES),
                MobCategory.MONSTER,
                GigEntities.EGG.get(),
                CommonMod.config.alienegg_spawn_weight,
                CommonMod.config.alienegg_min_group,
                CommonMod.config.alienegg_max_group);
        SpawnPlacements.register(GigEntities.EGG.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AlienEggEntity::checkMonsterSpawnRules);
    }
}
