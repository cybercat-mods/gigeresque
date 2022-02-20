package com.bvanseg.gigeresque.mixins.common.recipe;

import java.util.Map;

import com.bvanseg.gigeresque.common.recipe.Recipes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.bvanseg.gigeresque.common.Gigeresque;
import com.google.gson.JsonElement;

import net.minecraft.recipe.RecipeManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

/**
 * Derived from: https://fabricmc.net/wiki/tutorial:dynamic_recipe_generation
 *
 * @author Boston Vanseghi
 */
@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
	@Inject(method = "apply", at = @At("HEAD"))
	public void interceptApply(Map<Identifier, JsonElement> map, ResourceManager resourceManager, Profiler profiler, CallbackInfo info) {
		if (Gigeresque.config.getFeatures().getSurgeryKit()) {
			map.put(new Identifier(Gigeresque.MOD_ID, "surgery_kit"), Recipes.SURGERY_KIT_RECIPE);
		}
	}
}
