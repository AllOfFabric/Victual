package io.github.alloffabric.victual;

import io.github.alloffabric.victual.client.render.CuttingBoardBlockEntityRenderer;
import io.github.alloffabric.victual.client.render.ToasterBlockEntityRenderer;
import io.github.alloffabric.victual.registry.VictualBlockEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;

public class VictualClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockEntityRendererRegistry.INSTANCE.register(VictualBlockEntities.CUTTING_BOARD, (CuttingBoardBlockEntityRenderer::new));
		BlockEntityRendererRegistry.INSTANCE.register(VictualBlockEntities.TOASTER, (ToasterBlockEntityRenderer::new));
	}
}
