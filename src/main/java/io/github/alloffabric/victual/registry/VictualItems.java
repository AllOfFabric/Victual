package io.github.alloffabric.victual.registry;

import io.github.alloffabric.victual.Victual;
import io.github.alloffabric.victual.item.KnifeItem;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

public class VictualItems {
	public static final KnifeItem KNIFE = register("knife", new KnifeItem(2, -2.4F, new Item.Settings().group(Victual.GROUP)));
	
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
