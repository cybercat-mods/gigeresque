//package com.bvanseg.gigeresque.client.extensions
//
//import net.fabricmc.api.EnvType
//import net.fabricmc.api.Environment
//
///**
// * @author Boston Vanseghi
// */
//val Int.red: Int
//    @Environment(EnvType.CLIENT)
//    get() = (this shr 16) and 0xFF
//
//val Int.green: Int
//    @Environment(EnvType.CLIENT)
//    get() = (this shr 8) and 0xFF
//
//val Int.blue: Int
//    @Environment(EnvType.CLIENT)
//    get() = this and 0xFF
