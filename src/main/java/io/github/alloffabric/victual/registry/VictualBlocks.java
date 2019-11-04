package io.github.alloffabric.victual.registry;

import io.github.alloffabric.victual.Victual;
import io.github.alloffabric.victual.block.CounterBlock;
import io.github.alloffabric.victual.block.CuttingBoardBlock;
import io.github.alloffabric.victual.block.OvenBlock;
import io.github.alloffabric.victual.block.PanBlock;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

import java.util.function.Function;

public class VictualBlocks {
	public static final CounterBlock COUNTER = register("counter", new CounterBlock(FabricBlockSettings.copy(Blocks.STONE).build()));
	public static final CuttingBoardBlock CUTTING_BOARD = register("cutting_board", new CuttingBoardBlock(FabricBlockSettings.copy(Blocks.STONE).build()));
	public static final OvenBlock OVEN = register("oven", new OvenBlock(FabricBlockSettings.copy(Blocks.STONE).build()));
	public static final PanBlock PAN = register("pan", new PanBlock(FabricBlockSettings.copy(Blocks.ANVIL).build()));
	
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
