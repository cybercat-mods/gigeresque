package mods.cybercat.gigeresque.common.block;

import mods.cybercat.gigeresque.client.particle.Particles;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.MathUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
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

public class AcidBlock extends FallingBlock implements SimpleWaterloggedBlock {
    private static final int MAX_THICKNESS = 4;
    public static final IntegerProperty THICKNESS = IntegerProperty.create("thickness", 0, MAX_THICKNESS);
    private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private int age = 0;

    AcidBlock(Properties settings) {
        super(settings);
        registerDefaultState(
                (getStateDefinition().any().setValue(WATERLOGGED, false)).setValue(THICKNESS, MAX_THICKNESS));
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

    @Deprecated(since = "1.20")
    @Override
    public void onPlace(@NotNull BlockState state, Level world, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean notify) {
        if (age > MathUtil.clamp(world.random.nextInt(2) + 1, 1, 52)) {
            world.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
            age = -1;
        }
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

    @Deprecated(since = "1.20")
    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Deprecated(since = "1.20")
    @Override
    public boolean isPathfindable(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull PathComputationType type) {
        return true;
    }

    @Deprecated(since = "1.20")
    @Override
    public @NotNull FluidState getFluidState(BlockState state) {
        return Boolean.TRUE.equals(state.getValue(StairBlock.WATERLOGGED)) ? Fluids.WATER.getSource(
                false) : super.getFluidState(state);
    }

    @Deprecated(since = "1.20")
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
    public void playerWillDestroy(@NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Player player) {
        // Stop Vanilla Stuff
    }

    private void setThickness(ServerLevel world, BlockPos pos, BlockState state, int consume) {
        var newState = state.setValue(THICKNESS, Math.max(getThickness(state) - consume, 0));

        if (world.getBlockState(pos).getBlock() == Blocks.WATER) newState = newState.setValue(WATERLOGGED, true);

        world.setBlockAndUpdate(pos, newState);
    }

    @Override
    public void playerDestroy(@NotNull Level world, @NotNull Player player, @NotNull BlockPos pos, @NotNull BlockState state, BlockEntity blockEntity, @NotNull ItemStack stack) {
        // Stop Vanilla Stuff
    }

    @Deprecated(since = "1.20")
    @Override
    public void spawnAfterBreak(@NotNull BlockState state, @NotNull ServerLevel world, @NotNull BlockPos pos, @NotNull ItemStack stack, boolean dropExperience) {
        // Stop Vanilla Stuff
    }

    @Override
    public void animateTick(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull RandomSource random) {
        for (var i = 0; i < (getThickness(state) * 2) + 1; i++)
            world.addAlwaysVisibleParticle(Particles.ACID, pos.getX() + random.nextDouble(),
                    pos.getY() + (Boolean.TRUE.equals(state.getValue(WATERLOGGED)) ? random.nextDouble() : 0.01),
                    pos.getZ() + random.nextDouble(), 0.0, 0.0, 0.0);
        if (random.nextInt(5 * ((MAX_THICKNESS + 1) - getThickness(state))) == 0)
            world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS,
                    0.2f + random.nextFloat() * 0.2f, 0.9f + random.nextFloat() * 0.15f, false);
    }

    @Deprecated(since = "1.20")
    @Override
    public void tick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        var currentThickness = getThickness(state);
        var canGrief = level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
        if (level.getBlockState(pos.above()).is(GigBlocks.ACID_BLOCK) && currentThickness < 4)
            this.setThickness(level, pos, state, currentThickness + 1);
        if (random.nextInt(8 - currentThickness) == 0 && currentThickness >= 1) {
            if (currentThickness < 4) setThickness(level, pos, state, Math.min(4, level.getRandom().nextInt(3) + 1));
            if (canGrief && !level.getBlockState(pos.below()).is(GigTags.ACID_RESISTANT)) {
                level.setBlock(pos.below(), Blocks.AIR.defaultBlockState(), 2);
                level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.LAVA_EXTINGUISH,
                        SoundSource.BLOCKS, 0.2f + random.nextFloat() * 0.2f, 0.9f + random.nextFloat() * 0.15f, false);
            }
        }

        super.tick(state, level, pos, random);
        scheduleTickIfNotScheduled(level, pos);
    }

    @Deprecated(since = "1.20")
    @Override
    public void randomTick(@NotNull BlockState state, @NotNull ServerLevel world, @NotNull BlockPos pos, @NotNull RandomSource random) {
        scheduleTickIfNotScheduled(world, pos);
    }

    @Deprecated(since = "1.20")
    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter view, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return Block.box(0, 0, 0, 16, 2, 16);
    }

    @Deprecated(since = "1.20")
    @Override
    public void attack(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull Player player) {
        // Stop Vanilla stuff
    }

    @Deprecated(since = "1.20")
    @Override
    public boolean canSurvive(@NotNull BlockState state, LevelReader world, @NotNull BlockPos pos) {
        return world.getBlockState(pos).isAir() && !world.getBlockState(pos).is(Blocks.LAVA);
    }

    @Deprecated(since = "1.20")
    @Override
    public void entityInside(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            if (livingEntity.hasEffect(GigStatusEffects.ACID)) return;
            if (livingEntity.getType().is(GigTags.ACID_RESISTANT_ENTITY)) return;
            var itemStack = livingEntity.getItemBySlot(EquipmentSlot.FEET);
            if (!(livingEntity instanceof Player) || (livingEntity instanceof Player playerEntity && !(playerEntity.isCreative() || playerEntity.isSpectator()))) {
                livingEntity.addEffect(new MobEffectInstance(GigStatusEffects.ACID, 60, this.getThickness(blockState)));
            }
            if (!itemStack.equals(ItemStack.EMPTY) && !itemStack.is(GigTags.ACID_RESISTANT_ITEMS)) {
                itemStack.setDamageValue(
                        itemStack.getDamageValue() + (level.getRandom().nextInt(2) * this.getThickness(blockState)));
            }
        }
        if (entity instanceof ItemEntity itemEntity && level.getRandom().nextInt(20) < 2) {
            if (itemEntity.getItem().is(GigTags.ACID_RESISTANT_ITEMS)) return;
            if (itemEntity.getItem().getMaxDamage() < 2) {
                itemEntity.getItem().shrink(1);
            } else {
                itemEntity.getItem().setDamageValue(
                        itemEntity.getItem().getDamageValue() + (level.getRandom().nextInt(2) * this.getThickness(
                                blockState)));
            }
        }
    }
}
