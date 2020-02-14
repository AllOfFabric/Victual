package io.github.alloffabric.victual;

import io.github.alloffabric.victual.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Victual implements ModInitializer {
	public static final String MOD_ID = "victual";
	public static final Logger LOGGER = LogManager.getLogger("Victual");
	public static final ItemGroup GROUP = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "group"), () -> new ItemStack(Items.COOKED_BEEF));
	
	public static Identifier id(String name) {
		return new Identifier(MOD_ID, name);
	}
	
	@Override
	public void onInitialize() {
		VictualConfig.sync(false);
		VictualBlocks.init();
		VictualItems.init();
		VictualBlockEntities.init();
		VictualTags.init();
		VictualEvents.init();
		VictualRecipes.init();

//		if (FabricLoader.getInstance().isModLoaded("libcd")) {
//			VictualTweaker.init();
//		}
	}
}
