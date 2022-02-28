package mods.cybercat.gigeresque.common.block;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ConnectingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class NestResinWebBlock extends Block {
	public static final BooleanProperty UP = ConnectingBlock.UP;
	public static final BooleanProperty NORTH = ConnectingBlock.NORTH;
	public static final BooleanProperty EAST = ConnectingBlock.EAST;
	public static final BooleanProperty SOUTH = ConnectingBlock.SOUTH;
	public static final BooleanProperty WEST = ConnectingBlock.WEST;

	public static final Map<Direction, BooleanProperty> FACING_PROPERTIES = ConnectingBlock.FACING_PROPERTIES.entrySet()
			.stream().filter(entry -> entry.getKey() != Direction.DOWN).collect(Util.toMap());
	public static final EnumProperty<NestResinWebVariant> VARIANTS = EnumProperty.of("nest_resin_web_variant",
			NestResinWebVariant.class);

	private static final VoxelShape UP_SHAPE = createCuboidShape(0.0, 15.0, 0.0, 16.0, 16.0, 16.0);
	private static final VoxelShape EAST_SHAPE = createCuboidShape(0.0, 0.0, 0.0, 1.0, 16.0, 16.0);
	private static final VoxelShape WEST_SHAPE = createCuboidShape(15.0, 0.0, 0.0, 16.0, 16.0, 16.0);
	private static final VoxelShape SOUTH_SHAPE = createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 1.0);
	private static final VoxelShape NORTH_SHAPE = createCuboidShape(0.0, 0.0, 15.0, 16.0, 16.0, 16.0);

	private static VoxelShape getShapeForState(BlockState state) {
		VoxelShape voxelShape = VoxelShapes.empty();
		if (state.get(UP)) {
			voxelShape = UP_SHAPE;
		}
		if (state.get(NORTH)) {
			voxelShape = VoxelShapes.union(voxelShape, SOUTH_SHAPE);
		}
		if (state.get(SOUTH)) {
			voxelShape = VoxelShapes.union(voxelShape, NORTH_SHAPE);
		}
		if (state.get(EAST)) {
			voxelShape = VoxelShapes.union(voxelShape, WEST_SHAPE);
		}
		if (state.get(WEST)) {
			voxelShape = VoxelShapes.union(voxelShape, EAST_SHAPE);
		}
		return voxelShape.isEmpty() ? VoxelShapes.fullCube() : voxelShape;
	}

	public static boolean shouldConnectTo(BlockView world, BlockPos pos, Direction direction) {
		BlockState blockState = world.getBlockState(pos);
		return isFaceFullSquare(blockState.getCollisionShape(world, pos), direction.getOpposite());
	}

	public static BooleanProperty getFacingProperty(Direction direction) {
		return FACING_PROPERTIES.get(direction);
	}

	private Map<BlockState, VoxelShape> shapesByState;

	public NestResinWebBlock(Settings settings) {
		super(settings);
		setDefaultState(getStateManager().getDefaultState().with(UP, false).with(NORTH, false).with(EAST, false)
				.with(SOUTH, false).with(WEST, false).with(VARIANTS, NestResinWebVariant.ONE));
		shapesByState = getStateManager().getStates().stream()
				.collect(Collectors.toMap(Function.identity(), NestResinWebBlock::getShapeForState));
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return shapesByState.get(state);
	}

	@Override
	public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
		return true;
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		return hasAdjacentBlocks(getPlacementShape(state, world, pos));
	}

	private boolean hasAdjacentBlocks(BlockState state) {
		return getAdjacentBlockCount(state) > 0;
	}

	private int getAdjacentBlockCount(BlockState state) {
		final int[] i = { 0 };
		FACING_PROPERTIES.values().forEach(it -> {
			if (state.get(it)) {
				i[0]++;
			}
		});
		return i[0];
	}

	private boolean shouldHaveSide(BlockView world, BlockPos pos, Direction side) {
		if (side == Direction.DOWN) {
			return false;
		} else {
			BlockPos blockPos = pos.offset(side);
			if (shouldConnectTo(world, blockPos, side)) {
				return true;
			} else if (side.getAxis() == Direction.Axis.Y) {
				return false;
			} else {
				BlockState blockState = world.getBlockState(pos.up());
				return blockState.isOf(this) && blockState.get(FACING_PROPERTIES.get(side));
			}
		}
	}

	private BlockState getPlacementShape(BlockState state, BlockView world, BlockPos pos) {
		BlockPos blockPos = pos.up();
		if (state.get(UP)) {
			state = state.with(UP, shouldConnectTo(world, blockPos, Direction.DOWN));
		}
		BlockState blockState = null;
		Iterator<Direction> iterator = Direction.Type.HORIZONTAL.iterator();
		while (true) {
			Direction direction;
			BooleanProperty booleanProperty;
			do {
				if (!iterator.hasNext()) {
					return state;
				}
				direction = iterator.next();
				booleanProperty = getFacingProperty(direction);
			} while (!state.get(booleanProperty));
			boolean bl = shouldHaveSide(world, pos, direction);
			if (!bl) {
				if (blockState == null) {
					blockState = world.getBlockState(blockPos);
				}
				bl = blockState.isOf(this) && blockState.get(booleanProperty);
			}
			state = state.with(booleanProperty, bl);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
			WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (direction == Direction.DOWN) {
			return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
		} else {
			BlockState blockState = getPlacementShape(state, world, pos);
			return !hasAdjacentBlocks(blockState) ? Blocks.AIR.getDefaultState() : blockState;
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean canReplace(BlockState state, ItemPlacementContext context) {
		BlockState blockState = context.getWorld().getBlockState(context.getBlockPos());
		return blockState.isOf(this) ? getAdjacentBlockCount(blockState) < FACING_PROPERTIES.size()
				: super.canReplace(state, context);
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockState blockState = ctx.getWorld().getBlockState(ctx.getBlockPos());
		boolean bl = blockState.isOf(this);
		BlockState blockState2 = bl ? blockState : getDefaultState();
		Direction[] placementDirections = ctx.getPlacementDirections();
		for (Direction direction : placementDirections) {
			if (direction != Direction.DOWN) {
				BooleanProperty booleanProperty = getFacingProperty(direction);
				boolean bl2 = bl && blockState.get(booleanProperty);
				if (!bl2 && shouldHaveSide(ctx.getWorld(), ctx.getBlockPos(), direction)) {
					return blockState2.with(booleanProperty, true).with(VARIANTS,
							Arrays.stream(NestResinWebVariant.values()).toList()
									.get(new Random().nextInt(NestResinWebVariant.values().length)));
				}
			}
		}
		return bl ? blockState2 : null;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(UP, NORTH, EAST, SOUTH, WEST, VARIANTS);
	}

	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return switch (rotation) {
		case CLOCKWISE_180 -> state.with(NORTH, state.get(SOUTH)).with(EAST, state.get(WEST))
				.with(SOUTH, state.get(NORTH)).with(WEST, state.get(EAST));
		case COUNTERCLOCKWISE_90 -> state.with(NORTH, state.get(EAST)).with(EAST, state.get(SOUTH))
				.with(SOUTH, state.get(WEST)).with(WEST, state.get(NORTH));
		case CLOCKWISE_90 -> state.with(NORTH, state.get(WEST)).with(EAST, state.get(NORTH))
				.with(SOUTH, state.get(EAST)).with(WEST, state.get(SOUTH));
		default -> state;
		};
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return switch (mirror) {
		case LEFT_RIGHT -> state.with(NORTH, state.get(SOUTH)).with(SOUTH, state.get(NORTH));
		case FRONT_BACK -> state.with(EAST, state.get(WEST)).with(WEST, state.get(EAST));
		default -> super.mirror(state, mirror);
		};
	}
}
