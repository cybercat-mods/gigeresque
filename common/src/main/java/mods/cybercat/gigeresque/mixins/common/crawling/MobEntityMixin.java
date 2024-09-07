package mods.cybercat.gigeresque.mixins.common.crawling;

import mods.azure.bettercrawling.entity.mob.IMobEntityLivingTickHook;
import mods.azure.bettercrawling.entity.mob.IMobEntityRegisterGoalsHook;
import mods.azure.bettercrawling.entity.mob.IMobEntityTickHook;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public abstract class MobEntityMixin implements IMobEntityLivingTickHook, IMobEntityTickHook, IMobEntityRegisterGoalsHook {
    @Inject(method = "aiStep", at = @At("HEAD"))
    private void onLivingTick(CallbackInfo ci) {
        this.onLivingTick();
    }

    @Override
    public void onLivingTick() {
    }

    @Inject(method = "tick()V", at = @At("RETURN"))
    private void onTick(CallbackInfo ci) {
        this.onTick();
    }

    @Override
    public void onTick() {
    }
}
