package mods.cybercat.gigeresque.common.util;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.GameEventTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipBlockStateContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.gameevent.vibrations.VibrationListener.ReceivingEvent;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class GigVibrationListener implements GameEventListener {
	protected final PositionSource listenerSource;
	protected final int listenerRange;
	protected final GigVibrationListenerConfig config;
	@Nullable
	protected ReceivingEvent receivingEvent;
	protected float receivingDistance;
	protected int travelTimeInTicks;

	public static Codec<GigVibrationListener> codec(GigVibrationListenerConfig vibrationListenerConfig) {
		return RecordCodecBuilder.create(instance -> instance
				.group(((MapCodec) PositionSource.CODEC.fieldOf("source"))
						.forGetter(vibrationListener -> ((GigVibrationListener) vibrationListener).listenerSource),
						((MapCodec) ExtraCodecs.NON_NEGATIVE_INT.fieldOf("range")).forGetter(
								vibrationListener -> ((GigVibrationListener) vibrationListener).listenerRange),
						ReceivingEvent.CODEC.optionalFieldOf("event")
								.forGetter(vibrationListener -> Optional
										.ofNullable(((GigVibrationListener) vibrationListener).receivingEvent)),
						((MapCodec) Codec.floatRange(0.0f, Float.MAX_VALUE).fieldOf("event_distance"))
								.orElse(Float.valueOf(0.0f))
								.forGetter(vibrationListener -> Float
										.valueOf(((GigVibrationListener) vibrationListener).receivingDistance)),
						((MapCodec) ExtraCodecs.NON_NEGATIVE_INT.fieldOf("event_delay")).orElse(0).forGetter(
								vibrationListener -> ((GigVibrationListener) vibrationListener).travelTimeInTicks))
				.apply(instance,
						(positionSource, integer, optional, float_, integer2) -> new GigVibrationListener(
								(PositionSource) positionSource, (int) integer, vibrationListenerConfig,
								((@Nullable ReceivingEvent) optional), ((Float) float_).floatValue(), (int) integer2)));
	}

	public GigVibrationListener(PositionSource positionSource, int i,
			GigVibrationListenerConfig vibrationListenerConfig, @Nullable ReceivingEvent receivingEvent, float f,
			int j) {
		this.listenerSource = positionSource;
		this.listenerRange = i;
		this.config = vibrationListenerConfig;
		this.receivingEvent = receivingEvent;
		this.receivingDistance = f;
		this.travelTimeInTicks = j;
	}

	public void tick(Level level) {
		if (level instanceof ServerLevel) {
			ServerLevel serverLevel = (ServerLevel) level;
			if (this.receivingEvent != null) {
				--this.travelTimeInTicks;
				if (this.travelTimeInTicks <= 0) {
					this.travelTimeInTicks = 0;
					this.config.onSignalReceive(serverLevel, this, new BlockPos(this.receivingEvent.pos()),
							this.receivingEvent.gameEvent(), this.receivingEvent.getEntity(serverLevel).orElse(null),
							this.receivingEvent.getProjectileOwner(serverLevel).orElse(null), this.receivingDistance);
					this.receivingEvent = null;
				}
			}
		}
	}

	@Override
	public PositionSource getListenerSource() {
		return this.listenerSource;
	}

	@Override
	public int getListenerRadius() {
		return this.listenerRange;
	}

	@Override
	public boolean handleGameEvent(ServerLevel level, GameEvent.Message eventMessage) {
		GameEvent.Context context;
		if (this.receivingEvent != null) {
			return false;
		}
		GameEvent gameEvent = eventMessage.gameEvent();
		if (!this.config.isValidVibration(gameEvent, context = eventMessage.context())) {
			return false;
		}
		Optional<Vec3> optional = this.listenerSource.getPosition(level);
		if (optional.isEmpty()) {
			return false;
		}
		Vec3 vec3 = eventMessage.source();
		Vec3 vec32 = optional.get();
		if (!this.config.shouldListen(level, this, new BlockPos(vec3), gameEvent, context)) {
			return false;
		}
		if (GigVibrationListener.isOccluded(level, vec3, vec32)) {
			return false;
		}
		this.scheduleSignal(level, gameEvent, context, vec3, vec32);
		return true;
	}

	private void scheduleSignal(ServerLevel level, GameEvent event, GameEvent.Context context, Vec3 origin,
			Vec3 destination) {
		this.receivingDistance = (float) origin.distanceTo(destination);
		this.receivingEvent = new ReceivingEvent(event, this.receivingDistance, origin, context.sourceEntity());
		this.travelTimeInTicks = Mth.floor(this.receivingDistance);
		this.config.onSignalSchedule();
	}

	private static boolean isOccluded(Level level, Vec3 from, Vec3 to) {
		Vec3 vec3 = new Vec3((double) Mth.floor(from.x) + 0.5, (double) Mth.floor(from.y) + 0.5,
				(double) Mth.floor(from.z) + 0.5);
		Vec3 vec32 = new Vec3((double) Mth.floor(to.x) + 0.5, (double) Mth.floor(to.y) + 0.5,
				(double) Mth.floor(to.z) + 0.5);
		for (Direction direction : Direction.values()) {
			Vec3 vec33 = vec3.relative(direction, 1.0E-5f);
			if (level
					.isBlockInLine(new ClipBlockStateContext(vec33, vec32,
							blockState -> blockState.is(BlockTags.OCCLUDES_VIBRATION_SIGNALS)))
					.getType() == HitResult.Type.BLOCK)
				continue;
			return false;
		}
		return true;
	}

	public static interface GigVibrationListenerConfig {
		default public TagKey<GameEvent> getListenableEvents() {
			return GameEventTags.VIBRATIONS;
		}

		default public boolean canTriggerAvoidVibration() {
			return false;
		}

		default public boolean isValidVibration(GameEvent event, GameEvent.Context context) {
			if (!event.is(this.getListenableEvents())) {
				return false;
			}
			Entity entity = context.sourceEntity();
			if (entity != null) {
				if (entity.isSpectator()) {
					return false;
				}
				if (entity.isSteppingCarefully() && event.is(GameEventTags.IGNORE_VIBRATIONS_SNEAKING)) {
					return false;
				}
				if (entity.dampensVibrations()) {
					return false;
				}
			}
			if (context.affectedState() != null) {
				return !context.affectedState().is(BlockTags.DAMPENS_VIBRATIONS);
			}
			return true;
		}

		public boolean shouldListen(ServerLevel var1, GameEventListener var2, BlockPos var3, GameEvent var4,
				GameEvent.Context var5);

		public void onSignalReceive(ServerLevel var1, GameEventListener var2, BlockPos var3, GameEvent var4,
				@Nullable Entity var5, @Nullable Entity var6, float var7);

		default public void onSignalSchedule() {
		}
	}
}