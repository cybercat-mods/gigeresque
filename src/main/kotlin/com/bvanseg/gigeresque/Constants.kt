package com.bvanseg.gigeresque

import java.time.format.DateTimeFormatter
import java.util.*

/**
 * @author Boston Vanseghi
 */
object Constants {
    const val TPS = 20 // Ticks per second
    const val TPM = TPS * 60 // Ticks per minute
    const val TPD = TPM * 20 // Ticks per day

    val FILE_DATE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd_HH-mm-ss", Locale.US)
}