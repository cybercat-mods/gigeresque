package mods.cybercat.gigeresque.mixins.common.entity;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import mods.cybercat.gigeresque.common.entity.AlienEntity;

@Mixin(AbstractHorse.class)
public abstract class HorseMixin extends Animal {

    protected HorseMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = { "tick" }, at = { @At("HEAD") }, cancellable = true)
    void noBuckling(CallbackInfo callbackInfo) {
        if (this.getPassengers().stream().anyMatch(AlienEntity.class::isInstance)) {
            this.removeFreeWill();
            this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 400, 10));
        }
    }
}
