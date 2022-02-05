package com.bvanseg.gigeresque.common.extensions

import java.util.*

/**
 * @author Boston Vanseghi
 */
fun <T> Optional<T>.getOrNull(): T? = if (this.isPresent) get() else null