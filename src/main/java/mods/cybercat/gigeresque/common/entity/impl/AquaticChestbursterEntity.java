package mods.cybercat.gigeresque.common.entity.impl;

import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager.ControllerRegistrar;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.util.AzureLibUtil;
import mods.cybercat.gigeresque.common.entity.Entities;
import mods.cybercat.gigeresque.common.entity.ai.pathing.AmphibiousNavigation;
import mods.cybercat.gigeresque.common.entity.ai.pathing.CrawlerNavigation;
import mods.cybercat.gigeresque.common.entity.helper.GigAnimationsDefault;
import mods.cybercat.gigeresque.common.entity.helper.Growable;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

public class AquaticChestbursterEntity extends ChestbursterEntity implements GeoEntity, Growable {

	private final GroundPathNavigation landNavigation = new CrawlerNavigation(this, level);
	private final AmphibiousNavigation swimNavigation = new AmphibiousNavigation(this, level);
	private final MoveControl landMoveControl = new MoveControl(this);
	private final LookControl landLookControl = new LookControl(this);
	private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);
	private final SmoothSwimmingMoveControl swimMoveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.7f, 1.0f, false);
	private final SmoothSwimmingLookControl swimLookControl = new SmoothSwimmingLookControl(this, 10);

	public AquaticChestbursterEntity(EntityType<? extends AquaticChestbursterEntity> type, Level world) {
		super(type, world);
		maxUpStep = 1.0f;

		navigation = swimNavigation;
		moveControl = swimMoveControl;
		lookControl = swimLookControl;
		setPathfindingMalus(BlockPathTypes.WATER, 0.0f);
	}

	@Override
	public void travel(Vec3 movementInput) {
		this.navigation = (this.isUnderWater() || this.isInWater()) ? swimNavigation : landNavigation;
		this.moveControl = (this.wasEyeInWater || this.isInWater()) ? swimMoveControl : landMoveControl;
		this.lookControl = (this.wasEyeInWater || this.isInWater()) ? swimLookControl : landLookControl;

		if (this.tickCount % 10 == 0)
			this.refreshDimensions();

		if (isEffectiveAi() && this.isInWater()) {
			moveRelative(getSpeed(), movementInput);
			move(MoverType.SELF, getDeltaMovement());
			setDeltaMovement(getDeltaMovement().scale(0.9));
			if (getTarget() == null)
				setDeltaMovement(getDeltaMovement().add(0.0, -0.005, 0.0));
		} else
			super.travel(movementInput);
	}

	@Override
	public LivingEntity growInto() {
		var entity = new AquaticAlienEntity(Entities.AQUATIC_ALIEN, level);

		if (hasCustomName())
			entity.setCustomName(this.getCustomName());

		return entity;
	}

	@Override
	public boolean canBreatheUnderwater() {
		return true;
	}

	@Override
	public PathNavigation createNavigation(Level world) {
		return swimNavigation;
	}

	@Override
	public boolean isPushedByFluid() {
		return false;
	}

	@Override
	protected void jumpInLiquid(TagKey<Fluid> fluid) {
	}

	@Override
	public EntityDimensions getDimensions(Pose pose) {
		return this.wasEyeInWater ? super.getDimensions(pose).scale(1.0f, 0.5f) : super.getDimensions(pose);
	}

	/*
	 * ANIMATIONS
	 */
	@Override
	public void registerControllers(ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>(this, "livingController", 5, event -> {
			var isDead = this.dead || this.getHealth() < 0.01 || this.isDeadOrDying();
			if (event.isMoving() && !isDead && animationSpeedOld > 0.15F)
				if (this.isUnderWater())
					if (animationSpeedOld >= 0.35F)
						return event.setAndContinue(GigAnimationsDefault.RUSH_SWIM);
					else
						return event.setAndContinue(GigAnimationsDefault.SWIM);
				else if (animationSpeedOld >= 0.35F)
					return event.setAndContinue(GigAnimationsDefault.RUSH_SLITHER);
				else
					return event.setAndContinue(GigAnimationsDefault.SLITHER);
			else if (this.entityData.get(EAT) == true && !this.isDeadOrDying())
				return event.setAndContinue(GigAnimationsDefault.CHOMP);
			else if (isDead)
				return event.setAndContinue(GigAnimationsDefault.DEATH);
			else {
				if (this.tickCount < 5 && this.entityData.get(BIRTHED) == true)
					return event.setAndContinue(GigAnimationsDefault.BIRTH);
				else {
					if (this.isUnderWater())
						return event.setAndContinue(GigAnimationsDefault.IDLE_WATER);
					else
						return event.setAndContinue(GigAnimationsDefault.IDLE_LAND);
				}
			}
		}).setSoundKeyframeHandler(event -> {
			if (event.getKeyframeData().getSound().matches("stepSoundkey"))
				if (this.level.isClientSide)
					this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.BURSTER_CRAWL, SoundSource.HOSTILE, 0.25F, 1.0F, true);
		}));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}
}
