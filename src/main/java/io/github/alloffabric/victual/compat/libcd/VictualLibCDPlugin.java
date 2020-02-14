package io.github.alloffabric.victual.compat.libcd;

import io.github.cottonmc.libcd.api.LibCDInitializer;
import io.github.cottonmc.libcd.api.condition.ConditionManager;
import io.github.cottonmc.libcd.api.tweaker.TweakerManager;

public class VictualLibCDPlugin implements LibCDInitializer {
	@Override
	public void initTweakers(TweakerManager manager) {
		manager.addAssistant("victual.recipe.VictualTweaker", new VictualTweaker());
	}

	@Override
	public void initConditions(ConditionManager conditionManager) {

	}
}
