package com.bvanseg.gigeresque.common.block

import com.bvanseg.gigeresque.common.Gigeresque
import com.bvanseg.gigeresque.common.block.material.Materials
import com.bvanseg.gigeresque.common.fluid.Fluids
import com.bvanseg.gigeresque.common.item.group.ItemGroups
import com.bvanseg.gigeresque.common.util.GigeresqueInitializer
import com.bvanseg.gigeresque.common.util.initializingBlock
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry
import net.minecraft.block.*
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemGroup
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry


/**
 * @author Boston Vanseghi
 */
object Blocks : GigeresqueInitializer {

    /*
        FLUID BLOCKS
     */
    val BLACK_FLUID = object : FluidBlock(
        Fluids.BLACK_FLUID_STILL,
        Settings.of(Material.WATER).noCollision().strength(100.0F).dropsNothing()
    ) {}

    /*
        NORMAL BLOCKS
     */

    val ACID_BLOCK = AcidBlock(FabricBlockSettings.of(Materials.ACID).noCollision())

    val NEST_RESIN =
        NestResinBlock(FabricBlockSettings.of(Materials.NEST_RESIN).sounds(BlockSoundGroup.HONEY).strength(5.0f, 8.0f))
    val NEST_RESIN_BLOCK =
        Block(FabricBlockSettings.of(Materials.NEST_RESIN).sounds(BlockSoundGroup.HONEY).strength(5.0f, 8.0f))
    val NEST_RESIN_WEB =
        NestResinWebBlock(
            FabricBlockSettings.of(Materials.NEST_RESIN).sounds(BlockSoundGroup.HONEY).noCollision()
                .strength(5.0f, 8.0f)
        )
    val NEST_RESIN_WEB_CROSS =
        NestResinWebFullBlock(
            FabricBlockSettings.of(Materials.NEST_RESIN_WEB).sounds(BlockSoundGroup.HONEY).noCollision().nonOpaque()
                .requiresTool().strength(5.0f, 8.0f)
        )

    private val ORGANIC_ALIEN_BLOCK = Block(
        FabricBlockSettings.of(Materials.ORGANIC_ALIEN_BLOCK)
            .strength(3.0F, 6.0F).sounds(BlockSoundGroup.NETHERRACK)
    )
    private val RESINOUS_ALIEN_BLOCK = Block(
        FabricBlockSettings.of(Materials.ROUGH_ALIEN_BLOCK).requiresTool()
            .strength(3.0F, 6.0F).sounds(BlockSoundGroup.DEEPSLATE)
    )
    private val RIBBED_ALIEN_BLOCK = Block(
        FabricBlockSettings.of(Materials.ROUGH_ALIEN_BLOCK).requiresTool()
            .strength(3.0F, 6.0F).sounds(BlockSoundGroup.DEEPSLATE)
    )
    private val ROUGH_ALIEN_BLOCK = Block(
        FabricBlockSettings.of(Materials.ROUGH_ALIEN_BLOCK).requiresTool()
            .strength(3.0F, 6.0F).sounds(BlockSoundGroup.DEEPSLATE)
    )
    private val SINOUS_ALIEN_BLOCK = Block(
        FabricBlockSettings.of(Materials.SINOUS_ALIEN_BLOCK).requiresTool()
            .strength(3.0F, 6.0F).sounds(BlockSoundGroup.DEEPSLATE)
    )

    /*
        PILLARS
     */
    private val RESINOUS_ALIEN_PILLAR = PillarBlock(AbstractBlock.Settings.copy(ROUGH_ALIEN_BLOCK))
    private val RIBBED_ALIEN_PILLAR = PillarBlock(AbstractBlock.Settings.copy(ROUGH_ALIEN_BLOCK))
    private val SMOOTH_ALIEN_PILLAR = PillarBlock(AbstractBlock.Settings.copy(SINOUS_ALIEN_BLOCK))

    /*
        SLABS
     */
    private val ORGANIC_ALIEN_SLAB = SlabBlock(AbstractBlock.Settings.copy(ORGANIC_ALIEN_BLOCK))
    private val RESINOUS_ALIEN_SLAB = SlabBlock(AbstractBlock.Settings.copy(RESINOUS_ALIEN_BLOCK))
    private val RIBBED_ALIEN_SLAB = SlabBlock(AbstractBlock.Settings.copy(RIBBED_ALIEN_BLOCK))
    private val ROUGH_ALIEN_SLAB = SlabBlock(AbstractBlock.Settings.copy(ROUGH_ALIEN_BLOCK))
    private val SINOUS_ALIEN_SLAB = SlabBlock(AbstractBlock.Settings.copy(SINOUS_ALIEN_BLOCK))

