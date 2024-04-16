package mods.cybercat.gigeresque.common.fluid;

import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.block.GigBlocks;
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
        return fluid == GigFluids.BLACK_FLUID_STILL || fluid == GigFluids.BLACK_FLUID_FLOWING;
    }

    @Override
    public @NotNull Item getBucket() {
        return GigItems.BLACK_FLUID_BUCKET;
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
        return GigBlocks.BLACK_FLUID.defaultBlockState().setValue(BlockStateProperties.LEVEL, getLegacyLevel(state));
    }

    @Override
    public @NotNull Fluid getFlowing() {
        return GigFluids.BLACK_FLUID_FLOWING;
    }

    @Override
    public @NotNull Fluid getSource() {
        return GigFluids.BLACK_FLUID_STILL;
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
        int i = randomSource.nextInt(50);
        if (i > 40) for (var j = 0; j < 10; ++j) {
            blockPos = blockPos.offset(randomSource.nextInt(3) - 1, 1, randomSource.nextInt(3) - 1);
            if (!level.isLoaded(blockPos.offset(randomSource.nextInt(10) - 1, 1, randomSource.nextInt(10) - 1))) return;
            if (this.isSporeReplaceable(level, blockPos)) {
                if (!this.hasSporeReplacements(level, blockPos)) continue;
                level.setBlockAndUpdate(blockPos, GigBlocks.SPORE_BLOCK.defaultBlockState());
                return;
            }
            if (!level.getBlockState(blockPos).blocksMotion()) return;
        }
    }

    private boolean hasSporeReplacements(LevelReader levelReader, BlockPos blockPos) {
        for (var direction : Direction.values()) {
            if (!this.isSporeReplaceable(levelReader, blockPos.relative(direction))) continue;
            return true;
        }
        return false;
    }

    private boolean isSporeReplaceable(LevelReader levelReader, BlockPos blockPos) {
        if (blockPos.getY() >= levelReader.getMinBuildHeight() && blockPos.getY() < levelReader.getMaxBuildHeight() && !levelReader.hasChunkAt(
                blockPos)) return false;
        return levelReader.getBlockState(blockPos).is(GigTags.SPORE_REPLACE) && Gigeresque.config.enableDevEntites;
    }

    @Override
    protected boolean isRandomlyTicking() {
        return true;
    }

    static class Flowing extends BlackFluid {
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

    static class Still extends BlackFluid {

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
