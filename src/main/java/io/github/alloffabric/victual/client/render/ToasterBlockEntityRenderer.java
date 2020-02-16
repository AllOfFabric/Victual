package io.github.alloffabric.victual.client.render;

import io.github.alloffabric.victual.block.entity.ToasterBlockEntity;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;

public class ToasterBlockEntityRenderer extends BlockEntityRenderer<ToasterBlockEntity> {
	public ToasterBlockEntityRenderer(BlockEntityRenderDispatcher blockEntityRenderDispatcher) {
		super(blockEntityRenderDispatcher);
	}

	@Override
	public void render(ToasterBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		int x = blockEntity.getPos().getX();
		int y = blockEntity.getPos().getY();
		int z = blockEntity.getPos().getZ();
		ItemStack firstStack = blockEntity.getStack(0);
		ItemStack secondStack = blockEntity.getStack(1);
		Direction direction = blockEntity.getWorld().getBlockState(blockEntity.getPos()).get(HorizontalFacingBlock.FACING);

		if (!firstStack.isEmpty() || !secondStack.isEmpty()) {
			matrices.translate(0.5, (3F / 16F), 0.5);

			if (direction == Direction.NORTH || direction == Direction.SOUTH) {
				matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(direction.asRotation() - 90));
			} else {
				matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(direction.getOpposite().asRotation() - 90));
			}

			matrices.scale(0.625F, 0.625F, 0.625F);


			if (blockEntity.recipeTime > 0) {
				float position = blockEntity.prevTimeLeft + (blockEntity.timeLeft - blockEntity.prevTimeLeft) * tickDelta;
				float boundPosition = position / blockEntity.recipeTime;

				matrices.translate(-(0.25F / 16F), -boundPosition / 4 + 0.25, 0);
			} else {
				matrices.translate(-(0.25F / 16F), 0.25, 0);
			}

			matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(90));

			if (!firstStack.isEmpty()) {
				renderItem(firstStack, matrices, vertexConsumers, light);
			}

			if (!secondStack.isEmpty()) {
				matrices.push();
				matrices.translate(0, 0, 4F / 16F);

				renderItem(secondStack, matrices, vertexConsumers, light);
				matrices.pop();
			}
		}
	}

	public void renderItem(ItemStack stack, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		matrices.push();
		matrices.scale(0.5F, 0.5F, 0.5F);

		matrices.translate(0, 0, -3.5F / 16F);

		MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.FIXED, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers);
		matrices.pop();
	}
}
