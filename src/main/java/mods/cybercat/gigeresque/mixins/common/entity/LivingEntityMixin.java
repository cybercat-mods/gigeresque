package mods.cybercat.gigeresque.mixins.common.entity;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.particle.Particles;
import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.entity.Entities;
import mods.cybercat.gigeresque.common.entity.impl.classic.AlienEggEntity;
import mods.cybercat.gigeresque.common.entity.impl.classic.FacehuggerEntity;
import mods.cybercat.gigeresque.common.fluid.GigFluids;
import mods.cybercat.gigeresque.common.source.GigDamageSources;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import mods.cybercat.gigeresque.common.status.effect.impl.DNAStatusEffect;
import mods.cybercat.gigeresque.common.status.effect.impl.ImpregnationStatusEffect;
import mods.cybercat.gigeresque.common.status.effect.impl.SporeStatusEffect;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;
import mods.cybercat.gigeresque.interfacing.Eggmorphable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

/**
 * @author Boston Vanseghi
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Eggmorphable {

    private static final EntityDataAccessor<Float> EGGMORPH_TICKS = SynchedEntityData.defineId(LivingEntity.class,
            EntityDataSerializers.FLOAT);
    public float ticksUntilEggmorpth = -1.0f;
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
    public abstract RandomSource getRandom();

    @Shadow
    public abstract boolean isAlive();

    @Shadow
    public abstract float getHealth();

    @Shadow
    public abstract void kill();

    @Shadow
    @Nullable
    public abstract MobEffectInstance getEffect(MobEffect effect);

    @Shadow
    public abstract Collection<MobEffectInstance> getActiveEffects();

    @Inject(method = {"hurt"}, at = {@At("HEAD")}, cancellable = true)
    public void hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfo) {
        if (this.getVehicle() != null && this.getVehicle().getType().is(
                GigTags.GIG_ALIENS) && (source == damageSources().drown() || source == damageSources().inWall()) && amount < 1)
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
        if (this.level().isClientSide && (Constants.shouldApplyImpEffects.test(this))) {
            this.applyParticle();
        }
        if (!this.level().isClientSide) {
            if (Constants.shouldApplyImpEffects.test(this)) {
                this.hurt(GigDamageSources.of(this.level(), GigDamageSources.CHESTBURSTING), 0.2f);
            }
            var getType = this.level().getFluidState(this.blockPosition()).getType();
            if ((getType == GigFluids.BLACK_FLUID_STILL || getType == GigFluids.BLACK_FLUID_FLOWING)) {
                this.handleBlackGooLogic(this);
            }
            this.handleReset(this);
            if (GigEntityUtils.isTargetHostable(this)) {
                handleEggingLogic();
            }
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

    private void handleReset(Entity entity) {
        if (!(entity instanceof LivingEntity livingEntity)) return;
        if (level().getDifficulty() != Difficulty.PEACEFUL || this.getType().is(
                GigTags.FACEHUGGER_BLACKLIST) || Constants.isCreativeSpecPlayer.test(livingEntity)) {
            resetEggmorphing();
        }
    }

    private void applyParticle() {
        if (this.isAlive()) {
            var yOffset = this.getEyeY() - ((this.getEyeY() - this.blockPosition().getY()) / 2.0);
            var customX = this.getX() + ((this.getRandom().nextDouble() / 2.0) - 0.5) * (this.getRandom().nextBoolean() ? -1 : 1);
            var customZ = this.getZ() + ((this.getRandom().nextDouble() / 2.0) - 0.5) * (this.getRandom().nextBoolean() ? -1 : 1);
            this.level().addAlwaysVisibleParticle(Particles.ACID, customX, yOffset, customZ, 0.0, -0.15, 0.0);
        }
    }

    private void handleBlackGooLogic(Entity entity) {
        if (!(entity instanceof LivingEntity livingEntity)) return;
        if (this.hasEffect(GigStatusEffects.DNA) || GigEntityUtils.isTargetDNAImmune(livingEntity)) return;
        if (Constants.notPlayer.test(livingEntity) && !Constants.isCreeper.test(this))
            this.addEffect(new MobEffectInstance(GigStatusEffects.DNA, Gigeresque.config.getgooEffectTickTimer(), 0));
        if (Constants.isCreeper.test(this) && Constants.notPlayer.test(livingEntity))
            this.addEffect(new MobEffectInstance(GigStatusEffects.DNA, 60000, 0));
        if (Constants.isNotCreativeSpecPlayer.test(livingEntity))
            this.addEffect(new MobEffectInstance(GigStatusEffects.DNA, Gigeresque.config.getgooEffectTickTimer(), 0));
    }

    private void handleEggingLogic() {
        if (isEggmorphing() && GigEntityUtils.isTargetHostable(this) && !this.hasEffect(GigStatusEffects.IMPREGNATION))
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

    @Inject(method = {"isImmobile"}, at = {@At("RETURN")}, cancellable = true)
    protected void isImmobile(CallbackInfoReturnable<Boolean> callbackInfo) {
        if (this.getPassengers().stream().anyMatch(FacehuggerEntity.class::isInstance) || this.isEggmorphing())
            callbackInfo.setReturnValue(true);
    }

    @Inject(method = {"defineSynchedData"}, at = {@At("RETURN")})
    void defineSynchedData(CallbackInfo callbackInfo) {
        entityData.define(EGGMORPH_TICKS, -1.0f);
    }

    @Inject(method = {"addAdditionalSaveData"}, at = {@At("RETURN")})
    void addAdditionalSaveData(CompoundTag nbt, CallbackInfo callbackInfo) {
        nbt.putFloat("ticksUntilEggmorphed", getTicksUntilEggmorphed());
    }

    @Inject(method = {"readAdditionalSaveData"}, at = {@At("RETURN")})
    void readAdditionalSaveData(CompoundTag nbt, CallbackInfo callbackInfo) {
        if (nbt.contains("ticksUntilEggmorphed")) setTicksUntilEggmorphed(nbt.getInt("ticksUntilEggmorphed"));
    }

    @Inject(method = {"removeAllEffects"}, at = {@At("HEAD")}, cancellable = true)
    public void noMilkRemoval(CallbackInfoReturnable<Boolean> callbackInfo) {
        if (this.hasEffect(GigStatusEffects.ACID) || this.hasEffect(GigStatusEffects.DNA))
            callbackInfo.setReturnValue(false);
    }

    @Override
    public boolean isEggmorphing() {
        var cameraBlock = this.level().getBlockState(this.blockPosition()).getBlock();
        var pos = this.getFeetBlockState().getBlock();
        var isCoveredInResin = cameraBlock == GigBlocks.NEST_RESIN_WEB_CROSS || pos == GigBlocks.NEST_RESIN_WEB_CROSS;
        var notAlien = !this.getType().is(GigTags.GIG_ALIENS);
        var notHost = GigEntityUtils.isTargetHostable(this);
        if (Constants.isCreativeSpecPlayer.test(this)) return false;
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

    @Inject(method = "onEffectRemoved(Lnet/minecraft/world/effect/MobEffectInstance;)V", at = @At(value = "TAIL"))
    private void runAtEffectRemoval(MobEffectInstance mobEffectInstance, CallbackInfo ci) {
        if (mobEffectInstance.getEffect() instanceof DNAStatusEffect)
            DNAStatusEffect.effectRemoval((LivingEntity) (Object) this);
        if (mobEffectInstance.getEffect() instanceof SporeStatusEffect)
            SporeStatusEffect.effectRemoval((LivingEntity) (Object) this, mobEffectInstance);
        if (mobEffectInstance.getEffect() instanceof ImpregnationStatusEffect)
            ImpregnationStatusEffect.effectRemoval((LivingEntity) (Object) this);
    }
}
