package com.blueskullgames.horserpg.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.blueskullgames.horserpg.HorseRPG;
import com.blueskullgames.horserpg.RPGHorse;

@SuppressWarnings("deprecation")
public class ShopManager implements Listener {
	private Inventory shop;

	public static String title = ChatColor.GOLD + "McMMO Horses Shop";

	public ShopManager() {
		shop = Bukkit.createInventory(null, ((((Variant.values().length + Color.values().length) / 9) + 1) * 9), title);
		for (Variant v : Variant.values()) {
			if (v.name().equals("LLAMA"))
				continue;
			if (v == Variant.HORSE)
				for (Color c : Color.values())
					shop.addItem(createHorse(HorseRPG.costForHorse.get(v), c, v));
			else
				shop.addItem(createHorse(HorseRPG.costForHorse.get(v), null, v));
		}
	}

	public ItemStack createHorse(double cost, Color color, Variant var) {
		ItemStack item = new ItemStack(Material.SADDLE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Buy a " + var.name());
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.GOLD + "Cost: " + cost);
		if (color != null)
			lore.add(ChatColor.WHITE + "Color: " + color.name());
		lore.add(ChatColor.GRAY + "Sex: Random");
		lore.add(ChatColor.GRAY + "Saddle: Included");
		lore.add(ChatColor.GRAY + "AgilityXp: 0");
		lore.add(ChatColor.GRAY + "SwiftnessXp: 0");
		lore.add(ChatColor.GRAY + "VitalityXp: 0");
		lore.add(ChatColor.GRAY + "WrathXp: 0");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public void openInventory(CommandSender p) {
		if (!(p instanceof Player)) {
			HorseRPG.msg(p, HorseRPG.PLAYER_ONLY);
			return;
		}
		((HumanEntity) p).openInventory(shop);
	}

	@EventHandler
	public void onClickInv(InventoryClickEvent e) {
		if (e.getInventory().getTitle().equals(title)) {
			e.setCancelled(true);
			if (e.getCurrentItem() != null) {
				
				for (Variant v : Variant.values()) {
					if (e.getCurrentItem().getItemMeta().getDisplayName().endsWith(" "+v.name())) {
						e.getWhoClicked().closeInventory();
						if (HorseRPG.econ != null && HorseRPG.econ
								.getBalance((OfflinePlayer) e.getWhoClicked()) >= HorseRPG.costForHorse.get(v)) {
							RPGHorse horse = new RPGHorse(RPGHorse.randomName((Player) e.getWhoClicked()),
									e.getWhoClicked().getName(), getColorInLore(e.getCurrentItem()), Style.NONE, v,
									false, 0, 0, 0, 0, UUID.randomUUID(), 2.25, 2.25,Math.random()<0.5);
							horse.hasSaddle = true;
							HorseRPG.ownedHorses.get(e.getWhoClicked().getName()).add(horse);
							HorseRPG.horses.add(horse);
							if (!HorseRPG.pCurrentHorse.containsKey(e.getWhoClicked())) {
								HorseRPG.pCurrentHorse.put((Player) e.getWhoClicked(), horse);
							}
							HorseRPG.msg(e.getWhoClicked(), HorseRPG.BOUGHT_HORSE + horse.name + "!");
							HorseRPG.msg(e.getWhoClicked(), HorseRPG.BOUGHT_HORSE_EXP1);
							e.getWhoClicked().closeInventory();
							HorseRPG.econ.withdrawPlayer((OfflinePlayer) e.getWhoClicked(),
									HorseRPG.costForHorse.get(v));
						} else {
							HorseRPG.msg(e.getWhoClicked(), HorseRPG.NOT_ENOUGH_MONEY);
						}
						break;
					}
				}
			}
		}
	}

	public Color getColorInLore(ItemStack is) {
		for (String l : is.getItemMeta().getLore()) {
			if (l.startsWith(ChatColor.WHITE + "Color:")) {
				return Color.valueOf(l.split(":")[1].trim());
			}
		}
		return Color.GRAY;
	}
}
