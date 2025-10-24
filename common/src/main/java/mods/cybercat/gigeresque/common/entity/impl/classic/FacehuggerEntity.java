package mods.cybercat.gigeresque.common.entity.impl.classic;

import mod.azure.azurelib.common.util.MoveAnalysis;
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
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.ai.goals.attack.FacehuggerRunToTargetGoal;
import mods.cybercat.gigeresque.common.entity.ai.goals.attack.LungeAtTargetGoal;
import mods.cybercat.gigeresque.common.entity.ai.goals.movement.FleeExplodingCreeperGoal;
import mods.cybercat.gigeresque.common.entity.ai.goals.movement.FleeFightGoal;
import mods.cybercat.gigeresque.common.entity.ai.goals.movement.FleeFireGoal;
import mods.cybercat.gigeresque.common.entity.ai.goals.movement.StrollAroundInWaterGoal;
import mods.cybercat.gigeresque.common.entity.helper.AnimationDispatcher;
import mods.cybercat.gigeresque.common.entity.helper.AzureVibrationUser;
import mods.cybercat.gigeresque.common.entity.helper.GigCommonMethods;
import mods.cybercat.gigeresque.common.entity.helper.GigMeleeAttackSelector;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;

/**
 * TODO: Ensure crawling works good
 */
public class FacehuggerEntity extends AlienEntity {

    private static final EntityDataAccessor<Boolean> IS_INFERTILE = SynchedEntityData.defineId(
        FacehuggerEntity.class,
        EntityDataSerializers.BOOLEAN
    );

    public float ticksAttachedToHost = -1.0f;

    public FacehuggerEntity(EntityType<? extends AlienEntity> type, Level world) {
        super(type, world, Options.standardAlien());
        this.animationDispatcher = new AnimationDispatcher(this);
        this.moveAnalysis = new MoveAnalysis(this);
        this.vibrationUser = new AzureVibrationUser(this, 1.0F);
        this.animationSelector = GigMeleeAttackSelector.HUGGER_SELECTOR;
        this.climbingManager.canClimb = true;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
            .add(Attributes.MAX_HEALTH, CommonMod.config.entityConfigs.facehuggerConfigs.facehuggerHealth)
            .add(
                Attributes.ARMOR,
                1.0
            )
            .add(Attributes.ARMOR_TOUGHNESS, 0.0)
            .add(
                Attributes.KNOCKBACK_RESISTANCE,
                0.0
            )
            .add(Attributes.ATTACK_KNOCKBACK, 0.0)
            .add(Attributes.ATTACK_DAMAGE, 0.0)
            .add(
                Attributes.FOLLOW_RANGE,
                16.0
            )
            .add(Attributes.MOVEMENT_SPEED, 0.3300000041723251);
    }

    @Override
    protected void tickDeath() {
        ++this.deathTime;
        if (this.deathTime == 60) {
            this.remove(RemovalReason.KILLED);
            super.tickDeath();
            this.dropExperience(this);
        }
    }

    @Override
    public int getBloodDiameter() {
        return 1;
    }

    public boolean isInfertile() {
        return entityData.get(IS_INFERTILE);
    }

    public void setIsInfertile(boolean value) {
        entityData.set(IS_INFERTILE, value);
    }

