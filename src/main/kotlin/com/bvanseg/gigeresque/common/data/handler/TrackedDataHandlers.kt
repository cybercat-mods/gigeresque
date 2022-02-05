package com.bvanseg.gigeresque.common.data.handler

import com.bvanseg.gigeresque.common.entity.AlienAttackType
import com.bvanseg.gigeresque.common.util.GigeresqueInitializer
import com.bvanseg.gigeresque.common.util.initializingBlock
import net.minecraft.entity.data.TrackedDataHandler
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.network.PacketByteBuf

/**
 * @author Boston Vanseghi
 */
object TrackedDataHandlers: GigeresqueInitializer {

    val ALIEN_ATTACK_TYPE: TrackedDataHandler<AlienAttackType> = object : TrackedDataHandler<AlienAttackType> {
        override fun write(packetByteBuf: PacketByteBuf, alienAttackType: AlienAttackType) {
            packetByteBuf.writeEnumConstant(alienAttackType)
        }

        override fun read(packetByteBuf: PacketByteBuf): AlienAttackType {
            return packetByteBuf.readEnumConstant(AlienAttackType::class.java)
        }

        override fun copy(alienAttackType: AlienAttackType): AlienAttackType {
            return alienAttackType
        }
    }

    override fun initialize() = initializingBlock("TrackedDataHandlers") {
        TrackedDataHandlerRegistry.register(ALIEN_ATTACK_TYPE)
    }
}