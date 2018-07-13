package com.blueskullgames.horserpg.utils;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.blueskullgames.horserpg.HorseRPG;

public class TameToClaimListener implements Listener {

	@EventHandler
	public void onTame(final PlayerInteractEntityEvent e) {
		if (!HorseRPG.disableTamedHorses) {
			//Default setting. No tame lock.
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
		} 
		int senderHorses = HorseRPG.ownedHorses.containsKey(e.getPlayer().getName())
				? HorseRPG.ownedHorses.get(e.getPlayer().getName()).size()
				: 0;
		if (HorseRPG.maxHorses(e.getPlayer()) != -1 && senderHorses >= HorseRPG.maxHorses(e.getPlayer())) {
			HorseRPG.msg(e.getPlayer(), HorseRPG.MAX_HORSES);
			e.setCancelled(true);
			return;
		}

		new BukkitRunnable() {
			int i = 0;

			@SuppressWarnings("deprecation")
			public void run() {
				i++;
				if (i >= 20) {
					e.getRightClicked().eject();
					cancel();
				}
				try {
					if (((org.bukkit.entity.AbstractHorse) e.getRightClicked()).getPassenger() == null) {
						cancel();
						return;
					}
					if (((org.bukkit.entity.AbstractHorse) e.getRightClicked()).isTamed()) {
						if(((org.bukkit.entity.AbstractHorse) e.getRightClicked()).getInventory().getSaddle()==null) {
							if(e.getPlayer().getInventory().contains(Material.SADDLE)) {
								int slot = e.getPlayer().getInventory().first(Material.SADDLE);
								((org.bukkit.entity.AbstractHorse) e.getRightClicked()).getInventory().setSaddle(e.getPlayer().getInventory().getItem(slot));
								e.getPlayer().getInventory().setItem(slot, new ItemStack(Material.AIR));
							}else {
								e.getRightClicked().eject();
								e.getPlayer().sendMessage(HorseRPG.HORSE_NEEDS_TAME);
								cancel();
								return;
							}
						}
						HorseRPG.claimHorse(((org.bukkit.entity.AbstractHorse) e.getRightClicked()).getPassenger());
						cancel();
						return;
					}
				} catch (Exception | Error e54) {
					if (((org.bukkit.entity.Horse) e.getRightClicked()).getPassenger() == null) {
						cancel();
						return;
					}
					if (((org.bukkit.entity.Horse) e.getRightClicked()).isTamed()) {
						if(((org.bukkit.entity.Horse) e.getRightClicked()).getInventory().getSaddle()==null) {
							if(e.getPlayer().getInventory().contains(Material.SADDLE)) {
								int slot = e.getPlayer().getInventory().first(Material.SADDLE);
								((org.bukkit.entity.Horse) e.getRightClicked()).getInventory().setSaddle(e.getPlayer().getInventory().getItem(slot));
								e.getPlayer().getInventory().setItem(slot, new ItemStack(Material.AIR));
							}else {
								e.getRightClicked().eject();
								e.getPlayer().sendMessage(HorseRPG.HORSE_NEEDS_TAME);
								cancel();
								return;
							}
						}
						HorseRPG.claimHorse(((org.bukkit.entity.Horse) e.getRightClicked()).getPassenger());
						cancel();
						return;
					}
				}
			}

		}.runTaskTimer(HorseRPG.instance, 5, 20);
	}
}
