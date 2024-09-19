package mods.cybercat.gigeresque.common.entity.impl.templebeast;

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
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyLightsBlocksSensor;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyRepellentsSensor;
import mods.cybercat.gigeresque.common.entity.ai.tasks.attack.AlienMeleeAttack;
import mods.cybercat.gigeresque.common.entity.ai.tasks.blocks.KillLightsTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.misc.BuildNestTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.movement.FleeFireTask;
import mods.cybercat.gigeresque.common.entity.helper.GigAnimationsDefault;
import mods.cybercat.gigeresque.common.entity.helper.GigMeleeAttackSelector;
import mods.cybercat.gigeresque.common.entity.impl.runner.RunnerAlienEntity;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
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

public class DraconicTempleBeastEntity extends AlienEntity implements SmartBrainOwner<DraconicTempleBeastEntity>  {
    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);

    public DraconicTempleBeastEntity(EntityType<? extends AlienEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public int getAcidDiameter() {
        return 3;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.MAX_HEALTH,
                CommonMod.config.draconicTempleBeastConfigs.draconicTempleBeastXenoHealth).add(Attributes.ARMOR,
                CommonMod.config.draconicTempleBeastConfigs.draconicTempleBeastXenoArmor).add(Attributes.ARMOR_TOUGHNESS, 0.0).add(
                Attributes.KNOCKBACK_RESISTANCE, 0.0).add(Attributes.FOLLOW_RANGE, 16.0).add(Attributes.MOVEMENT_SPEED,
                1.000000041723251).add(Attributes.ATTACK_DAMAGE,
                CommonMod.config.draconicTempleBeastConfigs.draconicTempleBeastAttackDamage).add(Attributes.ATTACK_KNOCKBACK, 5.0);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, Constants.LIVING_CONTROLLER, 5, event -> {
            var isDead = this.dead || this.getHealth() < 0.01 || this.isDeadOrDying();
            if (isDead) return event.setAndContinue(GigAnimationsDefault.DEATH);
            if (event.isMoving() && !(this.isCrawling() || this.isTunnelCrawling()) && !this.isExecuting() && !this.isPassedOut() && !this.swinging && !(this.level().getFluidState(
                    this.blockPosition()).is(Fluids.WATER) && this.level().getFluidState(
                    this.blockPosition()).getAmount() >= 8))
                if (walkAnimation.speedOld >= 0.45F && this.getFirstPassenger() == null)
                    return event.setAndContinue(GigAnimationsDefault.RUN);
                else if (!this.isExecuting() && walkAnimation.speedOld < 0.45F)
                    return event.setAndContinue(GigAnimationsDefault.WALK);
            return event.setAndContinue(this.isNoAi() ? GigAnimationsDefault.STATIS_ENTER : GigAnimationsDefault.IDLE);
        }).triggerableAnim("death", GigAnimationsDefault.DEATH) // death
                .triggerableAnim("idle", GigAnimationsDefault.IDLE) // idle
                .setSoundKeyframeHandler(event -> {
                    if (event.getKeyframeData().getSound().matches("footstepSoundkey") && this.level().isClientSide)
                        this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_FOOTSTEP.get(),
                                SoundSource.HOSTILE, 0.5F, 1.0F, true);
                    if (event.getKeyframeData().getSound().matches("idleSoundkey") && this.level().isClientSide)
                        this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_AMBIENT.get(),
                                SoundSource.HOSTILE, 1.0F, 1.0F, true);
                })).add(new AnimationController<>(this, Constants.ATTACK_CONTROLLER, 1, event -> {
            if (event.getAnimatable().isPassedOut())
                return event.setAndContinue(RawAnimation.begin().thenLoop("stasis_loop"));
            return PlayState.STOP;
        }).triggerableAnim("alert", GigAnimationsDefault.AMBIENT) // reset hands
                .triggerableAnim("death", GigAnimationsDefault.DEATH) // death
                .triggerableAnim("alert", GigAnimationsDefault.HISS) // reset hands
                .triggerableAnim("passout", GigAnimationsDefault.STATIS_ENTER) // pass out
                .triggerableAnim("passoutloop", GigAnimationsDefault.STATIS_LOOP) // pass out
                .triggerableAnim("wakeup",
                        GigAnimationsDefault.STATIS_LEAVE.then("idle_land", Animation.LoopType.PLAY_ONCE)) // wake up
                .triggerableAnim("swipe", GigAnimationsDefault.LEFT_CLAW) // swipe
                .triggerableAnim("left_claw", GigAnimationsDefault.LEFT_CLAW) // attack
                .triggerableAnim("right_claw", GigAnimationsDefault.RIGHT_CLAW) // attack
                .triggerableAnim("left_tail_basic", GigAnimationsDefault.LEFT_TAIL_BASIC) // attack
                .triggerableAnim("right_tail_basic", GigAnimationsDefault.RIGHT_TAIL_BASIC) // attack
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
                    this.blockPosition()).getAmount() >= 8))
                return event.setAndContinue(GigAnimationsDefault.HISS);
            return PlayState.STOP;
        }).setSoundKeyframeHandler(event -> {
            if (event.getKeyframeData().getSound().matches("hissSoundkey") && this.level().isClientSide)
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.ALIEN_HISS.get(),
                        SoundSource.HOSTILE, 1.0F, 1.0F, true);
        }).triggerableAnim("hiss", GigAnimationsDefault.HISS));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
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
    public List<ExtendedSensor<DraconicTempleBeastEntity>> getSensors() {
        return ObjectArrayList.of(new NearbyPlayersSensor<>(),
                new NearbyLivingEntitySensor<DraconicTempleBeastEntity>().setPredicate(
                        GigEntityUtils::entityTest),
                new NearbyBlocksSensor<DraconicTempleBeastEntity>().setRadius(7),
                new NearbyRepellentsSensor<DraconicTempleBeastEntity>().setRadius(15).setPredicate(
                        (block, entity) -> block.is(GigTags.ALIEN_REPELLENTS) || block.is(Blocks.LAVA)),
                new NearbyLightsBlocksSensor<DraconicTempleBeastEntity>().setRadius(7).setPredicate(
                        (block, entity) -> block.is(GigTags.DESTRUCTIBLE_LIGHT)), new HurtBySensor<>(),
                new UnreachableTargetSensor<>(), new HurtBySensor<>());
    }

    @Override
    public BrainActivityGroup<DraconicTempleBeastEntity> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                // Looks at target
                new LookAtTarget<>().stopIf(entity -> this.isPassedOut()).startCondition(
                        entity -> !this.isPassedOut() || !this.isSearching()),
                // Flee Fire
                new FleeFireTask<>(3.5F),
                // Move to target
                new MoveToWalkTarget<>().startCondition(entity -> !this.isPassedOut()).stopIf(
                        entity -> this.isPassedOut()));
    }

    @Override
    public BrainActivityGroup<DraconicTempleBeastEntity> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
                // Kill Lights
                new KillLightsTask<>().stopIf(target -> (this.isAggressive() || this.isVehicle())),
                // Do first
                new FirstApplicableBehaviour<DraconicTempleBeastEntity>(
                        // Targeting
                        new TargetOrRetaliate<>(),
                        // Look at players
                        new SetPlayerLookTarget<>().predicate(
                                target -> target.isAlive() && (!target.isCreative() || !target.isSpectator())),
                        // Look around randomly
                        new SetRandomLookTarget<>()),
                // Random
                new OneRandomBehaviour<>(
                        // Randomly walk around
                        new SetRandomWalkTarget<>().speedModifier(0.90f).startCondition(
                                entity -> !this.isPassedOut()).stopIf(entity -> this.isPassedOut()),
                        // Idle
                        new Idle<>().startCondition(entity -> !this.isAggressive()).runFor(
                                entity -> entity.getRandom().nextInt(30, 60))));
    }

    @Override
    public BrainActivityGroup<DraconicTempleBeastEntity> getFightTasks() {
        return BrainActivityGroup.fightTasks(
                // Invalidate Target
                new InvalidateAttackTarget<>().invalidateIf(
                        (entity, target) -> GigEntityUtils.removeTarget(target)),
                // Walk to Target
                new SetWalkTargetToAttackTarget<>().speedMod(
                        (owner, target) -> (12.0F)).stopIf(
                        entity -> this.isPassedOut()),
                // Xeno attacking
                new AlienMeleeAttack<>(10, GigMeleeAttackSelector.DRACONIC_ANIM_SELECTOR));
    }

}
