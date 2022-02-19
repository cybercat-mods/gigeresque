package com.bvanseg.gigeresque.common.config

import com.bvanseg.gigeresque.ConstantsJava
import com.bvanseg.gigeresque.common.GigeresqueJava
import me.shedaniel.autoconfig.ConfigData
import me.shedaniel.autoconfig.annotation.Config
import me.shedaniel.autoconfig.annotation.ConfigEntry
import net.minecraft.block.Blocks
import net.minecraft.entity.EntityType
import net.minecraft.util.registry.Registry

/**
 * @author Boston Vanseghi
 */
@Config(name = GigeresqueJava.MOD_ID)
class GigeresqueConfig : ConfigData {

    class Features {
        @ConfigEntry.Gui.Tooltip(count = 3)
        val isolationMode: Boolean = false

        @ConfigEntry.Gui.Tooltip(count = 3)
        val surgeryKit: Boolean = false
    }

    class Miscellaneous {
        @ConfigEntry.Gui.Tooltip(count = 1)
        val acidResistantBlocks = listOf(
            "gigeresque:nest_resin",
            "gigeresque:nest_resin_block",
            "gigeresque:nest_resin_web",
            "gigeresque:nest_resin_web_cross",
            "gigeresque:organic_alien_block",
            "gigeresque:organic_alien_slab",
            "gigeresque:organic_alien_stairs",
            "gigeresque:organic_alien_wall",
            "gigeresque:resinous_alien_block",
            "gigeresque:resinous_alien_pillar",
            "gigeresque:resinous_alien_slab",
            "gigeresque:resinous_alien_stairs",
            "gigeresque:resinous_alien_wall",
            "gigeresque:ribbed_alien_block",
            "gigeresque:ribbed_alien_pillar",
            "gigeresque:ribbed_alien_slab",
            "gigeresque:ribbed_alien_stairs",
            "gigeresque:ribbed_alien_wall",
            "gigeresque:rough_alien_block",
            "gigeresque:rough_alien_slab",
            "gigeresque:rough_alien_stairs",
            "gigeresque:rough_alien_wall",
            "gigeresque:sinous_alien_block",
            "gigeresque:sinous_alien_slab",
            "gigeresque:sinous_alien_stairs",
            "gigeresque:sinous_alien_wall",
            "gigeresque:smooth_alien_pillar",
            "gigeresque:smooth_alien_stairs",
            Registry.BLOCK.getId(Blocks.BEACON).toString(),
            Registry.BLOCK.getId(Blocks.BEDROCK).toString(),
            Registry.BLOCK.getId(Blocks.COAL_BLOCK).toString(),
            Registry.BLOCK.getId(Blocks.CONDUIT).toString(),
            Registry.BLOCK.getId(Blocks.CRYING_OBSIDIAN).toString(),
            Registry.BLOCK.getId(Blocks.DIAMOND_BLOCK).toString(),
            Registry.BLOCK.getId(Blocks.DRAGON_EGG).toString(),
            Registry.BLOCK.getId(Blocks.EMERALD_BLOCK).toString(),
            Registry.BLOCK.getId(Blocks.END_PORTAL).toString(),
            Registry.BLOCK.getId(Blocks.END_PORTAL_FRAME).toString(),
            Registry.BLOCK.getId(Blocks.GOLD_BLOCK).toString(),
            Registry.BLOCK.getId(Blocks.OBSIDIAN).toString(),
            Registry.BLOCK.getId(Blocks.NETHER_PORTAL).toString(),
            Registry.BLOCK.getId(Blocks.NETHERITE_BLOCK).toString(),
            Registry.BLOCK.getId(Blocks.RESPAWN_ANCHOR).toString(),
        )

        @ConfigEntry.Gui.Tooltip(count = 1)
        var alienGrowthMultiplier = 1.0f

        @ConfigEntry.Gui.Tooltip(count = 2)
        var alienSpawnCap = ConstantsJava.ALIEN_SPAWN_CAP

        @ConfigEntry.Gui.Tooltip(count = 1)
        var aquaticAlienGrowthMultiplier = 1.0f

        @ConfigEntry.Gui.Tooltip(count = 1)
        var aquaticChestbursterGrowthMultiplier = 1.0f

        @ConfigEntry.Gui.Tooltip(count = 1)
        var chestbursterGrowthMultiplier = 1.0f

