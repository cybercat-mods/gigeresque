package mods.cybercat.gigeresque.common.entity.impl.mutant;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mod.azure.azurelib.rewrite.util.MoveAnalysis;
import mod.azure.azurelib.sblforked.api.SmartBrainOwner;
import mod.azure.azurelib.sblforked.api.core.BrainActivityGroup;
import mod.azure.azurelib.sblforked.api.core.SmartBrainProvider;
import mod.azure.azurelib.sblforked.api.core.behaviour.FirstApplicableBehaviour;
import mod.azure.azurelib.sblforked.api.core.behaviour.OneRandomBehaviour;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.look.LookAtTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.misc.Idle;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.move.MoveToWalkTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.path.SetRandomWalkTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.target.InvalidateAttackTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.target.SetPlayerLookTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.target.SetRandomLookTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.target.TargetOrRetaliate;
import mod.azure.azurelib.sblforked.api.core.sensor.ExtendedSensor;
import mod.azure.azurelib.sblforked.api.core.sensor.custom.NearbyBlocksSensor;
import mod.azure.azurelib.sblforked.api.core.sensor.custom.UnreachableTargetSensor;
import mod.azure.azurelib.sblforked.api.core.sensor.vanilla.HurtBySensor;
import mod.azure.azurelib.sblforked.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import mod.azure.azurelib.sblforked.api.core.sensor.vanilla.NearbyPlayersSensor;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyLightsBlocksSensor;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyRepellentsSensor;
import mods.cybercat.gigeresque.common.entity.ai.tasks.attack.AlienMeleeAttack;
import mods.cybercat.gigeresque.common.entity.ai.tasks.blocks.KillLightsTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.movement.FleeFightTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.movement.FleeFireTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.movement.LeapAtTargetTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.movement.RunToAttackTargetTask;
import mods.cybercat.gigeresque.common.entity.helper.*;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.DamageSourceUtils;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;

/**
 * TODO: Ensure crawling works good
 */
public class StalkerEntity extends AlienEntity implements SmartBrainOwner<StalkerEntity> {

    public StalkerEntity(EntityType<? extends AlienEntity> entityType, Level world) {
        super(entityType, world);
        this.vibrationUser = new AzureVibrationUser(this, 1.9F);
        this.animationDispatcher = new AnimationDispatcher(this);
        this.moveAnalysis = new MoveAnalysis(this);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
            .add(
                Attributes.MAX_HEALTH,
                CommonMod.config.stalkerConfigs.stalkerXenoHealth
            )
            .add(Attributes.ARMOR, CommonMod.config.stalkerConfigs.stalkerXenoArmor)
            .add(
                Attributes.ARMOR_TOUGHNESS,
                0.0
            )
            .add(Attributes.KNOCKBACK_RESISTANCE, 0.0)
            .add(
                Attributes.FOLLOW_RANGE,
                16.0
            )
            .add(Attributes.MOVEMENT_SPEED, 0.3300000041723251)
            .add(
                Attributes.ATTACK_DAMAGE,
                CommonMod.config.stalkerConfigs.stalkerAttackDamage
            )
            .add(Attributes.ATTACK_KNOCKBACK, 0.3);
    }

    @Override
    public int getAcidDiameter() {
        return 3;
    }

    protected void handleAnimations() {
        if (this.isDeadOrDying()) {
            GigCommonMethods.setAnimation(animationDispatcher::sendDeath);
            return;
        }
        if (this.getLastDamageSource() != null && this.hurtDuration > 0 && !this.isInWater()) {
            GigCommonMethods.setAnimation(animationDispatcher::sendHurt);
        }
        if (this.moveAnalysis.isMoving()) {
            this.handleMovementAnimations();
        } else {
            this.handleIdleAnimations();
        }
    }

    protected void handleAggroMovementAnimations() {
        if (this.isInWater()) {
            GigCommonMethods.setAnimation(animationDispatcher::sendSwim);
        } else {
            GigCommonMethods.setAnimation(animationDispatcher::sendRun);
        }
    }

    protected void handleMovementAnimations() {
        if (this.isAggressive() && !this.swinging) {
            this.handleAggroMovementAnimations();
        } else if (this.isInWater()) {
            GigCommonMethods.setAnimation(animationDispatcher::sendSwim);
        } else {
            GigCommonMethods.setAnimation(animationDispatcher::sendWalk);
        }
    }

    protected void handleIdleAnimations() {
        if (!this.swinging) {
            if (this.isInWater()) {
                GigCommonMethods.setAnimation(animationDispatcher::sendIdleWater);
            } else {
                GigCommonMethods.setAnimation(animationDispatcher::sendIdle);
            }
        }
    }

    @Override
    protected Brain.@NotNull Provider<?> brainProvider() {
        return new SmartBrainProvider<>(this);
    }

    @Override
    protected void customServerAiStep() {
        tickBrain(this);
        super.customServerAiStep();
    }

