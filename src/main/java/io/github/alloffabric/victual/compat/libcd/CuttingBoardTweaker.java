package io.github.alloffabric.victual.compat.libcd;

import io.github.alloffabric.victual.recipe.cuttingboard.CuttingBoardRecipe;
import io.github.cottonmc.libcd.tweaker.RecipeParser;
import io.github.cottonmc.libcd.tweaker.RecipeTweaker;
import io.github.cottonmc.libcd.tweaker.Tweaker;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;

public class CuttingBoardTweaker {
	public static final CuttingBoardTweaker INSTANCE = new CuttingBoardTweaker(CuttingBoardRecipe.Type.INSTANCE);
	private CuttingBoardRecipe.Type type;
	private RecipeTweaker tweaker = RecipeTweaker.INSTANCE;

	public CuttingBoardTweaker(CuttingBoardRecipe.Type type) {
		this.type = type;
	}

	public static void init() {
		Tweaker.addAssistant("CuttingBoardTweaker", INSTANCE);
	}

	/**
	 * Register a cutting board recipe.
	 * @param input The input item.
	 * @param output The output of the recipe.
	 */
	public void add(Object input, Object output) {
		try {
			ItemStack stack = RecipeParser.processItemStack(output);
			Identifier recipeId = tweaker.getRecipeId(stack);
			Ingredient ingredient = RecipeParser.processIngredient(input);
			tweaker.addRecipe(new CuttingBoardRecipe(ingredient, stack, recipeId));
		} catch (Exception e) {
			tweaker.getLogger().error("Error parsing recipe - " + e.getMessage());
		}
	}
}
