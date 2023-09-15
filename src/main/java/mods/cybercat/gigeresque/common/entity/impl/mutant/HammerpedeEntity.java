package mods.cybercat.gigeresque.common.entity.impl.mutant;

import java.util.List;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mod.azure.azurelib.ai.pathing.AzureNavigation;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager.ControllerRegistrar;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.core.object.PlayState;
import mod.azure.azurelib.util.AzureLibUtil;
import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.block.AcidBlock;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyRepellentsSensor;
import mods.cybercat.gigeresque.common.entity.ai.tasks.AlienMeleeAttack;
import mods.cybercat.gigeresque.common.entity.ai.tasks.AlienPanic;
import mods.cybercat.gigeresque.common.entity.ai.tasks.FleeFireTask;
import mods.cybercat.gigeresque.common.entity.helper.AzureVibrationUser;
import mods.cybercat.gigeresque.common.entity.helper.GigAnimationsDefault;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.DamageSourceUtils;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetPlayerLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.custom.NearbyBlocksSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;

public class HammerpedeEntity extends AlienEntity implements GeoEntity, SmartBrainOwner<HammerpedeEntity> {

	private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);
	private final AzureNavigation landNavigation = new AzureNavigation(this, level());

	public HammerpedeEntity(EntityType<? extends Monster> entityType, Level world) {
		super(entityType, world);
		setMaxUpStep(1.5f);
		this.vibrationUser = new AzureVibrationUser(this, 0.9F);
		navigation = landNavigation;
	}

	@Override
	public void registerControllers(ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>(this, "livingController", 5, event -> {
			var velocityLength = this.getDeltaMovement().horizontalDistance();
			var isDead = this.dead || this.getHealth() < 0.01 || this.isDeadOrDying();
			if (velocityLength >= 0.000000001 && !isDead && this.entityData.get(STATE) == 0)
				if (!this.isAggressive())
					return event.setAndContinue(GigAnimationsDefault.WALK);
				else
					return event.setAndContinue(GigAnimationsDefault.WALK_HOSTILE);
			else if (this.getTarget() != null && !event.isMoving() && !isDead)
				return event.setAndContinue(GigAnimationsDefault.WALK_HOSTILE);
			else if (this.isAggressive())
				return event.setAndContinue(RawAnimation.begin().thenLoop("idle_alert"));
			else
				return event.setAndContinue(GigAnimationsDefault.IDLE);
		}));
		controllers.add(new AnimationController<>(this, "attackController", 0, event -> {
			return PlayState.STOP;
		}).triggerableAnim("attack", GigAnimationsDefault.ATTACK).triggerableAnim("death", GigAnimationsDefault.DEATH));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return cache;
	}

	@Override
	protected Brain.Provider<?> brainProvider() {
		return new SmartBrainProvider<>(this);
	}

	@Override
	protected void customServerAiStep() {
		tickBrain(this);
		super.customServerAiStep();
	}

	@Override
	public List<ExtendedSensor<HammerpedeEntity>> getSensors() {
		return ObjectArrayList.of(new NearbyPlayersSensor<>(),
				new NearbyLivingEntitySensor<HammerpedeEntity>().setPredicate((target, self) -> GigEntityUtils.entityTest(target, self)),
				new NearbyBlocksSensor<HammerpedeEntity>().setRadius(7), new NearbyRepellentsSensor<HammerpedeEntity>().setRadius(15).setPredicate((block, entity) -> block.is(GigTags.ALIEN_REPELLENTS) || block.is(Blocks.LAVA)), new HurtBySensor<>());
	}

	@Override
	public BrainActivityGroup<HammerpedeEntity> getCoreTasks() {
		return BrainActivityGroup.coreTasks(new LookAtTarget<>(), new FleeFireTask<>(1.2F), new AlienPanic(2.0f), new MoveToWalkTarget<>());
	}

	@Override
	public BrainActivityGroup<HammerpedeEntity> getIdleTasks() {
		return BrainActivityGroup.idleTasks(new FirstApplicableBehaviour<HammerpedeEntity>(new TargetOrRetaliate<>(), new SetPlayerLookTarget<>().predicate(target -> target.isAlive() && (!target.isCreative() || !target.isSpectator())), new SetRandomLookTarget<>()), new OneRandomBehaviour<>(new SetRandomWalkTarget<>().speedModifier(0.65f), new Idle<>().runFor(entity -> entity.getRandom().nextInt(30, 60))));
	}

	@Override
	public BrainActivityGroup<HammerpedeEntity> getFightTasks() {
		return BrainActivityGroup.fightTasks(new InvalidateAttackTarget<>().invalidateIf((entity, target) -> GigEntityUtils.removeTarget(target, this)), new SetWalkTargetToAttackTarget<>().speedMod((owner, target) -> 1.05F), new AlienMeleeAttack(7).attackInterval(entity -> 80));
	}

	@Override
	protected void registerGoals() {
	}

	public static AttributeSupplier.Builder createAttributes() {
		return LivingEntity.createLivingAttributes().add(Attributes.MAX_HEALTH, Gigeresque.config.hammerpedeHealth).add(Attributes.ARMOR, 1.0).add(Attributes.ARMOR_TOUGHNESS, 0.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.0).add(Attributes.ATTACK_KNOCKBACK, 0.0).add(Attributes.ATTACK_DAMAGE, Gigeresque.config.hammerpedeAttackDamage).add(Attributes.FOLLOW_RANGE, 16.0).add(Attributes.MOVEMENT_SPEED, 0.3300000041723251);
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (!this.level().isClientSide) {
			var attacker = source.getEntity();
			if (source.getEntity() != null)
				if (attacker instanceof LivingEntity living)
					this.brain.setMemory(MemoryModuleType.ATTACK_TARGET, living);
		}

		if (DamageSourceUtils.isDamageSourceNotPuncturing(source, this.damageSources()))
			return super.hurt(source, amount);

		if (!this.level().isClientSide && source != damageSources().genericKill()) {
			var acidThickness = this.getHealth() < (this.getMaxHealth() / 2) ? 1 : 0;

			if (this.getHealth() < (this.getMaxHealth() / 4))
				acidThickness += 1;
			if (amount >= 5)
				acidThickness += 1;
			if (amount > (this.getMaxHealth() / 10))
				acidThickness += 1;
			if (acidThickness == 0)
				return super.hurt(source, amount);

			var newState = GigBlocks.BLACK_FLUID_BLOCK.defaultBlockState().setValue(AcidBlock.THICKNESS, Math.min(4, acidThickness));

			if (this.getFeetBlockState().getBlock() == Blocks.WATER)
				newState = newState.setValue(BlockStateProperties.WATERLOGGED, true);
			if (!this.getFeetBlockState().is(GigTags.ACID_RESISTANT))
				level().setBlockAndUpdate(this.blockPosition(), newState);
		}
		return super.hurt(source, amount);
	}

	@Override
	public void generateAcidPool(int xOffset, int zOffset) {
		var pos = this.blockPosition().offset(xOffset, 0, zOffset);
		var posState = level().getBlockState(pos);
		var newState = GigBlocks.BLACK_FLUID.defaultBlockState();

		if (posState.getBlock() == Blocks.WATER)
			newState = newState.setValue(BlockStateProperties.WATERLOGGED, true);

		if (!(posState.getBlock() instanceof LiquidBlock))
			return;
		level().setBlockAndUpdate(pos, newState);
	}

	@Override
	protected int getAcidDiameter() {
		return 2;
	}

}
