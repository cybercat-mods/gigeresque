package mods.cybercat.gigeresque.mixins.common.entity;

import mods.cybercat.gigeresque.common.entity.impl.classic.FacehuggerEntity;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Boston Vanseghi
 */
@Mixin(Mob.class)
public abstract class MobEntityMixin extends LivingEntity {

    @Shadow
    private boolean persistenceRequired;

    protected MobEntityMixin(EntityType<? extends LivingEntity> type, Level world) {
        super(type, world);
    }

    @Inject(method = {"playAmbientSound"}, at = {@At("HEAD")}, cancellable = true)
    public void playAmbientSound(CallbackInfo callbackInfo) {
        if (this.hasEffect(GigStatusEffects.EGGMORPHING)) callbackInfo.cancel();
        if (this.getPassengers().stream().anyMatch(FacehuggerEntity.class::isInstance)) callbackInfo.cancel();
    }

    @Inject(method = {"requiresCustomPersistence"}, at = {@At("RETURN")}, cancellable = true)
    public void cannotDespawn(CallbackInfoReturnable<Boolean> callbackInfo) {
        if (this.hasEffect(GigStatusEffects.IMPREGNATION)) callbackInfo.setReturnValue(true);
        if (this.hasEffect(GigStatusEffects.EGGMORPHING)) callbackInfo.setReturnValue(true);
    }

    @Inject(method = {"tick"}, at = {@At("HEAD")})
    void tick(CallbackInfo callbackInfo) {
        if (this.hasEffect(GigStatusEffects.IMPREGNATION) || this.hasEffect(GigStatusEffects.DNA))
            this.persistenceRequired = true;
    }
}
