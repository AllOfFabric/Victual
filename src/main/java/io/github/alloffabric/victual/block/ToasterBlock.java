package io.github.alloffabric.victual.block;

import io.github.alloffabric.victual.block.entity.ToasterBlockEntity;
import io.github.alloffabric.victual.recipe.toaster.ToasterRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;

public class ToasterBlock extends HorizontalFacingBlock implements BlockEntityProvider {
	public ToasterBlock(Settings settings) {
		super(settings);

		setDefaultState(getDefaultState().with(FACING, Direction.NORTH));
	}

	@Override
	public ActionResult onUse(BlockState blockState, World world, BlockPos blockPos, PlayerEntity playerEntity, Hand hand, BlockHitResult blockHitResult) {
		BlockEntity blockEntity = world.getBlockEntity(blockPos);
		Direction direction = blockState.get(FACING);

		if (blockEntity instanceof ToasterBlockEntity) {
			ToasterBlockEntity toasterBlockEntity = (ToasterBlockEntity) blockEntity;

			if (!playerEntity.isSneaking() && toasterBlockEntity.timeLeft <= 0) {
				ItemStack stack = playerEntity.getStackInHand(hand);
				BasicInventory inventory = new BasicInventory(1);
				inventory.setInvStack(0, stack);

				Optional<ToasterRecipe> recipe = world.getRecipeManager().getFirstMatch(ToasterRecipe.Type.INSTANCE, inventory, world);

				if (!stack.isEmpty() && toasterBlockEntity.isEmpty(0) && recipe.isPresent()) {
					toasterBlockEntity.setStack(new ItemStack(stack.getItem(), 1), 0);
					stack.decrement(1);
					return ActionResult.SUCCESS;
				} else if (stack.isEmpty() && !toasterBlockEntity.isEmpty(0)){
					playerEntity.inventory.offerOrDrop(world, toasterBlockEntity.getStack(0));
					toasterBlockEntity.removeStack(0);
					return ActionResult.SUCCESS;
				} else if (!stack.isEmpty() && toasterBlockEntity.isEmpty(1) && recipe.isPresent()) {
					toasterBlockEntity.setStack(new ItemStack(stack.getItem(), 1), 1);
					stack.decrement(1);
					return ActionResult.SUCCESS;
				} else if (stack.isEmpty() && !toasterBlockEntity.isEmpty(1)){
					playerEntity.inventory.offerOrDrop(world, toasterBlockEntity.getStack(1));
					toasterBlockEntity.removeStack(1);
					return ActionResult.SUCCESS;
				}
			} else if (playerEntity.isSneaking() && toasterBlockEntity.timeLeft <= 0 && !toasterBlockEntity.isEmpty(0) || playerEntity.isSneaking() && toasterBlockEntity.timeLeft <= 0 && !toasterBlockEntity.isEmpty(1)) {
				toasterBlockEntity.activateToaster();
				return ActionResult.SUCCESS;
			}
		}
		return ActionResult.PASS;
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockView blockView) {
		return new ToasterBlockEntity();
	}

	@Override
	public VoxelShape getOutlineShape(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1, EntityContext entityContext_1) {
		return VoxelShapes.cuboid(3F / 16F, 0, 3F / 16F, 13F / 16F, 7F / 16F, 13F / 16F);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateFactoryBuilder) {
		stateFactoryBuilder.add(new Property[] { FACING });
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext itemPlacementContext) {
		return this.getDefaultState().with(FACING, itemPlacementContext.getPlayer().isSneaking() ? itemPlacementContext.getPlayerFacing().getOpposite() : itemPlacementContext.getPlayerFacing());
	}

	@Override
	public void onBlockRemoved(BlockState blockState, World world, BlockPos blockPos, BlockState blockState2, boolean boolean_1) {
		if (blockState.getBlock() != blockState2.getBlock()) {
			BlockEntity blockEntity = world.getBlockEntity(blockPos);
			if (blockEntity instanceof ToasterBlockEntity) {
				ItemScatterer.spawn(world, blockPos, ((ToasterBlockEntity) blockEntity).stacks);
				world.updateHorizontalAdjacent(blockPos, this);
			}

			super.onBlockRemoved(blockState, world, blockPos, blockState2, boolean_1);
		}
	}
}
