package mods.cybercat.gigeresque.common.data.handler;

import mods.cybercat.gigeresque.common.entity.AlienAttackType;
import mods.cybercat.gigeresque.common.util.GigeresqueInitializer;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.network.PacketByteBuf;

public class TrackedDataHandlers implements GigeresqueInitializer {
	private TrackedDataHandlers() {
	}

	private static TrackedDataHandlers instance;

	synchronized public static TrackedDataHandlers getInstance() {
		if (instance == null) {
			instance = new TrackedDataHandlers();
		}
		return instance;
	}

	public static final TrackedDataHandler<AlienAttackType> ALIEN_ATTACK_TYPE = new TrackedDataHandler<>() {
		@Override
		public void write(PacketByteBuf packetByteBuf, AlienAttackType alienAttackType) {
			packetByteBuf.writeEnumConstant(alienAttackType);
		}

		@Override
		public AlienAttackType read(PacketByteBuf packetByteBuf) {
			return packetByteBuf.readEnumConstant(AlienAttackType.class);
		}

		@Override
		public AlienAttackType copy(AlienAttackType alienAttackType) {
			return alienAttackType;
		}
	};

	@Override
	public void initialize() {
		TrackedDataHandlerRegistry.register(ALIEN_ATTACK_TYPE);
	}
}
