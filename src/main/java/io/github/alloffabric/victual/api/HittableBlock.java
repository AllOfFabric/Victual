package io.github.alloffabric.victual.api;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public interface HittableBlock {
	ActionResult onHit(PlayerEntity player, World world, Hand hand, ItemStack heldStack, BlockPos pos, BlockState state, Direction direction);
}
