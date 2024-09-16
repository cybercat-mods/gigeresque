package mods.cybercat.gigeresque.common.entity.impl.neo;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mod.azure.azurelib.common.internal.common.util.AzureLibUtil;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.Animation;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.core.object.PlayState;
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
import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.GigEntities;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyLightsBlocksSensor;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyRepellentsSensor;
import mods.cybercat.gigeresque.common.entity.ai.tasks.attack.AlienMeleeAttack;
import mods.cybercat.gigeresque.common.entity.ai.tasks.blocks.KillLightsTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.movement.FleeFireTask;
import mods.cybercat.gigeresque.common.entity.helper.AzureVibrationUser;
import mods.cybercat.gigeresque.common.entity.helper.GigAnimationsDefault;
import mods.cybercat.gigeresque.common.entity.helper.GigMeleeAttackSelector;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NeomorphAdolescentEntity extends AlienEntity implements SmartBrainOwner<NeomorphAdolescentEntity> {

    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);

    public NeomorphAdolescentEntity(EntityType<? extends AlienEntity> entityType, Level world) {
        super(entityType, world);
        this.vibrationUser = new AzureVibrationUser(this,
                (this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(
                        this.blockPosition()).getAmount() >= 8) ? 1.3F : 2.65F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.MAX_HEALTH,
                CommonMod.config.neomorph_adolescentXenoHealth).add(Attributes.ARMOR, 0.0f).add(
                Attributes.ARMOR_TOUGHNESS, 6.0).add(Attributes.KNOCKBACK_RESISTANCE, 7.0).add(Attributes.FOLLOW_RANGE,
                32.0).add(Attributes.MOVEMENT_SPEED, 0.3300000041723251).add(Attributes.ATTACK_DAMAGE,
                CommonMod.config.neomorph_adolescentAttackDamage).add(Attributes.ATTACK_KNOCKBACK, 1.0);
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity target) {
        if (target instanceof LivingEntity livingEntity && !this.level().isClientSide && this.getRandom().nextInt(0,
                10) > 7) {
            livingEntity.hurt(damageSources().mobAttack(this),
                    this.getRandom().nextInt(4) > 2 ? CommonMod.config.runnerXenoTailAttackDamage : 0.0f);
            this.heal(1.0833f);
            return super.doHurtTarget(target);
        }
        this.heal(1.0833f);
        return super.doHurtTarget(target);
    }

    /*
     * GROWTH
     */
    @Override
    public float getGrowthMultiplier() {
        return CommonMod.config.chestbursterGrowthMultiplier;
    }

    @Override
    public float getMaxGrowth() {
        return Constants.TPD / 2.0f;
    }

    @Override
    public LivingEntity growInto() {
        return GigEntities.NEOMORPH.get().create(level());
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        if (spawnType != MobSpawnType.NATURAL) setGrowth(getMaxGrowth());
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
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
    public List<ExtendedSensor<NeomorphAdolescentEntity>> getSensors() {
        return ObjectArrayList.of(new NearbyPlayersSensor<>(),
                new NearbyLivingEntitySensor<NeomorphAdolescentEntity>().setPredicate(GigEntityUtils::entityTest),
                new NearbyBlocksSensor<NeomorphAdolescentEntity>().setRadius(7),
                new NearbyRepellentsSensor<NeomorphAdolescentEntity>().setRadius(15).setPredicate(
                        (block, entity) -> block.is(GigTags.ALIEN_REPELLENTS) || block.is(Blocks.LAVA)),
                new NearbyLightsBlocksSensor<NeomorphAdolescentEntity>().setRadius(7).setPredicate(
                        (block, entity) -> block.is(GigTags.DESTRUCTIBLE_LIGHT)), new HurtBySensor<>(),
                new UnreachableTargetSensor<>(), new HurtBySensor<>());
    }

    @Override
    public BrainActivityGroup<NeomorphAdolescentEntity> getCoreTasks() {
        return BrainActivityGroup.coreTasks(new LookAtTarget<>(), new FleeFireTask<>(3.5F), new MoveToWalkTarget<>());
    }

    @Override
    public BrainActivityGroup<NeomorphAdolescentEntity> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
                new KillLightsTask<>().stopIf(target -> (this.isAggressive() || this.isVehicle())),
                new FirstApplicableBehaviour<NeomorphAdolescentEntity>(new TargetOrRetaliate<>(),
                        new SetPlayerLookTarget<>().predicate(
                                target -> target.isAlive() && (!target.isCreative() || !target.isSpectator())),
                        new SetRandomLookTarget<>()),
                new OneRandomBehaviour<>(new SetRandomWalkTarget<>().speedModifier(1.05f),
                        new Idle<>().startCondition(entity -> !this.isAggressive()).runFor(
                                entity -> entity.getRandom().nextInt(30, 60))));
    }

    @Override
    public BrainActivityGroup<NeomorphAdolescentEntity> getFightTasks() {
        return BrainActivityGroup.fightTasks(
                new InvalidateAttackTarget<>().invalidateIf((entity, target) -> GigEntityUtils.removeTarget(target)),
                new SetWalkTargetToAttackTarget<>().speedMod((owner, target) -> 2.5f),
                new AlienMeleeAttack<>(10, GigMeleeAttackSelector.NORMAL_ANIM_SELECTOR));
    }

    /*
     * ANIMATIONS
     */
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, Constants.LIVING_CONTROLLER, 0, event -> {
            var isDead = this.dead || this.getHealth() < 0.01 || this.isDeadOrDying();
            var velocityLength = this.getDeltaMovement().horizontalDistance();
            if (velocityLength >= 0.000000001 && !(this.isCrawling() || this.isTunnelCrawling()) && !this.isExecuting() && !isDead && !this.isPassedOut() && !this.swinging)
                if (!(this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(
                        this.blockPosition()).getAmount() >= 8) && !this.isExecuting()) {
                    if (walkAnimation.speedOld > 0.35F && this.getFirstPassenger() == null)
                        return event.setAndContinue(GigAnimationsDefault.RUN);
                    else if (!this.isExecuting() && walkAnimation.speedOld < 0.35F || (!(this.isCrawling() || this.isTunnelCrawling()) && !this.onGround()))
                        return event.setAndContinue(GigAnimationsDefault.WALK);
                } else if (this.wasEyeInWater && !this.isExecuting() && !this.isVehicle())
                    if (this.isAggressive() && !this.isVehicle())
                        return event.setAndContinue(GigAnimationsDefault.RUSH_SWIM);
                    else return event.setAndContinue(GigAnimationsDefault.SWIM);
            return event.setAndContinue(
                    (this.level().getFluidState(this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(
                            this.blockPosition()).getAmount() >= 8) ? GigAnimationsDefault.IDLE_WATER : GigAnimationsDefault.IDLE_LAND);
        }).triggerableAnim("death", GigAnimationsDefault.DEATH) // death
                .triggerableAnim("idle", GigAnimationsDefault.IDLE_LAND) // idle
                .setSoundKeyframeHandler(event -> {
                    if (event.getKeyframeData().getSound().matches("thudSoundkey") && this.level().isClientSide)
                        this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_DEATH_THUD.get(),
                                SoundSource.HOSTILE, 0.5F, 2.6F, true);
                    if (event.getKeyframeData().getSound().matches("footstepSoundkey") && this.level().isClientSide)
                        this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_HANDSTEP.get(),
                                SoundSource.HOSTILE, 0.5F, 1.5F, true);
                })).add(new AnimationController<>(this, Constants.ATTACK_CONTROLLER, 1,
                event -> PlayState.STOP).triggerableAnim("alert",
                        RawAnimation.begin().then("hiss", Animation.LoopType.PLAY_ONCE)) // reset hands
                .triggerableAnim("swipe", GigAnimationsDefault.LEFT_CLAW) // swipe
                .triggerableAnim("death", GigAnimationsDefault.DEATH) // death
                .triggerableAnim("left_claw", GigAnimationsDefault.LEFT_CLAW) // attack
                .triggerableAnim("right_claw", GigAnimationsDefault.RIGHT_CLAW) // attack
                .triggerableAnim("left_tail", GigAnimationsDefault.LEFT_TAIL) // attack
                .triggerableAnim("right_tail", GigAnimationsDefault.RIGHT_TAIL) // attack
                .setSoundKeyframeHandler(event -> {
                    if (event.getKeyframeData().getSound().matches("clawSoundkey") && this.level().isClientSide)
                        this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_CLAW.get(),
                                SoundSource.HOSTILE, 0.25F, 1.0F, true);
                    if (event.getKeyframeData().getSound().matches("tailSoundkey") && this.level().isClientSide)
                        this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_TAIL.get(),
                                SoundSource.HOSTILE, 0.25F, 1.0F, true);
                })).add(new AnimationController<>(this, "hissController", 0, event -> {
            var isDead = this.dead || this.getHealth() < 0.01 || this.isDeadOrDying();
            if (this.isHissing() && !this.isVehicle() && !this.isExecuting() && !isDead && !(this.level().getFluidState(
                    this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(
                    this.blockPosition()).getAmount() >= 8)) return event.setAndContinue(GigAnimationsDefault.HISS);
            return PlayState.STOP;
        }).setSoundKeyframeHandler(event -> {
            if (event.getKeyframeData().getSound().matches("hissSoundkey") && this.level().isClientSide)
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_HISS.get(),
                        SoundSource.HOSTILE, 1.0F, 1.0F, true);
        }).triggerableAnim("hiss", GigAnimationsDefault.HISS));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

}
