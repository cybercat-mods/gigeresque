package mods.cybercat.gigeresque.common.block.petrifiedblocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.block.petrifiedblocks.entity.PetrifiedOjbect2Entity;
import mods.cybercat.gigeresque.common.block.storage.StorageProperties;
import mods.cybercat.gigeresque.common.block.storage.StorageStates;
import mods.cybercat.gigeresque.common.entity.GigEntities;

public class PetrifiedObjectBlock2 extends BaseEntityBlock {

    public static final IntegerProperty HATCH = BlockStateProperties.AGE_25;

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public static final EnumProperty<StorageStates> STORAGE_STATE = StorageProperties.STORAGE_STATE;

    public static final MapCodec<PetrifiedObjectBlock2> CODEC = simpleCodec(PetrifiedObjectBlock2::new);

    public PetrifiedObjectBlock2(Properties properties) {
        super(properties);
        this.registerDefaultState(
            this.stateDefinition.any().setValue(HATCH, 0).setValue(STORAGE_STATE, StorageStates.CLOSED).setValue(FACING, Direction.SOUTH)
        );
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(
        @NotNull ItemStack stack,
        @NotNull BlockState state,
        Level level,
        @NotNull BlockPos pos,
        @NotNull Player player,
        @NotNull InteractionHand hand,
        @NotNull BlockHitResult hitResult
    ) {
        if (level.isClientSide())
            level.playSound(null, pos, SoundEvents.STONE_HIT, SoundSource.BLOCKS, 0.3f, 0.9f + level.getRandom().nextFloat() * 0.2f);
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    public void stepOn(Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Entity entity) {
        if (level.isClientSide())
            level.playSound(null, pos, SoundEvents.STONE_STEP, SoundSource.BLOCKS, 0.3f, 0.9f + level.getRandom().nextFloat() * 0.2f);
        super.stepOn(level, pos, state, entity);
    }

    @Override
    public void playerDestroy(
        @NotNull Level level,
        @NotNull Player player,
        @NotNull BlockPos pos,
        @NotNull BlockState state,
        @Nullable BlockEntity blockEntity,
        @NotNull ItemStack itemStack
    ) {
        if (state.getValue(STORAGE_STATE) == StorageStates.OPENED) {
            player.awardStat(Stats.BLOCK_MINED.get(this));
            player.causeFoodExhaustion(0.005F);
            if (level instanceof ServerLevel serverLevel) {
                var radius = (2 - 1) / 2;
                for (int i = 0; i < 2; i++) {
                    int x = serverLevel.getRandom().nextInt(2) - radius;
                    int z = serverLevel.getRandom().nextInt(2) - radius;
                    var acidEntity = GigEntities.ACID.get().create(serverLevel);
                    assert acidEntity != null;
                    acidEntity.moveTo(pos.offset(x, 0, z), 0, 0);
                    serverLevel.addFreshEntity(acidEntity);
                }
            }
            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        } else {
            dropResources(state, level, pos);
            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        }
    }

    public static void dropResources(@NotNull BlockState state, Level level, @NotNull BlockPos pos) {
        if (level instanceof ServerLevel serverLevel) {
            var d = EntityType.ITEM.getHeight() / 2.0;
            var x = pos.getX() + 0.5 + Mth.nextDouble(level.random, -0.25, 0.25);
            var y = pos.getY() + 0.5 + Mth.nextDouble(level.random, -0.25, 0.25) - d;
            var z = pos.getZ() + 0.5 + Mth.nextDouble(level.random, -0.25, 0.25);
            var itemEntity = new ItemEntity(
                level,
                x,
                y,
                z,
                GigBlocks.PETRIFIED_OBJECT_2_BLOCK.get().asItem().getDefaultInstance()
            );
            itemEntity.setDefaultPickUpDelay();
            level.addFreshEntity(itemEntity);
            state.spawnAfterBreak(serverLevel, pos, ItemStack.EMPTY, false);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HATCH, STORAGE_STATE, FACING);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return GigEntities.PETRIFIED_OBJECT_2.get().create(pos, state);
    }

    @Override
    public @NotNull VoxelShape getShape(
        @NotNull BlockState state,
        @NotNull BlockGetter world,
        @NotNull BlockPos pos,
        @NotNull CollisionContext context
    ) {
        return Block.box(4, 0, 5, 12, 2, 13);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
        @NotNull Level level,
        @NotNull BlockState state,
        @NotNull BlockEntityType<T> type
    ) {
        return createTickerHelper(type, GigEntities.PETRIFIED_OBJECT_2.get(), PetrifiedOjbect2Entity::tick);
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        if (context.getClickedFace() == Direction.UP || context.getClickedFace() == Direction.DOWN) {
            return this.defaultBlockState().setValue(FACING, Direction.SOUTH);
        }
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite().getOpposite());
    }
}
