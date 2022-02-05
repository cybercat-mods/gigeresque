package com.bvanseg.gigeresque.common.sound

import com.bvanseg.gigeresque.common.Gigeresque
import com.bvanseg.gigeresque.common.util.GigeresqueInitializer
import com.bvanseg.gigeresque.common.util.initializingBlock
import net.minecraft.sound.SoundEvent
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

/**
 * @author Boston Vanseghi
 */
object Sounds : GigeresqueInitializer {

    val ALIEN_AMBIENT = SoundEvent(Identifier(Gigeresque.MOD_ID, "alien_ambient"))
    val ALIEN_HURT = SoundEvent(Identifier(Gigeresque.MOD_ID, "alien_hurt"))
    val ALIEN_DEATH = SoundEvent(Identifier(Gigeresque.MOD_ID, "alien_death"))

    val EGG_NOTICE = SoundEvent(Identifier(Gigeresque.MOD_ID, "egg_notice"))
    val EGG_OPEN = SoundEvent(Identifier(Gigeresque.MOD_ID, "egg_open"))

    val FACEHUGGER_AMBIENT = SoundEvent(Identifier(Gigeresque.MOD_ID, "facehugger_ambient"))
    val FACEHUGGER_DEATH = SoundEvent(Identifier(Gigeresque.MOD_ID, "facehugger_death"))
    val FACEHUGGER_HURT = SoundEvent(Identifier(Gigeresque.MOD_ID, "facehugger_hurt"))
    val FACEHUGGER_IMPLANT = SoundEvent(Identifier(Gigeresque.MOD_ID, "facehugger_implant"))

    override fun initialize() = initializingBlock("Sounds") {
        register(ALIEN_AMBIENT)
        register(ALIEN_HURT)
        register(ALIEN_DEATH)

        register(EGG_NOTICE)
        register(EGG_OPEN)

        register(FACEHUGGER_AMBIENT)
        register(FACEHUGGER_DEATH)
        register(FACEHUGGER_HURT)
        register(FACEHUGGER_IMPLANT)
    }

    private fun register(soundEvent: SoundEvent) {
        Registry.register(Registry.SOUND_EVENT, soundEvent.id, soundEvent)
    }
}