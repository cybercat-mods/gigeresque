package mods.cybercat.gigeresque.common.entity.impl;

import static java.lang.Math.max;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.config.GigeresqueConfig;
import mods.cybercat.gigeresque.common.data.handler.TrackedDataHandlers;
import mods.cybercat.gigeresque.common.entity.AlienAttackType;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.ai.pathing.AmphibiousNavigation;
import mods.cybercat.gigeresque.common.entity.attribute.AlienEntityAttributes;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.ai.control.AquaticMoveControl;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.control.YawAdjustingLookControl;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ArmorItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.tag.TagKey;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.SoundKeyframeEvent;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class AquaticAlienEntity extends AdultAlienEntity {
	private boolean isSearching = false;
	private long searchingProgress = 0L;
	private long searchingCooldown = 0L;
	private int attackProgress = 0;
	private static final TrackedData<AlienAttackType> CURRENT_ATTACK_TYPE = DataTracker
			.registerData(AquaticAlienEntity.class, TrackedDataHandlers.ALIEN_ATTACK_TYPE);
	private final AnimationFactory animationFactory = new AnimationFactory(this);
	private final MobNavigation landNavigation = new MobNavigation(this, world);
	private final AmphibiousNavigation swimNavigation = new AmphibiousNavigation(this, world);
	private final MoveControl landMoveControl = new MoveControl(this);
	private final LookControl landLookControl = new LookControl(this);
	private final AquaticMoveControl swimMoveControl = new AquaticMoveControl(this, 85, 10, 0.7f, 1.0f, false);
	private final YawAdjustingLookControl swimLookControl = new YawAdjustingLookControl(this, 10);

	public AquaticAlienEntity(EntityType<? extends AlienEntity> type, World world) {
		super(type, world);
		ignoreCameraFrustum = true;
		stepHeight = 1.0f;

		navigation = swimNavigation;
		moveControl = swimMoveControl;
		lookControl = swimLookControl;
		setPathfindingPenalty(PathNodeType.WATER, 0.0f);
	}

	public static DefaultAttributeContainer.Builder createAttributes() {
		return LivingEntity.createLivingAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 90.0)
				.add(EntityAttributes.GENERIC_ARMOR, 4.0).add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 0.0)
				.add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.0)
				.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32.0)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2500000417232513)
				.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 7.0 * Constants.getIsolationModeDamageBase())
				.add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.0)
				.add(AlienEntityAttributes.INTELLIGENCE_ATTRIBUTE, 0.85);
	}

	@Override
	public float getGrowthMultiplier() {
		return GigeresqueConfig.aquaticAlienGrowthMultiplier;
	}

	@Override
	public void travel(Vec3d movementInput) {
		this.navigation = (this.isSubmergedInWater() || this.isTouchingWater()) ? swimNavigation : landNavigation;
		this.moveControl = (this.submergedInWater || this.isTouchingWater()) ? swimMoveControl : landMoveControl;
		this.lookControl = (this.submergedInWater || this.isTouchingWater()) ? swimLookControl : landLookControl;

		if (this.age % 10 == 0) {
			this.calculateDimensions();
		}

		if (canMoveVoluntarily() && this.isTouchingWater()) {
			updateVelocity(getMovementSpeed(), movementInput);
			move(MovementType.SELF, getVelocity());
			setVelocity(getVelocity().multiply(0.75));
			if (getTarget() == null) {
				setVelocity(getVelocity().add(0.0, -0.005, 0.0));
			}
		} else {
			super.travel(movementInput);
		}
	}

	@Override
	public boolean canBreatheInWater() {
		return true;
	}

	@Override
	public EntityNavigation createNavigation(World world) {
		return swimNavigation;
	}

	@Override
	public boolean isPushedByFluids() {
		return false;
	}

	@Override
	protected void swimUpward(TagKey<Fluid> fluid) {
	}

	@Override
	public EntityDimensions getDimensions(EntityPose pose) {
		return this.submergedInWater ? super.getDimensions(pose).scaled(1.0f, 0.5f) : super.getDimensions(pose);
	}

	@Override
	protected void initGoals() {
		super.initGoals();
		this.goalSelector.add(2, new MeleeAttackGoal(this, 2.0, false));
	}

	private AlienAttackType getCurrentAttackType() {
		return dataTracker.get(CURRENT_ATTACK_TYPE);
	}

	private void setCurrentAttackType(AlienAttackType value) {
		dataTracker.set(CURRENT_ATTACK_TYPE, value);
	}

	@Override
	public void initDataTracker() {
		super.initDataTracker();
		dataTracker.startTracking(CURRENT_ATTACK_TYPE, AlienAttackType.NONE);
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
			setCurrentAttackType(switch (random.nextInt(6)) {
			case 0 -> AlienAttackType.CLAW_LEFT;
			case 1 -> AlienAttackType.CLAW_RIGHT;
			case 2 -> AlienAttackType.TAIL_LEFT;
			case 3 -> AlienAttackType.TAIL_RIGHT;
			case 5 -> AlienAttackType.HEAD_BITE;
			default -> AlienAttackType.CLAW_LEFT;
			});
		}

		// Searching Logic

		if (world.isClient && this.getVelocity().horizontalLength() == 0.0 && !this.isAttacking()) {
			if (isSearching) {
				if (searchingProgress > Constants.TPS * 3) {
					searchingProgress = 0;
					searchingCooldown = Constants.TPS * 15L;
					isSearching = false;
				} else {
					searchingProgress++;
				}
			} else {
				searchingCooldown = max(searchingCooldown - 1, 0);

				if (searchingCooldown <= 0) {
					int next = random.nextInt(10);

					isSearching = next == 0 || next == 1;
				}
			}
		}
	}

	@Override
	public boolean tryAttack(Entity target) {
		float additionalDamage = switch (getCurrentAttackType().genericAttackType) {
		case BITE -> 23.0f;
		case TAIL -> 3.0f;
		default -> 0.0f;
		};

		if (target instanceof LivingEntity && !world.isClient) {
			switch (getCurrentAttackType().genericAttackType) {
			case NONE -> {
			}
			case BITE -> {
				var helmet = StreamSupport.stream(target.getArmorItems().spliterator(), false).filter(it -> {
					var item = it.getItem();
					return (item instanceof ArmorItem && ((ArmorItem) item).getSlotType() == EquipmentSlot.HEAD);
				}).findFirst().orElse(null);

				if (helmet != null) {
					helmet.damage(15, this, it -> {
					});
					additionalDamage -= 15;
				}
			}
			case CLAW -> {
				if (target instanceof PlayerEntity playerEntity && this.random.nextInt(7) == 0) {
					playerEntity.dropItem(playerEntity.getInventory().getMainHandStack(), true, false);
					playerEntity.getInventory().removeOne(playerEntity.getInventory().getMainHandStack());
				}
			}
			case TAIL -> {
				var armorItems = StreamSupport.stream(target.getArmorItems().spliterator(), false)
						.collect(Collectors.toList());
				if (!armorItems.isEmpty()) {
					armorItems.get(new Random().nextInt(armorItems.size())).damage(10, this, it -> {
					});
				}
			}
			}
		}

		target.damage(DamageSource.mob(this), additionalDamage);

		return super.tryAttack(target);
	}

	/*
	 * ANIMATIONS
	 */

	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
		var velocityLength = this.getVelocity().horizontalLength();

		if (this.isSubmergedInWater() && this.touchingWater) {
			if (this.isAttacking() && velocityLength > 0.0) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("rush_swim", true));
				return PlayState.CONTINUE;
			} else if (!this.isAttacking() && velocityLength > 0.0) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("swim", true));
				return PlayState.CONTINUE;
			} else {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("idle_water", true));
				return PlayState.CONTINUE;
			}
		} else {
			if (this.isAttacking() && velocityLength > 0.0) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("rush_crawl", true));
				return PlayState.CONTINUE;
			} else if (!this.isAttacking() && velocityLength > 0.0) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("crawl", true));
				return PlayState.CONTINUE;
			} else if (isSearching && !this.isAttacking()) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("ambient", false));
				return PlayState.CONTINUE;
			} else {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("idle_land2", true));
				return PlayState.CONTINUE;
			}
		}
	}

	private <E extends IAnimatable> PlayState attackPredicate(AnimationEvent<E> event) {
		if (getCurrentAttackType() != AlienAttackType.NONE && attackProgress > 0) {
			event.getController().setAnimation(new AnimationBuilder()
					.addAnimation(AlienAttackType.animationMappings.get(getCurrentAttackType()), true));
			return PlayState.CONTINUE;
		}
		if (this.dataTracker.get(IS_BREAKING) == true) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("left_claw", true));
			return PlayState.CONTINUE;
		}

		return PlayState.STOP;
	}

	private <E extends IAnimatable> PlayState hissPredicate(AnimationEvent<E> event) {
		if (isHissing()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("hiss", true));
			return PlayState.CONTINUE;
		}

		return PlayState.STOP;
	}

	private <ENTITY extends IAnimatable> void soundStepListener(SoundKeyframeEvent<ENTITY> event) {
		if (event.sound.matches("stepSoundkey")) {
			if (this.world.isClient) {
				this.getEntityWorld().playSound(this.getX(), this.getY(), this.getZ(), GigSounds.AQUA_LANDMOVE,
						SoundCategory.HOSTILE, 0.25F, 1.0F, true);
			}
		}
	}

	private <ENTITY extends IAnimatable> void soundAttackListener(SoundKeyframeEvent<ENTITY> event) {
		if (event.sound.matches("attackSoundkey")) {
			if (this.world.isClient) {
				this.getEntityWorld().playSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_ATTACK,
						SoundCategory.HOSTILE, 0.25F, 1.0F, true);
			}
		}
	}

	private <ENTITY extends IAnimatable> void soundHissListener(SoundKeyframeEvent<ENTITY> event) {
		if (event.sound.matches("hissSoundkey")) {
			if (this.world.isClient) {
				this.getEntityWorld().playSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_HISS,
						SoundCategory.HOSTILE, 0.25F, 1.0F, true);
			}
		}
	}

	@Override
	public void registerControllers(AnimationData data) {
		AnimationController<AquaticAlienEntity> main = new AnimationController<AquaticAlienEntity>(this, "controller",
				10f, this::predicate);
		main.registerSoundListener(this::soundStepListener);
		data.addAnimationController(main);
		AnimationController<AquaticAlienEntity> attacking = new AnimationController<AquaticAlienEntity>(this,
				"attackController", 5f, this::attackPredicate);
		attacking.registerSoundListener(this::soundAttackListener);
		data.addAnimationController(attacking);
		AnimationController<AquaticAlienEntity> hissing = new AnimationController<AquaticAlienEntity>(this,
				"hissController", 10f, this::hissPredicate);
		hissing.registerSoundListener(this::soundHissListener);
		data.addAnimationController(hissing);
	}

	@Override
	public AnimationFactory getFactory() {
		return animationFactory;
	}
}
