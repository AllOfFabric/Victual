package io.github.alloffabric.victual.client.render;

import io.github.alloffabric.victual.block.entity.CuttingBoardBlockEntity;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Direction;

import java.util.Random;

public class CuttingBoardBlockEntityRenderer extends BlockEntityRenderer<CuttingBoardBlockEntity> {
	public CuttingBoardBlockEntityRenderer(BlockEntityRenderDispatcher blockEntityRenderDispatcher) {
		super(blockEntityRenderDispatcher);
	}

	@Override
	public void render(CuttingBoardBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		int x = blockEntity.getPos().getX();
		int y = blockEntity.getPos().getY();
		int z = blockEntity.getPos().getZ();
		Random random = new Random();
		ItemStack stack = blockEntity.getStack();
		Direction direction = blockEntity.getWorld().getBlockState(blockEntity.getPos()).get(HorizontalFacingBlock.FACING);

		int seed = stack.isEmpty() ? 187 : Item.getRawId(stack.getItem()) + stack.getDamage();
		random.setSeed(seed);

		if (!stack.isEmpty()) {
			HitResult result = MinecraftClient.getInstance().player.rayTrace(16, 1.0F, false);
			if (result.getType() == HitResult.Type.BLOCK) {
				BlockHitResult blockHitResult = (BlockHitResult) result;

				if (blockHitResult.getBlockPos().equals(blockEntity.getPos())) {
					renderLabel(blockEntity, stack.getCount() + "x", matrices, vertexConsumers, light);
				}
			}

			matrices.translate(0.5, -0.5F / 16F, 0.5);

			if (direction == Direction.NORTH || direction == Direction.SOUTH) {
				matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(direction.asRotation() - 90));
			} else {
				matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(direction.getOpposite().asRotation() - 90));
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
				matrices.push();
				matrices.scale(0.5F, 0.5F, 0.5F);

				matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(90.0F));
				matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90.0F));

				if (i > 0) {
					float randomX = ((random.nextFloat() * 2.0F) - 1.0F) * 0.15F;
					float randomY = ((random.nextFloat() * 2.0F) - 1.0F) * 0.15F;
					float randomZ = i * -(1F / 16F);
					matrices.translate(randomX * 0.5, randomY * 0.5, randomZ * 0.75);
				}
				matrices.translate(0, 0, -3.5F / 16F);

				MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.FIXED, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers);
				matrices.pop();
			}
		}
	}

	protected void renderLabel(CuttingBoardBlockEntity entity, String string, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
		double d = MinecraftClient.getInstance().getEntityRenderManager().getSquaredDistanceToCamera(MinecraftClient.getInstance().player);
		if (d <= 4096.0D) {
			float f = 1.25F;
			int j = "deadmau5".equals(string) ? -10 : 0;
			matrixStack.push();
			matrixStack.translate(0.5D, (double)f, 0.5D);
			matrixStack.multiply(MinecraftClient.getInstance().getEntityRenderManager().getRotation());
			matrixStack.scale(-0.025F, -0.025F, 0.025F);
			Matrix4f matrix4f = matrixStack.peek().getModel();
			float g = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25F);
			int k = (int)(g * 255.0F) << 24;
			TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
			float h = (float)(-textRenderer.getStringWidth(string) / 2);
			textRenderer.draw(string, h, (float)j, 553648127, false, matrix4f, vertexConsumerProvider, false, k, 15728640);
			textRenderer.draw(string, h, (float)j, -1, false, matrix4f, vertexConsumerProvider, false, 0, 15728640);

			matrixStack.pop();
		}
	}
}
