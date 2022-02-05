package com.bvanseg.gigeresque.client.extensions

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.util.math.MatrixStack

/**
 * @author Boston Vanseghi
 */
@Environment(EnvType.CLIENT)
fun MatrixStack.scale(scaleFactor: Float) = this.scale(scaleFactor, scaleFactor, scaleFactor)