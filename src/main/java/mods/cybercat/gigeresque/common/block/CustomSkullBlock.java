package mods.cybercat.gigeresque.common.block;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.AbstractSkullBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.SkullBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class CustomSkullBlock extends AbstractSkullBlock {
	public static final IntProperty ROTATION = Properties.ROTATION;
	public static final VoxelShape SHAPE = createCuboidShape(4.0, 0.0, 4.0, 12.0, 8.0, 12.0);

	public enum AlienSkullType implements SkullBlock.SkullType {
		AQUA, CLASSIC, RUNNER,
	}

	public CustomSkullBlock(SkullBlock.SkullType type, Settings settings) {
		super(type, settings);
		setDefaultState(getStateManager().getDefaultState().with(ROTATION, 0));
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Override
	public VoxelShape getCullingShape(BlockState state, BlockView world, BlockPos pos) {
		return VoxelShapes.empty();
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return getDefaultState().with(ROTATION, MathHelper.floor((ctx.getPlayerYaw() * 16.0f / 360.0f) + 0.5) & 15);
	}

	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(ROTATION, rotation.rotate(state.get(ROTATION), 16));
	}

	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.with(ROTATION, mirror.mirror(state.get(ROTATION), 16));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(ROTATION);
	}
}
