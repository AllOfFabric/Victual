package io.github.alloffabric.victual.client.render;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import io.github.alloffabric.victual.block.entity.ToasterBlockEntity;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;

public class ToasterBlockEntityRenderer extends BlockEntityRenderer<ToasterBlockEntity> {
	@Override
	public void render(ToasterBlockEntity blockEntity, double x, double y, double z, float nanoTime, int destroyStage) {
		ItemStack firstStack = blockEntity.getStack(0);
		ItemStack secondStack = blockEntity.getStack(1);
		Direction direction = blockEntity.getWorld().getBlockState(blockEntity.getPos()).get(HorizontalFacingBlock.FACING);

		if (!firstStack.isEmpty() || !secondStack.isEmpty()) {
			GlStateManager.pushMatrix();

			int light = blockEntity.getWorld().getLightmapIndex(blockEntity.getPos().up(), 0);
			GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, (float) (light & 0xFFFF), (float) ((light >> 16) & 0xFFFF));

			GlStateManager.translated(x + 0.5, y + (3F / 16F), z + 0.5);

			if (direction == Direction.NORTH || direction == Direction.SOUTH) {
				GlStateManager.rotatef(direction.asRotation() - 90, 0, 1, 0);
			} else {
				GlStateManager.rotatef(direction.getOpposite().asRotation() - 90, 0, 1, 0);
			}

			GlStateManager.scaled(0.625, 0.625, 0.625);


			if (blockEntity.recipeTime > 0) {
				float position = blockEntity.prevTimeLeft + (blockEntity.timeLeft - blockEntity.prevTimeLeft) * nanoTime;
				float boundPosition = position / blockEntity.recipeTime;

				GlStateManager.translated(-(0.25F / 16F), -boundPosition / 4 + 0.25, 0);
			} else {
				GlStateManager.translated(-(0.25F / 16F), 0.25, 0);
			}

			GlStateManager.rotated(90, 0, 1, 0);

			if (!firstStack.isEmpty()) {
				renderItem(firstStack);
			}

			if (!secondStack.isEmpty()) {
				GlStateManager.translated(0, 0, 4F / 16F);

				renderItem(secondStack);
			}

			GlStateManager.popMatrix();
		}
	}

	public void renderItem(ItemStack stack) {
		GlStateManager.pushMatrix();

		GlStateManager.scaled(0.5, 0.5, 0.5);

		GlStateManager.translated(0, 0, -3.5F / 16F);

		MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Type.FIXED);

		GlStateManager.popMatrix();
	}
}
