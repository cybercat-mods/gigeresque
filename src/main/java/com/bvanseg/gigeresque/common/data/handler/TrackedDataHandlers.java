package com.bvanseg.gigeresque.common.data.handler;

import com.bvanseg.gigeresque.common.entity.AlienAttackType;
import com.bvanseg.gigeresque.common.util.GigeresqueInitializer;
import com.bvanseg.gigeresque.common.util.InitializationTimer;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.network.PacketByteBuf;

public class TrackedDataHandlers implements GigeresqueInitializer {
    private TrackedDataHandlers() {}

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
        InitializationTimer.initializingBlock("TrackedDataHandlers", this::initializeImpl);
    }

    private void initializeImpl() {
        TrackedDataHandlerRegistry.register(ALIEN_ATTACK_TYPE);
    }
}
