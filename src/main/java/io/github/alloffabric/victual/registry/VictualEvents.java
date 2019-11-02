package io.github.alloffabric.victual.registry;

import io.github.alloffabric.victual.api.HittableBlock;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;

public class VictualEvents {
	public static void init() {
		AttackBlockCallback.EVENT.register(((playerEntity, world, hand, blockPos, direction) -> {
			ItemStack stack = playerEntity.getStackInHand(hand);
			BlockState state = world.getBlockState(blockPos);

			if (state.getBlock() instanceof HittableBlock) {
				return ((HittableBlock) state.getBlock()).onHit(playerEntity, world, hand, stack, blockPos, state, direction);
			} else if (world.getBlockEntity(blockPos) instanceof HittableBlock) {
				return ((HittableBlock) world.getBlockEntity(blockPos)).onHit(playerEntity, world, hand, stack, blockPos, state, direction);
			}
			return ActionResult.PASS;
		}));
	}
}
