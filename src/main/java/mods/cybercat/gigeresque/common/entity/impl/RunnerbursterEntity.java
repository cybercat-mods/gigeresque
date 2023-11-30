package mods.cybercat.gigeresque.common.entity.impl;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager.ControllerRegistrar;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.object.PlayState;
import mod.azure.azurelib.util.AzureLibUtil;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.entity.Entities;
import mods.cybercat.gigeresque.common.entity.ai.sensors.ItemEntitySensor;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyRepellentsSensor;
import mods.cybercat.gigeresque.common.entity.ai.tasks.*;
import mods.cybercat.gigeresque.common.entity.helper.GigAnimationsDefault;
import mods.cybercat.gigeresque.common.entity.helper.Growable;
import mods.cybercat.gigeresque.common.entity.impl.classic.ChestbursterEntity;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
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

import java.util.List;

public class RunnerbursterEntity extends ChestbursterEntity implements GeoEntity, Growable {

    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);

    public RunnerbursterEntity(EntityType<? extends RunnerbursterEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.MAX_HEALTH, Gigeresque.config.runnerbusterHealth).add(Attributes.ARMOR, 2.0).add(Attributes.ARMOR_TOUGHNESS, 0.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.0).add(Attributes.FOLLOW_RANGE, 16.0).add(Attributes.MOVEMENT_SPEED, 0.23000000417232513).add(Attributes.ATTACK_DAMAGE, Gigeresque.config.runnerbusterAttackDamage).add(Attributes.ATTACK_KNOCKBACK, 0.3);
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
        return Gigeresque.config.runnerbursterGrowthMultiplier;
    }

    @Override
    public float getMaxGrowth() {
        return Constants.TPD / 2.0f;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount < 5) {
            this.triggerAnim("attackController", "birth");
            this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 10), this);
        }
    }

    @Override
    public LivingEntity growInto() {
        LivingEntity alien;
        if (hostId == "runner") alien = Entities.RUNNER_ALIEN.create(level());
        else alien = Entities.ALIEN.create(level());

        return alien;
    }

    @Override
    public List<ExtendedSensor<ChestbursterEntity>> getSensors() {
        return ObjectArrayList.of(new NearbyPlayersSensor<>(), new NearbyLivingEntitySensor<ChestbursterEntity>().setPredicate((target, self) -> GigEntityUtils.entityTest(target, self) || !(target instanceof Creeper || target instanceof IronGolem)), new NearbyBlocksSensor<ChestbursterEntity>().setRadius(7).setPredicate((block, entity) -> block.is(BlockTags.CROPS)), new NearbyRepellentsSensor<ChestbursterEntity>().setRadius(15).setPredicate((block, entity) -> block.is(GigTags.ALIEN_REPELLENTS) || block.is(Blocks.LAVA)), new ItemEntitySensor<ChestbursterEntity>(), new HurtBySensor<>());
    }

    @Override
    public BrainActivityGroup<ChestbursterEntity> getCoreTasks() {
        return BrainActivityGroup.coreTasks(new FleeFireTask<>(1.2F), new AlienPanic(2.0f), new MoveToWalkTarget<>());
    }

    @Override
    public BrainActivityGroup<ChestbursterEntity> getIdleTasks() {
        return BrainActivityGroup.idleTasks(new EatFoodTask<ChestbursterEntity>(0), new KillCropsTask<>(), new FirstApplicableBehaviour<ChestbursterEntity>(new TargetOrRetaliate<>(), new SetPlayerLookTarget<>().predicate(target -> target.isAlive() && (!target.isCreative() || !target.isSpectator())), new SetRandomLookTarget<>()), new OneRandomBehaviour<>(new SetRandomWalkTarget<>().speedModifier(0.65f), new Idle<>().runFor(entity -> entity.getRandom().nextInt(30, 60))));
    }

    @Override
    public BrainActivityGroup<ChestbursterEntity> getFightTasks() {
        return BrainActivityGroup.fightTasks(new InvalidateAttackTarget<>().invalidateIf((entity, target) -> GigEntityUtils.removeTarget(target, this) || target.getBbHeight() >= 0.8), new SetWalkTargetToAttackTarget<>().speedMod((owner, target) -> 1.2F), new AlienMeleeAttack(20));
    }

    /*
     * ANIMATIONS
     */
    @Override
    public void registerControllers(ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "livingController", 5, event -> {
            var isDead = this.dead || this.getHealth() < 0.01 || this.isDeadOrDying();
            if (event.isMoving() && !isDead)
                if (walkAnimation.speedOld >= 0.35F) return event.setAndContinue(GigAnimationsDefault.RUN);
                else return event.setAndContinue(GigAnimationsDefault.WALK);
            return event.setAndContinue(GigAnimationsDefault.IDLE);
        }));
        controllers.add(new AnimationController<>(this, "attackController", 0, event -> PlayState.STOP).triggerableAnim("eat", GigAnimationsDefault.CHOMP).triggerableAnim("birth", GigAnimationsDefault.BIRTH).triggerableAnim("death", GigAnimationsDefault.DEATH));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
