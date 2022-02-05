package com.bvanseg.gigeresque.common.extensions

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

/**
 * @author Boston Vanseghi
 */
fun World.playServerSound(
    player: PlayerEntity?,
    blockPos: BlockPos,
    event: SoundEvent,
    category: SoundCategory,
    volume: Float = 1.0f,
    pitch: Float = 1.0f
) {
    if (!this.isClient) {
        this.playSound(player, blockPos, event, category, volume, pitch)
    }
}