package mods.cybercat.gigeresque.common.block;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

public class NestResinWebBlock extends Block {
    public static final BooleanProperty UP = PipeBlock.UP;
    public static final BooleanProperty NORTH = PipeBlock.NORTH;
    public static final BooleanProperty EAST = PipeBlock.EAST;
    public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
    public static final BooleanProperty WEST = PipeBlock.WEST;

    public static final Map<Direction, BooleanProperty> FACING_PROPERTIES = PipeBlock.PROPERTY_BY_DIRECTION.entrySet().stream().filter(entry -> entry.getKey() != Direction.DOWN).collect(Util.toMap());
    public static final EnumProperty<NestResinWebVariant> VARIANTS = EnumProperty.create("nest_resin_web_variant", NestResinWebVariant.class);

    private static final VoxelShape UP_SHAPE = box(0.0, 15.0, 0.0, 16.0, 16.0, 16.0);
    private static final VoxelShape EAST_SHAPE = box(0.0, 0.0, 0.0, 1.0, 16.0, 16.0);
    private static final VoxelShape WEST_SHAPE = box(15.0, 0.0, 0.0, 16.0, 16.0, 16.0);
    private static final VoxelShape SOUTH_SHAPE = box(0.0, 0.0, 0.0, 16.0, 16.0, 1.0);
    private static final VoxelShape NORTH_SHAPE = box(0.0, 0.0, 15.0, 16.0, 16.0, 16.0);
    private final Map<BlockState, VoxelShape> shapesByState;

    public NestResinWebBlock(Properties settings) {
        super(settings);
        registerDefaultState(getStateDefinition().any().setValue(UP, false).setValue(NORTH, false).setValue(EAST, false).setValue(SOUTH, false).setValue(WEST, false).setValue(VARIANTS, NestResinWebVariant.ONE));
        shapesByState = getStateDefinition().getPossibleStates().stream().collect(Collectors.toMap(Function.identity(), NestResinWebBlock::getShapeForState));
    }

    private static VoxelShape getShapeForState(BlockState state) {
        var voxelShape = Shapes.empty();
        if (state.getValue(UP))
            voxelShape = UP_SHAPE;
        if (state.getValue(NORTH))
            voxelShape = Shapes.or(voxelShape, SOUTH_SHAPE);
        if (state.getValue(SOUTH))
            voxelShape = Shapes.or(voxelShape, NORTH_SHAPE);
        if (state.getValue(EAST))
            voxelShape = Shapes.or(voxelShape, WEST_SHAPE);
        if (state.getValue(WEST))
            voxelShape = Shapes.or(voxelShape, EAST_SHAPE);
        return voxelShape.isEmpty() ? Shapes.block() : voxelShape;
    }

    public static boolean shouldConnectTo(LevelReader world, BlockPos pos, Direction direction) {
        var blockState = world.getBlockState(pos);
        return isFaceFull(blockState.getCollisionShape(world, pos), direction.getOpposite());
    }

    public static BooleanProperty getFacingProperty(Direction direction) {
        return FACING_PROPERTIES.get(direction);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return shapesByState.get(state);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter world, BlockPos pos) {
        return true;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return hasAdjacentBlocks(getPlacementShape(state, world, pos));
    }

    private boolean hasAdjacentBlocks(BlockState state) {
        return getAdjacentBlockCount(state) > 0;
    }

    private int getAdjacentBlockCount(BlockState state) {
        final int[] i = {0};
        FACING_PROPERTIES.values().forEach(it -> {
            if (state.getValue(it)) {
                i[0]++;
            }
        });
        return i[0];
    }

    private boolean shouldHaveSide(LevelReader world, BlockPos pos, Direction side) {
        if (side == Direction.DOWN)
            return false;
        else {
            var blockPos = pos.relative(side);
            if (shouldConnectTo(world, blockPos, side))
                return true;
            else if (side.getAxis() == Direction.Axis.Y)
                return false;
            else {
                var blockState = world.getBlockState(pos.above());
                return blockState.is(this) && blockState.getValue(FACING_PROPERTIES.get(side));
            }
        }
    }

    private BlockState getPlacementShape(BlockState state, LevelReader world, BlockPos pos) {
        var blockPos = pos.above();
        if (state.getValue(UP))
            state = state.setValue(UP, shouldConnectTo(world, blockPos, Direction.DOWN));
        BlockState blockState = null;
        var iterator = Direction.Plane.HORIZONTAL.iterator();
        while (true) {
            Direction direction;
            BooleanProperty booleanProperty;
            do {
                if (!iterator.hasNext()) {
                    return state;
                }
                direction = iterator.next();
                booleanProperty = getFacingProperty(direction);
            } while (!state.getValue(booleanProperty));
            var bl = shouldHaveSide(world, pos, direction);
            if (!bl) {
                if (blockState == null)
                    blockState = world.getBlockState(blockPos);
                bl = blockState.is(this) && blockState.getValue(booleanProperty);
            }
            state = state.setValue(booleanProperty, bl);
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        if (direction == Direction.DOWN)
            return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
        else {
            var blockState = getPlacementShape(state, world, pos);
            return !hasAdjacentBlocks(blockState) ? Blocks.AIR.defaultBlockState() : blockState;
        }
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        var blockState = context.getLevel().getBlockState(context.getClickedPos());
        return blockState.is(this) ? getAdjacentBlockCount(blockState) < FACING_PROPERTIES.size() : super.canBeReplaced(state, context);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        var blockState = ctx.getLevel().getBlockState(ctx.getClickedPos());
        var bl = blockState.is(this);
        var blockState2 = bl ? blockState : defaultBlockState();
        var placementDirections = ctx.getNearestLookingDirections();
        for (Direction direction : placementDirections) {
            if (direction != Direction.DOWN) {
                var booleanProperty = getFacingProperty(direction);
                var bl2 = bl && blockState.getValue(booleanProperty);
                if (!bl2 && shouldHaveSide(ctx.getLevel(), ctx.getClickedPos(), direction))
                    return blockState2.setValue(booleanProperty, true).setValue(VARIANTS, Arrays.stream(NestResinWebVariant.values()).toList().get(new Random().nextInt(NestResinWebVariant.values().length)));
            }
        }
        return bl ? blockState2 : null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(UP, NORTH, EAST, SOUTH, WEST, VARIANTS);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return switch (rotation) {
            case CLOCKWISE_180 -> state.setValue(NORTH, state.getValue(SOUTH)).setValue(EAST, state.getValue(WEST)).setValue(SOUTH, state.getValue(NORTH)).setValue(WEST, state.getValue(EAST));
            case COUNTERCLOCKWISE_90 -> state.setValue(NORTH, state.getValue(EAST)).setValue(EAST, state.getValue(SOUTH)).setValue(SOUTH, state.getValue(WEST)).setValue(WEST, state.getValue(NORTH));
            case CLOCKWISE_90 -> state.setValue(NORTH, state.getValue(WEST)).setValue(EAST, state.getValue(NORTH)).setValue(SOUTH, state.getValue(EAST)).setValue(WEST, state.getValue(SOUTH));
            default -> state;
        };
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return switch (mirror) {
            case LEFT_RIGHT -> state.setValue(NORTH, state.getValue(SOUTH)).setValue(SOUTH, state.getValue(NORTH));
            case FRONT_BACK -> state.setValue(EAST, state.getValue(WEST)).setValue(WEST, state.getValue(EAST));
            default -> super.mirror(state, mirror);
        };
    }
}
