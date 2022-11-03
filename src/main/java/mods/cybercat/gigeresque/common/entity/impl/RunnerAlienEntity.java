package mods.cybercat.gigeresque.common.entity.impl;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.config.GigeresqueConfig;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.ai.enums.AlienAttackType;
import mods.cybercat.gigeresque.common.entity.ai.goal.AlienMeleeAttackGoal;
import mods.cybercat.gigeresque.common.entity.attribute.AlienEntityAttributes;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.SoundKeyframeEvent;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class RunnerAlienEntity extends AdultAlienEntity {

	private AnimationFactory animationFactory = GeckoLibUtil.createFactory(this);

	public RunnerAlienEntity(EntityType<? extends AlienEntity> type, Level world) {
		super(type, world);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return LivingEntity.createLivingAttributes().add(Attributes.MAX_HEALTH, 80.0)
				.add(Attributes.ARMOR, 4.0).add(Attributes.ARMOR_TOUGHNESS, 0.0)
				.add(Attributes.KNOCKBACK_RESISTANCE, 0.0)
				.add(Attributes.FOLLOW_RANGE, 32.0)
				.add(Attributes.MOVEMENT_SPEED, 0.13000000417232513)
				.add(Attributes.ATTACK_DAMAGE, 7.0 * Constants.getIsolationModeDamageBase())
				.add(Attributes.ATTACK_KNOCKBACK, 1.0)
				.add(AlienEntityAttributes.INTELLIGENCE_ATTRIBUTE, 0.5);
	}

	@Override
	public void tick() {
		super.tick();

		// Attack logic

		if (attackProgress > 0) {
			attackProgress--;

			if (!level.isClientSide && attackProgress <= 0) {
				setCurrentAttackType(AlienAttackType.NONE);
			}
		}

		if (attackProgress == 0 && swinging) {
			attackProgress = 10;
		}

		if (!level.isClientSide && getCurrentAttackType() == AlienAttackType.NONE) {
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
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(2, new AlienMeleeAttackGoal(this, 3.0, false));
	}

	/*
	 * ANIMATIONS
	 */

	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
		var velocityLength = this.getDeltaMovement().horizontalDistance();
		var isDead = this.dead || this.getHealth() < 0.01 || this.isDeadOrDying();
		if (velocityLength >= 0.000000001 && !this.isCrawling() && this.isExecuting() == false && !isDead
				&& this.isStatis() == false) {
			if (animationSpeedOld > 0.35F && this.getFirstPassenger() == null) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("run", EDefaultLoopTypes.LOOP)
						.addAnimation(AlienAttackType.animationMappings.get(getCurrentAttackType()), EDefaultLoopTypes.PLAY_ONCE));
				return PlayState.CONTINUE;
			} else if (this.isExecuting() == false && animationSpeedOld < 0.35F
					|| (!this.isCrawling() && !this.onGround)) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("walk", EDefaultLoopTypes.LOOP)
						.addAnimation(AlienAttackType.animationMappings.get(getCurrentAttackType()), EDefaultLoopTypes.PLAY_ONCE));
				return PlayState.CONTINUE;
			}
		} else if (this.isCrawling() && this.isExecuting() == false && this.isStatis() == false) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("crawl", EDefaultLoopTypes.LOOP));
			return PlayState.CONTINUE;
		} else if (isDead) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("death", EDefaultLoopTypes.LOOP));
			return PlayState.CONTINUE;
		} else {
			if (isSearching && !this.isAggressive() && this.isStatis() == false) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("ambient", EDefaultLoopTypes.PLAY_ONCE));
				return PlayState.CONTINUE;
			} else if (this.isStatis() == true || this.isNoAi() && !isDead) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("stasis", EDefaultLoopTypes.LOOP));
				return PlayState.CONTINUE;
			} else if (this.isStatis() == false) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("idle_land", EDefaultLoopTypes.LOOP));
				return PlayState.CONTINUE;
			}
		}
		return PlayState.CONTINUE;
	}

	private <E extends IAnimatable> PlayState attackPredicate(AnimationEvent<E> event) {
		if (getCurrentAttackType() != AlienAttackType.NONE && attackProgress > 0) {
			event.getController().setAnimation(new AnimationBuilder()
					.addAnimation(AlienAttackType.animationMappings.get(getCurrentAttackType()), EDefaultLoopTypes.LOOP));
			return PlayState.CONTINUE;
		}

		return PlayState.STOP;
	}

	private <E extends IAnimatable> PlayState hissPredicate(AnimationEvent<E> event) {
		var isDead = this.dead || this.getHealth() < 0.01 || this.isDeadOrDying();
		if (this.entityData.get(IS_HISSING) == true && !isDead) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("hiss_sound", EDefaultLoopTypes.LOOP));
			return PlayState.CONTINUE;
		}

		return PlayState.STOP;
	}

	private <ENTITY extends IAnimatable> void soundStepListener(SoundKeyframeEvent<ENTITY> event) {
		if (event.sound.matches("footstepSoundkey")) {
			if (this.level.isClientSide) {
				this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_FOOTSTEP,
						SoundSource.HOSTILE, 0.5F, 1.0F, true);
			}
		}
		if (event.sound.matches("handstepSoundkey")) {
			if (this.level.isClientSide) {
				this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_HANDSTEP,
						SoundSource.HOSTILE, 0.5F, 1.0F, true);
			}
		}
		if (event.sound.matches("idleSoundkey")) {
			if (this.level.isClientSide) {
				this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_AMBIENT,
						SoundSource.HOSTILE, 1.0F, 1.0F, true);
			}
		}
	}

	private <ENTITY extends IAnimatable> void soundAttackListener(SoundKeyframeEvent<ENTITY> event) {
		if (event.sound.matches("clawSoundkey")) {
			if (this.level.isClientSide) {
				this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_CLAW,
						SoundSource.HOSTILE, 0.25F, 1.0F, true);
			}
		}
		if (event.sound.matches("tailSoundkey")) {
			if (this.level.isClientSide) {
				this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_TAIL,
						SoundSource.HOSTILE, 0.25F, 1.0F, true);
			}
		}
	}

	private <ENTITY extends IAnimatable> void soundHissListener(SoundKeyframeEvent<ENTITY> event) {
		if (event.sound.matches("hissSoundkey")) {
			if (this.level.isClientSide) {
				this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_HISS,
						SoundSource.HOSTILE, 1.0F, 1.0F, true);
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
