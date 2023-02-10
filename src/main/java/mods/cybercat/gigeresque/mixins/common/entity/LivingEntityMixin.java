package mods.cybercat.gigeresque.mixins.common.entity;

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
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * @author Boston Vanseghi
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Host, Eggmorphable {

	private static final EntityDataAccessor<Boolean> IS_BLEEDING = SynchedEntityData.defineId(LivingEntity.class,
			EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Float> EGGMORPH_TICKS = SynchedEntityData.defineId(LivingEntity.class,
			EntityDataSerializers.FLOAT);
	public float ticksUntilImpregnation = -1.0f;
	public float ticksUntilEggmorpth = -1.0f;
	public boolean hasParasiteSpawned = false;
	public boolean hasEggSpawned = false;

	public LivingEntityMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@Shadow
	abstract boolean hasEffect(MobEffect effect);

	@Shadow
	abstract boolean addEffect(MobEffectInstance effect);

	@Shadow
	public abstract boolean hurt(DamageSource source, float amount);

	@Shadow
	public abstract boolean isDeadOrDying();

	@Shadow
	public abstract float getMaxHealth();

	@Shadow
	public abstract boolean isAlive();

	@Shadow
	public abstract float getHealth();

	@Shadow
	public abstract void kill();

	@Shadow
	public abstract MobType getMobType();

	private void handleStatusEffect(long offset, MobEffect statusEffect, Boolean checkStatusEffect) {
		if (ticksUntilImpregnation < offset && (!checkStatusEffect || !hasEffect(statusEffect))) {
			var amplifier = (int) (((Constants.TPD - (Constants.TPM * 8L)) - ticksUntilImpregnation)
					/ (Constants.TPS * 30));
			this.addEffect(new MobEffectInstance(statusEffect, (int) ticksUntilImpregnation, amplifier, true, true));
		}
	}

	@Inject(method = { "doPush" }, at = { @At("HEAD") }, cancellable = true)
	void pushAway(CallbackInfo callbackInfo) {
		if (this.isEggmorphing() && ConfigAccessor.isTargetAlienHost(this))
			callbackInfo.cancel();
	}

	@Inject(method = { "tick" }, at = { @At("HEAD") })
	void tick(CallbackInfo callbackInfo) {
		if (this.isAlive() && this.getCommandSenderWorld().isClientSide && Boolean.TRUE.equals(isBleeding())) {
			var yOffset = this.getEyeY() - ((this.getEyeY() - this.blockPosition().getY()) / 2.0);
			var customX = this.getX() + ((random.nextDouble() / 2.0) - 0.5) * (random.nextBoolean() ? -1 : 1);
			var customZ = this.getZ() + ((random.nextDouble() / 2.0) - 0.5) * (random.nextBoolean() ? -1 : 1);

			for (int i = 0; i < 1 + (int) (this.getMaxHealth() - this.getHealth()); i++)
				this.getCommandSenderWorld().addAlwaysVisibleParticle(Particles.BLOOD, customX, yOffset, customZ, 0.0,
						-0.15, 0.0);
		}
		if (!this.level.isClientSide)
			if ((this.level.getFluidState(this.blockPosition()).getType() == GigFluids.BLACK_FLUID_STILL
					|| this.level.getFluidState(this.blockPosition()).getType() == GigFluids.BLACK_FLUID_FLOWING)
					&& !ConfigAccessor.isTargetDNAImmune(this)) {
				if (!this.hasEffect(GigStatusEffects.DNA) && !(((Object) this) instanceof Player)
						&& !(((Object) this) instanceof AlienEntity) && !(((Object) this) instanceof Creeper)
						&& !(ConfigAccessor.isTargetDNAImmune(this)))
					this.addEffect(
							new MobEffectInstance(GigStatusEffects.DNA, GigeresqueConfig.getgooEffectTickTimer(), 0));
				if (!this.hasEffect(GigStatusEffects.DNA) && ((Object) this) instanceof Creeper
						&& !(((Object) this) instanceof Player) && !(((Object) this) instanceof AlienEntity))
					this.addEffect(new MobEffectInstance(GigStatusEffects.DNA, 60000, 0));
				if (!this.hasEffect(GigStatusEffects.DNA)
						&& (((Object) this)instanceof Player playerEntity
								&& !(playerEntity.isCreative() || this.isSpectator()))
						&& !(((Object) this) instanceof AlienEntity))
					this.addEffect(
							new MobEffectInstance(GigStatusEffects.DNA, GigeresqueConfig.getgooEffectTickTimer(), 0));
			}

		if (!this.level.isClientSide)
			if (((((Object) this)instanceof Player playerEntity && (playerEntity.isCreative() || this.isSpectator()))
					|| level.getDifficulty() == Difficulty.PEACEFUL) || (((Object) this) instanceof AlienEntity)
					|| ConfigAccessor.isTargetBlacklisted(FacehuggerEntity.class, this)) {
				removeParasite();
				resetEggmorphing();
				setBleeding(false);
			}
		if (ConfigAccessor.isTargetHostable(this)) {
			handleEggingLogic();
			handleHostLogic();
		}
	}

	@Inject(method = { "isUsingItem" }, at = { @At("RETURN") })
	public boolean isUsingItem(CallbackInfoReturnable<Boolean> callbackInfo) {
		if (this.getPassengers().stream().anyMatch(FacehuggerEntity.class::isInstance) || this.isEggmorphing())
			return false;
		return callbackInfo.getReturnValue();
	}

	@Inject(method = { "isPushable" }, at = { @At("RETURN") })
	public boolean noPush(CallbackInfoReturnable<Boolean> callbackInfo) {
		if (this.isEggmorphing() && ConfigAccessor.isTargetAlienHost(this))
			return false;
		return callbackInfo.getReturnValue();
	}

	private void handleEggingLogic() {
		if (isEggmorphing() && ConfigAccessor.isTargetAlienHost(this))
			setTicksUntilEggmorphed(ticksUntilEggmorpth++);
		else
			resetEggmorphing();

		if (getTicksUntilEggmorphed() == GigeresqueConfig.getEggmorphTickTimer() && !this.isDeadOrDying()) {
			var egg = new AlienEggEntity(Entities.EGG, level);
			egg.moveTo(this.blockPosition(), this.getYRot(), this.getXRot());
			level.addFreshEntity(egg);
			level.destroyBlock(this.blockPosition(), false);
			hasEggSpawned = true;
			hurt(GigDamageSources.EGGMORPHING, Float.MAX_VALUE);
		}
	}

	private void handleHostLogic() {
		if (hasParasite()) {
			ticksUntilImpregnation = Math.max(ticksUntilImpregnation - 1.0F, 0f);

			if (Boolean.TRUE.equals(!isBleeding()) && ticksUntilImpregnation >= 0
					&& ticksUntilImpregnation < Constants.TPS * 30L)
				setBleeding(true);

			handleStatusEffect(Constants.TPM * 12L, MobEffects.HUNGER, false);
			handleStatusEffect(Constants.TPM * 7L, MobEffects.WEAKNESS, true);
			handleStatusEffect(Constants.TPM * 2L, MobEffects.DIG_SLOWDOWN, true);
		}

		if (ticksUntilImpregnation == 0L) {
			if (tickCount % Constants.TPS == 0L)
				this.hurt(GigDamageSources.CHESTBURSTING, this.getMaxHealth() / 8f);

			if (this.isDeadOrDying() && !hasParasiteSpawned) {
				var identifier = BuiltInRegistries.ENTITY_TYPE.getKey(this.getType());
				var morphMappings = ConfigAccessor.getReversedMorphMappings();
				var producedVariant = morphMappings.getOrDefault(identifier.toString(),
						EntityIdentifiers.ALIEN.toString());

				ChestbursterEntity burster = switch (producedVariant) {
				case Gigeresque.MOD_ID + ":runner_alien" -> new RunnerbursterEntity(Entities.RUNNERBURSTER, this.level);
				case Gigeresque.MOD_ID
						+ ":aquatic_alien" -> new AquaticChestbursterEntity(Entities.AQUATIC_CHESTBURSTER, this.level);
				default -> new ChestbursterEntity(Entities.CHESTBURSTER, this.level);
				};
				if (level.isClientSide)
					this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.CHESTBURSTING,
							SoundSource.NEUTRAL, 2.0f, 1.0f, true);

				burster.setHostId(identifier.toString());
				burster.moveTo(this.blockPosition(), this.getYRot(), this.getXRot());

				if (this.hasCustomName())
					burster.setCustomName(this.getCustomName());
				burster.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 10), burster);
				burster.setBirthStatus(true);
				this.level.addFreshEntity(burster);
				hasParasiteSpawned = true;
			}
		}
	}

	@Inject(method = { "isImmobile" }, at = { @At("RETURN") })
	protected boolean isImmobile(CallbackInfoReturnable<Boolean> callbackInfo) {
		if (this.getPassengers().stream().anyMatch(FacehuggerEntity.class::isInstance) || this.isEggmorphing() == true)
			return true;
		return callbackInfo.getReturnValue();
	}

	@Inject(method = { "defineSynchedData" }, at = { @At("RETURN") })
	void defineSynchedData(CallbackInfo callbackInfo) {
		entityData.define(IS_BLEEDING, false);
		entityData.define(EGGMORPH_TICKS, -1.0f);
	}

	@Inject(method = { "addAdditionalSaveData" }, at = { @At("RETURN") })
	void addAdditionalSaveData(CompoundTag nbt, CallbackInfo callbackInfo) {
		nbt.putFloat("ticksUntilImpregnation", ticksUntilImpregnation);
		nbt.putFloat("ticksUntilEggmorphed", getTicksUntilEggmorphed());
		nbt.putBoolean("isBleeding", isBleeding());
	}

	@Inject(method = { "readAdditionalSaveData" }, at = { @At("RETURN") })
	void readAdditionalSaveData(CompoundTag nbt, CallbackInfo callbackInfo) {
		if (nbt.contains("ticksUntilImpregnation"))
			ticksUntilImpregnation = nbt.getInt("ticksUntilImpregnation");
		if (nbt.contains("ticksUntilEggmorphed"))
			setTicksUntilEggmorphed(nbt.getInt("ticksUntilEggmorphed"));
		if (nbt.contains("isBleeding"))
			setBleeding(nbt.getBoolean("isBleeding"));
	}

	@Inject(method = { "hurt" }, at = { @At("HEAD") }, cancellable = true)
	public void hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (this.getPassengers().stream().anyMatch(FacehuggerEntity.class::isInstance)
				&& (source == DamageSource.DROWN || source == DamageSource.IN_WALL)) {
			callbackInfo.setReturnValue(false);
			callbackInfo.cancel();
		}
	}

	@Inject(method = { "removeAllEffects" }, at = { @At("HEAD") }, cancellable = true)
	public void noMilkRemoval(CallbackInfoReturnable<Boolean> callbackInfo) {
		if (this.hasEffect(GigStatusEffects.ACID) || this.hasEffect(GigStatusEffects.DNA))
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
		var cameraBlock = this.level.getBlockState(this.blockPosition()).getBlock();
		var pos = this.getFeetBlockState().getBlock();
		var isCoveredInResin = cameraBlock == GIgBlocks.NEST_RESIN_WEB_CROSS || pos == GIgBlocks.NEST_RESIN_WEB_CROSS;
		var notAlien = !(((Object) this) instanceof AlienEntity);
		var notHost = ConfigAccessor.isTargetAlienHost(this);
		if (((((Object) this)instanceof Player playerEntity && (playerEntity.isCreative() || this.isSpectator())))
				&& !(((Object) this) instanceof AlienEntity))
			return false;
		if (ConfigAccessor.isTargetBlacklisted(FacehuggerEntity.class, this))
			return false;
		if (EntityUtils.isFacehuggerAttached(this))
			return false;
		if (!ConfigAccessor.isTargetAlienHost(this))
			return false;
		return notAlien && isCoveredInResin && notHost;
	}

	@Override
	public float getTicksUntilEggmorphed() {
		return entityData.get(EGGMORPH_TICKS);
	}

	@Override
	public void setTicksUntilEggmorphed(float ticksUntilEggmorphed) {
		this.entityData.set(EGGMORPH_TICKS, ticksUntilEggmorphed);
	}

	@Override
	public boolean isBleeding() {
		return entityData.get(IS_BLEEDING);
	}

	@Override
	public void setBleeding(boolean isBleeding) {
		entityData.set(IS_BLEEDING, isBleeding);
	}
}
