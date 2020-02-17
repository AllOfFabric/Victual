package io.github.alloffabric.victual.registry;

import io.github.alloffabric.victual.api.HittableBlock;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;

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

		UseItemCallback.EVENT.register(((playerEntity, world, hand) -> {
			if (!playerEntity.getStackInHand(hand).isEmpty()) {
				ItemStack stack = playerEntity.getStackInHand(hand);
				if (playerEntity.isSneaking() && stack.getItem() == Items.EGG) {
					if (!playerEntity.isCreative()) {
						playerEntity.getStackInHand(hand).decrement(1);
					}
					playerEntity.inventory.offerOrDrop(world, new ItemStack(VictualItems.EGG_YOLK, 1));
					if (!world.isClient()) {
						world.playSound(null, playerEntity.getBlockPos(), SoundEvents.ENTITY_TURTLE_EGG_CRACK, SoundCategory.BLOCKS, 1, 1);
					}
					return TypedActionResult.success(playerEntity.getStackInHand(hand));
				}
			}
			return TypedActionResult.pass(playerEntity.getStackInHand(hand));
		}));
	}
}
