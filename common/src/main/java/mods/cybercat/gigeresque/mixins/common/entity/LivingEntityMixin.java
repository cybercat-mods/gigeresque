package mods.cybercat.gigeresque.mixins.common.entity;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.GigEntities;
import mods.cybercat.gigeresque.common.entity.helper.GigCommonMethods;
import mods.cybercat.gigeresque.common.entity.impl.classic.FacehuggerEntity;
import mods.cybercat.gigeresque.common.fluid.GigFluids;
import mods.cybercat.gigeresque.common.source.GigDamageSources;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import mods.cybercat.gigeresque.common.status.effect.impl.*;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;

/**
 * @author Boston Vanseghi/AzureDoom
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    protected LivingEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Shadow
    abstract boolean hasEffect(Holder<MobEffect> effect);

    @Shadow
    abstract boolean addEffect(MobEffectInstance effect);

    @Shadow
    public abstract boolean hurt(@NotNull DamageSource source, float amount);

    @Shadow
    public abstract boolean isDeadOrDying();

    @Shadow
    public abstract float getMaxHealth();

    @Shadow
    public abstract boolean isAlive();

    @Shadow
    public abstract float getHealth();

    @Shadow
    public abstract boolean removeEffect(Holder<MobEffect> effect);

    @Inject(method = { "hurt" }, at = { @At("HEAD") }, cancellable = true)
    public void gigeresque$hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfo) {
        if (
            this.getVehicle() != null && this.getVehicle()
                .getType()
                .is(
                    GigTags.GIG_ALIENS
                ) && (source == damageSources().drown() || source == damageSources().inWall())
        )
            callbackInfo.setReturnValue(false);
        if (
            amount >= 2 && this.getFirstPassenger() != null && this.getPassengers()
                .stream()
                .anyMatch(
                    FacehuggerEntity.class::isInstance
                )
        ) {
            var facehugger = (FacehuggerEntity) this.getFirstPassenger();
            facehugger.hurt(source, amount / 2);
            facehugger.addEffect(
                new MobEffectInstance(
                    MobEffects.CONFUSION,
                    CommonMod.config.entityConfigs.facehuggerConfigs.facehuggerStunTickTimer,
                    60,
                    false,
                    false
                )
            );
            facehugger.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 10, false, false));
            facehugger.animationDispatcher.sendStunned();
            facehugger.detachFromHost();
        }
    }

    @Inject(method = { "tick" }, at = { @At("HEAD") })
    void gigeresque$tick(CallbackInfo callbackInfo) {
        if (!this.level().isClientSide) {
            if (this.getPassengers().stream().anyMatch(AlienEntity.class::isInstance)) {
                this.setAirSupply(this.getMaxAirSupply());
            }
            if (Constants.hasCureEffects.test(this)) {
                this.removeEffect(GigStatusEffects.DNA);
                if (Constants.self(this) instanceof ServerPlayer serverPlayer) {
                    var advancement = serverPlayer.server.getAdvancements().get(Constants.modResource("dna_cure"));
                    if (advancement != null) {
                        serverPlayer.getAdvancements().award(advancement, "criteria_key");
                    }
                }
            }
            if (
                Constants.hasEggEffect.test(this) &&
                    !GigEntityUtils.inResinEnoughToBeEggmorphed(this)

            ) {
                this.removeEffect(GigStatusEffects.EGGMORPHING);
            }
            if (Constants.isCreativeSpecPlayer.test(this)) {
                if (Constants.hasEggEffect.test(this)) {
                    this.removeEffect(GigStatusEffects.EGGMORPHING);
                }
                if (Constants.hasImpEffect.test(this)) {
                    this.removeEffect(GigStatusEffects.IMPREGNATION);
                }
                if (Constants.hasDNAEffect.test(this)) {
                    this.removeEffect(GigStatusEffects.DNA);
                }
                if (Constants.hasSporeEffect.test(this)) {
                    this.removeEffect(GigStatusEffects.SPORE);
                }
            }
            if (Constants.shouldApplyImpEffects.test(this)) {
                GigCommonMethods.placePool(GigEntities.BLOOD.get(), level(), blockPosition().above());
                this.hurt(GigDamageSources.of(this.level(), GigDamageSources.CHESTBURSTING), 0.2f);
            }
            var getType = this.level().getFluidState(this.blockPosition()).getType();
            if ((getType == GigFluids.BLACK_FLUID_STILL.get() || getType == GigFluids.BLACK_FLUID_FLOWING.get())) {
                this.gigeresque$handleBlackGooLogic(this);
            }
        }
    }

    @Unique
    private void gigeresque$handleBlackGooLogic(Entity entity) {
        if (!(entity instanceof LivingEntity livingEntity))
            return;
        if (this.hasEffect(GigStatusEffects.DNA) || GigEntityUtils.isTargetDNAImmune(livingEntity))
            return;
        if (Constants.notPlayer.test(livingEntity) && !Constants.isCreeper.test(this))
            this.addEffect(new MobEffectInstance(GigStatusEffects.DNA, CommonMod.config.getgooEffectTickTimer(), 0));
        if (Constants.isCreeper.test(this) && Constants.notPlayer.test(livingEntity))
            this.addEffect(new MobEffectInstance(GigStatusEffects.DNA, 60000, 0));
        if (Constants.isNotCreativeSpecPlayer.test(livingEntity))
            this.addEffect(new MobEffectInstance(GigStatusEffects.DNA, CommonMod.config.getgooEffectTickTimer(), 0));
    }

    @Inject(method = { "isImmobile" }, at = { @At("RETURN") }, cancellable = true)
    protected void gigeresque$isImmobile(CallbackInfoReturnable<Boolean> callbackInfo) {
        if (this.getPassengers().stream().anyMatch(FacehuggerEntity.class::isInstance))
            callbackInfo.setReturnValue(true);
    }

    @Inject(method = { "removeAllEffects" }, at = { @At("HEAD") }, cancellable = true)
    public void noMilkRemoval(CallbackInfoReturnable<Boolean> callbackInfo) {
        if (
            this.hasEffect(GigStatusEffects.EGGMORPHING) || this.hasEffect(GigStatusEffects.ACID) || this.hasEffect(GigStatusEffects.DNA)
                || this.hasEffect(GigStatusEffects.SPORE) || this.hasEffect(GigStatusEffects.IMPREGNATION)
        )
            callbackInfo.setReturnValue(false);
    }

    @Inject(method = "onEffectRemoved(Lnet/minecraft/world/effect/MobEffectInstance;)V", at = @At(value = "TAIL"))
    private void runAtEffectRemoval(MobEffectInstance mobEffectInstance, CallbackInfo ci) {
        if (!this.level().isClientSide) {
            DNAStatusEffect.effectRemoval(Constants.<LivingEntity>self(this), mobEffectInstance);
            SporeStatusEffect.effectRemoval(Constants.<LivingEntity>self(this), mobEffectInstance);
            ImpregnationStatusEffect.effectRemoval(Constants.<LivingEntity>self(this), mobEffectInstance);
            EggMorphingStatusEffect.effectRemoval(Constants.<LivingEntity>self(this), mobEffectInstance);
            TraumaStatusEffect.effectRemoval(Constants.<LivingEntity>self(this), mobEffectInstance);
        }
    }
}
