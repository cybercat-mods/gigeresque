package mods.cybercat.gigeresque.common.util;

import java.util.Optional;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public record GigReceivingEvent(GameEvent gameEvent, float distance, Vec3 pos, @Nullable UUID uuid,
		@Nullable UUID projectileOwnerUuid, @Nullable Entity entity) {

	public static final Codec<GigReceivingEvent> CODEC = RecordCodecBuilder.create(instance -> instance
			.group((Registry.GAME_EVENT.byNameCodec().fieldOf("game_event")).forGetter(GigReceivingEvent::gameEvent),
					(Codec.floatRange(0.0f, Float.MAX_VALUE).fieldOf("distance"))
							.forGetter(GigReceivingEvent::distance),
					(Vec3.CODEC.fieldOf("pos")).forGetter(GigReceivingEvent::pos),
					ExtraCodecs.UUID.optionalFieldOf("source")
							.forGetter(receivingEvent -> Optional.ofNullable(receivingEvent.uuid())),
					ExtraCodecs.UUID.optionalFieldOf("projectile_owner")
							.forGetter(receivingEvent -> Optional.ofNullable(receivingEvent.projectileOwnerUuid())))
			.apply(instance,
					(gameEvent, float_, vec3, optional, optional2) -> new GigReceivingEvent((GameEvent) gameEvent,
							float_.floatValue(), (Vec3) vec3, optional.orElse(null), optional2.orElse(null))));

	public GigReceivingEvent(GameEvent gameEvent, float f, Vec3 vec3, @Nullable UUID uUID, @Nullable UUID uUID2) {
		this(gameEvent, f, vec3, uUID, uUID2, null);
	}

	public GigReceivingEvent(GameEvent gameEvent, float f, Vec3 vec3, @Nullable Entity entity) {
		this(gameEvent, f, vec3, entity == null ? null : entity.getUUID(), GigReceivingEvent.getProjectileOwner(entity),
				entity);
	}

	@Nullable
	private static UUID getProjectileOwner(@Nullable Entity projectile) {
		Projectile projectile2;
		if (projectile instanceof Projectile && (projectile2 = (Projectile) projectile).getOwner() != null) {
			return projectile2.getOwner().getUUID();
		}
		return null;
	}

	public Optional<Entity> getEntity(ServerLevel level) {
		return Optional.ofNullable(this.entity).or(() -> Optional.ofNullable(this.uuid).map(level::getEntity));
	}

	public Optional<Entity> getProjectileOwner(ServerLevel level) {
		return this.getEntity(level).filter(entity -> entity instanceof Projectile).map(entity -> (Projectile) entity)
				.map(Projectile::getOwner)
				.or(() -> Optional.ofNullable(this.projectileOwnerUuid).map(level::getEntity));
	}
}