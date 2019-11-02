package io.github.alloffabric.victual.client.render;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import io.github.alloffabric.victual.block.entity.CuttingBoardBlockEntity;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Direction;

import java.util.Random;

public class CuttingBoardBlockEntityRenderer extends BlockEntityRenderer<CuttingBoardBlockEntity> {
	@Override
	public void render(CuttingBoardBlockEntity blockEntity, double x, double y, double z, float nanoTime, int destroyStage) {
		Random random = new Random();
		ItemStack stack = blockEntity.getStack();
		Direction direction = blockEntity.getWorld().getBlockState(blockEntity.getPos()).get(HorizontalFacingBlock.FACING);

		int seed = stack.isEmpty() ? 187 : Item.getRawId(stack.getItem()) + stack.getDamage();
		random.setSeed(seed);

		if (!stack.isEmpty()) {
			GlStateManager.pushMatrix();

			int light = blockEntity.getWorld().getLightmapIndex(blockEntity.getPos().up(), 0);
			GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, (float) (light & 0xFFFF), (float) ((light >> 16) & 0xFFFF));

			HitResult result = MinecraftClient.getInstance().player.rayTrace(16, 1.0F, false);
			if (result.getType() == HitResult.Type.BLOCK) {
				BlockHitResult blockHitResult = (BlockHitResult) result;

				if (blockHitResult.getBlockPos().equals(blockEntity.getPos())) {
					renderName(blockEntity, stack.getCount() + "x", x, y - 0.5, z, 12);
				}
			}

			GlStateManager.translated(x + 0.5, y + (3F / 16F), z + 0.5);

			if (direction == Direction.NORTH || direction == Direction.SOUTH) {
				GlStateManager.rotatef(direction.asRotation() - 90, 0, 1, 0);
			} else {
				GlStateManager.rotatef(direction.getOpposite().asRotation() - 90, 0, 1, 0);
			}

			int int_1 = 1;
			if (stack.getCount() > 48) {
				int_1 = 5;
			} else if (stack.getCount() > 32) {
				int_1 = 4;
			} else if (stack.getCount() > 16) {
				int_1 = 3;
			} else if (stack.getCount() > 1) {
				int_1 = 2;
			}

			for (int i = 0; i < int_1; i++) {
				GlStateManager.pushMatrix();

				GlStateManager.scaled(0.5, 0.5, 0.5);

				GlStateManager.rotated(-90, 0, 1, 0);
				GlStateManager.rotated(-90, 1, 0, 0);

				if (i > 0) {
					float randomX = ((random.nextFloat() * 2.0F) - 1.0F) * 0.15F;
					float randomY = ((random.nextFloat() * 2.0F) - 1.0F) * 0.15F;
					float randomZ = i * (1F / 16F);
					GlStateManager.translated(randomX * 0.5, randomY * 0.5, randomZ * 0.75);
				}
				GlStateManager.translated(0, 0, -3.5F / 16F);

				MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Type.FIXED);
				GlStateManager.popMatrix();
			}

			GlStateManager.popMatrix();
		}
	}
}
