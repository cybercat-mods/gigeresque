package com.bvanseg.gigeresque.mixins.common.entity;

import com.bvanseg.gigeresque.common.entity.impl.FacehuggerEntity;
import com.bvanseg.gigeresque.interfacing.Eggmorphable;
import com.bvanseg.gigeresque.interfacing.Host;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Boston Vanseghi
 */
@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {

    protected MobEntityMixin(EntityType<? extends LivingEntity> type, World world) {
        super(type, world);
    }

    @Inject(method = {"playAmbientSound"}, at = {@At("HEAD")}, cancellable = true)
    public void playAmbientSound(CallbackInfo callbackInfo) {
        if (((Eggmorphable) this).isEggmorphing()) {
            callbackInfo.cancel();
        }

        if (this.getPassengerList().stream().anyMatch(FacehuggerEntity.class::isInstance)) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = {"cannotDespawn"}, at = {@At("RETURN")})
    public boolean cannotDespawn(CallbackInfoReturnable<Boolean> callbackInfo) {
        if (this instanceof Host host && host.hasParasite()) {
            return true;
        }

        return callbackInfo.getReturnValue();
    }
}
