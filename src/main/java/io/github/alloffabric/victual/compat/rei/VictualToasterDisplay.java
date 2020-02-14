package io.github.alloffabric.victual.compat.rei;

import io.github.alloffabric.victual.recipe.toaster.ToasterRecipe;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeDisplay;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class VictualToasterDisplay implements RecipeDisplay {
    private ToasterRecipe display;
	private Ingredient input;
    private List<EntryStack> output;
    private int cookTime;

    public VictualToasterDisplay(ToasterRecipe recipe) {
        this.display = recipe;
        this.input = recipe.getIngredient();
		this.output = Collections.singletonList(EntryStack.create(recipe.getOutput()));
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
    public List<List<EntryStack>> getInputEntries() {
        return Collections.singletonList(Collections.singletonList(EntryStack.create(input.getMatchingStacksClient()[0])));
    }

    @Override
    public List<EntryStack> getOutputEntries() {
        return this.output;
    }

	public int getCookTime() {
		return cookTime;
	}

	@Override
    public List<List<EntryStack>> getRequiredEntries() {
		return Collections.singletonList(Collections.singletonList(EntryStack.create(input.getMatchingStacksClient()[0])));
    }
}
