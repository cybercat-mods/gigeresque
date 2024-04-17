package mods.cybercat.gigeresque.common.entity.impl.mutant;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mod.azure.azurelib.common.api.common.ai.pathing.AzureNavigation;
import mod.azure.azurelib.common.internal.common.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.common.internal.common.core.animation.AnimatableManager;
import mod.azure.azurelib.common.internal.common.core.animation.AnimationController;
import mod.azure.azurelib.common.internal.common.util.AzureLibUtil;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.Entities;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyRepellentsSensor;
import mods.cybercat.gigeresque.common.entity.ai.tasks.attack.AttackExplodeTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.misc.AlienPanic;
import mods.cybercat.gigeresque.common.entity.ai.tasks.movement.FleeFireTask;
import mods.cybercat.gigeresque.common.entity.helper.AzureVibrationUser;
import mods.cybercat.gigeresque.common.entity.helper.GigAnimationsDefault;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.DamageSourceUtils;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
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
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PopperEntity extends AlienEntity implements SmartBrainOwner<PopperEntity> {

    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);

    public PopperEntity(EntityType<? extends Monster> entityType, Level world) {
        super(entityType, world);
        setMaxUpStep(1.5f);
        this.vibrationUser = new AzureVibrationUser(this, 0.9F);
        navigation = new AzureNavigation(this, level());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.MAX_HEALTH, Gigeresque.config.popperHealth).add(
                Attributes.ARMOR, 1.0).add(Attributes.ARMOR_TOUGHNESS, 0.0).add(Attributes.KNOCKBACK_RESISTANCE,
                0.0).add(Attributes.ATTACK_KNOCKBACK, 0.0).add(Attributes.ATTACK_DAMAGE,
                Gigeresque.config.popperAttackDamage).add(Attributes.FOLLOW_RANGE, 16.0).add(Attributes.MOVEMENT_SPEED,
                0.3300000041723251);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, Constants.LIVING_CONTROLLER, 5, event -> {
            var isDead = this.dead || this.getHealth() < 0.01 || this.isDeadOrDying();
            if (event.isMoving() && !isDead && this.entityData.get(STATE) == 0 && this.onGround())
                if (walkAnimation.speedOld >= 0.35F) return event.setAndContinue(GigAnimationsDefault.RUN);
                else return event.setAndContinue(GigAnimationsDefault.WALK);
            else if (isDead) return event.setAndContinue(GigAnimationsDefault.DEATH);
            else if (event.getAnimatable().getAttckingState() == 1 && !this.onGround())
                return event.setAndContinue(GigAnimationsDefault.CHARGE);
            else return event.setAndContinue(GigAnimationsDefault.IDLE);
        }).triggerableAnim("death", GigAnimationsDefault.DEATH));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void tick() {
        super.tick();
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
    public List<ExtendedSensor<PopperEntity>> getSensors() {
        return ObjectArrayList.of(new NearbyPlayersSensor<>(),
                new NearbyLivingEntitySensor<PopperEntity>().setPredicate(GigEntityUtils::entityTest),
                new NearbyBlocksSensor<PopperEntity>().setRadius(7),
                new NearbyRepellentsSensor<PopperEntity>().setRadius(15).setPredicate(
                        (block, entity) -> block.is(GigTags.ALIEN_REPELLENTS) || block.is(Blocks.LAVA)),
                new HurtBySensor<>());
    }

    @Override
    public BrainActivityGroup<PopperEntity> getCoreTasks() {
        return BrainActivityGroup.coreTasks(new LookAtTarget<>(), new FleeFireTask<>(1.2F), new AlienPanic(2.0f),
                new MoveToWalkTarget<>());
    }

    @Override
    public BrainActivityGroup<PopperEntity> getIdleTasks() {
        return BrainActivityGroup.idleTasks(new FirstApplicableBehaviour<PopperEntity>(new TargetOrRetaliate<>(),
                new SetPlayerLookTarget<>().predicate(
                        target -> target.isAlive() && (!target.isCreative() || !target.isSpectator())),
                new SetRandomLookTarget<>()), new OneRandomBehaviour<>(new SetRandomWalkTarget<>().speedModifier(0.65f),
                new Idle<>().runFor(entity -> entity.getRandom().nextInt(30, 60))));
    }

    @Override
    public BrainActivityGroup<PopperEntity> getFightTasks() {
        return BrainActivityGroup.fightTasks(
                new InvalidateAttackTarget<>().invalidateIf((entity, target) -> GigEntityUtils.removeTarget(target)),
                new SetWalkTargetToAttackTarget<>().speedMod((owner, target) -> 1.2F), new AttackExplodeTask<>(17));
    }

    @Override
    protected void tickDeath() {
        super.tickDeath();
        if (this.deathTime == 55) {
            this.explode();
            this.remove(Entity.RemovalReason.KILLED);
            super.tickDeath();
            this.dropExperience();
        }
    }

    public void explode() {
        var areaEffectCloudEntity = new AreaEffectCloud(this.level(), this.getX(), this.getY() + 1, this.getZ());
        areaEffectCloudEntity.setRadius(2.0F);
        areaEffectCloudEntity.setDuration(30);
        areaEffectCloudEntity.setRadiusPerTick(
                -areaEffectCloudEntity.getRadius() / areaEffectCloudEntity.getDuration());
        areaEffectCloudEntity.addEffect(new MobEffectInstance(GigStatusEffects.DNA, 600, 0));
        this.level().addFreshEntity(areaEffectCloudEntity);
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
            if (getAcidDiameter() == 1) this.generateAcidPool(this.blockPosition(), 0, 0);
            else {
                var radius = (getAcidDiameter() - 1) / 2;
                for (int i = 0; i < getAcidDiameter(); i++) {
                    int x = this.level().getRandom().nextInt(getAcidDiameter()) - radius;
                    int z = this.level().getRandom().nextInt(getAcidDiameter()) - radius;
                    if (source != damageSources().genericKill() || source != damageSources().generic()) {
                        generateAcidPool(this.blockPosition(), x, z);
                    }
                }
            }
        }
        return super.hurt(source, amount);
    }

    @Override
    public void generateAcidPool(BlockPos pos, int xOffset, int zOffset) {
        var acidEntity = Entities.GOO.create(this.level());
        assert acidEntity != null;
        acidEntity.moveTo(pos.offset(xOffset, 0, zOffset), this.getYRot(), this.getXRot());
        this.level().addFreshEntity(acidEntity);
    }

    @Override
    protected int getAcidDiameter() {
        return 1;
    }

}
