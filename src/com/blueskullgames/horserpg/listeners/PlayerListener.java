package com.blueskullgames.horserpg.listeners;

import java.util.UUID;

import org.bukkit.*;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.blueskullgames.horserpg.HorseRPG;
import com.blueskullgames.horserpg.RPGHorse;
import com.blueskullgames.horserpg.utils.NewHorseUtil;

public class PlayerListener implements Listener {

	@EventHandler
	public void onJoin(final PlayerJoinEvent event) {
		// if(HorseRPG.banishonquit)
		// return;

		for (UUID p : HorseRPG.pCurrentHorse.keySet()) {
			if (p.equals(event.getPlayer().getUniqueId())) {
				// HorseRPG.pCurrentHorse.put(event.getPlayer(), HorseRPG.pCurrentHorse.get(p));
				break;
			}
		}
		// if (HorseRPG.banishonquit) {
		if (HorseRPG.ownedHorses.containsKey(event.getPlayer().getName()))
			new BukkitRunnable() {
				public void run() {
					for (RPGHorse rpg : HorseRPG.ownedHorses.get(event.getPlayer().getName())) {
						if (rpg.getHorse() != null)
							for (Entity e : rpg.getHorse().getNearbyEntities(60, 30, 60)) {
								if (e.getType() == rpg.getHorse().getType()
										&& e.getUniqueId().equals(rpg.getHorse().getUniqueId())) {
									HorseRPG.hSpawnedHorses.put(e, HorseRPG.hSpawnedHorses.remove(rpg.getHorse()));
									rpg.getHorse().remove();
									rpg.setHorse(e);
									break;
								}
							}
					}
				}
			}.runTaskLater(HorseRPG.instance, 2);

		// }
	}

	/**
	 * Player Move Event
	 **/
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerMove(PlayerMoveEvent event) {
		Player p = event.getPlayer();
		boolean b = false;
		try {
			NewHorseUtil.useNewHorses();
			b = p.getVehicle() instanceof AbstractHorse;
		} catch (NoClassDefFoundError e) {
			b = p.getVehicle() instanceof Horse;
		}
		if (p.isInsideVehicle() && b) {
			double distance = event.getFrom().distance(event.getTo());
			if (distance <= 0)
				return;

			if (!p.getLocation().getWorld().equals(event.getTo().getWorld()))
				return;

			RPGHorse h = HorseRPG.pCurrentHorse.get(p.getUniqueId());
			if (h != null)
				h.travel(distance);
		}
	}

	/**
	 * Player Quit Event
	 **/
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent evt) {
		if (HorseRPG.banishonquit) {
			if (HorseRPG.ownedHorses.containsKey(evt.getPlayer().getName()))
				for (RPGHorse h : HorseRPG.ownedHorses.get(evt.getPlayer().getName())) {
					if (h != null && h.getHorse() != null && HorseRPG.hSpawnedHorses.containsKey(h.getHorse())) {
						HorseRPG.hSpawnedHorses.remove(h.getHorse()).banish();
					}
				}
		}
	}

	/**
	 * Player Feeding Horse Event
	 **/
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerFeedingHorse(PlayerInteractEntityEvent event) {
		boolean b = false;
		try {
			NewHorseUtil.useNewHorses();
			b = event.getRightClicked() instanceof AbstractHorse;
		} catch (NoClassDefFoundError e) {
			b = event.getRightClicked() instanceof Horse;
		}
		if (b) {
			final Entity horse = event.getRightClicked();
			if (HorseRPG.hSpawnedHorses.containsKey(horse)) {
				final Player p = event.getPlayer();
				ItemStack item;
				try {
					item = p.getInventory().getItemInMainHand();
				} catch (Exception | Error e) {
					item = p.getItemInHand();
				}
				Material m = null;
				if (item != null && ((m = item.getType()) == Material.GOLDEN_APPLE || m == Material.GOLDEN_CARROT)) {
					RPGHorse h = HorseRPG.hSpawnedHorses.get(horse);
					if (h != null && h.owners_name.equalsIgnoreCase(p.getName())) {
						h.vitality.addXP(m == Material.GOLDEN_APPLE ? 5 : 4, p);
						ItemStack is = event.getPlayer().getItemInHand();
						if (is.getAmount() == 1) {
							is.setType(Material.AIR);
						} else {
							is.setAmount(is.getAmount() - 1);
						}
						event.getPlayer().setItemInHand(is);
					}
				} else {
					final RPGHorse rpg = HorseRPG.hSpawnedHorses.get(horse);
					if (rpg.owners_name.equals(p.getName())) {
						new BukkitRunnable() {
							public void run() {
								if (p.getVehicle() != null)
									if (p.getVehicle().equals(horse)) {
										HorseRPG.pCurrentHorse.put(p.getUniqueId(), rpg);
									}
							}
						}.runTaskLater(HorseRPG.instance, 2);
					}
				}
			}
		}

	}

}