    @Override
    public List<ExtendedSensor<StalkerEntity>> getSensors() {
        return ObjectArrayList.of(
            new NearbyPlayersSensor<>(),
            new NearbyLivingEntitySensor<StalkerEntity>().setRadius(30).setPredicate(GigEntityUtils::entityTest),
            new NearbyBlocksSensor<StalkerEntity>().setRadius(7),
            new NearbyRepellentsSensor<StalkerEntity>().setRadius(15)
                .setPredicate(
                    (block, entity) -> block.is(GigTags.ALIEN_REPELLENTS) || block.is(Blocks.LAVA)
                ),
            new NearbyLightsBlocksSensor<StalkerEntity>().setRadius(7)
                .setPredicate(
                    (block, entity) -> block.is(GigTags.DESTRUCTIBLE_LIGHT)
                ),
            new UnreachableTargetSensor<>(),
            new HurtBySensor<>()
        );
    }

    @Override
    public BrainActivityGroup<StalkerEntity> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
            // Flee fight at half or less health
            new FleeFightTask<>(1.0F).startCondition(entity -> this.getHealth() <= (this.getMaxHealth() / 2))
                .stopIf(entity -> this.getHealth() > (this.getMaxHealth() / 2)),
            new LookAtTarget<>(),
            new FleeFireTask<>(1.3F),
            new MoveToWalkTarget<>()
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public BrainActivityGroup<StalkerEntity> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
            new KillLightsTask<>().stopIf(target -> (this.isAggressive() || this.isVehicle() || this.isFleeing())),
            new FirstApplicableBehaviour<StalkerEntity>(
                new TargetOrRetaliate<>(),
                new SetPlayerLookTarget<>().predicate(
                    target -> target.isAlive() && (!target.isCreative() || !target.isSpectator())
                ),
                new SetRandomLookTarget<>()
            ),
            new OneRandomBehaviour<>(
                new SetRandomWalkTarget<>().dontAvoidWater().setRadius(20).speedModifier(0.9f),
                new Idle<>().startCondition(entity -> !this.isAggressive())
                    .runFor(
                        entity -> entity.getRandom().nextInt(30, 60)
                    )
            )
        );
    }

    @Override
    public BrainActivityGroup<StalkerEntity> getFightTasks() {
        return BrainActivityGroup.fightTasks(
            new InvalidateAttackTarget<>().invalidateIf((entity, target) -> GigEntityUtils.removeTarget(target)),
            new LeapAtTargetTask<>(0),
            new RunToAttackTargetTask<>().speedMod((owner, target) -> CommonMod.config.stalkerConfigs.stalkerAttackSpeed)
                .closeEnoughDist((mob, livingEntity) -> 0),
            // move to
            new AlienMeleeAttack<>(13, GigMeleeAttackSelector.STALKER_ANIM_SELECTOR)
        );// attack
    }

    @Override
    public void tick() {
        super.tick();
        moveAnalysis.update();
        GigEntityUtils.breakBlocks(this);
        if (this.hasEffect(MobEffects.MOVEMENT_SLOWDOWN))
            this.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);

        if (!this.level().isClientSide) {
            this.handleAnimations();
        }
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if (!this.level().isClientSide) {
            var attacker = source.getEntity();
            if (source.getEntity() != null && attacker instanceof LivingEntity living)
                this.brain.setMemory(MemoryModuleType.ATTACK_TARGET, living);
        }

        if (DamageSourceUtils.isDamageSourceNotPuncturing(source, this.damageSources()))
            return super.hurt(source, amount);

        if (!this.level().isClientSide && source != this.damageSources().genericKill()) {
            if (getAcidDiameter() == 1)
                GigCommonMethods.generateGooBlood(this, this.blockPosition(), 0, 0);
            else {
                var radius = (getAcidDiameter() - 1) / 2;
                for (int i = 0; i < getAcidDiameter(); i++) {
                    int x = this.level().getRandom().nextInt(getAcidDiameter()) - radius;
                    int z = this.level().getRandom().nextInt(getAcidDiameter()) - radius;
                    if (source != damageSources().genericKill() || source != damageSources().generic()) {
                        GigCommonMethods.generateGooBlood(this, this.blockPosition(), x, z);
                    }
                }
            }
        }
        return super.hurt(source, amount);
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity target) {
        if (
            target instanceof LivingEntity livingEntity && !this.level().isClientSide && this.getRandom()
                .nextInt(
                    0,
                    10
                ) > 7
        ) {
            livingEntity.hurt(
                damageSources().mobAttack(this),
                this.getRandom().nextInt(4) > 2 ? CommonMod.config.stalkerConfigs.stalkerTailAttackDamage : 0.0f
            );
            this.heal(1.0833f);
            return super.doHurtTarget(target);
        }
        if (target instanceof Creeper creeper)
            creeper.hurt(damageSources().mobAttack(this), creeper.getMaxHealth());
        this.heal(1.0833f);
        return super.doHurtTarget(target);
    }

    @Override
    public boolean isPathFinding() {
        return false;
    }

}
