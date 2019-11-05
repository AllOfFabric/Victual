package io.github.alloffabric.victual.block.entity;

import io.github.alloffabric.victual.recipe.cuttingboard.CuttingBoardRecipe;
import io.github.alloffabric.victual.recipe.toaster.ToasterRecipe;
import io.github.alloffabric.victual.registry.VictualBlockEntities;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.Tickable;

import java.util.Optional;

public class ToasterBlockEntity extends BlockEntity implements BlockEntityClientSerializable, Tickable {
	public DefaultedList<ItemStack> stacks = DefaultedList.ofSize(2, ItemStack.EMPTY);
	public int timeLeft = 0;
	public int prevTimeLeft = 0;
	public int recipeTime = 0;

	public ToasterBlockEntity() {
		this(VictualBlockEntities.TOASTER);
	}

	public ToasterBlockEntity(BlockEntityType type) {
		super(type);
	}

	@Override
	public void tick() {
		if (timeLeft > 0) {
			prevTimeLeft = timeLeft;
			timeLeft -= 1;
		} else if (recipeTime > 0) {
			recipeTime = 0;
			timeLeft = 0;

			DefaultedList<ItemStack> results = DefaultedList.ofSize(2, ItemStack.EMPTY);
			BasicInventory stackOne = new BasicInventory(1);
			BasicInventory stackTwo = new BasicInventory(1);
			stackOne.setInvStack(0, stacks.get(0));
			stackTwo.setInvStack(0, stacks.get(1));

			Optional<CuttingBoardRecipe> recipeOne = world.getRecipeManager().getFirstMatch(CuttingBoardRecipe.Type.INSTANCE, stackOne, world);
			Optional<CuttingBoardRecipe> recipeTwo = world.getRecipeManager().getFirstMatch(CuttingBoardRecipe.Type.INSTANCE, stackTwo, world);

			if (!isEmpty(0) && recipeOne.isPresent()) {
				results.set(0, recipeOne.orElse(null).craft(stackOne));
			}

			if (!isEmpty(1) && recipeTwo.isPresent()) {
				results.set(1, recipeTwo.orElse(null).craft(stackTwo));
			}

			stacks.clear();

			if (!world.isClient()) {
				ItemScatterer.spawn(world, getPos().getX(), getPos().getY() + 0.25, getPos().getZ(), results.get(0));
				ItemScatterer.spawn(world, getPos().getX(), getPos().getY() + 0.25, getPos().getZ(), results.get(1));
			}
		}
	}

	public void activateToaster() {
		BasicInventory stackOne = new BasicInventory(1);
		BasicInventory stackTwo = new BasicInventory(1);
		stackOne.setInvStack(0, stacks.get(0));
		stackTwo.setInvStack(0, stacks.get(1));

		if (!isEmpty(0) && world.getRecipeManager().getFirstMatch(ToasterRecipe.Type.INSTANCE, stackOne, world).isPresent() || !isEmpty(1) && world.getRecipeManager().getFirstMatch(ToasterRecipe.Type.INSTANCE, stackTwo, world).isPresent()) {
			int stackOneTime = !isEmpty(0) ? world.getRecipeManager().getFirstMatch(ToasterRecipe.Type.INSTANCE, stackOne, world).orElse(null).getCookTime() : 0;
			int stackTwoTime = !isEmpty(1) ? world.getRecipeManager().getFirstMatch(ToasterRecipe.Type.INSTANCE, stackTwo, world).orElse(null).getCookTime() : 0;
			int totalTime = stackOneTime + stackTwoTime;

			if (totalTime > 0) {
				recipeTime = totalTime;
				timeLeft = totalTime;
				prevTimeLeft = totalTime;
			}
		}
	}

	public boolean isEmpty(int slot) {
		return stacks.get(slot).isEmpty();
	}

	public ItemStack getStack(int slot) {
		return stacks.get(slot);
	}

	public void setStack(ItemStack stack, int slot) {
		stacks.set(slot, stack);
		markDirty();
	}

	public void removeStack(int slot) {
		stacks.set(slot, ItemStack.EMPTY);
		markDirty();
	}

	public void decrStack(int slot) {
		decrStack(1, slot);
	}

	public void decrStack(int count, int slot) {
		stacks.get(slot).decrement(count);
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
		Inventories.toTag(compoundTag, this.stacks);

		compoundTag.putInt("timeLeft", timeLeft);
		compoundTag.putInt("recipeTime", recipeTime);

		return super.toTag(compoundTag);
	}

	@Override
	public CompoundTag toClientTag(CompoundTag compoundTag) {
		return toTag(compoundTag);
	}

	@Override
	public void fromTag(CompoundTag compoundTag) {
		this.stacks = DefaultedList.ofSize(2, ItemStack.EMPTY);
		Inventories.fromTag(compoundTag, stacks);

		timeLeft = compoundTag.getInt("timeLeft");
		recipeTime = compoundTag.getInt("recipeTime");

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
