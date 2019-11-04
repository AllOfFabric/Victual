package io.github.alloffabric.victual.mixin;

import io.github.alloffabric.victual.util.FabricFoodComponent;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;

@Mixin(Item.class)
public class ItemMixin {
	@Unique
	private static HashMap<Identifier, FoodComponent> recipes = new HashMap<>();

	static {
		//recipes.put(new Identifier("minecraft", "saddle"), FabricFoodComponent.copy(FoodComponents.APPLE).setHunger(4).setAlwaysEdible(true).build());
	}

	@Inject(method = "getFoodComponent", at = @At(value = "RETURN"), cancellable = true)
	public void getFoodComponent(CallbackInfoReturnable<FoodComponent> callbackInfo) {
		Item item = (Item) ((Object) this);
		Identifier identifier = Registry.ITEM.getId(item);
		FoodComponent component = callbackInfo.getReturnValue();

		if (recipes.containsKey(identifier)) {
			callbackInfo.setReturnValue(recipes.get(identifier));
		} else if (item.isFood() && component != null && identifier.getNamespace().equals("minecraft")) {
			callbackInfo.setReturnValue(FabricFoodComponent.copy(component).setHunger(component.getHunger() / 2).build());
		}
	}

	@Inject(method = "isFood", at = @At(value = "RETURN"), cancellable = true)
	public void isFood(CallbackInfoReturnable<Boolean> callbackInfo) {
		Item item = (Item) ((Object) this);
		Identifier identifier = Registry.ITEM.getId(item);

		if (recipes.containsKey(identifier)) {
			callbackInfo.setReturnValue(true);
		}
	}
}
