package mods.cybercat.gigeresque.common.fluid;

import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.block.SporeBlock;
import mods.cybercat.gigeresque.common.item.GigItems;
import mods.cybercat.gigeresque.common.tags.GigTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TallGrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;

public abstract class BlackFluid extends FlowingFluid {
    @Override
    public boolean isSame(@NotNull Fluid fluid) {
        return fluid == GigFluids.BLACK_FLUID_STILL.get() || fluid == GigFluids.BLACK_FLUID_FLOWING.get();
    }

    @Override
    public @NotNull Item getBucket() {
        return GigItems.BLACK_FLUID_BUCKET.get();
    }

    @Override
    public boolean canBeReplacedWith(@NotNull FluidState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull Fluid fluid, @NotNull Direction direction) {
        return false;
    }

    @Override
    public int getTickDelay(@NotNull LevelReader world) {
        return 20;
    }

    @Override
    protected float getExplosionResistance() {
        return 100.0f;
    }

    @Override
    protected @NotNull BlockState createLegacyBlock(@NotNull FluidState state) {
        return GigBlocks.BLACK_FLUID.get().defaultBlockState().setValue(BlockStateProperties.LEVEL, getLegacyLevel(state));
    }

    @Override
    public @NotNull Fluid getFlowing() {
        return GigFluids.BLACK_FLUID_FLOWING.get();
    }

    @Override
    public @NotNull Fluid getSource() {
        return GigFluids.BLACK_FLUID_STILL.get();
    }

    @Override
    protected void beforeDestroyingBlock(@NotNull LevelAccessor world, @NotNull BlockPos pos, BlockState state) {
        var blockEntity = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
        Block.dropResources(state, world, pos, blockEntity);
    }

    @Override
    protected int getSlopeFindDistance(@NotNull LevelReader world) {
        return 2;
    }

    @Override
    protected int getDropOff(@NotNull LevelReader world) {
        return 2;
    }

    @Override
    protected void randomTick(@NotNull Level level, @NotNull BlockPos blockPos, @NotNull FluidState fluidState, RandomSource randomSource) {
        if (randomSource.nextInt(50) > 40) {
            for (var x = -5; x <= 5; x++) {
                for (var y = -5; y <= 5; y++) {
                    for (var z = -5; z <= 5; z++) {
                        var targetPos = blockPos.offset(x, y, z);
                        // Ensure the position is within the loaded world chunk
                        if (!level.isLoaded(targetPos)) continue;
                        var targetBlockState = level.getBlockState(targetPos);
                        if (targetBlockState.is(GigTags.SPORE_REPLACE) && CommonMod.config.enableDevEntites) {
                            // Handle tall grass: replace bottom, clear top
                            if (targetBlockState.getBlock() instanceof TallGrassBlock) {
                                var belowPos = targetPos.below();
                                var abovePos = targetPos.above();
                                // Check if the current block is the bottom of tall grass
                                if (level.getBlockState(belowPos).getBlock() instanceof TallGrassBlock) {
                                    // Replace the bottom part with SPORE_BLOCK
                                    level.setBlockAndUpdate(targetPos, GigBlocks.SPORE_BLOCK.get().defaultBlockState());
                                    // Set the top part to air
                                    if (level.getBlockState(abovePos).getBlock() instanceof TallGrassBlock) {
                                        level.setBlockAndUpdate(abovePos, Blocks.AIR.defaultBlockState());
                                    }
                                } else {
                                    // If the current block is the top part, just replace it with air
                                    level.setBlockAndUpdate(targetPos, Blocks.AIR.defaultBlockState());
                                }
                            } else {
                                // Replace the current block with SPORE_BLOCK if it's not tall grass
                                level.setBlockAndUpdate(targetPos, GigBlocks.SPORE_BLOCK.get().defaultBlockState());
                            }
                            return; // Stop after one replacement
                        }
                    }
                }
            }
        }
    }

    @Override
    protected boolean isRandomlyTicking() {
        return true;
    }

    public static class Flowing extends BlackFluid {
        @Override
        public void createFluidStateDefinition(StateDefinition.@NotNull Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        @Override
        public int getAmount(FluidState fluidState) {
            return fluidState.getValue(LEVEL);
        }

        @Override
        public boolean isSource(@NotNull FluidState fluidState) {
            return false;
        }

        @Override
        protected boolean canConvertToSource(@NotNull Level level) {
            return false;
        }
    }

    public static class Still extends BlackFluid {

        @Override
        public int getAmount(@NotNull FluidState fluidState) {
            return 8;
        }

        @Override
        public boolean isSource(@NotNull FluidState fluidState) {
            return true;
        }

        @Override
        protected boolean canConvertToSource(@NotNull Level level) {
            return false;
        }
    }
}
