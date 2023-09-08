package mods.cybercat.gigeresque.common.data.handler;

import mods.cybercat.gigeresque.common.entity.ai.enums.AlienAttackType;
import mods.cybercat.gigeresque.common.util.GigeresqueInitializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;

public record TrackedDataHandlers() implements GigeresqueInitializer {

	private static TrackedDataHandlers instance;

	synchronized public static TrackedDataHandlers getInstance() {
		if (instance == null)
			instance = new TrackedDataHandlers();
		return instance;
	}

	public static final EntityDataSerializer<AlienAttackType> ALIEN_ATTACK_TYPE = new EntityDataSerializer<>() {
		@Override
		public void write(FriendlyByteBuf packetByteBuf, AlienAttackType alienAttackType) {
			packetByteBuf.writeEnum(alienAttackType);
		}

		@Override
		public AlienAttackType read(FriendlyByteBuf packetByteBuf) {
			return packetByteBuf.readEnum(AlienAttackType.class);
		}

		@Override
		public AlienAttackType copy(AlienAttackType alienAttackType) {
			return alienAttackType;
		}
	};

	@Override
	public void initialize() {
		EntityDataSerializers.registerSerializer(ALIEN_ATTACK_TYPE);
	}
}
