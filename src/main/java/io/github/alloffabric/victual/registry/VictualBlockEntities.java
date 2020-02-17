package io.github.alloffabric.victual.registry;

import io.github.alloffabric.victual.Victual;
import io.github.alloffabric.victual.block.entity.CuttingBoardBlockEntity;
import io.github.alloffabric.victual.block.entity.PanBlockEntity;
import io.github.alloffabric.victual.block.entity.ToasterBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;

import java.util.function.Supplier;

public class VictualBlockEntities {
	public static final BlockEntityType CUTTING_BOARD = register("cutting_board", CuttingBoardBlockEntity::new, VictualBlocks.CUTTING_BOARD);
	public static final BlockEntityType TOASTER = register("toaster", ToasterBlockEntity::new, VictualBlocks.TOASTER);
	public static final BlockEntityType PAN = register("pan", PanBlockEntity::new, VictualBlocks.PAN);

	private VictualBlockEntities() {
		// NO-OP
	}
	
	public static void init() {
		// NO-OP
	}
	
	private static <B extends BlockEntity> BlockEntityType<B> register(String name, Supplier<B> supplier, Block... supportedBlocks) {
		return Registry.register(Registry.BLOCK_ENTITY_TYPE, Victual.id(name), BlockEntityType.Builder.create(supplier, supportedBlocks).build(null));
	}
}
