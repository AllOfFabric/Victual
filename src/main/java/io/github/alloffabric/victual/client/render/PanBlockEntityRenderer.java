package io.github.alloffabric.victual.client.render;

import io.github.alloffabric.victual.block.entity.CuttingBoardBlockEntity;
import io.github.alloffabric.victual.block.entity.PanBlockEntity;
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
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Direction;

import java.util.Random;

public class PanBlockEntityRenderer extends BlockEntityRenderer<PanBlockEntity> {
	public PanBlockEntityRenderer(BlockEntityRenderDispatcher blockEntityRenderDispatcher) {
		super(blockEntityRenderDispatcher);
	}

	@Override
	public void render(PanBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		int x = blockEntity.getPos().getX();
		int y = blockEntity.getPos().getY();
		int z = blockEntity.getPos().getZ();
		Random random = new Random();
		Direction direction = blockEntity.getWorld().getBlockState(blockEntity.getPos()).get(HorizontalFacingBlock.FACING);

		if (!blockEntity.getStack(0).isEmpty()) {
			HitResult result = MinecraftClient.getInstance().player.rayTrace(16, 1.0F, false);
			if (result.getType() == HitResult.Type.BLOCK) {
				BlockHitResult blockHitResult = (BlockHitResult) result;

				if (blockHitResult.getBlockPos().equals(blockEntity.getPos())) {
					//renderLabel(blockEntity, stack.getCount() + "x", matrices, vertexConsumers, light);
				}
			}

			matrices.translate(0.5, -0.5F / 16F, 0.5);

			if (direction == Direction.NORTH || direction == Direction.SOUTH) {
				matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(direction.asRotation() - 90));
			} else {
				matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(direction.getOpposite().asRotation() - 90));
			}

			renderItem(blockEntity.getStack(0), matrices, vertexConsumers, light);
			float offset = 0;

			if (blockEntity.getStack(0).getItem() instanceof BlockItem) {
				offset = 4F / 16F;
			} else {
				offset = 0.5F / 16F;
			}

			for (int i = 1; i <= 5; i++) {
				if (!blockEntity.getStack(i).isEmpty()) {
					matrices.push();
					matrices.translate(0, offset, 0);
					if (blockEntity.getStack(i).getItem() instanceof BlockItem) {
						offset += 4F / 16F;
					} else {
						offset += 0.5F / 16F;
					}
					renderItem(blockEntity.getStack(i), matrices, vertexConsumers, light);
					matrices.pop();
				}
			}
		}
	}

	public void renderItem(ItemStack stack, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		matrices.push();
		matrices.scale(0.5F, 0.5F, 0.5F);

		if (!(stack.getItem() instanceof BlockItem)) {
			matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(90.0F));
			matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90.0F));

			matrices.translate(0, 0, -3.5F / 16F);
		} else {
			matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(90.0F));

			matrices.translate(0, 7F / 16F, 0);
		}

		MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.FIXED, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers);
		matrices.pop();
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