    /*
        STAIRS
     */
    private val ORGANIC_ALIEN_STAIRS = CustomStairsBlock(ORGANIC_ALIEN_BLOCK)
    private val RESINOUS_ALIEN_STAIRS = CustomStairsBlock(RESINOUS_ALIEN_BLOCK)
    private val RIBBED_ALIEN_STAIRS = CustomStairsBlock(RIBBED_ALIEN_PILLAR)
    private val ROUGH_ALIEN_STAIRS = CustomStairsBlock(ROUGH_ALIEN_BLOCK)
    private val SINOUS_ALIEN_STAIRS = CustomStairsBlock(SINOUS_ALIEN_BLOCK)
    private val SMOOTH_ALIEN_STAIRS = CustomStairsBlock(SMOOTH_ALIEN_PILLAR)

    /*
        WALLS
     */
    private val ORGANIC_ALIEN_WALL = WallBlock(AbstractBlock.Settings.copy(ORGANIC_ALIEN_BLOCK))
    private val RESINOUS_ALIEN_WALL = WallBlock(AbstractBlock.Settings.copy(RESINOUS_ALIEN_BLOCK))
    private val RIBBED_ALIEN_WALL = WallBlock(AbstractBlock.Settings.copy(RIBBED_ALIEN_BLOCK))
    private val ROUGH_ALIEN_WALL = WallBlock(AbstractBlock.Settings.copy(ROUGH_ALIEN_BLOCK))
    private val SINOUS_ALIEN_WALL = WallBlock(AbstractBlock.Settings.copy(SINOUS_ALIEN_BLOCK))

    private fun registerItemBlock(
        path: String,
        block: Block,
        settings: FabricItemSettings = FabricItemSettings().group(ItemGroup.MISC)
    ) {
        Registry.register(Registry.BLOCK, Identifier(Gigeresque.MOD_ID, path), block)
        Registry.register(
            Registry.ITEM,
            Identifier(Gigeresque.MOD_ID, path),
            BlockItem(block, settings.group(ItemGroups.GENERAL))
        )
    }

    override fun initialize() = initializingBlock("Blocks") {
        Registry.register(Registry.BLOCK, Identifier(Gigeresque.MOD_ID, "black_fluid"), BLACK_FLUID)

        registerItemBlock("nest_resin", NEST_RESIN)
        registerItemBlock("nest_resin_block", NEST_RESIN_BLOCK)

        registerItemBlock("nest_resin_web", NEST_RESIN_WEB)
        registerItemBlock("nest_resin_web_cross", NEST_RESIN_WEB_CROSS)

        // Flammability
        FlammableBlockRegistry.getDefaultInstance().add(NEST_RESIN, 5, 5)
        FlammableBlockRegistry.getDefaultInstance().add(NEST_RESIN_BLOCK, 5, 5)
        FlammableBlockRegistry.getDefaultInstance().add(NEST_RESIN_WEB, 5, 5)
        FlammableBlockRegistry.getDefaultInstance().add(NEST_RESIN_WEB_CROSS, 5, 5)

        Registry.register(Registry.BLOCK, Identifier(Gigeresque.MOD_ID, "acid_block"), ACID_BLOCK)

        registerItemBlock("organic_alien_block", ORGANIC_ALIEN_BLOCK)
        registerItemBlock("resinous_alien_block", RESINOUS_ALIEN_BLOCK)
        registerItemBlock("ribbed_alien_block", RIBBED_ALIEN_BLOCK)
        registerItemBlock("rough_alien_block", ROUGH_ALIEN_BLOCK)
        registerItemBlock("sinous_alien_block", SINOUS_ALIEN_BLOCK)

        registerItemBlock("resinous_alien_pillar", RESINOUS_ALIEN_PILLAR)
        registerItemBlock("ribbed_alien_pillar", RIBBED_ALIEN_PILLAR)
        registerItemBlock("smooth_alien_pillar", SMOOTH_ALIEN_PILLAR)

        registerItemBlock("organic_alien_slab", ORGANIC_ALIEN_SLAB)
        registerItemBlock("resinous_alien_slab", RESINOUS_ALIEN_SLAB)
        registerItemBlock("ribbed_alien_slab", RIBBED_ALIEN_SLAB)
        registerItemBlock("rough_alien_slab", ROUGH_ALIEN_SLAB)
        registerItemBlock("sinous_alien_slab", SINOUS_ALIEN_SLAB)

        registerItemBlock("organic_alien_stairs", ORGANIC_ALIEN_STAIRS)
        registerItemBlock("resinous_alien_stairs", RESINOUS_ALIEN_STAIRS)
        registerItemBlock("ribbed_alien_stairs", RIBBED_ALIEN_STAIRS)
        registerItemBlock("rough_alien_stairs", ROUGH_ALIEN_STAIRS)
        registerItemBlock("smooth_alien_stairs", SMOOTH_ALIEN_STAIRS)
        registerItemBlock("sinous_alien_stairs", SINOUS_ALIEN_STAIRS)

        registerItemBlock("organic_alien_wall", ORGANIC_ALIEN_WALL)
        registerItemBlock("resinous_alien_wall", RESINOUS_ALIEN_WALL)
        registerItemBlock("ribbed_alien_wall", RIBBED_ALIEN_WALL)
        registerItemBlock("rough_alien_wall", ROUGH_ALIEN_WALL)
        registerItemBlock("sinous_alien_wall", SINOUS_ALIEN_WALL)
    }
}