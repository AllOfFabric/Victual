package io.github.alloffabric.victual.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.Direction;

public class OvenBlock extends HorizontalFacingBlock {
	public OvenBlock(Block.Settings settings) {
		super(settings);

		setDefaultState(getDefaultState().with(FACING, Direction.NORTH));
	}

	@Override
	protected void appendProperties(StateFactory.Builder<Block, BlockState> stateFactoryBuilder) {
		stateFactoryBuilder.add(new Property[] { FACING });
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext itemPlacementContext) {
		return this.getDefaultState().with(FACING, itemPlacementContext.getPlayer().isSneaking() ? itemPlacementContext.getPlayerFacing().getOpposite() : itemPlacementContext.getPlayerFacing());
	}
}
