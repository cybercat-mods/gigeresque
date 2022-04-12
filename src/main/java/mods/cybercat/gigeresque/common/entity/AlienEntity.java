package mods.cybercat.gigeresque.common.entity;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.block.AcidBlock;
import mods.cybercat.gigeresque.common.block.GIgBlocks;
import mods.cybercat.gigeresque.common.util.DamageSourceUtils;
import net.minecraft.block.AirBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.TorchBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AlienEntity extends HostileEntity {
	
	public static final TrackedData<Boolean> UPSIDE_DOWN = DataTracker.registerData(AlienEntity.class,
			TrackedDataHandlerRegistry.BOOLEAN);

	protected int getAcidDiameter() {
		return 3;
	}

    public boolean isUpsideDown() {
        return this.dataTracker.get(UPSIDE_DOWN);
    }

    public void setUpsideDown(boolean upsideDown) {
        this.dataTracker.set(UPSIDE_DOWN, Boolean.valueOf(upsideDown));
    }
	
	@Override
	public void initDataTracker() {
		super.initDataTracker();
		dataTracker.startTracking(UPSIDE_DOWN, false);
	}

	protected AlienEntity(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
		setPathfindingPenalty(PathNodeType.DANGER_FIRE, 16.0f);
		setPathfindingPenalty(PathNodeType.DAMAGE_FIRE, -1.0f);
		if (navigation != null) {
			navigation.setCanSwim(true);
		}
	}

	@Override
	public void tick() {
		super.tick();

		if (!world.isClient && world.getBlockState(getBlockPos()).getBlock() == GIgBlocks.NEST_RESIN
				&& this.age % Constants.TPS == 0) {
			this.heal(0.0833f);
		}
	}

	@Override
	public boolean cannotDespawn() {
		return true;
	}

	@Override
	public void checkDespawn() {
	}

	private void generateAcidPool(int xOffset, int zOffset) {
		BlockPos pos = this.getBlockPos().add(xOffset, 0, zOffset);
		BlockState posState = world.getBlockState(pos);

		BlockState newState = GIgBlocks.ACID_BLOCK.getDefaultState();

		if (posState.getBlock() == net.minecraft.block.Blocks.WATER) {
			newState = newState.with(Properties.WATERLOGGED, true);
		}

		if (!(posState.getBlock() instanceof AirBlock) && !(posState.getBlock() instanceof FluidBlock)
				&& !(posState.getBlock() instanceof TorchBlock)) {
			return;
		}
		world.setBlockState(pos, newState);
	}

	@Override
	public void onDeath(DamageSource source) {
		if (DamageSourceUtils.isDamageSourceNotPuncturing(source)) {
			super.onDeath(source);
			return;
		}
		if (source == DamageSource.OUT_OF_WORLD) {
			super.onDeath(source);
			return;
		}

		if (!this.world.isClient) {
			if (source != DamageSource.OUT_OF_WORLD) {
				if (getAcidDiameter() == 1) {
					generateAcidPool(0, 0);
				} else {
					int radius = (getAcidDiameter() - 1) / 2;

					for (int x = -radius; x <= radius; x++) {
						for (int z = -radius; z <= radius; z++) {
							generateAcidPool(x, z);
						}
					}
				}
			}
		}
		super.onDeath(source);
	}

	@Override
	public boolean damage(DamageSource source, float amount) {
		if (!this.world.isClient) {
			Entity attacker = source.getAttacker();
			if (attacker != null) {
				if (attacker instanceof LivingEntity) {
					this.brain.remember(MemoryModuleType.ATTACK_TARGET, (LivingEntity) attacker);
				}
			}
		}

		if (DamageSourceUtils.isDamageSourceNotPuncturing(source))
			return super.damage(source, amount);

		if (!this.world.isClient && source != DamageSource.OUT_OF_WORLD) {
			int acidThickness = this.getHealth() < (this.getMaxHealth() / 2) ? 1 : 0;

			if (this.getHealth() < (this.getMaxHealth() / 4)) {
				acidThickness += 1;
			}

			if (amount >= 5) {
				acidThickness += 1;
			}

			if (amount > (this.getMaxHealth() / 10)) {
				acidThickness += 1;
			}

			if (acidThickness == 0)
				return super.damage(source, amount);

			BlockState newState = GIgBlocks.ACID_BLOCK.getDefaultState().with(AcidBlock.THICKNESS, acidThickness);

			if (this.getBlockStateAtPos().getBlock() == net.minecraft.block.Blocks.WATER) {
				newState = newState.with(Properties.WATERLOGGED, true);
			}

			world.setBlockState(this.getBlockPos(), newState);
		}

		return super.damage(source, amount);
	}
}
