package io.github.alloffabric.victual.recipe.toaster;

import io.github.alloffabric.victual.Victual;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class ToasterRecipe implements Recipe<BasicInventory> {
	private final Ingredient ingredient;
	private final ItemStack result;
	private final int cookTime;
	private final Identifier recipeId;

	public ToasterRecipe(Ingredient ingredient, ItemStack result, int cookTime, Identifier recipeId) {
		this.ingredient = ingredient;
		this.result = result;
		this.cookTime = cookTime;
		this.recipeId = recipeId;
	}

	public Ingredient getIngredient() {
		return ingredient;
	}

	@Override
	public boolean matches(BasicInventory inventory, World world) {
		return hasRequiredIngredient(inventory.getInvStack(0));
	}

	private boolean hasRequiredIngredient(ItemStack potentialIngredient) {
		return ingredient.test(potentialIngredient);
	}

	@Override
	public ItemStack craft(BasicInventory inventory) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean fits(int var1, int var2) {
		return false;
	}

	@Override
	public ItemStack getOutput() {
		return result;
	}

	public int getCookTime() {
		return cookTime;
	}

	@Override
	public Identifier getId() {
		return recipeId;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ToasterRecipeSerializer.INSTANCE;
	}

	@Override
	public RecipeType<?> getType() {
		return Type.INSTANCE;
	}

	public static class Type implements RecipeType<ToasterRecipe> {
		public static final Type INSTANCE = new Type();
		public static final Identifier ID = Victual.id("toaster_recipe");

		private Type() {
			// NO-OP
		}
	}
}
