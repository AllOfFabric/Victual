package io.github.alloffabric.victual.compat.libcd;

import io.github.alloffabric.victual.recipe.toaster.ToasterRecipe;
import io.github.cottonmc.libcd.tweaker.RecipeParser;
import io.github.cottonmc.libcd.tweaker.RecipeTweaker;
import io.github.cottonmc.libcd.tweaker.Tweaker;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;

public class ToasterTweaker {
	public static final ToasterTweaker INSTANCE = new ToasterTweaker(ToasterRecipe.Type.INSTANCE);
	private ToasterRecipe.Type type;
	private RecipeTweaker tweaker = RecipeTweaker.INSTANCE;

	public ToasterTweaker(ToasterRecipe.Type type) {
		this.type = type;
	}

	public static void init() {
		Tweaker.addAssistant("ToasterTweaker", INSTANCE);
	}

	/**
	 * Register a toaster recipe.
	 * @param input The input item.
	 * @param output The output of the recipe.
	 */
	public void add(Object input, Object output, int cookTime) {
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
