package com.bvanseg.gigeresque.mixins.common.entity;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.bvanseg.gigeresque.Constants;
import com.bvanseg.gigeresque.client.particle.Particles;
import com.bvanseg.gigeresque.common.Gigeresque;
import com.bvanseg.gigeresque.common.block.Blocks;
import com.bvanseg.gigeresque.common.config.ConfigAccessor;
import com.bvanseg.gigeresque.common.entity.Entities;
import com.bvanseg.gigeresque.common.entity.EntityIdentifiers;
import com.bvanseg.gigeresque.common.entity.impl.AlienEggEntity;
import com.bvanseg.gigeresque.common.entity.impl.AquaticChestbursterEntity;
import com.bvanseg.gigeresque.common.entity.impl.ChestbursterEntity;
import com.bvanseg.gigeresque.common.entity.impl.FacehuggerEntity;
import com.bvanseg.gigeresque.common.entity.impl.RunnerbursterEntity;
import com.bvanseg.gigeresque.common.source.DamageSources;
import com.bvanseg.gigeresque.interfacing.Eggmorphable;
import com.bvanseg.gigeresque.interfacing.Host;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

/**
 * @author Boston Vanseghi
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Host, Eggmorphable {

	private static final TrackedData<Boolean> IS_BLEEDING = DataTracker.registerData(LivingEntity.class,
			TrackedDataHandlerRegistry.BOOLEAN);
	private static final TrackedData<Float> EGGMORPH_TICKS = DataTracker.registerData(LivingEntity.class,
			TrackedDataHandlerRegistry.FLOAT);
	public float ticksUntilImpregnation = -1.0f;
	public boolean hasParasiteSpawned = false;
	public boolean hasEggSpawned = false;

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Shadow
	abstract boolean hasStatusEffect(StatusEffect effect);

	@Shadow
	abstract boolean addStatusEffect(StatusEffectInstance effect);

	@Shadow
	public abstract boolean damage(DamageSource source, float amount);

	@Shadow
	public abstract boolean isDead();

	@Shadow
	public abstract float getMaxHealth();

	@Shadow
	public abstract boolean isAlive();

	@Shadow
	public abstract float getHealth();

	@Shadow
	public abstract void kill();

	private void handleStatusEffect(long offset, StatusEffect statusEffect, Boolean checkStatusEffect) {
		if (ticksUntilImpregnation < offset && (!checkStatusEffect || !hasStatusEffect(statusEffect))) {
			int amplifier = (int) (((Constants.TPD - (Constants.TPM * 8L)) - ticksUntilImpregnation)
					/ (Constants.TPS * 30));
			this.addStatusEffect(new StatusEffectInstance(statusEffect, (int) ticksUntilImpregnation, amplifier));
		}
	}

	@Inject(method = { "tick" }, at = { @At("HEAD") })
	void tick(CallbackInfo callbackInfo) {
		if (this.isAlive() && this.getEntityWorld().isClient && Boolean.TRUE.equals(isBleeding())) {
			double yOffset = this.getEyeY() - ((this.getEyeY() - this.getBlockPos().getY()) / 2.0);
			double d = this.getX() + ((random.nextDouble() / 2.0) - 0.5) * (random.nextBoolean() ? -1 : 1);
			double f = this.getZ() + ((random.nextDouble() / 2.0) - 0.5) * (random.nextBoolean() ? -1 : 1);

			for (int i = 0; i < 1 + (int) (this.getMaxHealth() - this.getHealth()); i++) {
				this.getEntityWorld().addImportantParticle(Particles.BLOOD, d, yOffset, f, 0.0, -0.15, 0.0);
			}
		}

		if (!this.world.isClient) {
			if (((((Object) this)instanceof PlayerEntity playerEntity && playerEntity.isCreative())
					|| this.isSpectator() || world.getDifficulty() == Difficulty.PEACEFUL)) {
				removeParasite();
				resetEggmorphing();
				setBleeding(false);
			}

			handleEggmorphingLogic();
			handleHostLogic();
		}
	}

	private void handleEggmorphingLogic() {
		if (isEggmorphing()) {
			setTicksUntilEggmorphed(Math.max(
					getTicksUntilEggmorphed() - Gigeresque.config.getMiscellaneous().getEggmorphTickMultiplier(), 0f));
		} else {
			// Reset eggmorphing counter if the entity is no longer eggmorphing at any
			// point.
			resetEggmorphing();
		}

		if (getTicksUntilEggmorphed() == 0L && !hasEggSpawned && !this.isDead()) {
			AlienEggEntity egg = new AlienEggEntity(Entities.EGG, world);
			egg.refreshPositionAndAngles(this.getBlockPos(), this.getYaw(), this.getPitch());
			world.spawnEntity(egg);
			this.hasEggSpawned = true;
			this.damage(DamageSources.EGGMORPHING, Float.MAX_VALUE);
		}
	}

	private void handleHostLogic() {
		if (hasParasite()) {
			ticksUntilImpregnation = Math.max(
					ticksUntilImpregnation - Gigeresque.config.getMiscellaneous().getImpregnationTickMultiplier(), 0f);

			if (Boolean.TRUE.equals(!isBleeding()) && ticksUntilImpregnation >= 0
					&& ticksUntilImpregnation < Constants.TPS * 30L) {
				setBleeding(true);
			}

			handleStatusEffect(Constants.TPM * 12L, StatusEffects.HUNGER, false);
			handleStatusEffect(Constants.TPM * 7L, StatusEffects.WEAKNESS, true);
			handleStatusEffect(Constants.TPM * 2L, StatusEffects.MINING_FATIGUE, true);
		}

		if (ticksUntilImpregnation == 0L) {
			if (age % Constants.TPS == 0L) {
				this.damage(DamageSources.CHESTBURSTING, this.getMaxHealth() / 8f);
			}

			if (this.isDead() && !hasParasiteSpawned) {
				Identifier identifier = Registry.ENTITY_TYPE.getId(this.getType());
				Map<String, String> morphMappings = ConfigAccessor.getReversedMorphMappings();
				String producedVariant = morphMappings.getOrDefault(identifier.toString(),
						EntityIdentifiers.ALIEN.toString());

				ChestbursterEntity burster = switch (producedVariant) {
				case Gigeresque.MOD_ID + ":runner_alien" -> new RunnerbursterEntity(Entities.RUNNERBURSTER, this.world);
				case Gigeresque.MOD_ID
						+ ":aquatic_alien" -> new AquaticChestbursterEntity(Entities.AQUATIC_CHESTBURSTER, this.world);
				default -> new ChestbursterEntity(Entities.CHESTBURSTER, this.world);
				};

				burster.setHostId(identifier.toString());
				burster.refreshPositionAndAngles(this.getBlockPos(), this.getYaw(), this.getPitch());

				if (this.hasCustomName()) {
					burster.setCustomName(this.getCustomName());
				}

				this.world.spawnEntity(burster);
				hasParasiteSpawned = true;
			}
		}
	}

	@Inject(method = { "isImmobile" }, at = { @At("RETURN") })
	protected boolean isImmobile(CallbackInfoReturnable<Boolean> callbackInfo) {
		if (this.getPassengerList().stream().anyMatch(FacehuggerEntity.class::isInstance) || this.isEggmorphing()) {
			return true;
		}
		return callbackInfo.getReturnValue();
	}

	@Inject(method = { "initDataTracker" }, at = { @At("RETURN") })
	void initDataTracker(CallbackInfo callbackInfo) {
		dataTracker.startTracking(IS_BLEEDING, false);
		dataTracker.startTracking(EGGMORPH_TICKS, -1.0f);
	}

	@Inject(method = { "writeCustomDataToNbt" }, at = { @At("RETURN") })
	void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo callbackInfo) {
		nbt.putFloat("ticksUntilImpregnation", ticksUntilImpregnation);
		nbt.putFloat("ticksUntilEggmorphed", getTicksUntilEggmorphed());
		nbt.putBoolean("isBleeding", isBleeding());
	}

	@Inject(method = { "readCustomDataFromNbt" }, at = { @At("RETURN") })
	void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo callbackInfo) {
		if (nbt.contains("ticksUntilImpregnation")) {
			ticksUntilImpregnation = nbt.getInt("ticksUntilImpregnation");
		}
		if (nbt.contains("ticksUntilEggmorphed")) {
			setTicksUntilEggmorphed(nbt.getInt("ticksUntilEggmorphed"));
		}
		if (nbt.contains("isBleeding")) {
			setBleeding(nbt.getBoolean("isBleeding"));
		}
	}

	@Inject(method = { "damage" }, at = { @At("HEAD") }, cancellable = true)
	public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (this.getPassengerList().stream().anyMatch(FacehuggerEntity.class::isInstance)
				&& (source == DamageSource.DROWN || source == DamageSource.IN_WALL)) {
			callbackInfo.setReturnValue(false);
			callbackInfo.cancel();
		}
	}

	@Override
	public boolean isPushable() {
		return super.isPushable() && isNotEggmorphing();
	}

	@Override
	public float getTicksUntilImpregnation() {
		return ticksUntilImpregnation;
	}

	@Override
	public void setTicksUntilImpregnation(float ticksUntilImpregnation) {
		this.ticksUntilImpregnation = ticksUntilImpregnation;
	}

	@Override
	public boolean isEggmorphing() {
		Block cameraBlock = this.world.getBlockState(this.getCameraBlockPos()).getBlock();
		Block pos = this.getBlockStateAtPos().getBlock();
		boolean isCoveredInResin = cameraBlock == Blocks.NEST_RESIN_WEB_CROSS || pos == Blocks.NEST_RESIN_WEB_CROSS;
		return getTicksUntilEggmorphed() >= 0 && isCoveredInResin;
	}

	@Override
	public float getTicksUntilEggmorphed() {
		return dataTracker.get(EGGMORPH_TICKS);
	}

	@Override
	public void setTicksUntilEggmorphed(float ticksUntilEggmorphed) {
		this.dataTracker.set(EGGMORPH_TICKS, ticksUntilEggmorphed);
	}

	@Override
	public boolean isBleeding() {
		return dataTracker.get(IS_BLEEDING);
	}

	@Override
	public void setBleeding(boolean isBleeding) {
		dataTracker.set(IS_BLEEDING, isBleeding);
	}
}
