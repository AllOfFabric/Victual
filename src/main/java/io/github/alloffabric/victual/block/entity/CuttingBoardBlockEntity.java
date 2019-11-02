package io.github.alloffabric.victual.block.entity;

import io.github.alloffabric.victual.api.HittableBlock;
import io.github.alloffabric.victual.block.CuttingBoardBlock;
import io.github.alloffabric.victual.item.KnifeItem;
import io.github.alloffabric.victual.registry.VictualBlockEntities;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class CuttingBoardBlockEntity extends BlockEntity implements BlockEntityClientSerializable, HittableBlock {
	public ItemStack stack = ItemStack.EMPTY;

	public CuttingBoardBlockEntity() {
		this(VictualBlockEntities.CUTTING_BOARD);
	}

	public CuttingBoardBlockEntity(BlockEntityType type) {
		super(type);
	}

	@Override
	public ActionResult onHit(PlayerEntity player, World world, Hand hand, ItemStack heldStack, BlockPos pos, BlockState state, Direction direction) {
		if (!heldStack.isEmpty() && heldStack.getItem() instanceof KnifeItem) {
			if (!world.isClient() && !stack.isEmpty() && CuttingBoardBlock.recipes.containsKey(stack.getItem())) {
				player.inventory.offerOrDrop(world, CuttingBoardBlock.recipes.get(stack.getItem()).copy());
				decrStack();
				world.playSound(null, getPos().getX(), getPos().getY(), getPos().getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.BLOCKS, 1.0F, 1.0F);
				return ActionResult.SUCCESS;
			}
		}
		return ActionResult.PASS;
	}

	public boolean isEmpty() {
		return stack.isEmpty();
	}

	public ItemStack getStack() {
		return stack;
	}

	public void setStack(ItemStack stack) {
		this.stack = stack;
		markDirty();
	}

	public void removeStack() {
		this.stack = ItemStack.EMPTY;
		markDirty();
	}

	public void decrStack() {
		decrStack(1);
	}

	public void decrStack(int count) {
		stack.decrement(count);
		markDirty();
	}

	public void sync() {
		if (world instanceof ServerWorld) {
			((ServerWorld)world).method_14178().markForUpdate(pos);
		}
	}

	@Override
	public void markDirty() {
		super.markDirty();
		sync();
	}

	@Override
	public CompoundTag toTag(CompoundTag compoundTag) {
		compoundTag.put("stack", stack.toTag(new CompoundTag()));

		return super.toTag(compoundTag);
	}

	@Override
	public CompoundTag toClientTag(CompoundTag compoundTag) {
		return toTag(compoundTag);
	}

	@Override
	public void fromTag(CompoundTag compoundTag) {
		stack = ItemStack.fromTag(compoundTag.getCompound("stack"));

		super.fromTag(compoundTag);
	}

	@Override
	public void fromClientTag(CompoundTag compoundTag) {
		fromTag(compoundTag);
	}

	@Override
	public CompoundTag toInitialChunkDataTag() {
		return toTag(new CompoundTag());
	}
}
