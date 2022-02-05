package com.bvanseg.gigeresque

import com.bvanseg.gigeresque.common.Gigeresque
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * @author Boston Vanseghi
 */
object Constants {
    const val TPS = 20 // Ticks per second
    const val TPM = TPS * 60 // Ticks per minute
    const val TPD = TPM * 20 // Ticks per day

    const val ALIEN_SPAWN_CAP = 70

    const val EGGMORPH_DURATION = TPM * 5

    val FILE_DATE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd_HH-mm-ss", Locale.US)

    val ISOLATION_MODE_DAMAGE_BASE: Float
        get() = if (Gigeresque.config.features.isolationMode) 10_000.0f else 1.0f
}