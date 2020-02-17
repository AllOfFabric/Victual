package io.github.alloffabric.victual.block;

import io.github.alloffabric.victual.block.entity.PanBlockEntity;
import io.github.alloffabric.victual.registry.VictualItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
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

public class PanBlock extends HorizontalFacingBlock implements BlockEntityProvider {
	public PanBlock(Settings settings) {
		super(settings);

		setDefaultState(getDefaultState().with(FACING, Direction.NORTH));
	}

	@Override
	public ActionResult onUse(BlockState blockState, World world, BlockPos blockPos, PlayerEntity playerEntity, Hand hand, BlockHitResult blockHitResult) {
		BlockEntity blockEntity = world.getBlockEntity(blockPos);
		Direction direction = blockState.get(FACING);

		if (blockEntity instanceof PanBlockEntity) {
			PanBlockEntity panBlockEntity = (PanBlockEntity) blockEntity;
			if (!playerEntity.isSneaking()) {
				ItemStack stack = playerEntity.getStackInHand(hand);

				if (!stack.isEmpty() && stack.getItem() == Items.EGG && panBlockEntity.getTopSetStack().isEmpty()) {
					if (!playerEntity.isCreative()) {
						stack.decrement(1);
					}
					panBlockEntity.setTopStack(new ItemStack(VictualItems.EGG_YOLK));
					world.playSound(null, blockPos, SoundEvents.ENTITY_TURTLE_EGG_CRACK, SoundCategory.BLOCKS, 1, 1);
					return ActionResult.SUCCESS;
				} else if (!stack.isEmpty() && panBlockEntity.getTopSetStack().isEmpty()) {
					panBlockEntity.setTopStack(new ItemStack(stack.getItem(), 1));
					stack.decrement(1);
					return ActionResult.SUCCESS;
				} else if (stack.isEmpty() && !panBlockEntity.getTopRemoveStack().isEmpty()){
					playerEntity.inventory.offerOrDrop(world, panBlockEntity.getTopRemoveStack().copy());
					panBlockEntity.removeTopStack();
					return ActionResult.SUCCESS;
				}
			}
		}
		return ActionResult.PASS;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1, EntityContext entityContext_1) {
		return VoxelShapes.cuboid(3F / 16F, 0, 3F / 16F, 13F / 16F, 2F / 16F, 13F / 16F);
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
			if (blockEntity instanceof PanBlockEntity) {
				ItemScatterer.spawn(world, blockPos, ((PanBlockEntity) blockEntity).stacks);
				world.updateHorizontalAdjacent(blockPos, this);
			}

			super.onBlockRemoved(blockState, world, blockPos, blockState2, boolean_1);
		}
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockView blockView) {
		return new PanBlockEntity();
	}
}
