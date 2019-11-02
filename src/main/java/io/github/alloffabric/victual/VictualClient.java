package io.github.alloffabric.victual;

import io.github.alloffabric.victual.block.entity.CuttingBoardBlockEntity;
import io.github.alloffabric.victual.client.render.CuttingBoardBlockEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.render.BlockEntityRendererRegistry;

public class VictualClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockEntityRendererRegistry.INSTANCE.register(CuttingBoardBlockEntity.class, new CuttingBoardBlockEntityRenderer());
	}
}
