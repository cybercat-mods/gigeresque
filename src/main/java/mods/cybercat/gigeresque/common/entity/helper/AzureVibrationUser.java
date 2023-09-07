package mods.cybercat.gigeresque.common.entity.helper;

import org.jetbrains.annotations.Nullable;

import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.block.GIgBlocks;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.AdultAlienEntity;
import mods.cybercat.gigeresque.common.entity.impl.classic.ChestbursterEntity;
import mods.cybercat.gigeresque.common.entity.impl.classic.FacehuggerEntity;
import mods.cybercat.gigeresque.common.entity.impl.mutant.HammerpedeEntity;
import mods.cybercat.gigeresque.common.entity.impl.mutant.PopperEntity;
import mods.cybercat.gigeresque.common.tags.GigTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.GameEventTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEvent.Context;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;

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
		return Gigeresque.config.xenoMaxSoundRange;
	}

	@Override
	public PositionSource getPositionSource() {
		return this.positionSource;
	}

	@Override
	public TagKey<GameEvent> getListenableEvents() {
		return GigTags.ALIEN_CAN_LISTEN;
	}

	@Override
	public boolean canTriggerAvoidVibration() {
		return true;
	}

	@Override
	public boolean isValidVibration(GameEvent gameEvent, Context context) {
		if (!gameEvent.is(this.getListenableEvents()))
			return false;

		var entity = context.sourceEntity();
		if (entity != null) {
			if (entity.isSpectator())
				return false;
			if (entity.isSteppingCarefully() && gameEvent.is(GameEventTags.IGNORE_VIBRATIONS_SNEAKING))
				return false;
			if (entity.dampensVibrations())
				return false;
		}
		if (context.affectedState() != null)
			return !context.affectedState().is(BlockTags.DAMPENS_VIBRATIONS) && !context.affectedState().is(GIgBlocks.ACID_BLOCK);
		return true;
	}

	@Override
	public boolean canReceiveVibration(ServerLevel serverLevel, BlockPos blockPos, GameEvent gameEvent, GameEvent.Context context) {
		if (mob.isNoAi() || mob.isDeadOrDying() || !mob.level().getWorldBorder().isWithinBounds(blockPos) || mob.isRemoved())
			return false;
		var entity = context.sourceEntity();
		return !(entity instanceof LivingEntity) || mob.canTargetEntity((LivingEntity) entity);
	}

	@Override
	public void onReceiveVibration(ServerLevel serverLevel, BlockPos blockPos, GameEvent gameEvent, @Nullable Entity entity, @Nullable Entity entity2, float f) {
		if (this.mob.isDeadOrDying())
			return;
		if (this.mob.isVehicle())
			return;
		if (this.mob instanceof AdultAlienEntity adult) {
			adult.wakeupCounter++;
			if (!adult.isPassedOut() & adult.wakeupCounter >= 3) {
				adult.getNavigation().moveTo(blockPos.getX(), blockPos.getY(), blockPos.getZ(), this.moveSpeed);
				adult.wakeupCounter = 0;
			}
			adult.setAggressive(true);
		}
		if (this.mob instanceof ChestbursterEntity || this.mob instanceof PopperEntity || this.mob instanceof HammerpedeEntity || this.mob instanceof FacehuggerEntity)
			if (!(entity2 instanceof IronGolem))
				mob.getNavigation().moveTo(blockPos.getX(), blockPos.getY(), blockPos.getZ(), this.moveSpeed);
	}
}