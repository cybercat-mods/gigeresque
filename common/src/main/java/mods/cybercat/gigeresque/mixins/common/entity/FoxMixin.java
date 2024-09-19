package mods.cybercat.gigeresque.mixins.common.entity;

import mods.cybercat.gigeresque.common.entity.AlienEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Fox.class)
public abstract class FoxMixin extends Animal {

    protected FoxMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = {"tick"}, at = {@At("HEAD")}, cancellable = true)
    void noBuckling(CallbackInfo callbackInfo) {
        if (this.getPassengers().stream().anyMatch(AlienEntity.class::isInstance)) {
            this.removeFreeWill();
            this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 400, 10));
        }
    }
}
