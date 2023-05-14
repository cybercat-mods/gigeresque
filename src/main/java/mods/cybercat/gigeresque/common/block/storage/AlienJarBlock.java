package mods.cybercat.gigeresque.common.block.storage;

import java.util.stream.Stream;

import mods.cybercat.gigeresque.common.block.entity.JarStorageEntity;
import mods.cybercat.gigeresque.common.block.material.Materials;
import mods.cybercat.gigeresque.common.entity.Entities;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AlienJarBlock extends BaseEntityBlock {

	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final EnumProperty<StorageStates> STORAGE_STATE = StorageProperties.STORAGE_STATE;
	private static final VoxelShape OUTLINE_SHAPE = Stream.of(Block.box(5.5, 0, 5.5, 10.5, 3, 10.5), Block.box(5, 3, 5, 11, 9, 11), Block.box(4.5, 9, 4.5, 11.5, 16, 11.5), Block.box(4.5, 12, 4.5, 11.5, 16, 11.5), Block.box(5, 16, 5, 11, 18, 11)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

	public AlienJarBlock() {
		super(FabricBlockSettings.of(Materials.ORGANIC_ALIEN_BLOCK).sounds(SoundType.DRIPSTONE_BLOCK).strength(5.0f, 8.0f).nonOpaque());
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(STORAGE_STATE, StorageStates.CLOSED));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, STORAGE_STATE);
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation((Direction) state.getValue(FACING)));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (!world.isClientSide && world.getBlockEntity(pos)instanceof JarStorageEntity jarStorageEntity)
			player.openMenu(jarStorageEntity);
		return InteractionResult.SUCCESS;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new JarStorageEntity(pos, state);
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
		return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(world.getBlockEntity(pos));
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.INVISIBLE;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return OUTLINE_SHAPE;
	}

	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		if (world.getBlockEntity(pos)instanceof JarStorageEntity jarStorageEntity)
			jarStorageEntity.tick();
	}

	@Override
	public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
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
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, Entities.ALIEN_STORAGE_BLOCK_ENTITY_2, JarStorageEntity::tick);
	}
}
