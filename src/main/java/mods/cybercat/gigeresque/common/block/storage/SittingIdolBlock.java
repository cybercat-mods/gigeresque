package mods.cybercat.gigeresque.common.block.storage;

import mods.cybercat.gigeresque.common.block.GIgBlocks;
import mods.cybercat.gigeresque.common.block.entity.IdolStorageEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class SittingIdolBlock extends BlockWithEntity {

	public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
	BlockPos[] blockPoss;

	public SittingIdolBlock() {
		super(FabricBlockSettings.of(Material.STONE).sounds(BlockSoundGroup.DRIPSTONE_BLOCK).strength(5.0f, 8.0f)
				.nonOpaque());
		this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation((Direction) state.get(FACING)));
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		return this.getDefaultState().with(FACING, context.getPlayerFacing().getOpposite());
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
			BlockHitResult hit) {
		if (!world.isClient) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof IdolStorageEntity) {
				player.openHandledScreen((IdolStorageEntity) blockEntity);
			}
		}
		return ActionResult.SUCCESS;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof IdolStorageEntity) {
				ItemScatterer.spawn(world, pos, (IdolStorageEntity) blockEntity);
				world.updateComparators(pos, this);
			}
			super.onStateReplaced(state, world, pos, newState, moved);
		}
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new IdolStorageEntity(pos, state);
	}

	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.INVISIBLE;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return Block.createCuboidShape(0, 0, 0, 16, 16, 16);
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		Direction direction = (Direction) state.get(FACING);
		switch (direction) {
		case NORTH: {
			for (BlockPos blockPos : blockPoss = new BlockPos[] { pos.up(), pos.up().up(), pos.north(),
					pos.north().up(), pos.north().up().up(), pos.north().north(), pos.north().north().up(),
					pos.north().north().up().up() }) {
				world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL);
			}
			break;
		}
		case SOUTH: {
			for (BlockPos blockPos : blockPoss = new BlockPos[] { pos.up(), pos.up().up(), pos.south(),
					pos.south().up(), pos.south().up().up(), pos.south().south(), pos.south().south().up(),
					pos.south().south().up().up() }) {
				world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL);
			}
			break;
		}
		case WEST: {
			for (BlockPos blockPos : blockPoss = new BlockPos[] { pos.up(), pos.up().up(), pos.west(), pos.west().up(),
					pos.west().up().up(), pos.west().west(), pos.west().west().up(), pos.west().west().up().up() }) {
				world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL);
			}
			break;
		}
		default:
			for (BlockPos blockPos : blockPoss = new BlockPos[] { pos.up(), pos.up().up(), pos.east(), pos.east().up(),
					pos.east().up().up(), pos.east().east(), pos.east().east().up(), pos.east().east().up().up() }) {
				world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL);
			}
			break;
		}
		super.onBreak(world, pos, state, player);
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		Direction direction = (Direction) state.get(FACING);
		switch (direction) {
		case NORTH: {
			for (BlockPos blockPos : blockPoss = new BlockPos[] { pos.up(), pos.up().up(), pos.north(),
					pos.north().up(), pos.north().up().up(), pos.north().north(), pos.north().north().up(),
					pos.north().north().up().up() }) {
				world.setBlockState(blockPos, GIgBlocks.ALIEN_STORAGE_BLOCK_INVIS2.getDefaultState(), Block.NOTIFY_ALL);
			}
			break;
		}
		case SOUTH: {
			for (BlockPos blockPos : blockPoss = new BlockPos[] { pos.up(), pos.up().up(), pos.south(),
					pos.south().up(), pos.south().up().up(), pos.south().south(), pos.south().south().up(),
					pos.south().south().up().up() }) {
				world.setBlockState(blockPos, GIgBlocks.ALIEN_STORAGE_BLOCK_INVIS2.getDefaultState(), Block.NOTIFY_ALL);
			}
			break;
		}
		case WEST: {
			for (BlockPos blockPos : blockPoss = new BlockPos[] { pos.up(), pos.up().up(), pos.west(), pos.west().up(),
					pos.west().up().up(), pos.west().west(), pos.west().west().up(), pos.west().west().up().up() }) {
				world.setBlockState(blockPos, GIgBlocks.ALIEN_STORAGE_BLOCK_INVIS2.getDefaultState(), Block.NOTIFY_ALL);
			}
			break;
		}
		default:
			for (BlockPos blockPos : blockPoss = new BlockPos[] { pos.up(), pos.up().up(), pos.east(), pos.east().up(),
					pos.east().up().up(), pos.east().east(), pos.east().east().up(), pos.east().east().up().up() }) {
				world.setBlockState(blockPos, GIgBlocks.ALIEN_STORAGE_BLOCK_INVIS2.getDefaultState(), Block.NOTIFY_ALL);
			}
			break;
		}
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		Direction direction = (Direction) state.get(FACING);
		switch (direction) {
		case NORTH: {
			return world.getBlockState(pos.up()).isAir() && world.getBlockState(pos.up().up()).isAir()
					&& world.getBlockState(pos.north()).isAir() && world.getBlockState(pos.north().up()).isAir()
					&& world.getBlockState(pos.north().up().up()).isAir()
					&& world.getBlockState(pos.north().north()).isAir()
					&& world.getBlockState(pos.north().north().up()).isAir()
					&& world.getBlockState(pos.north().north().up().up()).isAir();
		}
		case SOUTH: {
			return world.getBlockState(pos.up()).isAir() && world.getBlockState(pos.up().up()).isAir()
					&& world.getBlockState(pos.south()).isAir() && world.getBlockState(pos.south().up()).isAir()
					&& world.getBlockState(pos.south().up().up()).isAir()
					&& world.getBlockState(pos.south().south()).isAir()
					&& world.getBlockState(pos.south().south().up()).isAir()
					&& world.getBlockState(pos.south().south().up().up()).isAir();
		}
		case WEST: {
			return world.getBlockState(pos.up()).isAir() && world.getBlockState(pos.up().up()).isAir()
					&& world.getBlockState(pos.west()).isAir() && world.getBlockState(pos.west().up()).isAir()
					&& world.getBlockState(pos.west().up().up()).isAir()
					&& world.getBlockState(pos.west().west()).isAir()
					&& world.getBlockState(pos.west().west().up()).isAir()
					&& world.getBlockState(pos.west().west().up().up()).isAir();
		}
		default:
			return world.getBlockState(pos.up()).isAir() && world.getBlockState(pos.up().up()).isAir()
					&& world.getBlockState(pos.east()).isAir() && world.getBlockState(pos.east().up()).isAir()
					&& world.getBlockState(pos.east().up().up()).isAir()
					&& world.getBlockState(pos.east().east()).isAir()
					&& world.getBlockState(pos.east().east().up()).isAir()
					&& world.getBlockState(pos.east().east().up().up()).isAir();
		}
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof IdolStorageEntity) {
			((IdolStorageEntity) blockEntity).tick();
		}
	}
}