package com.bvanseg.gigeresque.common.data.handler;

import com.bvanseg.gigeresque.common.entity.AlienAttackTypeJava;
import com.bvanseg.gigeresque.common.util.GigeresqueInitializerJava;
import com.bvanseg.gigeresque.common.util.InitializationTimer;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.network.PacketByteBuf;

public class TrackedDataHandlersJava implements GigeresqueInitializerJava {
    public static final TrackedDataHandler<AlienAttackTypeJava> ALIEN_ATTACK_TYPE = new TrackedDataHandler<>() {
        @Override
        public void write(PacketByteBuf packetByteBuf, AlienAttackTypeJava alienAttackType) {
            packetByteBuf.writeEnumConstant(alienAttackType);
        }

        @Override
        public AlienAttackTypeJava read(PacketByteBuf packetByteBuf) {
            return packetByteBuf.readEnumConstant(AlienAttackTypeJava.class);
        }

        @Override
        public AlienAttackTypeJava copy(AlienAttackTypeJava alienAttackType) {
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
