package mods.cybercat.gigeresque.common.entity.impl;

import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager.ControllerRegistrar;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.util.AzureLibUtil;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.block.GIgBlocks;
import mods.cybercat.gigeresque.common.config.ConfigAccessor;
import mods.cybercat.gigeresque.common.config.GigeresqueConfig;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.Entities;
import mods.cybercat.gigeresque.common.entity.helper.GigAnimationsDefault;
import mods.cybercat.gigeresque.common.entity.helper.Growable;
import mods.cybercat.gigeresque.common.util.EntityUtils;
import mods.cybercat.gigeresque.interfacing.Eggmorphable;
import mods.cybercat.gigeresque.interfacing.Host;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;

public class RunnerbursterEntity extends ChestbursterEntity implements GeoEntity, Growable {

	private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);

	public RunnerbursterEntity(EntityType<? extends RunnerbursterEntity> type, Level level) {
		super(type, level);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return LivingEntity.createLivingAttributes().add(Attributes.MAX_HEALTH, GigeresqueConfig.runnerbusterHealth)
				.add(Attributes.ARMOR, 2.0).add(Attributes.ARMOR_TOUGHNESS, 0.0)
				.add(Attributes.KNOCKBACK_RESISTANCE, 0.0).add(Attributes.FOLLOW_RANGE, 16.0)
				.add(Attributes.MOVEMENT_SPEED, 0.23000000417232513)
				.add(Attributes.ATTACK_DAMAGE, GigeresqueConfig.runnerbusterAttackDamage)
				.add(Attributes.ATTACK_KNOCKBACK, 0.3);
	}

	@Override
	protected int getAcidDiameter() {
		return 1;
	}

	/*
	 * GROWTH
	 */
	@Override
	public float getGrowthMultiplier() {
		return GigeresqueConfig.runnerbursterGrowthMultiplier;
	}

	@Override
	public float getMaxGrowth() {
		return Constants.TPD / 2.0f;
	}

	@Override
	public void tick() {
		super.tick();
		if (this.isBirthed() == true && this.tickCount > 1200 && this.getGrowth() > 200)
			this.setBirthStatus(false);
	}

	@Override
	public LivingEntity growInto() {
		if (hostId == null)
			return new ClassicAlienEntity(Entities.ALIEN, level);

		var variantId = ConfigAccessor.getReversedMorphMappings().get(hostId);
		if (variantId == null)
			return new ClassicAlienEntity(Entities.ALIEN, level);
		var identifier = new ResourceLocation(variantId);
		var entityType = Registry.ENTITY_TYPE.getOptional(identifier).orElse(null);
		if (entityType == null)
			return new ClassicAlienEntity(Entities.ALIEN, level);
		var entity = entityType.create(level);

		if (hasCustomName())
			if (entity != null)
				entity.setCustomName(getCustomName());

		return (LivingEntity) entity;
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.35, false));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, true,
				entity -> !((entity instanceof AlienEntity || entity instanceof Warden || entity instanceof ArmorStand)
						|| (entity.getVehicle() != null
								&& entity.getVehicle().getSelfAndPassengers().anyMatch(AlienEntity.class::isInstance))
						|| (entity instanceof AlienEggEntity) || ((Host) entity).isBleeding() ||!(entity instanceof LivingEntity)
						|| ((Host) entity).hasParasite() || ((Eggmorphable) entity).isEggmorphing()
						|| (EntityUtils.isFacehuggerAttached(entity))
						|| (entity.getFeetBlockState().getBlock() == GIgBlocks.NEST_RESIN_WEB_CROSS))
						&& !ConfigAccessor.isTargetBlacklisted(FacehuggerEntity.class, entity) && entity.isAlive()));
	}

	/*
	 * ANIMATIONS
	 */
	@Override
	public void registerControllers(ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>(this, "livingController", 5, event -> {
			var isDead = this.dead || this.getHealth() < 0.01 || this.isDeadOrDying();
			if (event.isMoving() && !isDead)
				if (animationSpeedOld >= 0.35F)
					return event.setAndContinue(GigAnimationsDefault.RUN);
				else
					return event.setAndContinue(GigAnimationsDefault.WALK);
			else if (((this.getTarget() != null && this.doHurtTarget(getTarget()))
					|| (this.entityData.get(EAT) == true)) && !this.isDeadOrDying())
				return event.setAndContinue(GigAnimationsDefault.CHOMP);
			else if (isDead)
				return event.setAndContinue(GigAnimationsDefault.DEATH);
			else if (this.entityData.get(BIRTHED) == true)
				return event.setAndContinue(GigAnimationsDefault.BIRTH);
			else
				return event.setAndContinue(GigAnimationsDefault.IDLE);
		}));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}

	@Override
	public void onSignalReceive(ServerLevel var1, GameEventListener var2, BlockPos var3, GameEvent var4, Entity var5,
			Entity var6, float var7) {
		super.onSignalReceive(var1, var2, var3, var4, var5, var6, var7);
		this.getNavigation().moveTo(var3.getX(), var3.getY(), var3.getZ(), 1.2F);
	}
}
