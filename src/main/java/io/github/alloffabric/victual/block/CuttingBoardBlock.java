package io.github.alloffabric.victual.block;

import io.github.alloffabric.victual.block.entity.CuttingBoardBlockEntity;
import io.github.alloffabric.victual.recipe.cuttingboard.CuttingBoardRecipe;
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
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.Property;
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

public class CuttingBoardBlock extends HorizontalFacingBlock implements BlockEntityProvider {
	public CuttingBoardBlock(Block.Settings settings) {
		super(settings);

		setDefaultState(getDefaultState().with(FACING, Direction.NORTH));
	}

	@Override
	public boolean isOpaque(BlockState blockState_1) {
		return false;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1, EntityContext entityContext_1) {
		return VoxelShapes.cuboid(0, 0, 0, 1, 1F / 16F , 1);
	}

	@Override
	public boolean activate(BlockState blockState, World world, BlockPos blockPos, PlayerEntity playerEntity, Hand hand, BlockHitResult blockHitResult) {
		if (world.getBlockEntity(blockPos) instanceof CuttingBoardBlockEntity) {
			ItemStack stack = playerEntity.getStackInHand(hand);
			CuttingBoardBlockEntity blockEntity = (CuttingBoardBlockEntity) world.getBlockEntity(blockPos);
			BasicInventory inventory = new BasicInventory(1);
			inventory.setInvStack(0, stack);

			Optional<CuttingBoardRecipe> recipe = world.getRecipeManager().getFirstMatch(CuttingBoardRecipe.Type.INSTANCE, inventory, world);

			if (blockEntity.isEmpty() && !stack.isEmpty() && recipe.isPresent()) {
				blockEntity.setStack(stack);
				playerEntity.setStackInHand(hand, ItemStack.EMPTY);
				return true;
			} else if (!blockEntity.isEmpty() && stack.isEmpty()) {
				playerEntity.setStackInHand(hand, blockEntity.getStack());
				blockEntity.removeStack();
				return true;
			}
		}
		return false;
	}

	@Override
	public void onBlockRemoved(BlockState blockState, World world, BlockPos blockPos, BlockState blockState2, boolean boolean_1) {
		if (blockState.getBlock() != blockState2.getBlock()) {
			BlockEntity blockEntity = world.getBlockEntity(blockPos);
			if (blockEntity instanceof CuttingBoardBlockEntity) {
				ItemScatterer.spawn(world, blockPos.getX(), blockPos.getY(), blockPos.getZ(), ((CuttingBoardBlockEntity) blockEntity).getStack());
				world.updateHorizontalAdjacent(blockPos, this);
			}

			super.onBlockRemoved(blockState, world, blockPos, blockState2, boolean_1);
		}
	}

	@Override
	protected void appendProperties(StateFactory.Builder<Block, BlockState> stateFactoryBuilder) {
		stateFactoryBuilder.add(new Property[] { FACING });
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext itemPlacementContext) {
		return this.getDefaultState().with(FACING, itemPlacementContext.getPlayer().isSneaking() ? itemPlacementContext.getPlayerFacing().getOpposite() : itemPlacementContext.getPlayerFacing());
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockView blockView) {
		return new CuttingBoardBlockEntity();
	}
}
