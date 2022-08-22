package mods.cybercat.gigeresque.common.entity.impl;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.block.GIgBlocks;
import mods.cybercat.gigeresque.common.config.ConfigAccessor;
import mods.cybercat.gigeresque.common.config.GigeresqueConfig;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.Entities;
import mods.cybercat.gigeresque.common.entity.Growable;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.util.EntityUtils;
import mods.cybercat.gigeresque.interfacing.Eggmorphable;
import mods.cybercat.gigeresque.interfacing.Host;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.SoundKeyframeEvent;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class RunnerbursterEntity extends ChestbursterEntity implements IAnimatable, Growable, IAnimationTickable {

	public RunnerbursterEntity(EntityType<? extends RunnerbursterEntity> type, World world) {
		super(type, world);
	}

	public static DefaultAttributeContainer.Builder createAttributes() {
		return LivingEntity.createLivingAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 15.0)
				.add(EntityAttributes.GENERIC_ARMOR, 2.0).add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 0.0)
				.add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.0)
				.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16.0)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.23000000417232513)
				.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5.0).add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 0.3);
	}

	private AnimationFactory animationFactory = new AnimationFactory(this);

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
		if (this.isBirthed() == true && this.age > 30) {
			this.setBirthStatus(false);
		}
	}

	@Override
	public LivingEntity growInto() {
		if (hostId == null) {
			return new ClassicAlienEntity(Entities.ALIEN, world);
		}

		var variantId = ConfigAccessor.getReversedMorphMappings().get(hostId);
		if (variantId == null) {
			return new ClassicAlienEntity(Entities.ALIEN, world);
		}
		var identifier = new Identifier(variantId);
		var entityType = Registry.ENTITY_TYPE.getOrEmpty(identifier).orElse(null);
		if (entityType == null) {
			return new ClassicAlienEntity(Entities.ALIEN, world);
		}
		var entity = entityType.create(world);

		if (hasCustomName()) {
			if (entity != null) {
				entity.setCustomName(getCustomName());
			}
		}

		return (LivingEntity) entity;
	}

	@Override
	protected void initGoals() {
		super.initGoals();
		this.goalSelector.add(2, new MeleeAttackGoal(this, 1.35, false));
		this.targetSelector.add(2,
				new ActiveTargetGoal<>(this, LivingEntity.class, true,
						entity -> !((entity instanceof AlienEntity) || (entity instanceof WardenEntity)
								|| (entity instanceof AlienEggEntity) || ((Host) entity).isBleeding()
								|| (entity instanceof HostileEntity) || ((Eggmorphable) entity).isEggmorphing()
								|| (EntityUtils.isFacehuggerAttached(entity))
								|| (entity.getBlockStateAtPos().getBlock() == GIgBlocks.NEST_RESIN_WEB_CROSS))));
	}

	/*
	 * ANIMATIONS
	 */

	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
		var velocityLength = this.getVelocity().horizontalLength();
		var isDead = this.dead || this.getHealth() < 0.01 || this.isDead();
		if (velocityLength >= 0.000000001 && !isDead && lastLimbDistance > 0.15F) {
			if (lastLimbDistance >= 0.35F) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("run", true));
				return PlayState.CONTINUE;
			} else {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("walk", true));
				return PlayState.CONTINUE;
			}
		} else if (((this.getTarget() != null && this.tryAttack(getTarget())) || (this.dataTracker.get(EAT) == true))
				&& !this.isDead()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("chomp", false));
			return PlayState.CONTINUE;
		} else if (isDead) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("death", true));
			return PlayState.CONTINUE;
		} else {
			if (this.dataTracker.get(BIRTHED) == true) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("birth", false));
				return PlayState.CONTINUE;
			} else {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
				return PlayState.CONTINUE;
			}
		}
	}

	private <ENTITY extends IAnimatable> void soundStepListener(SoundKeyframeEvent<ENTITY> event) {
		if (event.sound.matches("stepSoundkey")) {
			if (this.world.isClient) {
				this.getEntityWorld().playSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_STEP,
						SoundCategory.HOSTILE, 0.5F, 1.0F, true);
			}
		}
		if (event.sound.matches("idleSoundkey")) {
			if (this.world.isClient) {
				this.getEntityWorld().playSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_AMBIENT,
						SoundCategory.HOSTILE, 1.0F, 1.0F, true);
			}
		}
		if (event.sound.matches("attackSoundkey")) {
			if (this.world.isClient) {
				this.getEntityWorld().playSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_ATTACK,
						SoundCategory.HOSTILE, 0.5F, 1.0F, true);
			}
		}
	}

	@Override
	public void registerControllers(AnimationData data) {
		AnimationController<RunnerbursterEntity> main = new AnimationController<RunnerbursterEntity>(this, "controller",
				10f, this::predicate);
		main.registerSoundListener(this::soundStepListener);
		data.addAnimationController(main);
	}

	@Override
	public AnimationFactory getFactory() {
		return animationFactory;
	}

	@Override
	public int tickTimer() {
		return age;
	}
}
