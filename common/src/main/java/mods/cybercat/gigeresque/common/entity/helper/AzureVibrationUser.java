package mods.cybercat.gigeresque.common.entity.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEvent.Context;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.classic.FacehuggerEntity;
import mods.cybercat.gigeresque.common.entity.impl.mutant.HammerpedeEntity;
import mods.cybercat.gigeresque.common.entity.impl.mutant.PopperEntity;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;

public class AzureVibrationUser implements VibrationSystem.User {

    private final AlienEntity mob;

    private final float moveSpeed;

    private final PositionSource positionSource;

    public AzureVibrationUser(AlienEntity entity, float speed) {
        this.positionSource = new EntityPositionSource(entity, entity.getEyeHeight());
        this.mob = entity;
        this.moveSpeed = speed;
    }

    @Override
    public int getListenerRadius() {
        var mobY = this.mob.getY();
        final var maxY = 320;
        final var minY = -64;
        var maxRadius = CommonMod.config.xenoMaxSoundRange;
        var factor = Math.clamp((mobY - minY) / (maxY - minY), 0.25, 1.0);

        return (int) (maxRadius * factor);
    }

    @Override
    public @NotNull PositionSource getPositionSource() {
        return this.positionSource;
    }

    @Override
    public @NotNull TagKey<GameEvent> getListenableEvents() {
        return GigTags.ALIEN_CAN_LISTEN;
    }

    @Override
    public boolean canTriggerAvoidVibration() {
        return true;
    }

    @Override
    public boolean isValidVibration(Holder<GameEvent> gameEvent, @NotNull Context context) {
        if (!gameEvent.is(this.getListenableEvents()))
            return false;

        var entity = context.sourceEntity();
        if (entity != null) {
            if (this.mob.isAggressive())
                return false;
            if (entity.isSpectator())
                return false;
            if (entity.dampensVibrations())
                return false;
            if (
                this.mob.level()
                    .getEntitiesOfClass(LivingEntity.class, this.mob.getBoundingBox().inflate(3))
                    .stream()
                    .anyMatch(target -> !target.getType().is(GigTags.GIG_ALIENS))
            )
                return true;
            if (
                this.mob.level()
                    .getEntitiesOfClass(LivingEntity.class, this.mob.getBoundingBox().inflate(this.getListenerRadius()))
                    .stream()
                    .anyMatch(
                        Entity::isSteppingCarefully
                    )
            )
                return false;
        }
        if (context.affectedState() != null)
            return !context.affectedState().is(BlockTags.DAMPENS_VIBRATIONS);
        return true;
    }

    @Override
    public boolean canReceiveVibration(
        @NotNull ServerLevel serverLevel,
        @NotNull BlockPos blockPos,
        @NotNull Holder<GameEvent> gameEvent,
        @NotNull Context context
    ) {
        if (
            mob.isNoAi() || mob.isDeadOrDying() || !mob.level()
                .getWorldBorder()
                .isWithinBounds(
                    blockPos
                ) || mob.isRemoved()
        )
            return false;
        var entity = context.sourceEntity();
        if (entity instanceof LivingEntity livingEntity)
            return GigEntityUtils.TARGET_PREDICATE.test(mob, livingEntity);
        return true;
    }

    @Override
    public void onReceiveVibration(
        @NotNull ServerLevel serverLevel,
        @NotNull BlockPos blockPos,
        @NotNull Holder<GameEvent> gameEvent,
        @Nullable Entity entity,
        @Nullable Entity entity2,
        float f
    ) {
        if (this.mob.isDeadOrDying())
            return;
        if (this.mob.isVehicle())
            return;
        this.doVibrationAction(blockPos, entity2);
    }

    @SuppressWarnings("deprecation")
    private void doVibrationAction(@NotNull BlockPos blockPos, @Nullable Entity entity2) {
        if (this.mob instanceof AlienEntity alienEntity && !alienEntity.crawlingManager.isCrawling()) {
            alienEntity.wakeupCounter++;
            if (alienEntity.stasisManager.isStasis() && alienEntity.wakeupCounter < 3) {
                // alienEntity.animationDispatcher.sendStatisLeave();
                // add "twitch" animation here
            }
            if (alienEntity.wakeupCounter >= 3) {
                alienEntity.animationDispatcher.sendStatisLeave();
                alienEntity.stasisManager.setStasis(false);
                if (entity2 instanceof LivingEntity livingEntity && GigEntityUtils.TARGET_PREDICATE.test(alienEntity, livingEntity)) {
                    this.mob.setTarget(livingEntity);
                }
                alienEntity.getNavigation().moveTo(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1.0F);
                alienEntity.wakeupCounter = 0;
            }
        }

        if (this.mob instanceof AlienEntity alienEntity && (alienEntity.crawlingManager.isCrawling())) {
            alienEntity.stasisManager.setStasis(false);
            alienEntity.getNavigation().moveTo(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1.0F);
        }

        if (
            this.mob instanceof PopperEntity || this.mob instanceof HammerpedeEntity || this.mob instanceof FacehuggerEntity
                && !(entity2 instanceof IronGolem)
        )
            mob.getNavigation().moveTo(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1.0F);
    }

}
