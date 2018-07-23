package com.blueskullgames.horserpg.utils;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import com.blueskullgames.horserpg.HorseRPG;

public class PermToRideListener implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler (priority=EventPriority.LOW,ignoreCancelled=true)
	public void onTame(final PlayerInteractEntityEvent e) {
		if (!e.getPlayer().hasPermission(HorseRPG.H_CLAIM)) {
			if(e.getRightClicked().getType().name().contains("HORSE")||e.getRightClicked().getType().name().contains("LLAMA")||e.getRightClicked().getType().name().contains("DONKEY")||e.getRightClicked().getType().name().contains("MULE")) {
				if(e.getPlayer().getItemInHand()!=null&&(e.getPlayer().getItemInHand().getType().name().equals("LEASH")||e.getPlayer().getItemInHand().getType().name().equals("LEAD")))
					return;
			}else {
				return;
			}
			HorseRPG.msg(e.getPlayer(), HorseRPG.NO_PERMISSION_HORSE);
			e.setCancelled(true);
		}
	}
}
