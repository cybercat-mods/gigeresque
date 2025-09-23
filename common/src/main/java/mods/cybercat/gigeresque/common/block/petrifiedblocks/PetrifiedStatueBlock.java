package mods.cybercat.gigeresque.common.block.petrifiedblocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.block.petrifiedblocks.entity.PetrifiedStatueEntity;
import mods.cybercat.gigeresque.common.entity.GigEntities;

public class PetrifiedStatueBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public static final BooleanProperty UNSAFE = BooleanProperty.create("unsafe");

    private static final VoxelShape OUTLINE_SHAPE = Block.box(0, 0, 0, 16, 16, 16);

    BlockPos[] blockPoss;

    public static final MapCodec<PetrifiedStatueBlock> CODEC = simpleCodec(PetrifiedStatueBlock::new);

    public PetrifiedStatueBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
            this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(UNSAFE, false)
        );
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, UNSAFE);
    }

    @Override
    public @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return GigEntities.PETRIFIED_STATUE.get().create(pos, state);
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public @NotNull VoxelShape getShape(
        @NotNull BlockState state,
        @NotNull BlockGetter world,
        @NotNull BlockPos pos,
        @NotNull CollisionContext context
    ) {
        return OUTLINE_SHAPE;
    }

    @Override
    public @NotNull BlockState playerWillDestroy(@NotNull Level world, @NotNull BlockPos pos, BlockState state, @NotNull Player player) {
        BlockPos.betweenClosed(pos, pos.relative(state.getValue(FACING), 1).above(2)).forEach(testPos -> {
            if (!testPos.equals(pos))
                world.destroyBlock(testPos, false);
        });
        return super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    public void setPlacedBy(
        @NotNull Level world,
        @NotNull BlockPos pos,
        BlockState state,
        LivingEntity placer,
        @NotNull ItemStack itemStack
    ) {
        BlockPos.betweenClosed(pos, pos.relative(state.getValue(FACING), 1).above(2)).forEach(testPos -> {
            if (!testPos.equals(pos))
                world.setBlock(testPos, GigBlocks.PETRIFIED_STATUE_BLOCK_INVIS.get().defaultBlockState(), Block.UPDATE_ALL);
        });
    }

    @Override
    public boolean canSurvive(BlockState state, @NotNull LevelReader world, @NotNull BlockPos pos) {
        for (BlockPos testPos : BlockPos.betweenClosed(pos, pos.relative(state.getValue(FACING), 1).above(2)))
            if (!testPos.equals(pos) && !world.getBlockState(testPos).isAir())
                return false;
        return true;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
        @NotNull Level level,
        @NotNull BlockState state,
        @NotNull BlockEntityType<T> type
    ) {
        return createTickerHelper(type, GigEntities.PETRIFIED_STATUE.get(), PetrifiedStatueEntity::tick);
    }
}
