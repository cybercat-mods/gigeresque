package mods.cybercat.gigeresque.common.block;

import com.mojang.serialization.MapCodec;
import mods.cybercat.gigeresque.client.particle.Particles;
import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import mods.cybercat.gigeresque.common.tags.GigTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("ALL")
public class BlackFluidBlock extends FallingBlock implements SimpleWaterloggedBlock {
    private static final int MAX_THICKNESS = 4;
    public static final IntegerProperty THICKNESS = IntegerProperty.create("thickness", 0, MAX_THICKNESS);
    private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected int age = 0;

    BlackFluidBlock(Properties settings) {
        super(settings);
        registerDefaultState(
                (getStateDefinition().any().setValue(WATERLOGGED, false)).setValue(THICKNESS, MAX_THICKNESS));
    }

    @Override
    protected MapCodec<? extends FallingBlock> codec() {
        return null;
    }

    public static boolean canFallThrough(BlockState state) {
        return (state.isAir() || state.is(BlockTags.FIRE)) && !state.liquid() && !state.is(
                GigTags.ACID_RESISTANT) && !state.is(GigBlocks.ACID_BLOCK);
    }

    private void scheduleTickIfNotScheduled(Level world, BlockPos pos) {
        if (!world.isClientSide && !world.getBlockTicks().hasScheduledTick(pos, this)) {
            age++;
            world.scheduleTick(pos, this, 40);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(THICKNESS, WATERLOGGED);
    }

    @Override
    public void onPlace(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean notify) {
        scheduleTickIfNotScheduled(world, pos);
    }

    @Override
    public boolean isRandomlyTicking(@NotNull BlockState state) {
        return true;
    }

    @Override
    public float getExplosionResistance() {
        return 100.0f;
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public boolean isPathfindable(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull PathComputationType type) {
        return true;
    }

    @Override
    public @NotNull FluidState getFluidState(BlockState state) {
        return Boolean.TRUE.equals(state.getValue(StairBlock.WATERLOGGED)) ? Fluids.WATER.getSource(
                false) : super.getFluidState(state);
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction, @NotNull BlockState neighborState, LevelAccessor world, @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
        if (!world.isClientSide() && Boolean.TRUE.equals(state.getValue(WATERLOGGED)))
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }

    private int getThickness(BlockState state) {
        return state.getValue(THICKNESS);
    }

    @Override
    public BlockState playerWillDestroy(@NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Player player) {
        return null;
    }

    protected void setThickness(ServerLevel world, BlockPos pos, BlockState state) {
        setThickness(world, pos, state, 1);
    }

    private void setThickness(ServerLevel world, BlockPos pos, BlockState state, int consume) {
        var newState = state.setValue(THICKNESS, Math.max(getThickness(state) - consume, 0));

        if (world.getBlockState(pos).getBlock() == Blocks.WATER) newState = newState.setValue(WATERLOGGED, true);

        world.setBlockAndUpdate(pos, newState);
    }

    @Override
    public void playerDestroy(@NotNull Level world, @NotNull Player player, @NotNull BlockPos pos, @NotNull BlockState state, BlockEntity blockEntity, @NotNull ItemStack stack) {
    }

    @Override
    public void spawnAfterBreak(@NotNull BlockState state, @NotNull ServerLevel world, @NotNull BlockPos pos, @NotNull ItemStack stack, boolean dropExperience) {
    }

    @Override
    public void animateTick(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull RandomSource random) {
        for (var i = 0; i < (getThickness(state) * 2) + 1; i++)
            world.addAlwaysVisibleParticle(Particles.GOO, pos.getX() + random.nextDouble(),
                    pos.getY() + (Boolean.TRUE.equals(state.getValue(WATERLOGGED)) ? random.nextDouble() : 0.01),
                    pos.getZ() + random.nextDouble(), 0.0, 0.0, 0.0);
        if (random.nextInt(5 * ((MAX_THICKNESS + 1) - getThickness(state))) == 0)
            world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.SCULK_BLOCK_SPREAD, SoundSource.BLOCKS,
                    0.2f + random.nextFloat() * 0.2f, 0.9f + random.nextFloat() * 0.15f, false);
    }

    @Override
    public void tick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, RandomSource random) {
        var currentThickness = getThickness(state);
        if (level.getBlockState(pos.above()).is(GigBlocks.BLACK_FLUID_BLOCK) && currentThickness < 4)
            this.setThickness(level, pos, state, currentThickness + 1);
        if (random.nextInt(8 - currentThickness) == 0 && currentThickness < 4)
            setThickness(level, pos, state, Math.min(4, level.getRandom().nextInt(3) + 1));
        if (this.age > 600) level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        super.tick(state, level, pos, random);
        scheduleTickIfNotScheduled(level, pos);
    }

    @Override
    public void randomTick(@NotNull BlockState state, @NotNull ServerLevel world, @NotNull BlockPos pos, @NotNull RandomSource random) {
        scheduleTickIfNotScheduled(world, pos);
    }

    @Override
    public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter view, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return Block.box(0, 0, 0, 16, 2, 16);
    }

    @Override
    public void attack(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull Player player) {
    }

    @Override
    public boolean canSurvive(@NotNull BlockState state, LevelReader world, @NotNull BlockPos pos) {
        return world.getBlockState(pos).isAir();
    }

    @Override
    public void entityInside(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, Entity entity) {
        if (entity.isAlive() && entity instanceof LivingEntity livingEntity) {
            if (livingEntity.hasEffect(GigStatusEffects.DNA)) return;
            if (livingEntity.getType().is(GigTags.DNAIMMUNE)) return;
            if (!(livingEntity instanceof Player) || (livingEntity instanceof Player playerEntity && !(playerEntity.isCreative() || playerEntity.isSpectator()))) {
                livingEntity.addEffect(
                        new MobEffectInstance(GigStatusEffects.DNA, Gigeresque.config.gooEffectTickTimer / 2, 0));
            }
        }
    }
}
