package com.bvanseg.gigeresque.mixins.common.recipe;

import java.util.ArrayList;
import java.util.List;

import com.bvanseg.gigeresque.common.Gigeresque;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

public class RecipesJava {
	public static final JsonElement SURGERY_KIT_RECIPE = createShapelessRecipeJson(
			List.of(new Pair<>("item", new Identifier("minecraft", "shears")),
					new Pair<>("item", new Identifier("minecraft", "string")),
					new Pair<>("item", new Identifier("minecraft", "iron_ingot")),
					new Pair<>("item", new Identifier("minecraft", "golden_apple")),
					new Pair<>("tag", new Identifier("minecraft", "wool"))),
			new Pair<>("item", new Identifier(Gigeresque.MOD_ID, "surgery_kit")));

	private static JsonObject createShapelessRecipeJson(List<Pair<String, Identifier>> ingredients,
			Pair<String, Identifier> output) {
		// Specify the type
		var json = new JsonObject();
		json.addProperty("type", "minecraft:crafting_shapeless");

		// Specify ingredients
		var jsonArray = new JsonArray();
		ingredients.forEach(it -> {
			var obj = new JsonObject();
			obj.addProperty(it.getLeft(), it.getRight().toString());
			jsonArray.add(obj);
		});
		json.add("ingredients", jsonArray);

		// Specify result
		var resultObject = new JsonObject();
		resultObject.addProperty(output.getLeft(), output.getRight().toString());
		json.add("result", resultObject);

		return json;
	}

	// Derived from: https://fabricmc.net/wiki/tutorial:dynamic_recipe_generation
	@SuppressWarnings("unused")
	private JsonObject createShapedRecipeJson(ArrayList<Character> keys, ArrayList<Identifier> items,
			ArrayList<String> type, ArrayList<String> pattern, Identifier output) {
		var json = new JsonObject();
		json.addProperty("type", "minecraft:crafting_shaped");

		var jsonArray = new JsonArray();
		jsonArray.add(pattern.get(0));
		jsonArray.add(pattern.get(1));
		jsonArray.add(pattern.get(2));
		json.add("pattern", jsonArray);

		JsonObject individualKey;
		var keyList = new JsonObject();
		for (int i = 0; i < keys.size(); i++) {
			individualKey = new JsonObject();
			individualKey.addProperty(type.get(i), items.get(i).toString());
			keyList.add(keys.get(i).toString() + "", individualKey);
		}
		json.add("key", keyList);

		var result = new JsonObject();
		result.addProperty("item", output.toString());
		result.addProperty("count", 1);
		json.add("result", result);

		return json;
	}
}
