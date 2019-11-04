package io.github.alloffabric.victual.api;

import net.minecraft.util.StringIdentifiable;

public enum CounterConnection implements StringIdentifiable {
	NONE("none"),
	INSIDE_LEFT("inside_left"),
	INSIDE_RIGHT("inside_right"),
	OUTSIDE_LEFT("outside_left"),
	OUTSIDE_RIGHT("outside_right");

	String name;

	CounterConnection(String name) {
		this.name = name;
	}

	@Override
	public java.lang.String asString() {
		return name;
	}
}
