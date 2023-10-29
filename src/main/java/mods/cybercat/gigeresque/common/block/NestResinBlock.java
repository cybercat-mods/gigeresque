package mods.cybercat.gigeresque.common.block;

import mods.cybercat.gigeresque.common.entity.AlienEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class NestResinBlock extends Block {
    public static final IntegerProperty LAYERS = BlockStateProperties.LAYERS;
    protected static final List<VoxelShape> ALIEN_LAYERS_TO_SHAPE = interpolateShapes(false, true);
    protected static final List<VoxelShape> LAYERS_TO_SHAPE = interpolateShapes(true, true);
    public NestResinBlock(Properties settings) {
        super(settings);

        registerDefaultState(getStateDefinition().any().setValue(BlockStateProperties.LAYERS, 1));
    }

    private static List<VoxelShape> interpolateShapes(boolean divide, boolean includeEmptyVoxelShape) {
        ArrayList<VoxelShape> list = new ArrayList<>();
        if (includeEmptyVoxelShape)
            list.add(Shapes.empty());
        for (var i = 0; i < 8; i++)
            list.add(box(0.0, 0.0, 0.0, 16.0, divide ? (i * 2.0) / 2.0 : i * 2.0, 16.0));
        return list;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter world, BlockPos pos, PathComputationType type) {
        return type == PathComputationType.LAND;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return ALIEN_LAYERS_TO_SHAPE.get(state.getValue(LAYERS));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return context instanceof EntityCollisionContext entitycollisioncontext && entitycollisioncontext.getEntity() instanceof AlienEntity ? ALIEN_LAYERS_TO_SHAPE.get(state.getValue(LAYERS)) : LAYERS_TO_SHAPE.get(state.getValue(LAYERS));
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState state, BlockGetter world, BlockPos pos) {
        return LAYERS_TO_SHAPE.get(state.getValue(LAYERS));
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return LAYERS_TO_SHAPE.get(state.getValue(LAYERS));
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        var blockState = world.getBlockState(pos.below());
        var isIce = blockState.is(Blocks.ICE);
        var isPackedIce = blockState.is(Blocks.PACKED_ICE);
        var isBarrier = blockState.is(Blocks.BARRIER);
        var isHoney = blockState.is(Blocks.HONEY_BLOCK);
        var isSoulSand = blockState.is(Blocks.SOUL_SAND);
        if (!isIce && !isPackedIce && !isBarrier) {
            if (!isHoney && !isSoulSand)
                return isFaceFull(blockState.getCollisionShape(world, pos.below()), Direction.UP) || blockState.is(this) && blockState.getValue(LAYERS) == 8;
            else
                return true;
        } else
            return false;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        return !state.canSurvive(world, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        var i = state.getValue(LAYERS);
        if (context.getItemInHand().is(asItem()) && i < 8)
            if (context.replacingClickedOnBlock())
                return context.getClickedFace() == Direction.UP;
            else
                return true;
        else
            return i == 1;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        var blockState = ctx.getLevel().getBlockState(ctx.getClickedPos());
        if (blockState.is(this))
            return blockState.setValue(LAYERS, Math.min(8, blockState.getValue(LAYERS) + 1));
        else
            return super.getStateForPlacement(ctx);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LAYERS);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
//		var test = RandomUtil.getRandomPositionWithinRange(blockPos, 3, 1, 3, false, serverLevel);
        if (serverLevel.getBlockState(blockPos).is(GigBlocks.NEST_RESIN))
            if (serverLevel.getBlockState(blockPos).getValue(LAYERS) < 8)
                serverLevel.setBlockAndUpdate(blockPos, blockState.setValue(LAYERS, Math.min(8, blockState.getValue(LAYERS) + 1)));
            else if (serverLevel.getBlockState(blockPos.above()).isAir() && serverLevel.getBlockState(blockPos.below()).isSolid())
                serverLevel.setBlockAndUpdate(blockPos.above(), GigBlocks.NEST_RESIN_WEB_CROSS.defaultBlockState());
//			else
//				for (var testPos : BlockPos.betweenClosed(test, test.above(1)))
//					if (serverLevel.getBlockState(testPos).isAir() && serverLevel.getBlockState(testPos.below()).isSolid())
//						if (serverLevel.getBlockState(testPos).getLightEmission() < 5)
//							if (randomSource.nextIntBetweenInclusive(0, 30) > 25)
//								NestBuildingHelper.tryBuildNestAround(serverLevel, testPos);
//							else
//								serverLevel.setBlockAndUpdate(testPos, (BlockState) blockState.setValue(LAYERS, Math.min(8, blockState.getValue(LAYERS) + 1)));
        super.randomTick(blockState, serverLevel, blockPos, randomSource);
    }
}
