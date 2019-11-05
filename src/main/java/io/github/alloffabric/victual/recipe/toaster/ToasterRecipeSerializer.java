package io.github.alloffabric.victual.recipe.toaster;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.github.alloffabric.victual.Victual;
import io.github.alloffabric.victual.exception.InvalidJsonException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.registry.Registry;

import java.util.Optional;

public class ToasterRecipeSerializer implements RecipeSerializer<ToasterRecipe> {
	private ToasterRecipeSerializer() {
		// NO-OP
	}

	public static final ToasterRecipeSerializer INSTANCE = new ToasterRecipeSerializer();
	public static final Identifier ID = Victual.id("toaster_recipe");

	private static final String INPUT_KEY = "input";
	private static final String RESULT_KEY = "result";
	private static final String ITEM_KEY = "item";
	private static final String COUNT_KEY = "count";

	@Override
	public ToasterRecipe read(Identifier id, JsonObject json) {
		Item result;
		int count;
		Ingredient ingredients = getIngredient(json);
		int cookTime = JsonHelper.getInt(json, "cookTime");

		// get item result && count
		if(json.get(RESULT_KEY).isJsonObject()) {
			JsonObject resultObject = json.getAsJsonObject(RESULT_KEY);
			result = getItem(resultObject);
			count = getCount(resultObject);
		} else {
			throw new InvalidJsonException("Expected a JsonObject as \"" + RESULT_KEY + "\", got " + json.get(INPUT_KEY).getClass() + "\n" + prettyPrintJson(json));
		}

		verifyIngredientsList(ingredients, json);

		return new ToasterRecipe(ingredients, new ItemStack(result, count), cookTime, id);
	}

	/**
	 * Verifies the given list of {@link Ingredient}s is not empty.
	 * If the list is empty, an exception is thrown, which prints the offending json recipe.
	 * @param ingredient list of Ingredients to check
	 * @param originalJson original JsonObject to print in the case of the list being empty
	 */
	private void verifyIngredientsList(Ingredient ingredient, JsonObject originalJson) {
		if(ingredient.isEmpty()) {
			throw new InvalidJsonException("Recipe Ingredient list can't be empty! " + "\n" + prettyPrintJson(originalJson));
		}
	}

	/**
	 * Attempts to extract the recipe result count from the given JsonObject.
	 * Assumes we're inside the top level of a result block:
	 *
	 *  "result": {
	 *    "item": "minecraft:cobblestone",
	 *    "count": 2
	 *  }
	 *
	 * If the count is invalid or is not an int, 0 is returned.
	 * @param countJson JsonObject to extract recipe result count from
	 * @return recipe result count
	 */
	private int getCount(JsonObject countJson) {
		int count;
		// get count int
		if(countJson.has(COUNT_KEY)) {
			if (countJson.get(COUNT_KEY).isJsonPrimitive()) {
				JsonPrimitive countPrimitive = countJson.getAsJsonPrimitive(COUNT_KEY);

				if (countPrimitive.isNumber()) {
					count = countPrimitive.getAsNumber().intValue();
				} else {
					throw new IllegalArgumentException("Expected JsonPrimitive to be an int, got " + countJson.getAsString() + "!\n" + prettyPrintJson(countJson));
				}
			} else {
				throw new InvalidJsonException("\"" + ITEM_KEY + "\" needs to be a JsonPrimitive int, found " + countJson.getClass() + "!\n" + prettyPrintJson(countJson));
			}
		} else {
			return 1;
		}

		return count;
	}

	/**
	 * Attempts to extract a {@link Item} from the given JsonObject.
	 * Assumes we're inside the top level of a result block:
	 *
	 *  "result": {
	 *    "item": "minecraft:cobblestone",
	 *    "count": 2
	 *  }
	 *
	 * If the Item does not exist in {@link Registry#ITEM}, an exception is thrown and {@link Items#AIR} is returned.
	 *
	 * @param itemJson JsonObject to extract Item from
	 * @return Item extracted from Json
	 */
	private Item getItem(JsonObject itemJson) {
		Item result;

		if(itemJson.get(ITEM_KEY).isJsonPrimitive()) {
			JsonPrimitive itemPrimitive = itemJson.getAsJsonPrimitive(ITEM_KEY);

			if(itemPrimitive.isString()) {
				Identifier itemIdentifier = new Identifier(itemPrimitive.getAsString());

				Optional<Item> opt = Registry.ITEM.getOrEmpty(itemIdentifier);
				if(opt.isPresent()) {
					result = opt.get();
				} else {
					throw new IllegalArgumentException("Item registry does not contain " + itemIdentifier.toString() + "!" + "\n" + prettyPrintJson(itemJson));
				}
			} else {
				throw new IllegalArgumentException("Expected JsonPrimitive to be a String, got " + itemPrimitive.getAsString() + "\n" + prettyPrintJson(itemJson));
			}
		} else {
			throw new InvalidJsonException("\"" + ITEM_KEY + "\" needs to be a String JsonPrimitive, found " + itemJson.getClass() + "!\n" + prettyPrintJson(itemJson));
		}

		return result;
	}

	/**
	 * Retrieves a list of required {@link Ingredient}s from the given JsonObject.
	 * If the JsonObject doesn't have a proper list, an exception is thrown.
	 *
	 * @param json JsonObject to take Ingredient list from
	 * @return list of Ingredients required for the recipe
	 */
	private Ingredient getIngredient(JsonObject json) {
		Ingredient ingredient = null;
		if(json.get(INPUT_KEY).isJsonObject()) {
			JsonObject input = json.get(INPUT_KEY).getAsJsonObject();
			ingredient = Ingredient.fromJson(input);
		} else {
			throw new InvalidJsonException("Expected a JsonArray as \"input\", got " + json.get(INPUT_KEY).getClass() + "\n" + prettyPrintJson(json));
		}

		return ingredient;
	}

	private static String prettyPrintJson(JsonObject json) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(json);
	}

	@Override
	public void write(PacketByteBuf buf, ToasterRecipe recipe) {
		recipe.getIngredient().write(buf);
		buf.writeItemStack(recipe.getOutput());
		buf.writeInt(recipe.getCookTime());
	}

	@Override
	public ToasterRecipe read(Identifier id, PacketByteBuf buf) {
		return new ToasterRecipe(Ingredient.fromPacket(buf), buf.readItemStack(), buf.readInt(), id);
	}
}
