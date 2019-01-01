package com.blueskullgames.horserpg.utils.atributeutils;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.Inventory;

public interface BaseAtributeUtil {

	public double getJumpHeight(Entity e);
	public double getSpeed(Entity e);
	public void setJumpHeight(Entity e,double f);
	public void setSpeed(Entity e, double f);
	public Inventory getInventory(Entity e);
}
