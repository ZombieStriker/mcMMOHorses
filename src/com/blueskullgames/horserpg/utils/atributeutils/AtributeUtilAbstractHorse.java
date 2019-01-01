package com.blueskullgames.horserpg.utils.atributeutils;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.Inventory;

public class AtributeUtilAbstractHorse implements BaseAtributeUtil {

	@Override
	public double getJumpHeight(Entity e) {
		return ((AbstractHorse) e).getAttribute(Attribute.HORSE_JUMP_STRENGTH).getValue();
	}

	@Override
	public double getSpeed(Entity e) {
		return ((AbstractHorse) e).getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getValue();
	}

	@Override
	public void setJumpHeight(Entity e, double f) {
		((AbstractHorse) e).getAttribute(Attribute.HORSE_JUMP_STRENGTH).setBaseValue(f);

	}

	@Override
	public void setSpeed(Entity e, double f) {
		((AbstractHorse) e).getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(f);
	}
	@Override
	public Inventory getInventory(Entity e) {
		return ((AbstractHorse) e).getInventory();
	}

}
