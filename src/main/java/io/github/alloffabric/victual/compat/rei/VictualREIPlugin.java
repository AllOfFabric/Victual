package io.github.alloffabric.victual.compat.rei;

import io.github.alloffabric.victual.Victual;
import io.github.alloffabric.victual.recipe.cuttingboard.CuttingBoardRecipe;
import io.github.alloffabric.victual.recipe.toaster.ToasterRecipe;
import io.github.alloffabric.victual.registry.VictualBlocks;
import me.shedaniel.rei.api.RecipeHelper;
import me.shedaniel.rei.api.plugins.REIPluginV0;
import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.util.version.VersionParsingException;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class VictualREIPlugin implements REIPluginV0 {
	public static final Identifier PLUGIN = new Identifier(Victual.MOD_ID, "victual_plugin");

	@Override
	public Identifier getPluginIdentifier() {
		return PLUGIN;
	}

	@Override
	public SemanticVersion getMinimumVersion() throws VersionParsingException {
		return SemanticVersion.parse("3.0-pre");
	}

	@Override
	public void registerPluginCategories(RecipeHelper recipeHelper) {
		recipeHelper.registerCategory(new VictualCuttingBoardCategory());
		recipeHelper.registerCategory(new VictualToasterCategory());
	}

	@Override
	public void registerRecipeDisplays(RecipeHelper recipeHelper) {
		recipeHelper.registerRecipes(CuttingBoardRecipe.Type.ID, CuttingBoardRecipe.class, VictualCuttingBoardDisplay::new);
		recipeHelper.registerRecipes(ToasterRecipe.Type.ID, ToasterRecipe.class, VictualToasterDisplay::new);
	}

	@Override
	public void registerOthers(RecipeHelper recipeHelper) {
		recipeHelper.registerWorkingStations(CuttingBoardRecipe.Type.ID, new ItemStack(VictualBlocks.CUTTING_BOARD));
		recipeHelper.registerWorkingStations(ToasterRecipe.Type.ID, new ItemStack(VictualBlocks.TOASTER));
	}
}
