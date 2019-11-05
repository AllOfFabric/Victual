package io.github.alloffabric.victual.compat.rei;

import io.github.alloffabric.victual.recipe.toaster.ToasterRecipe;
import me.shedaniel.rei.api.RecipeDisplay;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class VictualToasterDisplay implements RecipeDisplay {
    private ToasterRecipe display;
	private Ingredient input;
    private List<ItemStack> output;
    private int cookTime;

    public VictualToasterDisplay(ToasterRecipe recipe) {
        this.display = recipe;
        this.input = recipe.getIngredient();
        this.output = Collections.singletonList(recipe.getOutput());
        this.cookTime = recipe.getCookTime();
    }

    @Override
    public Identifier getRecipeCategory() {
        return ToasterRecipe.Type.ID;
    }

    @Override
    public Optional<Identifier> getRecipeLocation() {
        return Optional.ofNullable(display).map(ToasterRecipe::getId);
    }

    @Override
    public List<List<ItemStack>> getInput() {
        return Collections.singletonList(Collections.singletonList(input.getStackArray()[0]));
    }

    public Ingredient getIngredientInput() {
    	return input;
	}

    @Override
    public List<ItemStack> getOutput() {
        return this.output;
    }

	public int getCookTime() {
		return cookTime;
	}

	@Override
    public List<List<ItemStack>> getRequiredItems() {
		return Collections.singletonList(Collections.singletonList(input.getStackArray()[0]));
    }
}
