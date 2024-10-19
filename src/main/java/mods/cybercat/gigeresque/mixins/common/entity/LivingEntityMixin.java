package mods.cybercat.gigeresque.mixins.common.entity;

import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.particle.Particles;
import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.entity.impl.classic.FacehuggerEntity;
import mods.cybercat.gigeresque.common.fluid.GigFluids;
import mods.cybercat.gigeresque.common.source.GigDamageSources;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;

/**
 * @author Boston Vanseghi
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

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
    public abstract boolean removeEffect(MobEffect effect);

    @Inject(method = { "hurt" }, at = { @At("HEAD") }, cancellable = true)
    public void hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfo) {
        if (
            this.getVehicle() != null && this.getVehicle()
                .getType()
                .is(
                    GigTags.GIG_ALIENS
                ) && (source == damageSources().drown() || source == damageSources().inWall()) && amount < 1
        )
            callbackInfo.setReturnValue(false);
        if (
            amount >= 2 && this.getFirstPassenger() != null && this.getPassengers()
                .stream()
                .anyMatch(
                    FacehuggerEntity.class::isInstance
                )
        ) {
            this.getFirstPassenger().hurt(source, amount / 2);
            ((FacehuggerEntity) this.getFirstPassenger()).addEffect(
                new MobEffectInstance(
                    MobEffects.MOVEMENT_SLOWDOWN,
                    Gigeresque.config.facehuggerStunTickTimer,
                    100,
                    false,
                    false
                )
            );
            ((FacehuggerEntity) this.getFirstPassenger()).triggerAnim(Constants.LIVING_CONTROLLER, "stun");
            ((FacehuggerEntity) this.getFirstPassenger()).detachFromHost(false);
        }
    }

    @Inject(method = { "doPush" }, at = { @At("HEAD") }, cancellable = true)
    void pushAway(CallbackInfo callbackInfo) {
        if (this.hasEffect(GigStatusEffects.EGGMORPHING) && GigEntityUtils.isTargetHostable(this))
            callbackInfo.cancel();
    }

    @Inject(method = { "tick" }, at = { @At("HEAD") })
    void tick(CallbackInfo callbackInfo) {
        if (this.level().isClientSide && (Constants.shouldApplyImpEffects.test(this))) {
            this.applyParticle();
        }
        if (!this.level().isClientSide) {
            if (
                Constants.hasEggEffect.test(this) && !this.level()
                    .getBlockState(this.blockPosition())
                    .is(
                        GigBlocks.NEST_RESIN_WEB_CROSS
                    )
            ) {
                this.removeEffect(GigStatusEffects.EGGMORPHING);
            }
            if (Constants.isCreativeSpecPlayer.test(this)) {
                if (Constants.hasEggEffect.test(this)) {
                    this.removeEffect(GigStatusEffects.EGGMORPHING);
                }
                if (Constants.hasEggEffect.test(this)) {
                    this.removeEffect(GigStatusEffects.IMPREGNATION);
                }
                if (Constants.hasDNAEffect.test(this)) {
                    this.removeEffect(GigStatusEffects.IMPREGNATION);
                }
            }
            if (Constants.shouldApplyImpEffects.test(this)) {
                this.hurt(GigDamageSources.of(this.level(), GigDamageSources.CHESTBURSTING), 0.2f);
            }
            var getType = this.level().getFluidState(this.blockPosition()).getType();
            if ((getType == GigFluids.BLACK_FLUID_STILL || getType == GigFluids.BLACK_FLUID_FLOWING)) {
                this.handleBlackGooLogic(this);
            }
        }
    }

    @Inject(method = { "isUsingItem" }, at = { @At("RETURN") }, cancellable = true)
    public void isUsingItem(CallbackInfoReturnable<Boolean> callbackInfo) {
        if (
            this.getPassengers().stream().anyMatch(FacehuggerEntity.class::isInstance) || this.hasEffect(
                GigStatusEffects.EGGMORPHING
            )
        )
            callbackInfo.setReturnValue(false);
    }

    @Inject(method = { "isPushable" }, at = { @At("RETURN") }, cancellable = true)
    public void noPush(CallbackInfoReturnable<Boolean> callbackInfo) {
        if (this.hasEffect(GigStatusEffects.EGGMORPHING) && GigEntityUtils.isTargetHostable(this))
            callbackInfo.setReturnValue(false);
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
        if (!(entity instanceof LivingEntity livingEntity))
            return;
        if (this.hasEffect(GigStatusEffects.DNA) || GigEntityUtils.isTargetDNAImmune(livingEntity))
            return;
        if (Constants.notPlayer.test(livingEntity) && !(livingEntity instanceof Creeper))
            this.addEffect(new MobEffectInstance(GigStatusEffects.DNA, Gigeresque.config.getgooEffectTickTimer(), 0));
        if (livingEntity instanceof Creeper && Constants.notPlayer.test(livingEntity))
            this.addEffect(new MobEffectInstance(GigStatusEffects.DNA, 60000, 0));
        if (Constants.isNotCreativeSpecPlayer.test(livingEntity))
            this.addEffect(new MobEffectInstance(GigStatusEffects.DNA, Gigeresque.config.getgooEffectTickTimer(), 0));
    }

    @Inject(method = { "isImmobile" }, at = { @At("RETURN") }, cancellable = true)
    protected void isImmobile(CallbackInfoReturnable<Boolean> callbackInfo) {
        if (
            this.getPassengers().stream().anyMatch(FacehuggerEntity.class::isInstance) || this.hasEffect(
                GigStatusEffects.EGGMORPHING
            )
        )
            callbackInfo.setReturnValue(true);
    }

    @Inject(method = { "removeAllEffects" }, at = { @At("HEAD") }, cancellable = true)
    public void noMilkRemoval(CallbackInfoReturnable<Boolean> callbackInfo) {
        if (this.hasEffect(GigStatusEffects.ACID) || this.hasEffect(GigStatusEffects.DNA))
            callbackInfo.setReturnValue(false);
    }
}
