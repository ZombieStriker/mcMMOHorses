package com.blueskullgames.horserpg.utils;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.blueskullgames.horserpg.HorseRPG;

public class TameToClaimListener implements Listener {

	@EventHandler
	public void onTame(final PlayerInteractEntityEvent e) {
		
		Player p = e.getPlayer();
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

		new BukkitRunnable() {
			int i = 0;
			public void run() {
				i++;
				if(i>= 60)
					cancel();
				try {
					if (((org.bukkit.entity.AbstractHorse) e.getRightClicked()).getPassenger()==null) {
						cancel();
					return;
				}
					if (((org.bukkit.entity.AbstractHorse) e.getRightClicked()).isTamed()) {
						HorseRPG.claimHorse(((org.bukkit.entity.AbstractHorse) e.getRightClicked()).getPassenger());
						cancel();
						return;
					}
				} catch (Exception | Error e54) {
					if (((org.bukkit.entity.Horse) e.getRightClicked()).getPassenger()==null) {
						cancel();
						return;
					}
					if (((org.bukkit.entity.Horse) e.getRightClicked()).isTamed()) {
						HorseRPG.claimHorse(((org.bukkit.entity.Horse) e.getRightClicked()).getPassenger());
						cancel();
						return;
					}
				}
			}
			
		}.runTaskTimer(HorseRPG.instance, 5, 50);
	}
}
