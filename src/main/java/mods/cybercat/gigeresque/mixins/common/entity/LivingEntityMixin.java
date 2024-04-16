package mods.cybercat.gigeresque.mixins.common.entity;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.particle.Particles;
import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.Entities;
import mods.cybercat.gigeresque.common.entity.impl.classic.AlienEggEntity;
import mods.cybercat.gigeresque.common.entity.impl.classic.ClassicAlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.classic.FacehuggerEntity;
import mods.cybercat.gigeresque.common.entity.impl.runner.RunnerbursterEntity;
import mods.cybercat.gigeresque.common.fluid.GigFluids;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.source.GigDamageSources;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import mods.cybercat.gigeresque.common.status.effect.impl.DNAStatusEffect;
import mods.cybercat.gigeresque.common.status.effect.impl.SporeStatusEffect;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;
import mods.cybercat.gigeresque.interfacing.Eggmorphable;
import mods.cybercat.gigeresque.interfacing.Host;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Boston Vanseghi
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Host, Eggmorphable {

    private static final EntityDataAccessor<Boolean> IS_BLEEDING = SynchedEntityData.defineId(LivingEntity.class,
            EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> EGGMORPH_TICKS = SynchedEntityData.defineId(LivingEntity.class,
            EntityDataSerializers.FLOAT);
    public float ticksUntilImpregnation = -1.0f;
    public float ticksUntilEggmorpth = -1.0f;
    public boolean hasParasiteSpawned = false;
    public boolean hasEggSpawned = false;

    protected LivingEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Shadow
    abstract boolean hasEffect(MobEffect effect);

    @Shadow
    abstract boolean addEffect(MobEffectInstance effect);

    @Shadow
    public abstract boolean hurt(DamageSource source, float amount);

    @Shadow
    public abstract boolean isDeadOrDying();

    @Shadow
    public abstract float getMaxHealth();

    @Shadow
    public abstract boolean isAlive();

    @Shadow
    public abstract float getHealth();

    @Shadow
    public abstract void kill();

    private void handleStatusEffect(long offset, MobEffect statusEffect, Boolean checkStatusEffect) {
        if (ticksUntilImpregnation < offset && (!checkStatusEffect || !hasEffect(statusEffect))) {
            var amplifier = (int) (((Constants.TPD - (Constants.TPM * 8L)) - ticksUntilImpregnation) / (Constants.TPS * 30));
            this.addEffect(new MobEffectInstance(statusEffect, (int) ticksUntilImpregnation, amplifier, true, true));
        }
    }

    @Inject(method = {"hurt"}, at = {@At("HEAD")}, cancellable = true)
    public void hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfo) {
        if ((this.getVehicle() != null && this.getVehicle() instanceof AlienEntity) && (source == damageSources().drown() || source == damageSources().inWall()) && amount < 1)
            callbackInfo.setReturnValue(false);
        if ((this.getVehicle() != null && this.getVehicle() instanceof ClassicAlienEntity) && source == damageSources().inWall())
            callbackInfo.setReturnValue(false);
        if (amount >= 2 && this.getFirstPassenger() != null && this.getPassengers().stream().anyMatch(
                FacehuggerEntity.class::isInstance)) {
            this.getFirstPassenger().hurt(source, amount / 2);
            ((FacehuggerEntity) this.getFirstPassenger()).addEffect(
                    new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, Gigeresque.config.facehuggerStunTickTimer, 100,
                            false, false));
            ((FacehuggerEntity) this.getFirstPassenger()).triggerAnim(Constants.LIVING_CONTROLLER, "stun");
            ((FacehuggerEntity) this.getFirstPassenger()).detachFromHost(false);
        }
    }

    @Inject(method = {"doPush"}, at = {@At("HEAD")}, cancellable = true)
    void pushAway(CallbackInfo callbackInfo) {
        if (this.isEggmorphing() && GigEntityUtils.isTargetHostable(this)) callbackInfo.cancel();
    }

    @Inject(method = {"tick"}, at = {@At("HEAD")})
    void tick(CallbackInfo callbackInfo) {
        if (this.isAlive() && this.level().isClientSide && this.isBleeding() && this.hasParasite()) {
            var yOffset = this.getEyeY() - ((this.getEyeY() - this.blockPosition().getY()) / 2.0);
            var customX = this.getX() + ((random.nextDouble() / 2.0) - 0.5) * (random.nextBoolean() ? -1 : 1);
            var customZ = this.getZ() + ((random.nextDouble() / 2.0) - 0.5) * (random.nextBoolean() ? -1 : 1);

            for (var i = 0; i < 1 + (int) (this.getMaxHealth() - this.getHealth()); i++)
                this.level().addAlwaysVisibleParticle(Particles.BLOOD, customX, yOffset, customZ, 0.0, -0.15, 0.0);
        }
        if (!this.level().isClientSide && (this.level().getFluidState(
                this.blockPosition()).getType() == GigFluids.BLACK_FLUID_STILL || this.level().getFluidState(
                this.blockPosition()).getType() == GigFluids.BLACK_FLUID_FLOWING) && !GigEntityUtils.isTargetDNAImmune(
                this)) {
            if (!this.hasEffect(
                    GigStatusEffects.DNA) && !(((Object) this) instanceof Player) && !(((Object) this) instanceof AlienEntity) && !(((Object) this) instanceof Creeper) && !(GigEntityUtils.isTargetDNAImmune(
                    this))) this.addEffect(
                    new MobEffectInstance(GigStatusEffects.DNA, Gigeresque.config.getgooEffectTickTimer(), 0));
            if (!this.hasEffect(
                    GigStatusEffects.DNA) && ((Object) this) instanceof Creeper && !(((Object) this) instanceof Player) && !(((Object) this) instanceof AlienEntity))
                this.addEffect(new MobEffectInstance(GigStatusEffects.DNA, 60000, 0));
            if (!this.hasEffect(
                    GigStatusEffects.DNA) && (((Object) this) instanceof Player playerEntity && !(playerEntity.isCreative() || this.isSpectator())) && !(((Object) this) instanceof AlienEntity))
                this.addEffect(
                        new MobEffectInstance(GigStatusEffects.DNA, Gigeresque.config.getgooEffectTickTimer(), 0));
        }

        if (!this.level().isClientSide && ((((Object) this) instanceof Player playerEntity && (playerEntity.isCreative() || this.isSpectator())) || level().getDifficulty() == Difficulty.PEACEFUL) || (((Object) this) instanceof AlienEntity) || this.getType().is(
                GigTags.FACEHUGGER_BLACKLIST)) {
            removeParasite();
            resetEggmorphing();
            setBleeding(false);
        }
        if (!this.level().isClientSide && GigEntityUtils.isTargetHostable(this)) {
            handleEggingLogic();
            handleHostLogic();
        }
    }

    @Inject(method = {"isUsingItem"}, at = {@At("RETURN")}, cancellable = true)
    public void isUsingItem(CallbackInfoReturnable<Boolean> callbackInfo) {
        if (this.getPassengers().stream().anyMatch(FacehuggerEntity.class::isInstance) || this.isEggmorphing())
            callbackInfo.setReturnValue(false);
    }

    @Inject(method = {"isPushable"}, at = {@At("RETURN")}, cancellable = true)
    public void noPush(CallbackInfoReturnable<Boolean> callbackInfo) {
        if (this.isEggmorphing() && GigEntityUtils.isTargetHostable(this)) callbackInfo.setReturnValue(false);
    }

    private void handleEggingLogic() {
        if (isEggmorphing() && GigEntityUtils.isTargetHostable(this) && !hasParasite())
            setTicksUntilEggmorphed(ticksUntilEggmorpth++);
        else resetEggmorphing();

        if (getTicksUntilEggmorphed() == Gigeresque.config.getEggmorphTickTimer() && !this.isDeadOrDying()) {
            var egg = new AlienEggEntity(Entities.EGG, level());
            egg.moveTo(this.blockPosition(), this.getYRot(), this.getXRot());
            level().setBlockAndUpdate(this.blockPosition(), Blocks.AIR.defaultBlockState());
            level().setBlockAndUpdate(this.blockPosition().above(), Blocks.AIR.defaultBlockState());
            level().addFreshEntity(egg);
            hasEggSpawned = true;
            hurt(GigDamageSources.of(level(), GigDamageSources.EGGMORPHING), Float.MAX_VALUE);
        }
    }

    private void handleHostLogic() {
        if (hasParasite()) {
            ticksUntilImpregnation = Math.max(ticksUntilImpregnation - 1.0F, 0f);

            handleStatusEffect(Constants.TPM * 12L, MobEffects.HUNGER, false);
            handleStatusEffect(Constants.TPM * 7L, MobEffects.WEAKNESS, true);
            handleStatusEffect(Constants.TPM * 2L, MobEffects.DIG_SLOWDOWN, true);
        }

        if (ticksUntilImpregnation == 0L) {
            if (tickCount % Constants.TPS == 0L) {
                if (Boolean.TRUE.equals(!isBleeding())) setBleeding(true);
                this.hurt(GigDamageSources.of(this.level(), GigDamageSources.CHESTBURSTING), this.getMaxHealth() / 8f);
            }

            if (this.isDeadOrDying() && !hasParasiteSpawned) {
                LivingEntity burster = null;

                if (!this.hasEffect(GigStatusEffects.SPORE) && !this.hasEffect(GigStatusEffects.DNA)) {
                    if (this.getType().is(GigTags.RUNNER_HOSTS)) {
                        burster = Entities.RUNNERBURSTER.create(this.level());
                        assert burster != null;
                        ((RunnerbursterEntity) burster).setHostId("runner");
                    } else if (this.getType().is(GigTags.AQUATIC_HOSTS))
                        burster = Entities.AQUATIC_CHESTBURSTER.create(this.level());
                    else burster = Entities.CHESTBURSTER.create(this.level());
                } else if (this.getType().is(GigTags.NEOHOST) && this.hasEffect(GigStatusEffects.SPORE))
                    burster = Entities.NEOBURSTER.create(this.level());
                else if (this.getType().is(GigTags.CLASSIC_HOSTS) && this.hasEffect(GigStatusEffects.DNA))
                    burster = Entities.SPITTER.create(this.level());

                if (burster != null) {
                    if (this.hasCustomName()) burster.setCustomName(this.getCustomName());
                    burster.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 10), burster);
                    burster.moveTo(this.blockPosition(), this.getYRot(), this.getXRot());
                    this.level().addFreshEntity(burster);
                    if (level().isClientSide)
                        this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.CHESTBURSTING,
                                SoundSource.NEUTRAL, 2.0f, 1.0f, true);
                    hasParasiteSpawned = true;
                }
            }
        }
    }

    @Inject(method = {"isImmobile"}, at = {@At("RETURN")}, cancellable = true)
    protected void isImmobile(CallbackInfoReturnable<Boolean> callbackInfo) {
        if (this.getPassengers().stream().anyMatch(FacehuggerEntity.class::isInstance) || this.isEggmorphing())
            callbackInfo.setReturnValue(true);
    }

    @Inject(method = {"defineSynchedData"}, at = {@At("RETURN")})
    void defineSynchedData(CallbackInfo callbackInfo) {
        entityData.define(IS_BLEEDING, false);
        entityData.define(EGGMORPH_TICKS, -1.0f);
    }

    @Inject(method = {"addAdditionalSaveData"}, at = {@At("RETURN")})
    void addAdditionalSaveData(CompoundTag nbt, CallbackInfo callbackInfo) {
        nbt.putFloat("ticksUntilImpregnation", ticksUntilImpregnation);
        nbt.putFloat("ticksUntilEggmorphed", getTicksUntilEggmorphed());
        nbt.putBoolean("isBleeding", isBleeding());
    }

    @Inject(method = {"readAdditionalSaveData"}, at = {@At("RETURN")})
    void readAdditionalSaveData(CompoundTag nbt, CallbackInfo callbackInfo) {
        if (nbt.contains("ticksUntilImpregnation")) ticksUntilImpregnation = nbt.getInt("ticksUntilImpregnation");
        if (nbt.contains("ticksUntilEggmorphed")) setTicksUntilEggmorphed(nbt.getInt("ticksUntilEggmorphed"));
        if (nbt.contains("isBleeding")) setBleeding(nbt.getBoolean("isBleeding"));
    }

    @Inject(method = {"removeAllEffects"}, at = {@At("HEAD")}, cancellable = true)
    public void noMilkRemoval(CallbackInfoReturnable<Boolean> callbackInfo) {
        if (this.hasEffect(GigStatusEffects.ACID) || this.hasEffect(GigStatusEffects.DNA))
            callbackInfo.setReturnValue(false);
    }

    @Override
    public float getTicksUntilImpregnation() {
        return ticksUntilImpregnation;
    }

    @Override
    public void setTicksUntilImpregnation(float ticksUntilImpregnation) {
        this.ticksUntilImpregnation = ticksUntilImpregnation;
    }

    @Override
    public boolean isEggmorphing() {
        var cameraBlock = this.level().getBlockState(this.blockPosition()).getBlock();
        var pos = this.getFeetBlockState().getBlock();
        var isCoveredInResin = cameraBlock == GigBlocks.NEST_RESIN_WEB_CROSS || pos == GigBlocks.NEST_RESIN_WEB_CROSS;
        var notAlien = !(((Object) this) instanceof AlienEntity);
        var notHost = GigEntityUtils.isTargetHostable(this);
        if ((((Object) this) instanceof Player playerEntity && (playerEntity.isCreative() || this.isSpectator())) && !(((Object) this) instanceof AlienEntity))
            return false;
        if (GigEntityUtils.isFacehuggerAttached(this)) return false;
        if (!GigEntityUtils.isTargetHostable(this)) return false;
        return notAlien && isCoveredInResin && notHost;
    }

    @Override
    public float getTicksUntilEggmorphed() {
        return entityData.get(EGGMORPH_TICKS);
    }

    @Override
    public void setTicksUntilEggmorphed(float ticksUntilEggmorphed) {
        this.entityData.set(EGGMORPH_TICKS, ticksUntilEggmorphed);
    }

    @Override
    public boolean isBleeding() {
        return entityData.get(IS_BLEEDING);
    }

    @Override
    public void setBleeding(boolean isBleeding) {
        entityData.set(IS_BLEEDING, isBleeding);
    }

    @Inject(method = "onEffectRemoved(Lnet/minecraft/world/effect/MobEffectInstance;)V", at = @At(value = "TAIL"))
    private void runAtEffectRemoval(MobEffectInstance mobEffectInstance, CallbackInfo ci) {
        if (mobEffectInstance.getEffect() instanceof DNAStatusEffect)
            DNAStatusEffect.effectRemoval((LivingEntity) (Object) this, mobEffectInstance);
        if (mobEffectInstance.getEffect() instanceof SporeStatusEffect)
            SporeStatusEffect.effectRemoval((LivingEntity) (Object) this, mobEffectInstance);
    }
}
