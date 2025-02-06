package mods.cybercat.gigeresque.common.entity.impl.runner;

import mod.azure.azurelib.rewrite.util.MoveAnalysis;
import mod.azure.azurelib.sblforked.api.core.BrainActivityGroup;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import mod.azure.azurelib.sblforked.api.core.behaviour.custom.target.InvalidateAttackTarget;
import mods.cybercat.gigeresque.common.entity.ai.tasks.movement.RunToAttackTargetTask;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

import java.util.Objects;

import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.common.entity.GigEntities;
import mods.cybercat.gigeresque.common.entity.ai.tasks.attack.AlienMeleeAttack;
import mods.cybercat.gigeresque.common.entity.helper.*;
import mods.cybercat.gigeresque.common.entity.impl.classic.ChestbursterEntity;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;

public class RunnerbursterEntity extends ChestbursterEntity implements Growable {

    public RunnerbursterEntity(EntityType<? extends RunnerbursterEntity> type, Level level) {
        super(type, level);
        this.animationDispatcher = new AnimationDispatcher(this);
        this.moveAnalysis = new MoveAnalysis(this);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
            .add(
                Attributes.MAX_HEALTH,
                CommonMod.config.runnerbusterConfigs.runnerbusterHealth
            )
            .add(Attributes.ARMOR, 0.0f)
            .add(
                Attributes.ARMOR_TOUGHNESS,
                0.0f
            )
            .add(Attributes.KNOCKBACK_RESISTANCE, 8.0)
            .add(Attributes.FOLLOW_RANGE, 32.0)
            .add(Attributes.MOVEMENT_SPEED, 0.3300000041723251)
            .add(
                Attributes.ATTACK_DAMAGE,
                CommonMod.config.runnerbusterConfigs.runnerbusterAttackDamage
            )
            .add(Attributes.ATTACK_KNOCKBACK, 0.3);
    }

    /*
     * GROWTH
     */
    @Override
    public float getGrowthMultiplier() {
        return CommonMod.config.bursterConfigs.runnerbursterGrowthMultiplier;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount < 5) {
            this.animationDispatcher.sendBirth();
            this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 10), this);
        }
    }

    /**
     * TODO: Remove classic alien when Rom stages ready ready
     */
    @Override
    public LivingEntity growInto() {
        LivingEntity alien;
        if (Objects.equals(hostId, "runner"))
            alien = GigEntities.RUNNER_ALIEN.get().create(level());
        else
            alien = GigEntities.ALIEN.get().create(level());
        // alien = GigEntities.ROM_ALIEN.get().create(level());

        return alien;
    }

    @Override
    public BrainActivityGroup<ChestbursterEntity> getFightTasks() {
        return BrainActivityGroup.fightTasks(
            new InvalidateAttackTarget<>().invalidateIf(
                (entity, target) -> GigEntityUtils.removeTarget(target) || target.getBbHeight() >= 0.8
            ),
            new RunToAttackTargetTask<>().speedMod((owner, target) -> 1.0f).closeEnoughDist((mob, livingEntity) -> 0)
                .stopIf(entity -> this.stasisManager.isStasis() || this.isVehicle()),
            new AlienMeleeAttack<>(5, GigMeleeAttackSelector.RBUSTER_ANIM_SELECTOR)
        );
    }

    @Override
    protected void handleAggroMovementAnimations() {
        if (this.isInWater()) {
            GigCommonMethods.setAnimation(animationDispatcher::sendSwim);
        } else {
            GigCommonMethods.setAnimation(animationDispatcher::sendRun);
        }
    }

    @Override
    protected void handleMovementAnimations() {
        if (this.isAggressive()) {
            this.handleAggroMovementAnimations();
        } else if (this.isInWater()) {
            GigCommonMethods.setAnimation(animationDispatcher::sendSwim);
        } else {
            GigCommonMethods.setAnimation(animationDispatcher::sendRun);
        }
    }
}
