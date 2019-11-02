package io.github.alloffabric.victual.item;

import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;

public class KnifeItem extends SwordItem {
	public KnifeItem(int attackDamage, float attackSpeed, Settings settings) {
		super(ToolMaterials.IRON, attackDamage, attackSpeed, settings);
	}
}
