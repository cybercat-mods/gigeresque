package mods.cybercat.gigeresque.common.block.storage;

import com.mojang.serialization.MapCodec;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.block.entity.IdolStorageEntity;
import mods.cybercat.gigeresque.common.entity.GigEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class SittingIdolBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<StorageStates> STORAGE_STATE = StorageProperties.STORAGE_STATE;
    private static final VoxelShape OUTLINE_SHAPE = Block.box(0, 0, 0, 16, 16, 16);
    BlockPos[] blockPoss;
    public static final MapCodec<SittingIdolBlock> CODEC = simpleCodec(SittingIdolBlock::new);

    public SittingIdolBlock(Properties properties) {
        super(Properties.of().sound(SoundType.DRIPSTONE_BLOCK).strength(5.0f, 8.0f).noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(STORAGE_STATE, StorageStates.CLOSED));
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, STORAGE_STATE);
    }

    @Override
    public @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof IdolStorageEntity idolStorageEntity)
            player.openMenu(idolStorageEntity);
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return GigEntities.ALIEN_STORAGE_BLOCK_ENTITY_3.get().create(pos, state);
    }

    @Override
    public int getAnalogOutputSignal(@NotNull BlockState state, Level world, @NotNull BlockPos pos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(world.getBlockEntity(pos));
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return OUTLINE_SHAPE;
    }

    @Override
    public @NotNull BlockState playerWillDestroy(@NotNull Level world, @NotNull BlockPos pos, BlockState state, @NotNull Player player) {
        BlockPos.betweenClosed(pos, pos.relative(state.getValue(FACING), 2).above(2)).forEach(testPos -> {
            if (!testPos.equals(pos))
                world.destroyBlock(testPos, false);
        });
        super.playerWillDestroy(world, pos, state, player);
        return super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    public void setPlacedBy(@NotNull Level world, @NotNull BlockPos pos, BlockState state, LivingEntity placer, @NotNull ItemStack itemStack) {
        BlockPos.betweenClosed(pos, pos.relative(state.getValue(FACING), 2).above(2)).forEach(testPos -> {
            if (!testPos.equals(pos))
                world.setBlock(testPos, GigBlocks.ALIEN_STORAGE_BLOCK_INVIS2.get().defaultBlockState(), Block.UPDATE_ALL);
        });
    }

    @Override
    public boolean canSurvive(BlockState state, @NotNull LevelReader world, @NotNull BlockPos pos) {
        for (BlockPos testPos : BlockPos.betweenClosed(pos, pos.relative(state.getValue(FACING), 2).above(2)))
            if (!testPos.equals(pos) && !world.getBlockState(testPos).isAir())
                return false;
        return true;
    }

    @Override
    public void tick(@NotNull BlockState state, ServerLevel world, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (world.getBlockEntity(pos) instanceof IdolStorageEntity idolStorageEntity)
            idolStorageEntity.tick();
    }

    @Override
    public void onRemove(BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (blockState.is(blockState2.getBlock()))
            return;
        var blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof Container container) {
            Containers.dropContents(level, blockPos, container);
            level.updateNeighbourForOutputSignal(blockPos, this);
        }
        super.onRemove(blockState, level, blockPos, blockState2, bl);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return createTickerHelper(type, GigEntities.ALIEN_STORAGE_BLOCK_ENTITY_3.get(), IdolStorageEntity::tick);
    }
}