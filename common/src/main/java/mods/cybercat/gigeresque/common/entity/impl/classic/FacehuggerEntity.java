package mods.cybercat.gigeresque.common.entity.impl.classic;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mod.azure.azurelib.common.internal.common.util.AzureLibUtil;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.Animation;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;
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
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyRepellentsSensor;
import mods.cybercat.gigeresque.common.entity.ai.tasks.movement.FacehuggerPounceTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.movement.FleeFireTask;
import mods.cybercat.gigeresque.common.entity.helper.AzureVibrationUser;
import mods.cybercat.gigeresque.common.entity.helper.GigAnimationsDefault;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FacehuggerEntity extends AlienEntity implements SmartBrainOwner<FacehuggerEntity> {

    public static final EntityDataAccessor<Boolean> EGGSPAWN = SynchedEntityData.defineId(FacehuggerEntity.class,
            EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> ATTACKING = SynchedEntityData.defineId(FacehuggerEntity.class,
            EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> JUMPING = SynchedEntityData.defineId(FacehuggerEntity.class,
            EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_INFERTILE = SynchedEntityData.defineId(FacehuggerEntity.class,
            EntityDataSerializers.BOOLEAN);
    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);
    public float ticksAttachedToHost = -1.0f;

    public FacehuggerEntity(EntityType<? extends AlienEntity> type, Level world) {
        super(type, world);
        this.vibrationUser = new AzureVibrationUser(this, 1.2F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.MAX_HEALTH, CommonMod.config.facehuggerHealth).add(
                Attributes.ARMOR, 1.0).add(Attributes.ARMOR_TOUGHNESS, 0.0).add(Attributes.KNOCKBACK_RESISTANCE,
                0.0).add(Attributes.ATTACK_KNOCKBACK, 0.0).add(Attributes.ATTACK_DAMAGE, 0.0).add(
                Attributes.FOLLOW_RANGE, 16.0).add(Attributes.MOVEMENT_SPEED, 0.3300000041723251);
    }

    @Override
    protected void tickDeath() {
        ++this.deathTime;
        if (this.deathTime == 200) {
            this.remove(RemovalReason.KILLED);
            super.tickDeath();
            this.dropExperience(this);
        }
    }

    public boolean isEggSpawn() {
        return this.entityData.get(EGGSPAWN);
    }

    public void setEggSpawnState(boolean state) {
        this.entityData.set(EGGSPAWN, state);
    }

    @Override
    public int getAcidDiameter() {
        return this.isPassenger() ? 0 : 1;
    }

    public boolean isInfertile() {
        return entityData.get(IS_INFERTILE);
    }

    public void setIsInfertile(boolean value) {
        entityData.set(IS_INFERTILE, value);
    }

    public boolean isAttacking() {
        return entityData.get(ATTACKING);
    }

    public boolean isJumping() {
        return entityData.get(JUMPING);
    }

    @Override
    public void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_INFERTILE, false);
        builder.define(EGGSPAWN, false);
        builder.define(ATTACKING, false);
        builder.define(JUMPING, false);
    }

    public void detachFromHost(boolean removesParasite) {
        this.ticksAttachedToHost = -1.0f;
        this.unRide();
    }

    @Override
    public @NotNull Vec3 getDismountLocationForPassenger(@NotNull LivingEntity passenger) {
        return super.getDismountLocationForPassenger(passenger);
    }

    public boolean isAttachedToHost() {
        return this.getVehicle() instanceof LivingEntity;
    }

    @Override
    public int calculateFallDamage(float fallDistance, float damageMultiplier) {
        if (fallDistance <= 12) return 0;
        return super.calculateFallDamage(fallDistance, damageMultiplier);
    }

    @Override
    public int getMaxFallDistance() {
        return 12;
    }

    public void grabTarget(LivingEntity entity) {
        this.startRiding(entity, true);
        this.setAggressive(false);
        entity.xxa = 0;
        entity.zza = 0;
        entity.yya = 0;
        entity.yBodyRot = 0;
        entity.setSpeed(0.0f);
        if (CommonMod.config.facehuggerGivesBlindness) entity.addEffect(
                new MobEffectInstance(MobEffects.BLINDNESS, (int) CommonMod.config.facehuggerAttachTickTimer, 0));
        if (entity instanceof ServerPlayer player && (!player.isCreative() || !player.isSpectator()))
            player.connection.send(new ClientboundSetPassengersPacket(entity));
    }

    @Override
    public void tick() {
        super.tick();

        if (isAttachedToHost()) {
            ticksAttachedToHost += 1;

            var host = this.getVehicle();
            if (!(host instanceof LivingEntity livingEntity)) return;

            if (livingEntity != null) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 1000, 10, false, false));
                if (livingEntity.getHealth() > livingEntity.getMaxHealth()) livingEntity.heal(6);
                if (getVehicle() instanceof Player player && player.getFoodData().needsFood())
                    player.getFoodData().setFoodLevel(20);
                if (ticksAttachedToHost > CommonMod.config.getFacehuggerAttachTickTimer()) {
                    if (livingEntity.hasEffect(MobEffects.BLINDNESS)) {
                        livingEntity.removeEffect(MobEffects.BLINDNESS);
                    }
                    if (!livingEntity.hasEffect(GigStatusEffects.IMPREGNATION)) {
                        livingEntity.addEffect(new MobEffectInstance(GigStatusEffects.IMPREGNATION,
                                (int) CommonMod.config.getImpregnationTickTimer(), 0, false, true));
                    }
                    if (!level().isClientSide)
                        this.level().playSound(this, this.blockPosition(), GigSounds.HUGGER_IMPLANT.get(),
                                SoundSource.HOSTILE, 1.0F, 1.0F);
                    setIsInfertile(true);
                    this.unRide();
                    this.hurt(damageSources().genericKill(), Float.MAX_VALUE);
                }
            }

            if (livingEntity != null && livingEntity.hasEffect(GigStatusEffects.IMPREGNATION)) {
                if (livingEntity.hasEffect(MobEffects.BLINDNESS)) livingEntity.removeEffect(MobEffects.BLINDNESS);
                detachFromHost(true);
                setIsInfertile(true);
                this.kill();
            }
            if (Constants.isCreativeSpecPlayer.test(host)) {
                detachFromHost(true);
                setIsInfertile(true);
                this.kill();
            }
        } else ticksAttachedToHost = -1.0f;

        if (isInfertile()) {
            this.kill();
            this.removeFreeWill();
            return;
        }
        if (this.isEggSpawn() && this.tickCount > 30) {
            if (this.hasEffect(MobEffects.SLOW_FALLING)) this.removeEffect(MobEffects.SLOW_FALLING);
            this.setEggSpawnState(false);
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("isInfertile", isInfertile());
        nbt.putFloat("ticksAttachedToHost", ticksAttachedToHost);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        if (nbt.contains("isInfertile")) setIsInfertile(nbt.getBoolean("isInfertile"));
        if (nbt.contains("ticksAttachedToHost")) ticksAttachedToHost = nbt.getFloat("ticksAttachedToHost");
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if ((isAttachedToHost() || isInfertile()) && (source == damageSources().drown())) return false;

        if (!this.level().isClientSide && source.getEntity() != null && source.getEntity() instanceof LivingEntity livingEntity)
            this.brain.setMemory(MemoryModuleType.ATTACK_TARGET, livingEntity);

        return super.hurt(source, amount);
    }

    @Override
    public void knockback(double strength, double x, double z) {
        if (!isInfertile()) super.knockback(strength, x, z);
    }

    @Override
    public SoundEvent getHurtSound(@NotNull DamageSource source) {
        return (isAttachedToHost() || isInfertile()) ? SoundEvents.EMPTY : GigSounds.HUGGER_HURT.get();
    }

    @Override
    public SoundEvent getDeathSound() {
        return (isAttachedToHost() || isInfertile()) ? SoundEvents.EMPTY : GigSounds.HUGGER_DEATH.get();
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState state) {
        playSound(SoundEvents.STRIDER_STEP, 0.05f, 10.0f);
    }

    @Override
    protected void jumpInLiquid(@NotNull TagKey<Fluid> fluid) {
    }

    @Override
    public void stopRiding() {
        if (this.getVehicle() != null && this.getVehicle() instanceof LivingEntity livingEntity && livingEntity.isAlive() && ticksAttachedToHost < Constants.TPM * 5 && isInWater())
            return;
        super.stopRiding();
    }

    @Override
    public boolean isPathFinding() {
        return false;
    }

    @Override
    public boolean onClimbable() {
        setIsCrawling(this.horizontalCollision && !this.isNoGravity() && !this.level().getBlockState(
                this.blockPosition().above()).is(BlockTags.STAIRS) || this.isAggressive());
        return !this.level().getBlockState(this.blockPosition().above()).is(
                BlockTags.STAIRS) && !this.isAggressive() && (this.fallDistance <= 0.1 || this.isEggSpawn());
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
    public List<ExtendedSensor<FacehuggerEntity>> getSensors() {
        return ObjectArrayList.of(new NearbyPlayersSensor<>(),
                new NearbyLivingEntitySensor<FacehuggerEntity>().setPredicate(
                        (target, self) -> GigEntityUtils.entityTest(target,
                                self) || !(target instanceof Creeper || target instanceof IronGolem) && !target.getType().is(
                                EntityTypeTags.UNDEAD)),
                new NearbyBlocksSensor<FacehuggerEntity>().setRadius(7),
                new NearbyRepellentsSensor<FacehuggerEntity>().setRadius(15).setPredicate(
                        (block, entity) -> block.is(GigTags.ALIEN_REPELLENTS) || block.is(Blocks.LAVA)),
                new UnreachableTargetSensor<>(), new HurtBySensor<>());
    }

    @Override
    public BrainActivityGroup<FacehuggerEntity> getCoreTasks() {
        return BrainActivityGroup.coreTasks(new LookAtTarget<>(), new FleeFireTask<>(1.2F), new MoveToWalkTarget<>());
    }

    @Override
    public BrainActivityGroup<FacehuggerEntity> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
                new FirstApplicableBehaviour<FacehuggerEntity>(
                        new TargetOrRetaliate<>(),
                        new SetPlayerLookTarget<>().predicate(target -> target.isAlive() && (!target.isCreative() || !target.isSpectator())),
                        new SetRandomLookTarget<>()),
                new OneRandomBehaviour<>(
                        new SetRandomWalkTarget<>().speedModifier(0.65f),
                        new Idle<>().runFor(entity -> entity.getRandom().nextInt(30, 60))));
    }

    @Override
    public BrainActivityGroup<FacehuggerEntity> getFightTasks() {
        return BrainActivityGroup.fightTasks(new InvalidateAttackTarget<>().invalidateIf(
                        (entity, target) -> GigEntityUtils.removeFaceHuggerTarget(
                                target) || target.getType().is(EntityTypeTags.UNDEAD)),
                new SetWalkTargetToAttackTarget<>().speedMod((owner, target) -> 1.85F), new FacehuggerPounceTask<>(6));
    }

    /*
     * ANIMATIONS
     */
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, Constants.LIVING_CONTROLLER, 5, event -> {
            if (this.getVehicle() instanceof LivingEntity && !this.isDeadOrDying())
                return event.setAndContinue(GigAnimationsDefault.IMPREGNATE);
            if (!this.isJumping() && !this.isAttacking() && isInfertile() || this.isDeadOrDying())
                return event.setAndContinue(GigAnimationsDefault.DEATH);
            if (!this.isJumping() && this.isUnderWater() && !(this.isCrawling() || this.isTunnelCrawling()) && !this.isDeadOrDying())
                if (!this.isAttacking() && event.isMoving()) return event.setAndContinue(GigAnimationsDefault.SWIM);
                else if (this.isAttacking() && event.isMoving())
                    return event.setAndContinue(GigAnimationsDefault.RUSH_SWIM);
                else return event.setAndContinue(GigAnimationsDefault.IDLE_WATER);
            if (this.isJumping()) return event.setAndContinue(GigAnimationsDefault.CHARGE);
            if (this.isEggSpawn() && !this.isDeadOrDying())
                return event.setAndContinue(GigAnimationsDefault.HATCH_LEAP);
            if (!this.isJumping() && this.isAttacking() && !this.isDeadOrDying() && event.isMoving()) {
                event.getController().setAnimationSpeed(3f);
                return event.setAndContinue(GigAnimationsDefault.CRAWL_RUSH);
            }
            if (!this.isJumping() && !this.isEggSpawn() && event.isMoving() && !this.isDeadOrDying() && !this.isAttacking()) {
                event.getController().setAnimationSpeed(3f);
                return event.setAndContinue(GigAnimationsDefault.CRAWL);
            }
            return event.setAndContinue(GigAnimationsDefault.IDLE_LAND);
        }).setSoundKeyframeHandler(event -> {
            if (event.getKeyframeData().getSound().matches("huggingSoundkey") && this.level().isClientSide)
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.HUGGER_IMPLANT.get(),
                        SoundSource.HOSTILE, 0.25F, 1.0F, true);
        }).triggerableAnim("stun", RawAnimation.begin().then("stunned", Animation.LoopType.PLAY_ONCE)));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
