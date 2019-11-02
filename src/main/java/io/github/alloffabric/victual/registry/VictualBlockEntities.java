package io.github.alloffabric.victual.registry;

import io.github.alloffabric.victual.Victual;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;

import java.util.function.Supplier;

public class VictualBlockEntities {
	// Register Here

	private VictualBlockEntities() {
		// NO-OP
	}
	
	public static void init() {
		// NO-OP
	}
	
	private static <B extends BlockEntity> BlockEntityType<B> register(String name, Supplier<B> supplier, Block... supportedBlocks) {
		return Registry.register(Registry.BLOCK_ENTITY, Victual.id(name), BlockEntityType.Builder.create(supplier, supportedBlocks).build(null));
	}
}
