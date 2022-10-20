package mods.cybercat.gigeresque.common.entity;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.block.AcidBlock;
import mods.cybercat.gigeresque.common.block.GIgBlocks;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.DamageSourceUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

public abstract class AlienEntity extends Monster {

	public static final EntityDataAccessor<Boolean> UPSIDE_DOWN = SynchedEntityData.defineId(AlienEntity.class,
			EntityDataSerializers.BOOLEAN);

	@Override
	protected void tickDeath() {
		++this.deathTime;
		if (this.deathTime == 200) {
			this.remove(Entity.RemovalReason.KILLED);
			super.tickDeath();
			this.dropExperience();
		}
	}

	protected int getAcidDiameter() {
		return 3;
	}

	public boolean isUpsideDown() {
		return this.entityData.get(UPSIDE_DOWN);
	}

	public void setUpsideDown(boolean upsideDown) {
		this.entityData.set(UPSIDE_DOWN, Boolean.valueOf(upsideDown));
	}

	@Override
	public void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(UPSIDE_DOWN, false);
	}

	protected AlienEntity(EntityType<? extends Monster> entityType, Level world) {
		super(entityType, world);
		setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 16.0f);
		setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, -1.0f);
		if (navigation != null) {
			navigation.setCanFloat(true);
		}
	}

	@Override
	public void tick() {
		super.tick();

		if (!level.isClientSide && level.getBlockState(blockPosition()).getBlock() == GIgBlocks.NEST_RESIN
				&& this.tickCount % Constants.TPS == 0) {
			this.heal(0.0833f);
		}
	}

	@Override
	public boolean requiresCustomPersistence() {
		return true;
	}

	@Override
	public void checkDespawn() {
	}

	private void generateAcidPool(int xOffset, int zOffset) {
		BlockPos pos = this.blockPosition().offset(xOffset, 0, zOffset);
		BlockState posState = level.getBlockState(pos);

		BlockState newState = GIgBlocks.ACID_BLOCK.defaultBlockState();

		if (posState.getBlock() == Blocks.WATER) {
			newState = newState.setValue(BlockStateProperties.WATERLOGGED, true);
		}

		if (!(posState.getBlock() instanceof AirBlock)
				&& !(posState.getBlock() instanceof LiquidBlock && !(posState.is(GigTags.ACID_RESISTANT)))
				&& !(posState.getBlock() instanceof TorchBlock)) {
			return;
		}
		level.setBlockAndUpdate(pos, newState);
	}

	@Override
	public void die(DamageSource source) {
		if (DamageSourceUtils.isDamageSourceNotPuncturing(source)) {
			super.die(source);
			return;
		}
		if (source == DamageSource.OUT_OF_WORLD) {
			super.die(source);
			return;
		}

		if (!this.level.isClientSide) {
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
		super.die(source);
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (!this.level.isClientSide) {
			Entity attacker = source.getEntity();
			if (attacker != null) {
				if (attacker instanceof LivingEntity) {
					this.brain.setMemory(MemoryModuleType.ATTACK_TARGET, (LivingEntity) attacker);
				}
			}
		}

		if (DamageSourceUtils.isDamageSourceNotPuncturing(source))
			return super.hurt(source, amount);

		if (!this.level.isClientSide && source != DamageSource.OUT_OF_WORLD) {
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
				return super.hurt(source, amount);

			BlockState newState = GIgBlocks.ACID_BLOCK.defaultBlockState().setValue(AcidBlock.THICKNESS, acidThickness);

			if (this.getFeetBlockState().getBlock() == Blocks.WATER) {
				newState = newState.setValue(BlockStateProperties.WATERLOGGED, true);
			}
			if (!this.getFeetBlockState().is(GigTags.ACID_RESISTANT))
				level.setBlockAndUpdate(this.blockPosition(), newState);
		}

		return super.hurt(source, amount);
	}
}
