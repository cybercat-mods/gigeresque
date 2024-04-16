package mods.cybercat.gigeresque.common.entity.impl.classic;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager.ControllerRegistrar;
import mod.azure.azurelib.core.animation.Animation.LoopType;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.util.AzureLibUtil;
import mod.azure.bettercrawling.entity.movement.BetterSpiderPathNavigator;
import mod.azure.bettercrawling.entity.movement.ClimberLookController;
import mod.azure.bettercrawling.entity.movement.ClimberMoveController;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.entity.ai.pathing.AmphibiousNavigation;
import mods.cybercat.gigeresque.common.entity.ai.sensors.NearbyRepellentsSensor;
import mods.cybercat.gigeresque.common.entity.ai.tasks.movement.FacehuggerPounceTask;
import mods.cybercat.gigeresque.common.entity.ai.tasks.movement.FleeFireTask;
import mods.cybercat.gigeresque.common.entity.helper.AzureVibrationUser;
import mods.cybercat.gigeresque.common.entity.helper.CrawlerAlien;
import mods.cybercat.gigeresque.common.entity.helper.GigAnimationsDefault;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;
import mods.cybercat.gigeresque.interfacing.Host;
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
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
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
import net.tslat.smartbrainlib.api.core.sensor.custom.UnreachableTargetSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FacehuggerEntity extends CrawlerAlien implements GeoEntity, SmartBrainOwner<FacehuggerEntity> {

    public static final EntityDataAccessor<Boolean> EGGSPAWN = SynchedEntityData.defineId(FacehuggerEntity.class,
            EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> ATTACKING = SynchedEntityData.defineId(FacehuggerEntity.class,
            EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> JUMPING = SynchedEntityData.defineId(FacehuggerEntity.class,
            EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_INFERTILE = SynchedEntityData.defineId(FacehuggerEntity.class,
            EntityDataSerializers.BOOLEAN);
    private final BetterSpiderPathNavigator<?> landNavigation = new BetterSpiderPathNavigator<>(this, level(), false);
    private final AmphibiousNavigation swimNavigation = new AmphibiousNavigation(this, level());
    private final SmoothSwimmingMoveControl swimMoveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.7f, 1.0f,
            false);
    private final SmoothSwimmingLookControl swimLookControl = new SmoothSwimmingLookControl(this, 10);
    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);
    public float ticksAttachedToHost = -1.0f;

    public FacehuggerEntity(EntityType<? extends CrawlerAlien> type, Level world) {
        super(type, world);
        this.vibrationUser = new AzureVibrationUser(this, 1.2F);
        this.navigation = landNavigation;
        this.setMaxUpStep(0.1f);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.MAX_HEALTH, Gigeresque.config.facehuggerHealth).add(
                Attributes.ARMOR, 1.0).add(Attributes.ARMOR_TOUGHNESS, 0.0).add(Attributes.KNOCKBACK_RESISTANCE,
                0.0).add(Attributes.ATTACK_KNOCKBACK, 0.0).add(Attributes.ATTACK_DAMAGE, 0.0).add(
                Attributes.FOLLOW_RANGE, 16.0).add(Attributes.MOVEMENT_SPEED, 0.3300000041723251);
    }

    @Override
    protected void tickDeath() {
        ++this.deathTime;
        if (this.deathTime == 200) {
            this.remove(Entity.RemovalReason.KILLED);
            super.tickDeath();
            this.dropExperience();
        }
    }

    public boolean isEggSpawn() {
        return this.entityData.get(EGGSPAWN);
    }

    public void setEggSpawnState(boolean state) {
        this.entityData.set(EGGSPAWN, state);
    }

    @Override
    protected int getAcidDiameter() {
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
    public void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(IS_INFERTILE, false);
        entityData.define(EGGSPAWN, false);
        entityData.define(ATTACKING, false);
        entityData.define(JUMPING, false);
    }

    public void detachFromHost(boolean removesParasite) {
        this.ticksAttachedToHost = -1.0f;
        var vehicle = this.getVehicle();
        if (vehicle instanceof LivingEntity && removesParasite) ((Host) vehicle).removeParasite();
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
        entity.yBodyRot = this.yBodyRot;
        entity.xxa = 0;
        entity.zza = 0;
        entity.yya = 0;
        entity.yBodyRot = 0;
        entity.setSpeed(0.0f);
        if (Gigeresque.config.facehuggerGivesBlindness) entity.addEffect(
                new MobEffectInstance(MobEffects.BLINDNESS, (int) Gigeresque.config.facehuggerAttachTickTimer, 0));
        if (entity instanceof ServerPlayer player && (!player.isCreative() || !player.isSpectator()))
            player.connection.send(new ClientboundSetPassengersPacket(entity));
    }

    @Override
    public void tick() {
        super.tick();

        if (isAttachedToHost()) {
            ticksAttachedToHost += 1;

            var host = (Host) this.getVehicle();

            if (host != null) {
                ((LivingEntity) getVehicle()).addEffect(
                        new MobEffectInstance(MobEffects.WEAKNESS, 1000, 10, false, false));
                if (((LivingEntity) getVehicle()).getHealth() > ((LivingEntity) getVehicle()).getMaxHealth())
                    ((LivingEntity) getVehicle()).heal(6);
                if (getVehicle() instanceof Player player && player.getFoodData().needsFood())
                    player.getFoodData().setFoodLevel(20);
                if (host.doesNotHaveParasite()) host.setTicksUntilImpregnation(
                        Gigeresque.config.getImpregnationTickTimer() + Gigeresque.config.getFacehuggerAttachTickTimer());
                if (ticksAttachedToHost > Gigeresque.config.getFacehuggerAttachTickTimer()) {
                    if (((LivingEntity) host).hasEffect(MobEffects.BLINDNESS))
                        ((LivingEntity) host).removeEffect(MobEffects.BLINDNESS);
                    if (!level().isClientSide)
                        this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.HUGGER_IMPLANT,
                                SoundSource.HOSTILE, 1.0F, 1.0F, true);
                    setIsInfertile(true);
                    this.unRide();
                    this.hurt(damageSources().genericKill(), Float.MAX_VALUE);
                }
            }

            var vehicle = this.getVehicle();
            if (vehicle != null && ((Host) vehicle).isBleeding()) {
                if (((LivingEntity) vehicle).hasEffect(MobEffects.BLINDNESS))
                    ((LivingEntity) vehicle).removeEffect(MobEffects.BLINDNESS);
                detachFromHost(true);
                setIsInfertile(true);
                this.kill();
            }
            if (vehicle instanceof Player player && (player.isCreative() || player.isSpectator())) {
                assert host != null;
                host.setTicksUntilImpregnation(-1);
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
        if (this.isEggSpawn() && this.tickCount > 30) this.setEggSpawnState(false);
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
    public boolean doHurtTarget(@NotNull Entity target) {
        return super.doHurtTarget(target);
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
    public double getMeleeAttackRangeSqr(@NotNull LivingEntity target) {
        return 4.5;
    }

    @Override
    public SoundEvent getHurtSound(@NotNull DamageSource source) {
        return (isAttachedToHost() || isInfertile()) ? SoundEvents.EMPTY : GigSounds.HUGGER_HURT;
    }

    @Override
    public SoundEvent getDeathSound() {
        return (isAttachedToHost() || isInfertile()) ? SoundEvents.EMPTY : GigSounds.HUGGER_DEATH;
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState state) {
        playSound(SoundEvents.STRIDER_STEP, 0.05f, 10.0f);
    }

    @Override
    protected float nextStep() {
        return this.moveDist + 0.25f;
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
    public void travel(@NotNull Vec3 movementInput) {
        this.navigation = (this.isUnderWater() || (this.level().getFluidState(this.blockPosition()).is(
                Fluids.WATER) && this.level().getFluidState(
                this.blockPosition()).getAmount() >= 8)) ? swimNavigation : landNavigation;
        this.moveControl = (this.wasEyeInWater || (this.level().getFluidState(this.blockPosition()).is(
                Fluids.WATER) && this.level().getFluidState(
                this.blockPosition()).getAmount() >= 8)) ? swimMoveControl : new ClimberMoveController<>(this);
        this.lookControl = (this.wasEyeInWater || (this.level().getFluidState(this.blockPosition()).is(
                Fluids.WATER) && this.level().getFluidState(
                this.blockPosition()).getAmount() >= 8)) ? swimLookControl : new ClimberLookController<>(this);

        this.navigation.setCanFloat(true);
        if (isEffectiveAi() && (this.level().getFluidState(this.blockPosition()).is(
                Fluids.WATER) && this.level().getFluidState(this.blockPosition()).getAmount() >= 8)) {
            moveRelative(getSpeed(), movementInput);
            move(MoverType.SELF, getDeltaMovement());
            setDeltaMovement(getDeltaMovement().scale(0.9));
            if (getTarget() == null) setDeltaMovement(getDeltaMovement().add(0.0, -0.005, 0.0));
        } else super.travel(movementInput);
    }

    @Override
    public boolean isPathFinding() {
        return false;
    }

    @Override
    public @NotNull EntityDimensions getDimensions(@NotNull Pose pose) {
        return super.getDimensions(pose);
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
                                self) || !(target instanceof Creeper || target instanceof IronGolem) && target.getMobType() != MobType.UNDEAD),
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
        return BrainActivityGroup.idleTasks(new FirstApplicableBehaviour<FacehuggerEntity>(new TargetOrRetaliate<>(),
                new SetPlayerLookTarget<>().predicate(
                        target -> target.isAlive() && (!target.isCreative() || !target.isSpectator())),
                new SetRandomLookTarget<>()), new OneRandomBehaviour<>(new SetRandomWalkTarget<>().speedModifier(0.65f),
                new Idle<>().runFor(entity -> entity.getRandom().nextInt(30, 60))));
    }

    @Override
    public BrainActivityGroup<FacehuggerEntity> getFightTasks() {
        return BrainActivityGroup.fightTasks(new InvalidateAttackTarget<>().invalidateIf(
                        (entity, target) -> GigEntityUtils.removeFaceHuggerTarget(
                                target) || target.getMobType() == MobType.UNDEAD),
                new SetWalkTargetToAttackTarget<>().speedMod((owner, target) -> 1.05F), new FacehuggerPounceTask<>(6));
    }

    /*
     * ANIMATIONS
     */
    @Override
    public void registerControllers(ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, Constants.LIVING_CONTROLLER, 5, event -> {
            if (this.getVehicle() instanceof LivingEntity && !this.isDeadOrDying())
                return event.setAndContinue(GigAnimationsDefault.IMPREGNATE);
            if (!this.isUpsideDown() && !this.isJumping() && !this.isAttacking() && isInfertile() || this.isDeadOrDying())
                return event.setAndContinue(GigAnimationsDefault.DEATH);
            if (!this.isUpsideDown() && !this.isJumping() && this.isUnderWater() && !(this.isCrawling() || this.isTunnelCrawling()) && !this.isDeadOrDying())
                if (!this.isAttacking() && event.isMoving()) return event.setAndContinue(GigAnimationsDefault.SWIM);
                else if (this.isAttacking() && event.isMoving())
                    return event.setAndContinue(GigAnimationsDefault.RUSH_SWIM);
                else return event.setAndContinue(GigAnimationsDefault.IDLE_WATER);
            if (this.isJumping()) return event.setAndContinue(GigAnimationsDefault.CHARGE);
            if (this.isEggSpawn() && !this.isDeadOrDying())
                return event.setAndContinue(GigAnimationsDefault.HATCH_LEAP);
            if (!this.isUpsideDown() && !this.isJumping() && this.isAttacking() && !this.isDeadOrDying()) {
                event.getController().setAnimationSpeed(3f);
                return event.setAndContinue(GigAnimationsDefault.CRAWL_RUSH);
            }
            if (!this.isUpsideDown() && !this.isJumping() && !this.isAttacking() && !this.isEggSpawn() && (walkAnimation.speedOld > 0.05F) && !(this.isCrawling() || this.isTunnelCrawling()) && !this.isDeadOrDying()) {
                event.getController().setAnimationSpeed(3f);
                return event.setAndContinue(GigAnimationsDefault.CRAWL);
            }
            if (!this.isUpsideDown() && !this.isJumping() && (this.isCrawling() || this.isTunnelCrawling()) && !this.isDeadOrDying()) {
                event.getController().setAnimationSpeed(3f);
                return event.setAndContinue(GigAnimationsDefault.CRAWL);
            }
            return event.setAndContinue(GigAnimationsDefault.IDLE_LAND);
        }).setSoundKeyframeHandler(event -> {
            if (event.getKeyframeData().getSound().matches("huggingSoundkey") && this.level().isClientSide)
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.HUGGER_IMPLANT,
                        SoundSource.HOSTILE, 0.25F, 1.0F, true);
        }).triggerableAnim("stun", RawAnimation.begin().then("stunned", LoopType.PLAY_ONCE)));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