    @Override
    public void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_INFERTILE, false);
    }

    public void detachFromHost() {
        this.ticksAttachedToHost = -1.0f;
        this.unRide();
    }

    public boolean isAttachedToHost() {
        return this.getVehicle() instanceof LivingEntity && this.getVehicle().isAlive();
    }

    @Override
    public int calculateFallDamage(float fallDistance, float damageMultiplier) {
        if (fallDistance <= 12)
            return 0;
        return super.calculateFallDamage(fallDistance, damageMultiplier);
    }

    @Override
    public int getMaxFallDistance() {
        return 12;
    }

    public void grabTarget(LivingEntity entity) {
        this.startRiding(entity, true);
        this.setAggressive(false);
        entity.setSpeed(0.0f);
        if (CommonMod.config.entityConfigs.facehuggerConfigs.facehuggerGivesBlindness)
            entity.addEffect(
                new MobEffectInstance(MobEffects.BLINDNESS, (int) CommonMod.config.getFacehuggerAttachTickTimer(), 0)
            );
        if (entity instanceof ServerPlayer player && (!player.isCreative() || !player.isSpectator()))
            player.connection.send(new ClientboundSetPassengersPacket(entity));
    }

    public void handleAttachmentToHost() {
        if (isAttachedToHost()) {
            ticksAttachedToHost += 1;

            var host = this.getVehicle();
            if (!(host instanceof LivingEntity livingEntity))
                return;

            livingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 1000, 10, false, false));
            if (livingEntity.getHealth() > livingEntity.getMaxHealth())
                livingEntity.heal(6);
            if (getVehicle() instanceof Player player && player.getFoodData().needsFood())
                player.getFoodData().setFoodLevel(20);
            if (ticksAttachedToHost > CommonMod.config.getFacehuggerAttachTickTimer()) {
                if (getVehicle() instanceof Player player && player instanceof ServerPlayer serverPlayer) {
                    var advancement = serverPlayer.server.getAdvancements().get(Constants.modResource("facehugged"));
                    if (advancement != null && !serverPlayer.getAdvancements().getOrStartProgress(advancement).isDone()) {
                        for (var s : serverPlayer.getAdvancements().getOrStartProgress(advancement).getRemainingCriteria()) {
                            serverPlayer.getAdvancements().award(advancement, s);
                        }
                    }
                }
                if (livingEntity.hasEffect(MobEffects.BLINDNESS)) {
                    livingEntity.removeEffect(MobEffects.BLINDNESS);
                }
                if (!livingEntity.hasEffect(GigStatusEffects.IMPREGNATION)) {
                    livingEntity.addEffect(
                        new MobEffectInstance(
                            GigStatusEffects.IMPREGNATION,
                            (int) CommonMod.config.getImpregnationTickTimer(),
                            0,
                            false,
                            true
                        )
                    );
                }
                if (!level().isClientSide)
                    this.level()
                        .playSound(
                            this,
                            this.blockPosition(),
                            GigSounds.HUGGER_IMPLANT.get(),
                            SoundSource.HOSTILE,
                            1.0F,
                            1.0F
                        );
                setIsInfertile(true);
                this.unRide();
                this.hurt(damageSources().genericKill(), Float.MAX_VALUE);
            }

            if (livingEntity.hasEffect(GigStatusEffects.IMPREGNATION)) {
                if (livingEntity.hasEffect(MobEffects.BLINDNESS))
                    livingEntity.removeEffect(MobEffects.BLINDNESS);
                detachFromHost();
                setIsInfertile(true);
                this.kill();
            }

            if (Constants.isCreativeSpecPlayer.test(host)) {
                detachFromHost();
                setIsInfertile(true);
                this.kill();
            }
        } else
            ticksAttachedToHost = -1.0f;
    }

    @Override
    public void tick() {
        super.tick();
        moveAnalysis.update();
        this.setGrowth(0);

        if (
            this.getTarget() != null && !this.getTarget().getUseItem().is(Items.SHIELD) && this.getBoundingBox()
                .intersects(this.getTarget().getBoundingBox()) && GigEntityUtils.faceHuggerTest(this.getTarget())
        ) {
            grabTarget(this.getTarget());
        }

        if (
            this.getTarget() != null && !this.level()
                .getEntitiesOfClass(
                    Mob.class,
                    this.getBoundingBox().inflate(5),
                    entity -> GigEntityUtils.faceHuggerTest(entity) &&
                        entity.getUseItem().is(Items.SHIELD)
                )
                .isEmpty()
        ) {
            grabTarget(this.getTarget());
        }
        if (this.isAttachedToHost() && !this.isDeadOrDying()) {
            GigCommonMethods.setAnimation(animationDispatcher::sendImpregate);
        }
        if (this.getVehicle() != null && !this.getVehicle().isAlive() && this.isAlive()) {
            this.stopRiding();
            GigCommonMethods.setAnimation(animationDispatcher::sendStunned);
            this.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 60, 10, false, false));
            this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 10, false, false));
        }
        this.handleAttachmentToHost();
        if (isInfertile()) {
            GigCommonMethods.setAnimation(animationDispatcher::sendDeath);
            this.kill();
            this.removeAllGoals(goals -> true);
            this.getBrain().removeAllBehaviors();
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
        if (nbt.contains("isInfertile"))
            setIsInfertile(nbt.getBoolean("isInfertile"));
        if (nbt.contains("ticksAttachedToHost"))
            ticksAttachedToHost = nbt.getFloat("ticksAttachedToHost");
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if ((isAttachedToHost() || isInfertile()) && (source == damageSources().drown()))
            return false;

        return super.hurt(source, amount);
    }

    @Override
    public void knockback(double strength, double x, double z) {
        if (!isInfertile())
            super.knockback(strength, x, z);
    }

    @Override
    public @NotNull SoundEvent getHurtSound(@NotNull DamageSource source) {
        return (isAttachedToHost() || isInfertile()) ? SoundEvents.EMPTY : GigSounds.HUGGER_HURT.get();
    }

    @Override
    public @NotNull SoundEvent getDeathSound() {
        return (isAttachedToHost() || isInfertile()) ? SoundEvents.EMPTY : GigSounds.HUGGER_DEATH.get();
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState state) {
        playSound(SoundEvents.STRIDER_STEP, 0.05f, 10.0f);
    }

    @Override
    public void stopRiding() {
        if (
            this.getVehicle() != null && this.getVehicle() instanceof LivingEntity livingEntity && livingEntity.isAlive()
                && ticksAttachedToHost < Constants.TPM * 5 && isUnderWater()
        )
            return;
        super.stopRiding();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FleeExplodingCreeperGoal(this));
        this.goalSelector.addGoal(1, new StrollAroundInWaterGoal(this, 0.6));
        this.goalSelector.addGoal(1, new WaterAvoidingRandomStrollGoal(this, 0.96));
        this.goalSelector.addGoal(1, new FleeFightGoal(this));
        this.goalSelector.addGoal(1, new FacehuggerRunToTargetGoal(this, 1.3F, 0));
        this.goalSelector.addGoal(1, new LungeAtTargetGoal(this, 0.75F, 40, 5).setOnLungeCallback(this::runLungeAnimation));
        this.goalSelector.addGoal(5, new FleeFireGoal(this));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 15.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, LivingEntity.class, 15.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, AlienEntity.class).setAlertOthers());
        this.targetSelector.addGoal(
            2,
            new NearestAttackableTargetGoal<>(
                this,
                LivingEntity.class,
                false,
                target -> this.getHealth() > (this.getMaxHealth() / 2) && GigEntityUtils.removeFaceHuggerTarget(target) && !this.hasEffect(
                    MobEffects.CONFUSION
                )
            )
        );
    }

    @Override
    protected void runLungeAnimation() {
        animationDispatcher.sendFacehuggerLunge();
    }

    @Override
    protected @Nullable EntityDimensions swimmingDimensions(Pose pose) {
        return null;
    }

}
