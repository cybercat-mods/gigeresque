//package com.bvanseg.gigeresque.common.recipe
//
//import com.bvanseg.gigeresque.common.Gigeresque
//import com.google.gson.JsonArray
//import com.google.gson.JsonElement
//import com.google.gson.JsonObject
//import net.minecraft.util.Identifier
//
///**
// * @author Boston Vanseghi
// */
//object Recipes {
//
//    val SURGERY_KIT_RECIPE: JsonElement = createShapelessRecipeJson(
//        ingredients = arrayListOf(
//            "item" to Identifier("minecraft", "shears"),
//            "item" to Identifier("minecraft", "string"),
//            "item" to Identifier("minecraft", "iron_ingot"),
//            "item" to Identifier("minecraft", "golden_apple"),
//            "tag" to Identifier("minecraft", "wool")
//        ),
//        output = "item" to Identifier(Gigeresque.MOD_ID, "surgery_kit")
//    )
//
//    private fun createShapelessRecipeJson(
//        ingredients: ArrayList<Pair<String, Identifier>>,
//        output: Pair<String, Identifier>
//    ): JsonObject {
//        // Specify the type
//        val json = JsonObject()
//        json.addProperty("type", "minecraft:crafting_shapeless")
//
//        // Specify ingredients
//        val jsonArray = JsonArray()
//        ingredients.forEach {
//            val obj = JsonObject()
//            obj.addProperty(it.first, it.second.toString())
//            jsonArray.add(obj)
//        }
//        json.add("ingredients", jsonArray)
//
//        // Specify result
//        val resultObject = JsonObject()
//        resultObject.addProperty(output.first, output.second.toString())
//        json.add("result", resultObject)
//
//        return json
//    }
//
//    // Derived from: https://fabricmc.net/wiki/tutorial:dynamic_recipe_generation
//    private fun createShapedRecipeJson(
//        keys: ArrayList<Char>,
//        items: ArrayList<Identifier>,
//        type: ArrayList<String?>,
//        pattern: ArrayList<String?>,
//        output: Identifier
//    ): JsonObject {
//        val json = JsonObject()
//        json.addProperty("type", "minecraft:crafting_shaped")
//
//        val jsonArray = JsonArray()
//        jsonArray.add(pattern[0])
//        jsonArray.add(pattern[1])
//        jsonArray.add(pattern[2])
//        json.add("pattern", jsonArray)
//
//        var individualKey: JsonObject
//        val keyList = JsonObject()
//        for (i in keys.indices) {
//            individualKey = JsonObject()
//            individualKey.addProperty(
//                type[i],
//                items[i].toString()
//            )
//            keyList.add(keys[i].toString() + "", individualKey)
//        }
//        json.add("key", keyList)
//
//        val result = JsonObject()
//        result.addProperty("item", output.toString())
//        result.addProperty("count", 1)
//        json.add("result", result)
//
//        return json
//    }
//}