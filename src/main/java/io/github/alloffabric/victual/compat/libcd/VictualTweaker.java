package io.github.alloffabric.victual.compat.libcd;

import io.github.alloffabric.victual.recipe.cuttingboard.CuttingBoardRecipe;
import io.github.alloffabric.victual.recipe.toaster.ToasterRecipe;
import io.github.cottonmc.libcd.api.tweaker.recipe.RecipeParser;
import io.github.cottonmc.libcd.api.tweaker.recipe.RecipeTweaker;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;

public class VictualTweaker {
	private RecipeTweaker tweaker = RecipeTweaker.INSTANCE;

	/**
	 * Register a cutting board recipe.
	 * @param input The input item.
	 * @param output The output of the recipe.
	 */
	public void addCuttingBoard(Object input, Object output) {
		try {
			ItemStack stack = RecipeParser.processItemStack(output);
			Identifier recipeId = tweaker.getRecipeId(stack);
			Ingredient ingredient = RecipeParser.processIngredient(input);
			tweaker.addRecipe(new CuttingBoardRecipe(ingredient, stack, recipeId));
		} catch (Exception e) {
			tweaker.getLogger().error("Error parsing recipe - " + e.getMessage());
		}
	}

	/**
	 * Register a toaster recipe.
	 * @param input The input item.
	 * @param output The output of the recipe.
	 * @param cookTime The cooking time of the recipe in ticks.
	 */
	public void addToaster(Object input, Object output, int cookTime) {
		try {
			ItemStack stack = RecipeParser.processItemStack(output);
			Identifier recipeId = tweaker.getRecipeId(stack);
			Ingredient ingredient = RecipeParser.processIngredient(input);
			tweaker.addRecipe(new ToasterRecipe(ingredient, stack, cookTime, recipeId));
		} catch (Exception e) {
			tweaker.getLogger().error("Error parsing recipe - " + e.getMessage());
		}
	}
}
