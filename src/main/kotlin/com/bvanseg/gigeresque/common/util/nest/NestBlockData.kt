package com.bvanseg.gigeresque.common.util.nest

/**
 * @author Boston Vanseghi
 */
data class NestBlockData(
    val coverage: Int,
    val isCorner: Boolean,
    val isFloor: Boolean,
    val isWall: Boolean,
    val upCoverage: Boolean,
    val downCoverage: Boolean,
    val northCoverage: Boolean,
    val southCoverage: Boolean,
    val eastCoverage: Boolean,
    val westCoverage: Boolean
)
