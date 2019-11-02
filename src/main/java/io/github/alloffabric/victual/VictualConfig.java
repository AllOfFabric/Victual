package io.github.alloffabric.victual;

import io.github.indicode.fabric.tinyconfig.ModConfig;

public class VictualConfig {
	private static ModConfig modConfig = new ModConfig(Victual.MOD_ID);
	
	public static void sync(boolean overwrite) {
		modConfig.configure(overwrite, config -> {

		});
	}
}
