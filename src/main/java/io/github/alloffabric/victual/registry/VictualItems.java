package io.github.alloffabric.victual.registry;

import io.github.alloffabric.victual.Victual;
import io.github.alloffabric.victual.item.KnifeItem;
import io.github.alloffabric.victual.util.FabricFoodComponent;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

public class VictualItems {
	public static final KnifeItem KNIFE = register("knife", new KnifeItem(2, -2.4F, new Item.Settings().group(Victual.GROUP)));

	public static final Item BREAD_SLICE = register("bread_slice", new Item(new Item.Settings().group(Victual.GROUP).food(FabricFoodComponent.of(1).build())));
	public static final Item TOAST = register("toast", new Item(new Item.Settings().group(Victual.GROUP).food(FabricFoodComponent.of(3).build())));

	public static final Item EGG_YOLK = register("egg_yolk", new Item(new Item.Settings().group(Victual.GROUP).food(FabricFoodComponent.of(1).build())));

	public static final Item HAM_SLICE = register("ham_slice", new Item(new Item.Settings().group(Victual.GROUP).food(FabricFoodComponent.of(1).build())));

	public static final Item SHRIMP = register("shrimp", new Item(new Item.Settings().group(Victual.GROUP).food(FabricFoodComponent.of(2).build())));
	public static final Item COOKED_SHRIMP = register("cooked_shrimp", new Item(new Item.Settings().group(Victual.GROUP).food(FabricFoodComponent.of(5).build())));

	public static final Item CHEESE_SLICE = register("cheese_slice", new Item(new Item.Settings().group(Victual.GROUP).food(FabricFoodComponent.of(1).build())));


	private VictualItems() {
		// NO-OP
	}
	
	static Item.Settings newSettings() {
		return new Item.Settings().group(Victual.GROUP);
	}
	
	public static void init() {

	}
	
	protected static <T extends Item> T register(String name, T item) {
		return Registry.register(Registry.ITEM, Victual.id(name), item);
	}
}
