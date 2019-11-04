package io.github.alloffabric.victual.block;

import io.github.alloffabric.victual.api.CounterConnection;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;

public class CounterBlock extends HorizontalFacingBlock {
	public static EnumProperty<CounterConnection> CONNECTION = EnumProperty.of("connection", CounterConnection.class);

	public CounterBlock(Settings settings) {
		super(settings);

		setDefaultState(getDefaultState().with(FACING, Direction.NORTH).with(CONNECTION, CounterConnection.NONE));
	}

	@Override
	public BlockState rotate(BlockState blockState, BlockRotation blockRotation) {
		return blockState.with(FACING, blockRotation.rotate(blockState.get(FACING)));
	}

	@Override
	public BlockState mirror(BlockState blockState, BlockMirror blockMirror) {
		Direction direction = blockState.get(FACING);
		CounterConnection counterConnection = blockState.get(CONNECTION);
		switch(blockMirror) {
			case LEFT_RIGHT:
				if (direction.getAxis() == Direction.Axis.Z) {
					switch(counterConnection) {
						case INSIDE_LEFT:
							return blockState.rotate(BlockRotation.CLOCKWISE_180).with(CONNECTION, CounterConnection.INSIDE_RIGHT);
						case INSIDE_RIGHT:
							return blockState.rotate(BlockRotation.CLOCKWISE_180).with(CONNECTION, CounterConnection.INSIDE_LEFT);
						case OUTSIDE_LEFT:
							return blockState.rotate(BlockRotation.CLOCKWISE_180).with(CONNECTION, CounterConnection.OUTSIDE_RIGHT);
						case OUTSIDE_RIGHT:
							return blockState.rotate(BlockRotation.CLOCKWISE_180).with(CONNECTION, CounterConnection.OUTSIDE_LEFT);
						default:
							return blockState.rotate(BlockRotation.CLOCKWISE_180);
					}
				}
				break;
			case FRONT_BACK:
				if (direction.getAxis() == Direction.Axis.X) {
					switch(counterConnection) {
						case INSIDE_LEFT:
							return blockState.rotate(BlockRotation.CLOCKWISE_180).with(CONNECTION, CounterConnection.INSIDE_LEFT);
						case INSIDE_RIGHT:
							return blockState.rotate(BlockRotation.CLOCKWISE_180).with(CONNECTION, CounterConnection.INSIDE_RIGHT);
						case OUTSIDE_LEFT:
							return blockState.rotate(BlockRotation.CLOCKWISE_180).with(CONNECTION, CounterConnection.OUTSIDE_RIGHT);
						case OUTSIDE_RIGHT:
							return blockState.rotate(BlockRotation.CLOCKWISE_180).with(CONNECTION, CounterConnection.OUTSIDE_LEFT);
						case NONE:
							return blockState.rotate(BlockRotation.CLOCKWISE_180);
					}
				}
		}

		return super.mirror(blockState, blockMirror);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext itemPlacementContext) {
		BlockPos blockPos = itemPlacementContext.getBlockPos();
		BlockState blockState = this.getDefaultState().with(FACING, itemPlacementContext.getPlayerFacing());
		return blockState.with(CONNECTION, calculateShape(blockState, itemPlacementContext.getWorld(), blockPos));
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState blockState, Direction direction, BlockState fromState, IWorld world, BlockPos blockPos, BlockPos fromPos) {
		return direction.getAxis().isHorizontal() ? blockState.with(CONNECTION, calculateShape(blockState, world, blockPos)) : super.getStateForNeighborUpdate(blockState, direction, fromState, world, blockPos, fromPos);
	}

	private static CounterConnection calculateShape(BlockState blockState, BlockView world, BlockPos blockPos) {
		Direction direction = blockState.get(FACING);
		BlockState frontState = world.getBlockState(blockPos.offset(direction));
		if (isCounter(frontState)) {
			Direction frontDir = frontState.get(FACING);
			if (frontDir.getAxis() != blockState.get(FACING).getAxis() && compareCounter(blockState, world, blockPos, frontDir.getOpposite())) {
				if (frontDir==direction.rotateYCounterclockwise()) {
					return CounterConnection.OUTSIDE_LEFT;
				}

				return CounterConnection.OUTSIDE_RIGHT;
			}
		}

		BlockState backState = world.getBlockState(blockPos.offset(direction.getOpposite()));
		if (isCounter(backState)) {
			Direction backDir = backState.get(FACING);
			if (backDir.getAxis() != blockState.get(FACING).getAxis() && compareCounter(blockState, world, blockPos, backDir)) {
				if (backDir == direction.rotateYCounterclockwise()) {
					return CounterConnection.INSIDE_LEFT;
				}

				return CounterConnection.INSIDE_RIGHT;
			}
		}

		return CounterConnection.NONE;
	}

	private static boolean compareCounter(BlockState blockState, BlockView world, BlockPos blockPos, Direction direction) {
		BlockState frontState = world.getBlockState(blockPos.offset(direction));
		return !isCounter(frontState) || frontState.get(FACING) != blockState.get(FACING);
	}

	public static boolean isCounter(BlockState blockState) {
		return blockState.getBlock() instanceof CounterBlock;
	}

	@Override
	public boolean isOpaque(BlockState blockState) {
		return false;
	}

	@Override
	protected void appendProperties(StateFactory.Builder<Block, BlockState> stateFactoryBuilder) {
		stateFactoryBuilder.add(new Property[] { FACING, CONNECTION });
	}
}
