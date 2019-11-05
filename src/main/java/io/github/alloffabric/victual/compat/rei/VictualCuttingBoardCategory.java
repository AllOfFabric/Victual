package io.github.alloffabric.victual.compat.rei;

import io.github.alloffabric.victual.recipe.cuttingboard.CuttingBoardRecipe;
import io.github.alloffabric.victual.registry.VictualBlocks;
import me.shedaniel.math.api.Point;
import me.shedaniel.math.api.Rectangle;
import me.shedaniel.math.compat.RenderHelper;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.gui.widget.EntryWidget;
import me.shedaniel.rei.gui.widget.RecipeBaseWidget;
import me.shedaniel.rei.gui.widget.Widget;
import me.shedaniel.rei.plugin.DefaultPlugin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GuiLighting;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class VictualCuttingBoardCategory implements RecipeCategory<VictualCuttingBoardDisplay> {
	private final CuttingBoardRecipe.Type type;

	VictualCuttingBoardCategory() {
        this.type = CuttingBoardRecipe.Type.INSTANCE;
    }

	@Override
	public Identifier getIdentifier() {
		return CuttingBoardRecipe.Type.ID;
	}

	@Override
	public String getCategoryName() {
		return I18n.translate("rei.category." + getIdentifier().getPath());
	}

	@Override
	public EntryStack getLogo() {
		return EntryStack.create(VictualBlocks.CUTTING_BOARD);
	}

	@Override
	public List<Widget> setupDisplay(Supplier<VictualCuttingBoardDisplay> recipeDisplaySupplier, Rectangle bounds) {
		Point startPoint = new Point(bounds.getCenterX() - 41, bounds.getCenterY() - 13);
		List<Widget> widgets = new LinkedList<>(Arrays.asList(new RecipeBaseWidget(bounds) {
			@Override
			public void render(int mouseX, int mouseY, float delta) {
				super.render(mouseX, mouseY, delta);
				RenderHelper.color4f(1.0F, 1.0F, 1.0F, 1.0F);
				GuiLighting.disable();
				MinecraftClient.getInstance().getTextureManager().bindTexture(DefaultPlugin.getDisplayTexture());
				blit(startPoint.x, startPoint.y, 0, 221, 82, 26);
			}
		}));
		widgets.add(EntryWidget.create(startPoint.x + 4, startPoint.y + 5).entries(recipeDisplaySupplier.get().getInputEntries().get(0)).noBackground());
		widgets.add(EntryWidget.create(startPoint.x + 61, startPoint.y + 5).entries(recipeDisplaySupplier.get().getOutputEntries()).noBackground());
		return widgets;
	}

	@Override
	public int getDisplayHeight() {
		return 36;
	}
}
