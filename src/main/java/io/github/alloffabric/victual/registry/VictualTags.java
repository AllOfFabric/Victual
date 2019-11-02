package io.github.alloffabric.victual.registry;

import io.github.alloffabric.victual.Victual;
import net.fabricmc.fabric.api.tag.TagRegistry;

import net.minecraft.item.Item;
import net.minecraft.tag.Tag;

public class VictualTags {
	// Register Here
	
	private VictualTags() {
		// NO-OP
	}
	
	public static void init() {
		// NO-OP
	}
	
	public static Tag<Item> register(String name) {
		return TagRegistry.item(Victual.id(name));
	}
}
