package com.blueskullgames.horserpg.utils;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import com.blueskullgames.horserpg.HorseRPG;

public class PermToRideListener implements Listener {

	@EventHandler
	public void onTame(final PlayerInteractEntityEvent e) {		
		try {
			if (!(e.getRightClicked() instanceof org.bukkit.entity.AbstractHorse))
				return;
			if (((org.bukkit.entity.AbstractHorse) e.getRightClicked()).isTamed())
				return;
		} catch (Exception | Error e54) {
			if (!(e.getRightClicked() instanceof org.bukkit.entity.Horse))
				return;
			if (((org.bukkit.entity.Horse) e.getRightClicked()).isTamed())
				return;
		}
		
		if(!e.getPlayer().hasPermission(HorseRPG.H_CLAIM)) {
			e.setCancelled(true);
		}
	}
}
