package mods.cybercat.gigeresque.common.entity.impl.neo;

import java.util.List;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager.ControllerRegistrar;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.object.PlayState;
import mod.azure.azurelib.util.AzureLibUtil;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.block.GIgBlocks;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.Entities;
import mods.cybercat.gigeresque.common.entity.ai.sensors.ItemEntitySensor;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyRepellentsSensor;
import mods.cybercat.gigeresque.common.entity.ai.tasks.AlienPanic;
import mods.cybercat.gigeresque.common.entity.ai.tasks.EatFoodTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.FleeFireTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.KillCropsTask;
import mods.cybercat.gigeresque.common.entity.attribute.AlienEntityAttributes;
import mods.cybercat.gigeresque.common.entity.helper.GigAnimationsDefault;
import mods.cybercat.gigeresque.common.entity.impl.RunnerbursterEntity;
import mods.cybercat.gigeresque.common.entity.impl.classic.AlienEggEntity;
import mods.cybercat.gigeresque.common.entity.impl.classic.ChestbursterEntity;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;
import mods.cybercat.gigeresque.interfacing.Eggmorphable;
import mods.cybercat.gigeresque.interfacing.Host;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
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

public class NeobursterEntity extends RunnerbursterEntity {

	private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);

	public NeobursterEntity(EntityType<? extends RunnerbursterEntity> type, Level level) {
		super(type, level);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return LivingEntity.createLivingAttributes().add(Attributes.MAX_HEALTH, Gigeresque.config.neobursterXenoHealth).add(Attributes.ARMOR, 0.0f).add(Attributes.ARMOR_TOUGHNESS, 6.0).add(Attributes.KNOCKBACK_RESISTANCE, 7.0).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.MOVEMENT_SPEED, 0.23000000417232513).add(Attributes.ATTACK_DAMAGE, Gigeresque.config.neobursterAttackDamage).add(Attributes.ATTACK_KNOCKBACK, 1.0).add(AlienEntityAttributes.INTELLIGENCE_ATTRIBUTE, 0.5);
	}

	/*
	 * GROWTH
	 */
	@Override
	public float getGrowthMultiplier() {
		return Gigeresque.config.chestbursterGrowthMultiplier;
	}

	@Override
	public float getMaxGrowth() {
		return Constants.TPD / 2.0f;
	}

	@Override
	public LivingEntity growInto() {
		return Entities.NEOMORPH_ADOLESCENT.create(level());
	}

	@Override
	public List<ExtendedSensor<ChestbursterEntity>> getSensors() {
		return ObjectArrayList.of(new NearbyPlayersSensor<>(),
				new NearbyLivingEntitySensor<ChestbursterEntity>().setPredicate((entity,
						target) -> !((entity instanceof AlienEntity || entity instanceof Warden || entity instanceof ArmorStand || entity instanceof Bat) || !target.hasLineOfSight(entity) || (entity.getVehicle() != null && entity.getVehicle().getSelfAndPassengers().anyMatch(AlienEntity.class::isInstance)) || (target.getFeetBlockState().getBlock() == GIgBlocks.NEST_RESIN_WEB_CROSS) || (target.getBlockStateOn().getBlock() == GIgBlocks.NEST_RESIN_WEB_CROSS) || (entity instanceof AlienEggEntity)
								|| entity.level().getBlockStates(entity.getBoundingBox().inflate(1)).anyMatch(state -> state.is(GIgBlocks.NEST_RESIN_WEB_CROSS)) || ((Host) entity).isBleeding() || ((Host) entity).hasParasite() || ((Eggmorphable) entity).isEggmorphing() || this.isVehicle() || (GigEntityUtils.isFacehuggerAttached(entity)) && entity.isAlive())),
				new NearbyBlocksSensor<ChestbursterEntity>().setRadius(7).setPredicate((block, entity) -> block.is(BlockTags.CROPS)), new NearbyRepellentsSensor<ChestbursterEntity>().setRadius(15).setPredicate((block, entity) -> block.is(GigTags.ALIEN_REPELLENTS) || block.is(Blocks.LAVA)), new ItemEntitySensor<ChestbursterEntity>(), new HurtBySensor<>());
	}

	@Override
	public BrainActivityGroup<ChestbursterEntity> getCoreTasks() {
		return BrainActivityGroup.coreTasks(new FleeFireTask<>(1.2F), new AlienPanic(2.0f), new MoveToWalkTarget<>());
	}

	@Override
	public BrainActivityGroup<ChestbursterEntity> getIdleTasks() {
		return BrainActivityGroup.idleTasks(new EatFoodTask<ChestbursterEntity>(5), new KillCropsTask<>(), new FirstApplicableBehaviour<ChestbursterEntity>(new TargetOrRetaliate<>(), new SetPlayerLookTarget<>().stopIf(target -> !target.isAlive() || target instanceof Player && ((Player) target).isCreative()), new SetRandomLookTarget<>()), new OneRandomBehaviour<>(new SetRandomWalkTarget<>().speedModifier(0.45f), new Idle<>().runFor(entity -> entity.getRandom().nextInt(30, 60))));
	}

	@Override
	public BrainActivityGroup<ChestbursterEntity> getFightTasks() {
		return BrainActivityGroup.fightTasks(new InvalidateAttackTarget<>().stopIf(target -> !target.isAlive()), new SetWalkTargetToAttackTarget<>().speedMod(2.3F), new AnimatableMeleeAttack(20));
	}

	/*
	 * ANIMATIONS
	 */
	@Override
	public void registerControllers(ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>(this, "livingController", 5, event -> {
			var isDead = this.dead || this.getHealth() < 0.01 || this.isDeadOrDying();
			var velocityLength = this.getDeltaMovement().horizontalDistance();
			if (velocityLength >= 0.000000001 && !isDead)
				if (walkAnimation.speedOld >= 0.35F)
					return event.setAndContinue(GigAnimationsDefault.RUN);
				else
					return event.setAndContinue(GigAnimationsDefault.WALK);
			else if (isDead)
				return event.setAndContinue(GigAnimationsDefault.DEATH);
			else if (this.entityData.get(BIRTHED) == true)
				return event.setAndContinue(GigAnimationsDefault.BIRTH);
			else
				return event.setAndContinue(GigAnimationsDefault.IDLE);
		}).setSoundKeyframeHandler(event -> {
			if (event.getKeyframeData().getSound().matches("thudSoundkey"))
				if (this.level().isClientSide)
					this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_DEATH_THUD, SoundSource.HOSTILE, 0.5F, 2.6F, true);
			if (event.getKeyframeData().getSound().matches("stepSoundkey"))
				if (this.level().isClientSide)
					this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_HANDSTEP, SoundSource.HOSTILE, 0.3F, 1.5F, true);
		}));
		controllers.add(new AnimationController<>(this, "attackController", 0, event -> {
			return PlayState.STOP;
		}).triggerableAnim("eat", GigAnimationsDefault.CHOMP).triggerableAnim("death", GigAnimationsDefault.DEATH));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}

}
