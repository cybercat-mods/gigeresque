package mods.cybercat.gigeresque.mixins.common;

import mods.cybercat.gigeresque.common.entity.GigEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Raid.class)
public class RaidMixin {
    @Shadow
    private ServerLevel level;

    @Inject(method = "joinRaid", at = @At("HEAD"))
    public void injectCustomMob(int wave, Raider raider, BlockPos pos, boolean isRecruited, CallbackInfo ci) {
        if (wave >= 2 && this.level.getRandom().nextIntBetweenInclusive(0, 10) >= 7) {
            var runnerMob = GigEntities.RUNNER_ALIEN.get().create(this.level);
            runnerMob.setPos(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5);
            runnerMob.finalizeSpawn(this.level, this.level.getCurrentDifficultyAt(pos), MobSpawnType.EVENT, null);
            runnerMob.setOnGround(true);
            this.level.addFreshEntityWithPassengers(runnerMob);
        }
    }
}
