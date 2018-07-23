package com.blueskullgames.horserpg.utils;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.blueskullgames.horserpg.HorseRPG;

public class TameToClaimListener implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onTame(final PlayerInteractEntityEvent e) {
		if (!HorseRPG.disableTamedHorses) {
			// Default setting. No tame lock.
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
		if (e.getRightClicked().getType().name().contains("HORSE")
				|| e.getRightClicked().getType().name().contains("LLAMA")
				|| e.getRightClicked().getType().name().contains("DONKEY")
				|| e.getRightClicked().getType().name().contains("MULE")) {
			if (e.getPlayer().getItemInHand() != null && (e.getPlayer().getItemInHand().getType().name().equals("LEASH")
					|| e.getPlayer().getItemInHand().getType().name().equals("LEAD")))
				return;
		} else {
			return;
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
						if (((org.bukkit.entity.AbstractHorse) e.getRightClicked()).getInventory()
								.getSaddle() == null) {
							ItemStack used = null;
							int slot = 0;
							int takenfrom = 0;
							if (e.getRightClicked().getType().name().equals("LLAMA")) {
								slot = 1;
								for (int i = 0; i < e.getPlayer().getInventory().getSize(); i++) {
									ItemStack is = e.getPlayer().getInventory().getItem(i);
									if (is != null && is.getType().name().contains("CARPET")) {
										used = is;
										takenfrom = i;
										break;
									}
								}
							} else {
								takenfrom = e.getPlayer().getInventory().first(Material.SADDLE);
								if (takenfrom > -1)
									used = e.getPlayer().getInventory().getItem(takenfrom);
							}
							if (used != null) {
								if (used.getAmount() > 1) {
									ItemStack temp = used.clone();
									temp.setAmount(1);
									((org.bukkit.entity.AbstractHorse) e.getRightClicked()).getInventory().setItem(slot,
											temp);
									used.setAmount(used.getAmount() - 1);
									e.getPlayer().getInventory().setItem(slot, used);
								} else {
									((org.bukkit.entity.AbstractHorse) e.getRightClicked()).getInventory().setItem(slot,
											used);
									e.getPlayer().getInventory().setItem(slot, used);
								}
							} else {
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
						if (((org.bukkit.entity.Horse) e.getRightClicked()).getInventory().getSaddle() == null) {
							if (e.getPlayer().getInventory().contains(Material.SADDLE)) {
								int slot = e.getPlayer().getInventory().first(Material.SADDLE);
								((org.bukkit.entity.Horse) e.getRightClicked()).getInventory()
										.setSaddle(e.getPlayer().getInventory().getItem(slot));
								e.getPlayer().getInventory().setItem(slot, new ItemStack(Material.AIR));
							} else {
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
