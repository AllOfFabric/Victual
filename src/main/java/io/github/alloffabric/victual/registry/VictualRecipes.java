package io.github.alloffabric.victual.registry;

import io.github.alloffabric.victual.recipe.cuttingboard.CuttingBoardRecipe;
import io.github.alloffabric.victual.recipe.cuttingboard.CuttingBoardRecipeSerializer;
import io.github.alloffabric.victual.recipe.toaster.ToasterRecipe;
import io.github.alloffabric.victual.recipe.toaster.ToasterRecipeSerializer;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.registry.Registry;

public class VictualRecipes {
	public static final RecipeSerializer<ToasterRecipe> TOASTER_RECIPE_SERIALIZER = Registry.register(
		Registry.RECIPE_SERIALIZER,
		ToasterRecipeSerializer.ID,
		ToasterRecipeSerializer.INSTANCE
	);

	public static final RecipeType<ToasterRecipe> TOASTER_RECIPE = Registry.register(
		Registry.RECIPE_TYPE,
		ToasterRecipe.Type.ID,
		ToasterRecipe.Type.INSTANCE
	);

	public static final RecipeSerializer<CuttingBoardRecipe> CUTTING_BOARD_RECIPE_SERIALIZER = Registry.register(
		Registry.RECIPE_SERIALIZER,
		CuttingBoardRecipeSerializer.ID,
		CuttingBoardRecipeSerializer.INSTANCE
	);

	public static final RecipeType<CuttingBoardRecipe> CUTTING_BOARD_RECIPE = Registry.register(
		Registry.RECIPE_TYPE,
		CuttingBoardRecipe.Type.ID,
		CuttingBoardRecipe.Type.INSTANCE
	);

	public static void init() {
		// NO-OP
	}

	private VictualRecipes() {
		// NO-OP
	}
}
