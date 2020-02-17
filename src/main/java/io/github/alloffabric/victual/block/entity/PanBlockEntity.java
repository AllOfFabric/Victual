package io.github.alloffabric.victual.block.entity;

import io.github.alloffabric.victual.registry.VictualBlockEntities;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.util.registry.Registry;

public class PanBlockEntity extends BlockEntity implements BlockEntityClientSerializable, Tickable {
	public DefaultedList<ItemStack> stacks = DefaultedList.ofSize(6, ItemStack.EMPTY);
	public Fluid fluid = Fluids.EMPTY;

	public PanBlockEntity() {
		this(VictualBlockEntities.PAN);
	}

	public PanBlockEntity(BlockEntityType type) {
		super(type);
	}

	@Override
	public void tick() {

	}

	public boolean isEmpty(int slot) {
		return stacks.get(slot).isEmpty();
	}

	public Fluid getFluid() {
		return fluid;
	}

	public void setFluid(Fluid fluid) {
		this.fluid = fluid;
	}

	public ItemStack getStack(int slot) {
		return stacks.get(slot);
	}

	public ItemStack getTopSetStack() {
		if (getStack(0).isEmpty()) {
			return getStack(0);
		} else if (getStack(1).isEmpty()) {
			return getStack(1);
		} else if (getStack(2).isEmpty()) {
			return getStack(2);
		} else if (getStack(3).isEmpty()) {
			return getStack(3);
		} else if (getStack(4).isEmpty()) {
			return getStack(4);
		} else if (getStack(5).isEmpty()) {
			return getStack(5);
		}
		return getStack(0);
	}

	public ItemStack getTopRemoveStack() {
		if (!getStack(5).isEmpty()) {
			return getStack(5);
		} else if (!getStack(4).isEmpty()) {
			return getStack(4);
		} else if (!getStack(3).isEmpty()) {
			return getStack(3);
		} else if (!getStack(2).isEmpty()) {
			return getStack(2);
		} else if (!getStack(1).isEmpty()) {
			return getStack(1);
		} else if (!getStack(0).isEmpty()) {
			return getStack(0);
		}
		return getStack(0);
	}

	public void setTopStack(ItemStack stack) {
		if (getStack(0).isEmpty()) {
			setStack(stack, 0);
		} else if (getStack(1).isEmpty()) {
			setStack(stack, 1);
		} else if (getStack(2).isEmpty()) {
			setStack(stack, 2);
		} else if (getStack(3).isEmpty()) {
			setStack(stack, 3);
		} else if (getStack(4).isEmpty()) {
			setStack(stack, 4);
		} else if (getStack(5).isEmpty()) {
			setStack(stack, 5);
		}
	}

	public void removeTopStack() {
		if (!getStack(5).isEmpty()) {
			removeStack(5);
		} else if (!getStack(4).isEmpty()) {
			removeStack(4);
		} else if (!getStack(3).isEmpty()) {
			removeStack(3);
		} else if (!getStack(2).isEmpty()) {
			removeStack(2);
		} else if (!getStack(1).isEmpty()) {
			removeStack(1);
		} else if (!getStack(0).isEmpty()) {
			removeStack(0);
		}
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
			((ServerWorld)world).getChunkManager().markForUpdate(pos);
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
		compoundTag.putString("fluid", Registry.FLUID.getId(fluid).toString());

		return super.toTag(compoundTag);
	}

	@Override
	public CompoundTag toClientTag(CompoundTag compoundTag) {
		return toTag(compoundTag);
	}

	@Override
	public void fromTag(CompoundTag compoundTag) {
		this.stacks = DefaultedList.ofSize(6, ItemStack.EMPTY);
		Inventories.fromTag(compoundTag, stacks);
		fluid = Registry.FLUID.get(new Identifier(compoundTag.getString("fluid")));

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
