package io.github.alloffabric.victual.registry;

import io.github.alloffabric.victual.Victual;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

import java.util.function.Function;

public class VictualBlocks {
	// Register Here
	
	private VictualBlocks() {
		// NO-OP
	}
	
	public static void init() {
		// NO-OP
	}
	
	static <T extends Block> T register(String name, T block, Item.Settings settings) {
		return register(name, block, new BlockItem(block, settings));
	}
	
	static <T extends Block> T register(String name, T block) {
		return register(name, block, new Item.Settings().group(Victual.GROUP));
	}
	
	static <T extends Block> T register(String name, T block, Function<T, BlockItem> itemFactory) {
		return register(name, block, itemFactory.apply(block));
	}
	
	static <T extends Block> T register(String name, T block, BlockItem item) {
		T b = Registry.register(Registry.BLOCK, Victual.id(name), block);
		if (item != null) {
			BlockItem bi = VictualItems.register(name, item);
			bi.appendBlocks(BlockItem.BLOCK_ITEMS, bi);
		}
		return b;
	}
}
