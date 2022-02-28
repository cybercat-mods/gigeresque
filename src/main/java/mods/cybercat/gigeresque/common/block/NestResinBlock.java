package mods.cybercat.gigeresque.common.block;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.util.MathUtil;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.EntityShapeContext;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class NestResinBlock extends Block {
	public NestResinBlock(Settings settings) {
		super(settings);

		setDefaultState(getStateManager().getDefaultState().with(Properties.LAYERS, 1));
	}

	private static List<VoxelShape> interpolateShapes(boolean divide, boolean includeEmptyVoxelShape) {
		ArrayList<VoxelShape> list = new ArrayList<>();
		if (includeEmptyVoxelShape) {
			list.add(VoxelShapes.empty());
		}
		for (int i = 0; i < 8; i++) {
			double minY = divide ? (i * 2.0) / 2.0 : i * 2.0;
			list.add(createCuboidShape(0.0, 0.0, 0.0, 16.0, minY, 16.0));
		}
		return list;
	}

	public static final IntProperty LAYERS = Properties.LAYERS;
	public static final List<VoxelShape> ALIEN_LAYERS_TO_SHAPE = interpolateShapes(false, true);
	public static final List<VoxelShape> LAYERS_TO_SHAPE = interpolateShapes(true, true);

	@Override
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return type == NavigationType.LAND;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return ALIEN_LAYERS_TO_SHAPE.get(state.get(LAYERS));
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return context instanceof EntityShapeContext
				&& ((EntityShapeContext) context).getEntity() instanceof AlienEntity
						? ALIEN_LAYERS_TO_SHAPE.get(state.get(LAYERS))
						: LAYERS_TO_SHAPE.get(state.get(LAYERS));
	}

	@Override
	public VoxelShape getSidesShape(BlockState state, BlockView world, BlockPos pos) {
		return LAYERS_TO_SHAPE.get(state.get(LAYERS));
	}

	@Override
	public VoxelShape getCameraCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return LAYERS_TO_SHAPE.get(state.get(LAYERS));
	}

	@Override
	public boolean hasSidedTransparency(BlockState state) {
		return true;
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		BlockState blockState = world.getBlockState(pos.down());
		boolean isIce = blockState.isOf(Blocks.ICE);
		boolean isPackedIce = blockState.isOf(Blocks.PACKED_ICE);
		boolean isBarrier = blockState.isOf(Blocks.BARRIER);
		boolean isHoney = blockState.isOf(Blocks.HONEY_BLOCK);
		boolean isSoulSand = blockState.isOf(Blocks.SOUL_SAND);
		if (!isIce && !isPackedIce && !isBarrier) {
			if (!isHoney && !isSoulSand) {
				return isFaceFullSquare(blockState.getCollisionShape(world, pos.down()), Direction.UP)
						|| blockState.isOf(this) && blockState.get(LAYERS) == 8;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
			WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		return !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState()
				: super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}

	@Override
	public boolean canReplace(BlockState state, ItemPlacementContext context) {
		int i = state.get(LAYERS);
		if (context.getStack().isOf(asItem()) && i < 8) {
			if (context.canReplaceExisting()) {
				return context.getSide() == Direction.UP;
			} else {
				return true;
			}
		} else {
			return i == 1;
		}
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockState blockState = ctx.getWorld().getBlockState(ctx.getBlockPos());
		if (blockState.isOf(this)) {
			int i = blockState.get(LAYERS);
			return blockState.with(LAYERS, MathUtil.coerceAtMost(8, i + 1));
		} else {
			return super.getPlacementState(ctx);
		}
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(LAYERS);
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (!(entity instanceof AlienEntity)) {
			double multiplier = MathUtil.clamp(1.0 / state.get(LAYERS), 0.0, 1.0);
			entity.slowMovement(state, new Vec3d(1.0 * multiplier, 1.0, 1.0 * multiplier));
		}
	}
}
