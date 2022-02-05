package com.bvanseg.gigeresque.common.util

import com.bvanseg.gigeresque.common.Gigeresque
import com.bvanseg.gigeresque.common.extensions.format
import kotlin.system.measureNanoTime

/**
 * @author Boston Vanseghi
 */
fun initializingBlock(initializerName: String, block: () -> Unit) {
    Gigeresque.LOGGER.info("$initializerName initializing.")
    val timeTaken = measureNanoTime(block)
    Gigeresque.LOGGER.info("$initializerName initialized in ${timeTaken.format()}ns (${(timeTaken / 1_000_000).format()}ms).")
}