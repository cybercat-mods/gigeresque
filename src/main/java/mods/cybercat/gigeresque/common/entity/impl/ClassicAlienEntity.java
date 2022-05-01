package mods.cybercat.gigeresque.common.entity.impl;

import static java.lang.Math.max;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.data.handler.TrackedDataHandlers;
import mods.cybercat.gigeresque.common.entity.AlienAttackType;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.attribute.AlienEntityAttributes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class ClassicAlienEntity extends AdultAlienEntity {
	public static DefaultAttributeContainer.Builder createAttributes() {
		return LivingEntity.createLivingAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 100.0)
				.add(EntityAttributes.GENERIC_ARMOR, 6.0).add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 0.0)
				.add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.0)
				.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32.0)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.13000000417232513)
				.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 7.0 * Constants.getIsolationModeDamageBase())
				.add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.0)
				.add(AlienEntityAttributes.INTELLIGENCE_ATTRIBUTE, 1.0);
	}

	private static final TrackedData<AlienAttackType> CURRENT_ATTACK_TYPE = DataTracker
			.registerData(ClassicAlienEntity.class, TrackedDataHandlers.ALIEN_ATTACK_TYPE);

	private AnimationFactory animationFactory = new AnimationFactory(this);

	private AlienAttackType getCurrentAttackType() {
		return dataTracker.get(CURRENT_ATTACK_TYPE);
	}

	private void setCurrentAttackType(AlienAttackType value) {
		dataTracker.set(CURRENT_ATTACK_TYPE, value);
	}

	private int attackProgress = 0;
	private boolean isSearching = false;
	private long searchingProgress = 0L;
	private long searchingCooldown = 0L;

	public ClassicAlienEntity(@NotNull EntityType<? extends AlienEntity> type, @NotNull World world) {
		super(type, world);
	}

	@Override
	public void travel(Vec3d movementInput) {
		this.navigation = (this.isSubmergedInWater() || this.isTouchingWater()) ? swimNavigation
				: this.isCrawling() ? landNavigation : landNavigation;
		this.moveControl = (this.submergedInWater || this.isTouchingWater()) ? swimMoveControl
				: this.isCrawling() ? landMoveControl : landMoveControl;
		this.lookControl = (this.submergedInWater || this.isTouchingWater()) ? swimLookControl : landLookControl;

		super.travel(movementInput);
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
		if (this.isAttacking()) {
			this.setPose(EntityPose.CROUCHING);
		} else {
			this.setPose(EntityPose.STANDING);
		}
	}

	@Override
	public float getGrowthMultiplier() {
		return Gigeresque.config.miscellaneous.alienGrowthMultiplier;
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

	@Override
	protected Box calculateBoundingBox() {
		return super.calculateBoundingBox();
	}

	@Override
	public Box getBoundingBox(EntityPose pose) {
		return this.getBoundingBox().shrink(0, -1, 0);
	}

	/*
	 * ANIMATIONS
	 */

	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
		var velocityLength = this.getVelocity().horizontalLength();

		if (velocityLength > 0.0 && !this.isTouchingWater() && !this.isCrawling()) {
			if (this.isAttacking()) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("run", true)
						.addAnimation(AlienAttackType.animationMappings.get(getCurrentAttackType()), true));
				event.getController().setAnimationSpeed(1 + this.getMovementSpeed());
				return PlayState.CONTINUE;
			} else {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("walk", true)
						.addAnimation(AlienAttackType.animationMappings.get(getCurrentAttackType()), true));
				event.getController().setAnimationSpeed(1);
				return PlayState.CONTINUE;
			}
		} else if (this.isCrawling()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("crawl", true));
			return PlayState.CONTINUE;
		} else {
			if (!this.isTouchingWater() && isSearching && !this.isAttacking()) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("ambient", false));
				return PlayState.CONTINUE;
			}

			event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
			return PlayState.CONTINUE;
		}
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
		if (isHissing()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("hiss", true));
			return PlayState.CONTINUE;
		}

		return PlayState.CONTINUE;
	}

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController<>(this, "controller", 10f, this::predicate));
		data.addAnimationController(new AnimationController<>(this, "attackController", 0f, this::attackPredicate));
		data.addAnimationController(new AnimationController<>(this, "hissController", 10f, this::hissPredicate));
	}

	@Override
	public AnimationFactory getFactory() {
		return animationFactory;
	}

}
