package io.github.alloffabric.victual.compat.rei;

import io.github.alloffabric.victual.Victual;
import io.github.alloffabric.victual.recipe.cuttingboard.CuttingBoardRecipe;
import me.shedaniel.rei.api.RecipeDisplay;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class VictualCuttingBoardDisplay implements RecipeDisplay {
    private CuttingBoardRecipe display;
	private Ingredient input;
    private List<ItemStack> output;

    public VictualCuttingBoardDisplay(CuttingBoardRecipe recipe) {
        this.display = recipe;
        this.input = recipe.getIngredient();
        this.output = Collections.singletonList(recipe.getOutput());
    }

    @Override
    public Identifier getRecipeCategory() {
        return Victual.id("cutting_board_recipe");
    }

    @Override
    public Optional<Identifier> getRecipeLocation() {
        return Optional.ofNullable(display).map(CuttingBoardRecipe::getId);
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

	@Override
    public List<List<ItemStack>> getRequiredItems() {
		return Collections.singletonList(Collections.singletonList(input.getStackArray()[0]));
    }
}
