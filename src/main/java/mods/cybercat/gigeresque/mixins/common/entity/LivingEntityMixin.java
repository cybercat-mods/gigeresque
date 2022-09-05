package mods.cybercat.gigeresque.mixins.common.entity;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.particle.Particles;
import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.block.GIgBlocks;
import mods.cybercat.gigeresque.common.config.ConfigAccessor;
import mods.cybercat.gigeresque.common.config.GigeresqueConfig;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.Entities;
import mods.cybercat.gigeresque.common.entity.EntityIdentifiers;
import mods.cybercat.gigeresque.common.entity.impl.AlienEggEntity;
import mods.cybercat.gigeresque.common.entity.impl.AquaticChestbursterEntity;
import mods.cybercat.gigeresque.common.entity.impl.ChestbursterEntity;
import mods.cybercat.gigeresque.common.entity.impl.FacehuggerEntity;
import mods.cybercat.gigeresque.common.entity.impl.RunnerbursterEntity;
import mods.cybercat.gigeresque.common.fluid.GigFluids;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.source.GigDamageSources;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import mods.cybercat.gigeresque.common.util.EntityUtils;
import mods.cybercat.gigeresque.interfacing.Eggmorphable;
import mods.cybercat.gigeresque.interfacing.Host;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
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
	public float ticksUntilEggmorpth = -1.0f;
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

	@Shadow
	public abstract EntityGroup getGroup();

	private void handleStatusEffect(long offset, StatusEffect statusEffect, Boolean checkStatusEffect) {
		if (ticksUntilImpregnation < offset && (!checkStatusEffect || !hasStatusEffect(statusEffect))) {
			int amplifier = (int) (((Constants.TPD - (Constants.TPM * 8L)) - ticksUntilImpregnation)
					/ (Constants.TPS * 30));
			this.addStatusEffect(
					new StatusEffectInstance(statusEffect, (int) ticksUntilImpregnation, amplifier, true, true));
		}
	}

	@Inject(method = { "pushAway" }, at = { @At("HEAD") }, cancellable = true)
	void pushAway(CallbackInfo callbackInfo) {
		if (this.isEggmorphing())
			callbackInfo.cancel();
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
			if ((this.world.getFluidState(this.getBlockPos()).getFluid() == GigFluids.BLACK_FLUID_STILL
					|| this.world.getFluidState(this.getBlockPos()).getFluid() == GigFluids.BLACK_FLUID_FLOWING)
					&& !ConfigAccessor.isTargetDNAImmune(this)) {
				if (!this.hasStatusEffect(GigStatusEffects.DNA) && !(((Object) this) instanceof PlayerEntity)
						&& !(((Object) this) instanceof AlienEntity) && !(((Object) this) instanceof CreeperEntity)
						&& !(ConfigAccessor.isTargetDNAImmune(this)))
					this.addStatusEffect(new StatusEffectInstance(GigStatusEffects.DNA,
							GigeresqueConfig.getgooEffectTickTimer(), 0));
				if (!this.hasStatusEffect(GigStatusEffects.DNA) && ((Object) this) instanceof CreeperEntity
						&& !(((Object) this) instanceof PlayerEntity) && !(((Object) this) instanceof AlienEntity))
					this.addStatusEffect(new StatusEffectInstance(GigStatusEffects.DNA, 60000, 0));
				if (!this.hasStatusEffect(GigStatusEffects.DNA)
						&& (((Object) this)instanceof PlayerEntity playerEntity
								&& !(playerEntity.isCreative() || this.isSpectator()))
						&& !(((Object) this) instanceof AlienEntity))
					this.addStatusEffect(new StatusEffectInstance(GigStatusEffects.DNA,
							GigeresqueConfig.getgooEffectTickTimer(), 0));
			}
		}

		if (!this.world.isClient) {
			if (((((Object) this)instanceof PlayerEntity playerEntity
					&& (playerEntity.isCreative() || this.isSpectator()))
					|| world.getDifficulty() == Difficulty.PEACEFUL) || (((Object) this) instanceof AlienEntity)
					|| ConfigAccessor.isTargetBlacklisted(FacehuggerEntity.class, this)) {
				removeParasite();
				resetEggmorphing();
				setBleeding(false);
			}
			handleEggingLogic();
			handleHostLogic();
		}
	}

	@Inject(method = { "isUsingItem" }, at = { @At("RETURN") })
	public boolean isUsingItem(CallbackInfoReturnable<Boolean> callbackInfo) {
		if (this.getPassengerList().stream().anyMatch(FacehuggerEntity.class::isInstance) || this.isEggmorphing()) {
			return false;
		}
		return callbackInfo.getReturnValue();
	}

	@Inject(method = { "isPushable" }, at = { @At("RETURN") })
	public boolean noPush(CallbackInfoReturnable<Boolean> callbackInfo) {
		if (this.isEggmorphing()) {
			return false;
		}
		return callbackInfo.getReturnValue();
	}

	private void handleEggingLogic() {
		if (isEggmorphing()) {
			setTicksUntilEggmorphed(ticksUntilEggmorpth++);
		} else {
			// Reset eggmorphing counter if the entity is no longer eggmorphing at any
			// point.
			resetEggmorphing();
		}
		if (getTicksUntilEggmorphed() == 6000 && !this.isDead()) {
			AlienEggEntity egg = new AlienEggEntity(Entities.EGG, world);
			egg.refreshPositionAndAngles(this.getBlockPos(), this.getYaw(), this.getPitch());
			world.spawnEntity(egg);
			world.breakBlock(this.getBlockPos(), false);
			hasEggSpawned = true;
			damage(GigDamageSources.EGGMORPHING, Float.MAX_VALUE);
		}
	}

	private void handleHostLogic() {
		if (hasParasite()) {
			ticksUntilImpregnation = Math.max(ticksUntilImpregnation - 1.0F, 0f);

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
				this.damage(GigDamageSources.CHESTBURSTING, this.getMaxHealth() / 8f);
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
				this.world.playSoundFromEntity(null, this, GigSounds.CHESTBURSTING, SoundCategory.NEUTRAL, 2.0f, 1.0f);

				burster.setHostId(identifier.toString());
				burster.refreshPositionAndAngles(this.getBlockPos(), this.getYaw(), this.getPitch());

				if (this.hasCustomName()) {
					burster.setCustomName(this.getCustomName());
				}
				burster.setStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 60, 10), burster);
				burster.setBirthStatus(true);
				this.world.spawnEntity(burster);
				hasParasiteSpawned = true;
			}
		}
	}

	@Inject(method = { "isImmobile" }, at = { @At("RETURN") })
	protected boolean isImmobile(CallbackInfoReturnable<Boolean> callbackInfo) {
		if (this.getPassengerList().stream().anyMatch(FacehuggerEntity.class::isInstance)
				|| this.isEggmorphing() == true) {
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

	@Inject(method = { "clearStatusEffects" }, at = { @At("HEAD") }, cancellable = true)
	public void noMilking(CallbackInfoReturnable<Boolean> callbackInfo) {
		if (this.hasStatusEffect(GigStatusEffects.ACID) || this.hasStatusEffect(GigStatusEffects.DNA))
			callbackInfo.setReturnValue(false);
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
		Block cameraBlock = this.world.getBlockState(this.getBlockPos()).getBlock();
		Block pos = this.getBlockStateAtPos().getBlock();
		boolean isCoveredInResin = cameraBlock == GIgBlocks.NEST_RESIN_WEB_CROSS
				|| pos == GIgBlocks.NEST_RESIN_WEB_CROSS;
		boolean notAlien = !(((Object) this) instanceof AlienEntity);
		if (((((Object) this)instanceof PlayerEntity playerEntity && (playerEntity.isCreative() || this.isSpectator())))
				&& !(((Object) this) instanceof AlienEntity)) {
			return false;
		}
		if (ConfigAccessor.isTargetBlacklisted(FacehuggerEntity.class, this)) {
			return false;
		}
		if (EntityUtils.isFacehuggerAttached(this)) {
			return false;
		}
		return notAlien && isCoveredInResin;
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
