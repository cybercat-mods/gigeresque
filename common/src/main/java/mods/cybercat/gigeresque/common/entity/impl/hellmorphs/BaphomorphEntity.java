package mods.cybercat.gigeresque.common.entity.impl.hellmorphs;

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
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
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
import mods.cybercat.gigeresque.common.entity.ai.tasks.misc.HissingTask;
import mods.cybercat.gigeresque.common.entity.helper.AnimationDispatcher;
import mods.cybercat.gigeresque.common.entity.helper.GigCommonMethods;
import mods.cybercat.gigeresque.common.entity.helper.GigMeleeAttackSelector;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;

public class BaphomorphEntity extends AlienEntity implements SmartBrainOwner<BaphomorphEntity> {

    public BaphomorphEntity(EntityType<? extends AlienEntity> entityType, Level level) {
        super(entityType, level);
        this.animationDispatcher = new AnimationDispatcher(this);
        this.moveAnalysis = new MoveAnalysis(this);
    }

    @Override
    public int getAcidDiameter() {
        return 3;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
            .add(
                Attributes.MAX_HEALTH,
                CommonMod.config.baphomorphConfigs.baphomorphXenoHealth
            )
            .add(Attributes.ARMOR, CommonMod.config.baphomorphConfigs.baphomorphXenoArmor)
            .add(
                Attributes.ARMOR_TOUGHNESS,
                CommonMod.config.baphomorphConfigs.baphomorphXenoArmor
            )
            .add(Attributes.KNOCKBACK_RESISTANCE, 8.0)
            .add(
                Attributes.FOLLOW_RANGE,
                32.0
            )
            .add(Attributes.MOVEMENT_SPEED, 0.3300000041723251)
            .add(
                Attributes.ATTACK_DAMAGE,
                CommonMod.config.baphomorphConfigs.baphomorphAttackDamage
            )
            .add(Attributes.ATTACK_KNOCKBACK, 1.0);
    }

    @Override
    public void tick() {
        super.tick();
        moveAnalysis.update();

        if (this.level().isClientSide()) {
            this.handleAnimations();
        }
    }

    protected void handleAnimations() {
        if (this.isDeadOrDying()) {
            GigCommonMethods.setAnimation(animationDispatcher::sendDeath);
            return;
        }
        if (this.moveAnalysis.isMoving()) {
            this.handleMovementAnimations();
        } else {
            this.handleIdleAnimations();
        }
    }

    protected void handleAggroMovementAnimations() {
        if (this.isInWater()) {
            GigCommonMethods.setAnimation(animationDispatcher::sendRushSwim);
        } else {
            GigCommonMethods.setAnimation(animationDispatcher::sendRun);
        }
    }

    protected void handleMovementAnimations() {
        if (this.isAggressive()) {
            this.handleAggroMovementAnimations();
        } else if (this.isInWater()) {
            GigCommonMethods.setAnimation(animationDispatcher::sendSwim);
        } else {
            GigCommonMethods.setAnimation(animationDispatcher::sendWalk);
        }
    }

    protected void handleIdleAnimations() {
        if (this.isInWater()) {
            GigCommonMethods.setAnimation(animationDispatcher::sendSwim);
        } else {
            GigCommonMethods.setAnimation(animationDispatcher::sendIdle);
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
    public List<ExtendedSensor<BaphomorphEntity>> getSensors() {
        return ObjectArrayList.of(
            new NearbyPlayersSensor<>(),
            new NearbyLivingEntitySensor<BaphomorphEntity>().setRadius(32)
                .setPredicate(GigEntityUtils::entityTest),
            new NearbyBlocksSensor<BaphomorphEntity>().setRadius(7),
            new NearbyRepellentsSensor<BaphomorphEntity>().setRadius(15)
                .setPredicate(
                    (block, entity) -> block.is(GigTags.ALIEN_REPELLENTS) || block.is(Blocks.LAVA)
                ),
            new NearbyLightsBlocksSensor<BaphomorphEntity>().setRadius(7)
                .setPredicate(
                    (block, entity) -> block.is(GigTags.DESTRUCTIBLE_LIGHT)
                ),
            new HurtBySensor<>(),
            new UnreachableTargetSensor<>()
        );
    }

    @Override
    public BrainActivityGroup<BaphomorphEntity> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
            // Looks at target
            new LookAtTarget<>().stopIf(entity -> this.stasisManager.isStasis())
                .startCondition(
                    entity -> !this.stasisManager.isStasis() || !this.searchingManager.isSearching()
                ),
            // Hisses
            new HissingTask<>(800).startCondition(entity -> !this.isAggressive())
                .stopIf(entity -> this.stasisManager.isStasis() || this.isExecuting() || this.isAggressive()),
            // Move to target
            new MoveToWalkTarget<>().startCondition(entity -> !this.isExecuting())
                .stopIf(entity -> this.stasisManager.isStasis() || this.isExecuting())
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public BrainActivityGroup<BaphomorphEntity> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
            // Kill Lights
            new KillLightsTask<>().startCondition(
                entity -> !this.isAggressive() || !this.stasisManager.isStasis() || !this.isExecuting() || !this.isFleeing()
            )
                .stopIf(
                    target -> (this.isAggressive() || this.isVehicle() || this.stasisManager.isStasis() || this.isFleeing())
                ),
            // Do first
            new FirstApplicableBehaviour<BaphomorphEntity>(
                // Targeting
                new TargetOrRetaliate<>().stopIf(
                    target -> (this.isVehicle() || this.isFleeing() || this.stasisManager.isStasis())
                ),
                // Look at players
                new SetPlayerLookTarget<>().predicate(
                    target -> target.isAlive() && (!target.isCreative() || !target.isSpectator())
                ).stopIf(entity -> this.stasisManager.isStasis() || this.isExecuting()),
                // Look around randomly
                new SetRandomLookTarget<>().startCondition(
                    entity -> !this.stasisManager.isStasis() || !this.searchingManager.isSearching()
                )
            ).stopIf(entity -> this.stasisManager.isStasis() || this.isExecuting() || this.isAggressive()),
            // Random
            new OneRandomBehaviour<>(
                // Randomly walk around
                new SetRandomWalkTarget<>().dontAvoidWater()
                    .setRadius(20)
                    .speedModifier(0.7f)
                    .startCondition(
                        entity -> !this.stasisManager.isStasis() || !this.isExecuting() || !this.isAggressive() || !this.isVehicle()
                    )
                    .stopIf(
                        entity -> this.isExecuting() || this.stasisManager.isStasis() || this.isAggressive() || this.isVehicle()
                    ),
                new Idle<>().startCondition(entity -> !this.isAggressive() || !this.isVehicle())
            ).stopIf(entity -> entity.getDeltaMovement().horizontalDistance() > 0 || this.isVehicle())
        );
    }

    @Override
    public BrainActivityGroup<BaphomorphEntity> getFightTasks() {
        return BrainActivityGroup.fightTasks(
            new InvalidateAttackTarget<>().invalidateIf(
                (entity, target) -> GigEntityUtils.removeTarget(target) || this.stasisManager.isStasis()
            ),
            new SetWalkTargetToAttackTarget<>().speedMod((owner, target) -> 1.05f)
                .stopIf(entity -> this.stasisManager.isStasis() || this.isVehicle()),
            new AlienMeleeAttack<>(12, GigMeleeAttackSelector.NORMAL_ANIM_SELECTOR)
        );
    }

}
