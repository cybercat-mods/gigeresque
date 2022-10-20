package mods.cybercat.gigeresque.common.block;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.util.MathUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class NestResinBlock extends Block {
	public NestResinBlock(Properties settings) {
		super(settings);

		registerDefaultState(getStateDefinition().any().setValue(BlockStateProperties.LAYERS, 1));
	}

	private static List<VoxelShape> interpolateShapes(boolean divide, boolean includeEmptyVoxelShape) {
		ArrayList<VoxelShape> list = new ArrayList<>();
		if (includeEmptyVoxelShape) {
			list.add(Shapes.empty());
		}
		for (int i = 0; i < 8; i++) {
			double minY = divide ? (i * 2.0) / 2.0 : i * 2.0;
			list.add(box(0.0, 0.0, 0.0, 16.0, minY, 16.0));
		}
		return list;
	}

	public static final IntegerProperty LAYERS = BlockStateProperties.LAYERS;
	public static final List<VoxelShape> ALIEN_LAYERS_TO_SHAPE = interpolateShapes(false, true);
	public static final List<VoxelShape> LAYERS_TO_SHAPE = interpolateShapes(true, true);

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
		return context instanceof EntityCollisionContext
				&& ((EntityCollisionContext) context).getEntity() instanceof AlienEntity
						? ALIEN_LAYERS_TO_SHAPE.get(state.getValue(LAYERS))
						: LAYERS_TO_SHAPE.get(state.getValue(LAYERS));
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
		BlockState blockState = world.getBlockState(pos.below());
		boolean isIce = blockState.is(Blocks.ICE);
		boolean isPackedIce = blockState.is(Blocks.PACKED_ICE);
		boolean isBarrier = blockState.is(Blocks.BARRIER);
		boolean isHoney = blockState.is(Blocks.HONEY_BLOCK);
		boolean isSoulSand = blockState.is(Blocks.SOUL_SAND);
		if (!isIce && !isPackedIce && !isBarrier) {
			if (!isHoney && !isSoulSand) {
				return isFaceFull(blockState.getCollisionShape(world, pos.below()), Direction.UP)
						|| blockState.is(this) && blockState.getValue(LAYERS) == 8;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world,
			BlockPos pos, BlockPos neighborPos) {
		return !state.canSurvive(world, pos) ? Blocks.AIR.defaultBlockState()
				: super.updateShape(state, direction, neighborState, world, pos, neighborPos);
	}

	@Override
	public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
		int i = state.getValue(LAYERS);
		if (context.getItemInHand().is(asItem()) && i < 8) {
			if (context.replacingClickedOnBlock()) {
				return context.getClickedFace() == Direction.UP;
			} else {
				return true;
			}
		} else {
			return i == 1;
		}
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		BlockState blockState = ctx.getLevel().getBlockState(ctx.getClickedPos());
		if (blockState.is(this)) {
			int i = blockState.getValue(LAYERS);
			return blockState.setValue(LAYERS, MathUtil.coerceAtMost(8, i + 1));
		} else {
			return super.getStateForPlacement(ctx);
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(LAYERS);
	}

	@Override
	public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
		if (!(entity instanceof AlienEntity)) {
			double multiplier = MathUtil.clamp(1.0 / state.getValue(LAYERS), 0.0, 1.0);
			entity.makeStuckInBlock(state, new Vec3(1.0 * multiplier, 1.0, 1.0 * multiplier));
		}
	}
}
