package mods.cybercat.gigeresque.mixins.common.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.warden.Warden;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import mods.cybercat.gigeresque.common.tags.GigTags;

@Mixin(Warden.class)
public class WardenEntityMixin {

    @Inject(method = { "canTargetEntity" }, at = { @At("HEAD") }, cancellable = true)
    void tick(@Nullable Entity entity, CallbackInfoReturnable<Boolean> ci) {
        if (entity != null && entity.getType().is(GigTags.GIG_ALIENS))
            ci.setReturnValue(false);
    }
}
