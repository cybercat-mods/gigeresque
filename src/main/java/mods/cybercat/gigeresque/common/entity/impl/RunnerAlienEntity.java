package mods.cybercat.gigeresque.common.entity.impl;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.config.GigeresqueConfig;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.ai.enums.AlienAttackType;
import mods.cybercat.gigeresque.common.entity.ai.goal.AlienMeleeAttackGoal;
import mods.cybercat.gigeresque.common.entity.attribute.AlienEntityAttributes;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.SoundKeyframeEvent;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class RunnerAlienEntity extends AdultAlienEntity {

	private AnimationFactory animationFactory = new AnimationFactory(this);

	public RunnerAlienEntity(EntityType<? extends AlienEntity> type, World world) {
		super(type, world);
	}

	public static DefaultAttributeContainer.Builder createAttributes() {
		return LivingEntity.createLivingAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 80.0)
				.add(EntityAttributes.GENERIC_ARMOR, 4.0).add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 0.0)
				.add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.0)
				.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32.0)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.13000000417232513)
				.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 7.0 * Constants.getIsolationModeDamageBase())
				.add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.0)
				.add(AlienEntityAttributes.INTELLIGENCE_ATTRIBUTE, 0.5);
	}

	@Override
	public void tick() {
		super.tick();

		// Attack logic

		if (attackProgress > 0) {
			attackProgress--;

			if (!world.isClient && attackProgress <= 0) {
				setCurrentAttackType(AlienAttackType.NONE);
			}
		}

		if (attackProgress == 0 && handSwinging) {
			attackProgress = 10;
		}

		if (!world.isClient && getCurrentAttackType() == AlienAttackType.NONE) {
			setCurrentAttackType(switch (random.nextInt(5)) {
			case 0 -> AlienAttackType.CLAW_LEFT;
			case 1 -> AlienAttackType.CLAW_RIGHT;
			case 2 -> AlienAttackType.TAIL_LEFT;
			case 3 -> AlienAttackType.TAIL_RIGHT;
			default -> AlienAttackType.CLAW_LEFT;
			});
		}
	}

	@Override
	public float getGrowthMultiplier() {
		return GigeresqueConfig.runnerAlienGrowthMultiplier;
	}

	@Override
	protected void initGoals() {
		super.initGoals();
		this.goalSelector.add(2, new AlienMeleeAttackGoal(this, 3.0, false));
	}

	/*
	 * ANIMATIONS
	 */

	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
		var velocityLength = this.getVelocity().horizontalLength();
		var isDead = this.dead || this.getHealth() < 0.01 || this.isDead();
		if (velocityLength >= 0.000000001 && !this.isCrawling() && this.isExecuting() == false && !isDead
				&& this.isStatis() == false) {
			if (lastLimbDistance > 0.35F && this.getFirstPassenger() == null) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("run", true)
						.addAnimation(AlienAttackType.animationMappings.get(getCurrentAttackType()), false));
				return PlayState.CONTINUE;
			} else if (this.isExecuting() == false && lastLimbDistance < 0.35F
					|| (!this.isCrawling() && !this.onGround)) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("walk", true)
						.addAnimation(AlienAttackType.animationMappings.get(getCurrentAttackType()), false));
				return PlayState.CONTINUE;
			}
		} else if (this.isCrawling() && this.isExecuting() == false && this.isStatis() == false) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("crawl", true));
			return PlayState.CONTINUE;
		} else if (isDead) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("death", true));
			return PlayState.CONTINUE;
		} else {
			if (isSearching && !this.isAttacking() && this.isStatis() == false) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("ambient", false));
				return PlayState.CONTINUE;
			} else if (this.isStatis() == true || this.isAiDisabled() && !isDead) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("stasis", true));
				return PlayState.CONTINUE;
			} else if (this.isStatis() == false) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("idle_land", true));
				return PlayState.CONTINUE;
			}
		}
		return PlayState.CONTINUE;
	}

	private <E extends IAnimatable> PlayState attackPredicate(AnimationEvent<E> event) {
		if (getCurrentAttackType() != AlienAttackType.NONE && attackProgress > 0) {
			event.getController().setAnimation(new AnimationBuilder()
					.addAnimation(AlienAttackType.animationMappings.get(getCurrentAttackType()), true));
			return PlayState.CONTINUE;
		}

		return PlayState.STOP;
	}

	private <E extends IAnimatable> PlayState hissPredicate(AnimationEvent<E> event) {
		var isDead = this.dead || this.getHealth() < 0.01 || this.isDead();
		if (this.dataTracker.get(IS_HISSING) == true && !isDead) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("hiss_sound", true));
			return PlayState.CONTINUE;
		}

		return PlayState.STOP;
	}

	private <ENTITY extends IAnimatable> void soundStepListener(SoundKeyframeEvent<ENTITY> event) {
		if (event.sound.matches("footstepSoundkey")) {
			if (this.world.isClient) {
				this.getEntityWorld().playSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_FOOTSTEP,
						SoundCategory.HOSTILE, 0.5F, 1.0F, true);
			}
		}
		if (event.sound.matches("handstepSoundkey")) {
			if (this.world.isClient) {
				this.getEntityWorld().playSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_HANDSTEP,
						SoundCategory.HOSTILE, 0.5F, 1.0F, true);
			}
		}
		if (event.sound.matches("idleSoundkey")) {
			if (this.world.isClient) {
				this.getEntityWorld().playSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_AMBIENT,
						SoundCategory.HOSTILE, 1.0F, 1.0F, true);
			}
		}
	}

	private <ENTITY extends IAnimatable> void soundAttackListener(SoundKeyframeEvent<ENTITY> event) {
		if (event.sound.matches("clawSoundkey")) {
			if (this.world.isClient) {
				this.getEntityWorld().playSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_CLAW,
						SoundCategory.HOSTILE, 0.25F, 1.0F, true);
			}
		}
		if (event.sound.matches("tailSoundkey")) {
			if (this.world.isClient) {
				this.getEntityWorld().playSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_TAIL,
						SoundCategory.HOSTILE, 0.25F, 1.0F, true);
			}
		}
	}

	private <ENTITY extends IAnimatable> void soundHissListener(SoundKeyframeEvent<ENTITY> event) {
		if (event.sound.matches("hissSoundkey")) {
			if (this.world.isClient) {
				this.getEntityWorld().playSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_HISS,
						SoundCategory.HOSTILE, 1.0F, 1.0F, true);
			}
		}
	}

	@Override
	public void registerControllers(AnimationData data) {
		AnimationController<RunnerAlienEntity> main = new AnimationController<RunnerAlienEntity>(this, "controller",
				10f, this::predicate);
		main.registerSoundListener(this::soundStepListener);
		data.addAnimationController(main);
		AnimationController<RunnerAlienEntity> attacking = new AnimationController<RunnerAlienEntity>(this,
				"attackController", 0f, this::attackPredicate);
		attacking.registerSoundListener(this::soundAttackListener);
		data.addAnimationController(attacking);
		AnimationController<RunnerAlienEntity> hissing = new AnimationController<RunnerAlienEntity>(this,
				"hissController", 10f, this::hissPredicate);
		hissing.registerSoundListener(this::soundHissListener);
		data.addAnimationController(hissing);
		data.addAnimationController(new AnimationController<>(this, "controller", 10f, this::predicate));
		data.addAnimationController(new AnimationController<>(this, "attackController", 0f, this::attackPredicate));
		data.addAnimationController(new AnimationController<>(this, "hissController", 10f, this::hissPredicate));
	}

	@Override
	public AnimationFactory getFactory() {
		return animationFactory;
	}
}
