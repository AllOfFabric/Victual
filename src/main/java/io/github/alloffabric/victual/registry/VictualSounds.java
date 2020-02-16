package io.github.alloffabric.victual.registry;

import io.github.alloffabric.victual.Victual;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.registry.Registry;

public class VictualSounds {
	public static final SoundEvent TOASTER_START = register("block.toaster.start");
	public static final SoundEvent TOASTER_STOP = register("block.toaster.stop");

	private VictualSounds() {
		// NO-OP
	}

	public static void init() {
		// NO-OP
	}

	private static SoundEvent register(String name) {
		return Registry.register(Registry.SOUND_EVENT, Victual.id(name), new SoundEvent(Victual.id(name)));
	}
}
