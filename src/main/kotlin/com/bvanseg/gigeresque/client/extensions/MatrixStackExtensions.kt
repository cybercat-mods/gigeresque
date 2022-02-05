package com.bvanseg.gigeresque.client.extensions

import net.minecraft.client.util.math.MatrixStack

/**
 * @author Boston Vanseghi
 */
fun MatrixStack.scale(scaleFactor: Float) = this.scale(scaleFactor, scaleFactor, scaleFactor)