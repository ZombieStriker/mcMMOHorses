package com.blueskullgames.horserpg.utils;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import com.blueskullgames.horserpg.HorseRPG;

public class PermToRideListener implements Listener {

	@EventHandler (priority=EventPriority.LOW,ignoreCancelled=true)
	public void onTame(final PlayerInteractEntityEvent e) {
		if (!e.getPlayer().hasPermission(HorseRPG.H_CLAIM)) {
			HorseRPG.msg(e.getPlayer(), HorseRPG.NO_PERMISSION_HORSE);
			e.setCancelled(true);
		}
	}
}
