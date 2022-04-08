package mods.cybercat.gigeresque.common.block;

import java.util.Random;

import mods.cybercat.gigeresque.client.particle.Particles;
import mods.cybercat.gigeresque.common.block.tag.GigBlockTags;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import mods.cybercat.gigeresque.common.util.MathUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.Waterloggable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class AcidBlock extends FallingBlock implements Waterloggable {
	private static final int MAX_THICKNESS = 4;
	public static final IntProperty THICKNESS = IntProperty.of("thickness", 0, MAX_THICKNESS);
	private static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

	AcidBlock(Settings settings) {
		super(settings);
		setDefaultState((getStateManager().getDefaultState().with(WATERLOGGED, false)).with(THICKNESS, MAX_THICKNESS));
	}

	private void scheduleTickIfNotScheduled(World world, BlockPos pos) {
		if (!world.isClient && !world.getBlockTickScheduler().isQueued(pos, this)) {
			world.createAndScheduleBlockTick(pos, this, 40);
		}
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(THICKNESS, WATERLOGGED);
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		scheduleTickIfNotScheduled(world, pos);
	}

	@Override
	public boolean hasRandomTicks(BlockState state) {
		return true;
	}

	@Override
	public boolean canMobSpawnInside() {
		return false;
	}

	@Override
	public float getBlastResistance() {
		return 100.0f;
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.INVISIBLE;
	}

	@Override
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(StairsBlock.WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
			WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (!world.isClient() && state.get(WATERLOGGED)) {
			world.createAndScheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}
		return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}

	private int getThickness(BlockState state) {
		return state.get(THICKNESS);
	}

	@SuppressWarnings("unused")
	private void setThickness(ServerWorld world, BlockPos pos, BlockState state) {
		setThickness(world, pos, state, 1);
	}

	private void setThickness(ServerWorld world, BlockPos pos, BlockState state, int consume) {
		int newThickness = Math.max(getThickness(state) - consume, 0);
		BlockState newState = state.with(THICKNESS, newThickness);

		if (world.getBlockState(pos).getBlock() == Blocks.WATER) {
			newState = newState.with(WATERLOGGED, true);
		}

		world.setBlockState(pos, newState);
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		int currentThickness = getThickness(state);

		if (currentThickness <= 0) {
			world.breakBlock(pos, false);
			return;
		}

		if (random.nextInt(8 - currentThickness) == 0) {
			boolean canGrief = world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING);

			BlockPos blockToEat = pos.down();

			if (currentThickness >= 1) {
				setThickness(world, pos, state, MathUtil.clamp(random.nextInt(2) + 1, 1, currentThickness));

				if (canGrief && !world.getBlockState(blockToEat).isIn(GigBlockTags.ACID_RESISTANT)) {
					//world.breakBlock(blockToEat, false);
					world.setBlockState(blockToEat, Blocks.AIR.getDefaultState());
					world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS,
							0.2f + random.nextFloat() * 0.2f, 0.9f + random.nextFloat() * 0.15f, false);
				} 
			}
		}

		super.scheduledTick(state, world, pos, random);
		scheduleTickIfNotScheduled(world, pos);
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		scheduleTickIfNotScheduled(world, pos);
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		if (getThickness(state) == 0 && !world.getBlockState(pos).isIn(GigBlockTags.ACID_RESISTANT)) {
			world.breakBlock(pos, false);
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
			return;
		}

		for (int i = 0; i < (getThickness(state) * 2) + 1; i++) {
			double yOffset = state.get(WATERLOGGED) ? random.nextDouble() : 0.01;
			double d = pos.getX() + random.nextDouble();
			double e = pos.getY() + yOffset;
			double f = pos.getZ() + random.nextDouble();
			world.addImportantParticle(Particles.ACID, d, e, f, 0.0, 0.0, 0.0);
		}
		if (random.nextInt(5 * ((MAX_THICKNESS + 1) - getThickness(state))) == 0) {
			world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS,
					0.2f + random.nextFloat() * 0.2f, 0.9f + random.nextFloat() * 0.15f, false);
		}
	}

	public static boolean canFallThrough(BlockState state) {
		Material material = state.getMaterial();
		return (state.isAir() || state.isIn(BlockTags.FIRE) || material.isReplaceable()) && !material.isLiquid();
	}
	
	private void dealAcidDamage(BlockState state, Entity entity) {
		if (entity instanceof LivingEntity && !(entity instanceof AlienEntity) && !(entity instanceof WitherEntity)) 
			((LivingEntity)entity).addStatusEffect(new StatusEffectInstance(GigStatusEffects.ACID, 60, 0));
	}
	
	@Override
	public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
		this.dealAcidDamage(state, entity);
		super.onSteppedOn(world, pos, state, entity);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
		return Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);
	}
}
