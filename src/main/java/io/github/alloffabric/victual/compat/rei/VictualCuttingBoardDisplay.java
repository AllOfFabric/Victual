package io.github.alloffabric.victual.compat.rei;

import io.github.alloffabric.victual.Victual;
import io.github.alloffabric.victual.recipe.cuttingboard.CuttingBoardRecipe;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeDisplay;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class VictualCuttingBoardDisplay implements RecipeDisplay {
    private CuttingBoardRecipe display;
	private Ingredient input;
    private List<EntryStack> output;

    public VictualCuttingBoardDisplay(CuttingBoardRecipe recipe) {
        this.display = recipe;
        this.input = recipe.getIngredient();
        this.output = Collections.singletonList(EntryStack.create(recipe.getOutput()));
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
	public List<List<EntryStack>> getInputEntries() {
		return Collections.singletonList(Collections.singletonList(EntryStack.create(input.getMatchingStacksClient()[0])));
	}

	@Override
	public List<EntryStack> getOutputEntries() {
		return this.output;
	}

	@Override
    public List<List<EntryStack>> getRequiredEntries() {
		return Collections.singletonList(Collections.singletonList(EntryStack.create(input.getMatchingStacksClient()[0])));
    }
}
