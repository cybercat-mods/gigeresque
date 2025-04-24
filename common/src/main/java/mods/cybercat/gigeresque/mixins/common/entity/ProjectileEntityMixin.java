package mods.cybercat.gigeresque.mixins.common.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import mods.cybercat.gigeresque.common.entity.ai.goals.movement.DodgeProjectilesGoal;
import mods.cybercat.gigeresque.common.tags.GigTags;

@Mixin(Projectile.class)
public abstract class ProjectileEntityMixin extends Entity {

    public ProjectileEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = { "tick" }, at = { @At("HEAD") }, cancellable = true)
    private void gigeresque$tick(CallbackInfo ci) {
        var projectile = (Projectile) (Object) this;
        if (projectile.getType().is(GigTags.DODGEABLE_PROJECTILE) && !projectile.onGround()) {
            DodgeProjectilesGoal.doDodgeCheckForProjectile(projectile);
        }
    }
}
