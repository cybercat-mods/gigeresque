//package com.bvanseg.gigeresque.common.extensions
//
///**
// * @author Boston Vanseghi
// */
//fun Map<String, Set<String>>.combine(other: Map<String, Set<String>>): Map<String, Set<String>> {
//    val newMap = hashMapOf<String, Set<String>>()
//    newMap.putAll(this)
//    other.forEach { (key, value) -> newMap.computeIfAbsent(key) { value } }
//    return newMap.mapValues { (key, value) ->
//        value + (other[key] ?: emptySet())
//    }
//}