        @ConfigEntry.Gui.Tooltip(count = 2)
        var eggmorphTickMultiplier = 1.0f

        @ConfigEntry.Gui.Tooltip(count = 2)
        var facehuggerAttachTickMultiplier = 1.0f

        @ConfigEntry.Gui.Tooltip(count = 2)
        var impregnationTickMultiplier = 1.0f

        @ConfigEntry.Gui.Tooltip(count = 1)
        var runnerAlienGrowthMultiplier = 1.0f

        @ConfigEntry.Gui.Tooltip(count = 1)
        var runnerbursterGrowthMultiplier = 1.0f
    }

    class Morphing {

        @ConfigEntry.Gui.Tooltip(count = 1)
        val alienHosts = listOf(
            Registry.ENTITY_TYPE.getId(EntityType.EVOKER).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.ILLUSIONER).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.PILLAGER).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.PIGLIN).toString(), // TODO: Remove when hellmorphs added
            Registry.ENTITY_TYPE.getId(EntityType.PIGLIN_BRUTE).toString(), // TODO: Remove when hellmorphs added
            Registry.ENTITY_TYPE.getId(EntityType.PLAYER).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.WITCH).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.VILLAGER).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.VINDICATOR).toString()
        )

        @ConfigEntry.Gui.Tooltip(count = 1)
        val aquaticAlienHosts = listOf(
            Registry.ENTITY_TYPE.getId(EntityType.DOLPHIN).toString()
        )

        @ConfigEntry.Gui.Tooltip(count = 1)
        val runnerHosts = listOf(
            Registry.ENTITY_TYPE.getId(EntityType.COW).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.DONKEY).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.FOX).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.GOAT).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.HOGLIN).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.HORSE).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.LLAMA).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.MOOSHROOM).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.MULE).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.PANDA).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.PIG).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.POLAR_BEAR).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.RAVAGER).toString(), // TODO: Remove when ubermorphs added
            Registry.ENTITY_TYPE.getId(EntityType.SHEEP).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.WOLF).toString(),
        )
    }

    class Targeting {

        @ConfigEntry.Gui.Tooltip(count = 1)
        val alienWhitelist = listOf("")

        @ConfigEntry.Gui.Tooltip(count = 1)
        val alienBlacklist = listOf(
            Registry.ENTITY_TYPE.getId(EntityType.BAT).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.BEE).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.CAT).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.CREEPER).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.PARROT).toString()
        )

        @ConfigEntry.Gui.Tooltip(count = 1)
        val aquaticAlienWhitelist = listOf("")

        @ConfigEntry.Gui.Tooltip(count = 1)
        val aquaticAlienBlacklist = listOf(
            Registry.ENTITY_TYPE.getId(EntityType.BAT).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.BEE).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.CAT).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.CREEPER).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.PARROT).toString()
        )

        @ConfigEntry.Gui.Tooltip(count = 1)
        val facehuggerWhitelist = listOf("")

        @ConfigEntry.Gui.Tooltip(count = 1)
        val facehuggerBlacklist = listOf(
            Registry.ENTITY_TYPE.getId(EntityType.BLAZE).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.CAVE_SPIDER).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.CHICKEN).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.COD).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.ENDER_DRAGON).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.ENDERMAN).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.ENDERMITE).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.GHAST).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.GLOW_SQUID).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.IRON_GOLEM).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.MAGMA_CUBE).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.PARROT).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.PUFFERFISH).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.RABBIT).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.SALMON).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.SLIME).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.SILVERFISH).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.SNOW_GOLEM).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.SPIDER).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.SQUID).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.TROPICAL_FISH).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.VEX).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.WITHER).toString()
        )

        @ConfigEntry.Gui.Tooltip(count = 1)
        val runnerWhitelist = listOf("")

        @ConfigEntry.Gui.Tooltip(count = 1)
        val runnerBlacklist = listOf(
            Registry.ENTITY_TYPE.getId(EntityType.BAT).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.BEE).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.CAT).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.CREEPER).toString(),
            Registry.ENTITY_TYPE.getId(EntityType.PARROT).toString()
        )
    }

    @ConfigEntry.Gui.CollapsibleObject
    val features = Features()

    @ConfigEntry.Gui.CollapsibleObject
    val miscellaneous = Miscellaneous()

    @ConfigEntry.Gui.CollapsibleObject
    val morphing = Morphing()

    @ConfigEntry.Gui.CollapsibleObject
    val targeting = Targeting()
}