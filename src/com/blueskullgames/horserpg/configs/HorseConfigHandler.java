package com.blueskullgames.horserpg.configs;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.blueskullgames.horserpg.HorseRPG;
import com.blueskullgames.horserpg.RPGHorse;

@SuppressWarnings("deprecation")
public class HorseConfigHandler {

	private final File file;
	private final FileConfiguration config;

	public HorseConfigHandler(File file) {
		this.file = file;
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.config = YamlConfiguration.loadConfiguration(file);
	}

	/*
	 * public void setGlobalVar(String path, Object var) { config.set("Global" +
	 * path, var); try { config.save(file); } catch (IOException e) {
	 * e.printStackTrace(); }
	 * 
	 * }
	 * 
	 * public boolean containsGlobalVariable(String path) { return
	 * config.contains("Global" + path); }
	 * 
	 * public Object getGlobalVariable(String path) { return config.get("Global" +
	 * path); }
	 */

	public void setVariable(RPGHorse horse, String path, Object var) {
		config.set("Horses." + horse.owner + "." + horse.rpgUUID.toString() + path, var);
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Object getVariable(RPGHorse horse, String path) {
		return config.get("Horses." + horse.owner + "." + horse.rpgUUID.toString() + path);
	}

	public boolean anyOwners() {
		return config.contains("Horses");
	}

	public Set<String> getOwners() {
		return config.getConfigurationSection("Horses").getKeys(false);
	}

	public Set<String> getHorses(String owner) {
		return config.getConfigurationSection("Horses." + owner).getKeys(false);
	}

	public void saveHorse(RPGHorse horse, boolean save) {
		config.set("Horses." + horse.owner + "." + horse.rpgUUID.toString() + Keys.name, horse.name);
		config.set("Horses." + horse.owner + "." + horse.rpgUUID.toString() + Keys.wrath, horse.wrath.xp);
		config.set("Horses." + horse.owner + "." + horse.rpgUUID.toString() + Keys.agility, horse.agility.xp);
		config.set("Horses." + horse.owner + "." + horse.rpgUUID.toString() + Keys.swiftness, horse.swiftness.xp);
		config.set("Horses." + horse.owner + "." + horse.rpgUUID.toString() + Keys.vitality, horse.vitality.xp);
		config.set("Horses." + horse.owner + "." + horse.rpgUUID.toString() + Keys.color, horse.color.name());
		config.set("Horses." + horse.owner + "." + horse.rpgUUID.toString() + Keys.isdead, horse.isDead);
		config.set("Horses." + horse.owner + "." + horse.rpgUUID.toString() + Keys.godmode, horse.godmode);
		config.set("Horses." + horse.owner + "." + horse.rpgUUID.toString() + Keys.powerlevel, horse.powerLevel);
		config.set("Horses." + horse.owner + "." + horse.rpgUUID.toString() + Keys.style, horse.style.name());
		config.set("Horses." + horse.owner + "." + horse.rpgUUID.toString() + Keys.variant, horse.variant.name());
		config.set("Horses." + horse.owner + "." + horse.rpgUUID.toString() + Keys.hassaddle, horse.hasSaddle);
		config.set("Horses." + horse.owner + "." + horse.rpgUUID.toString() + Keys.sex, horse.isMale);
		if (horse.hasChest)
			config.set("Horses." + horse.owner + "." + horse.rpgUUID.toString() + Keys.hasChest, horse.hasChest);
		if (horse.horse != null && !horse.isDead && !horse.isBanished) {
			config.set("Horses." + horse.owner + "." + horse.rpgUUID.toString() + Keys.entityslastUUID,
					horse.horse.getUniqueId().toString());
			config.set("Horses." + horse.owner + "." + horse.rpgUUID.toString() + Keys.entityslastWorld,
					horse.horse.getWorld().getName());
			if (horse.generic_speed < 0) {
				if (horse.horse != null)
					config.set("Horses." + horse.owner + "." + horse.rpgUUID.toString() + Keys.sprint,
							RPGHorse.attributeUtil.getSpeed(horse.horse));
			} else {
				config.set("Horses." + horse.owner + "." + horse.rpgUUID.toString() + Keys.sprint, horse.generic_speed);
			}
			if (horse.generic_speed <= 0) {
				if (horse.horse != null)
					config.set("Horses." + horse.owner + "." + horse.rpgUUID.toString() + Keys.jump,
							RPGHorse.attributeUtil.getJumpHeight(horse.horse));
			} else {
			config.set("Horses." + horse.owner + "." + horse.rpgUUID.toString() + Keys.jump, horse.generic_jump);
			}
			try {
				config.set("Horses." + horse.owner + "." + horse.rpgUUID.toString() + Keys.inventory,
						((HorseInventory) ((AbstractHorse) horse.horse).getInventory()).getContents());
			} catch (Exception | Error e) {
				try {
					config.set("Horses." + horse.owner + "." + horse.rpgUUID.toString() + Keys.inventory,
							((HorseInventory) ((Horse) horse.horse).getInventory()).getContents());
				} catch (Exception | Error e2) {
					config.set("Horses." + horse.owner + "." + horse.rpgUUID.toString() + Keys.inventory,
							((Inventory) ((AbstractHorse) horse.horse).getInventory()).getContents());
				}
			}

		} else {
			config.set("Horses." + horse.owner + "." + horse.rpgUUID.toString() + Keys.entityslastUUID, null);
			config.set("Horses." + horse.owner + "." + horse.rpgUUID.toString() + Keys.entityslastWorld, null);
		}
		if (save)
			save();
	}

	public void save() {
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void removeHorse(RPGHorse horse) {
		setVariable(horse, "", null);
	}

	@SuppressWarnings("unchecked")
	public RPGHorse getHorse(final String owner, String horseUUID) {
		for (final String rpguuids : config.getConfigurationSection("Horses." + owner).getKeys(false)) {
			try {
				if (rpguuids.equalsIgnoreCase(horseUUID)) {
					Color c = Color.DARK_BROWN;

					if (config.contains("Horses." + owner + "." + rpguuids + Keys.color)) {
						c = Color.valueOf((config.getString("Horses." + owner + "." + rpguuids + Keys.color)));
					}
					Style s = Style.NONE;

					if (config.contains("Horses." + owner + "." + rpguuids + Keys.style)) {
						s = Style.valueOf((config.getString("Horses." + owner + "." + rpguuids + Keys.style)));
					}
					Variant v = Variant.HORSE;
					if (config.contains("Horses." + owner + "." + rpguuids + Keys.variant)) {
						v = Variant.valueOf(config.getString("Horses." + owner + "." + rpguuids + Keys.variant));
					}
					boolean gm = config.getBoolean("Horses." + owner + "." + rpguuids + Keys.godmode);
					int sswift = config.getInt("Horses." + owner + "." + rpguuids + Keys.swiftness);
					int sagil = config.getInt("Horses." + owner + "." + rpguuids + Keys.agility);
					int svit = config.getInt("Horses." + owner + "." + rpguuids + Keys.vitality);
					int swrath = config.getInt("Horses." + owner + "." + rpguuids + Keys.wrath);
					String horsename = config.getString("Horses." + owner + "." + rpguuids + Keys.name);
					UUID uuid = UUID.fromString(rpguuids);

					double jumpPow = config.contains("Horses." + owner + "." + rpguuids + Keys.jump)
							? config.getDouble("Horses." + owner + "." + rpguuids + Keys.jump)
							: -1;
					double sprintPow = config.contains("Horses." + owner + "." + rpguuids + Keys.sprint)
							? config.getDouble("Horses." + owner + "." + rpguuids + Keys.sprint)
							: -1;

					boolean sex = config.contains("Horses." + owner + "." + rpguuids + Keys.sex)
							? config.getBoolean("Horses." + owner + "." + rpguuids + Keys.sex)
							: Math.random() < 0.5;

					final RPGHorse rpgHorse = new RPGHorse(horsename, owner, c, s, v, gm, sswift, sagil, svit, swrath,
							uuid, jumpPow, sprintPow, sex);
					if (config.contains("Horses." + owner + "." + rpguuids + Keys.hasChest)) {
						rpgHorse.setHasChest(config.getBoolean("Horses." + owner + "." + rpguuids + Keys.hasChest));
					}

					if (config.contains("Horses." + owner + "." + rpguuids + Keys.inventory.toString())) {
						ItemStack[] dymbtemp2 = null;
						try {
							Object[] dumptemp = null;
							dumptemp = ((List<ItemStack>) config
									.get("Horses." + owner + "." + rpguuids + Keys.inventory.toString())).toArray();
							dymbtemp2 = new ItemStack[dumptemp.length];
							for (int i = 0; i < dumptemp.length; i++) {
								dymbtemp2[i] = (ItemStack) dumptemp[i];
							}
						} catch (Error | Exception e5) {
							dymbtemp2 = ((ItemStack[]) config
									.get("Horses." + owner + "." + rpguuids + Keys.inventory.toString()));
						}
						rpgHorse.inventory = dymbtemp2;
					}
					if (config.contains("Horses." + owner + "." + rpguuids + Keys.entityslastUUID.toString())) {
						final UUID uuid2 = UUID.fromString(
								config.getString("Horses." + owner + "." + rpguuids + Keys.entityslastUUID));
						rpgHorse.isBanished = false;
						new BukkitRunnable() {

							@Override
							public void run() {
								World world = Bukkit.getWorld(
										config.getString("Horses." + owner + "." + rpguuids + Keys.entityslastWorld));
								for (Entity e : world.getEntities()) {
									if (e.getUniqueId().equals(uuid2)) {
										rpgHorse.horse = e;
										break;
									}
								}
								HorseRPG.hSpawnedHorses.put(rpgHorse.horse, rpgHorse);
								if (Bukkit.getPlayer(owner) != null)
									HorseRPG.pCurrentHorse.put(Bukkit.getPlayer(owner), rpgHorse);
							}
						}.runTaskLater(HorseRPG.instance, 2);
					}
					rpgHorse.hasSaddle = config.getBoolean("Horses." + owner + "." + rpguuids + Keys.hassaddle);
					System.out.println("Loading " + rpgHorse.name);
					return rpgHorse;
				}
			} catch (Error | Exception e45) {
			}
		}
		System.out.println("Could not load horse " + horseUUID);
		return null;
	}

	public enum Keys {
		entityslastUUID(".lastUUIDinstance"), entityslastWorld(".lastWorldname"), name(".name"), godmode(
				".godmode"), inventory(".inventory"), hassaddle(".hassaddle"), wrath(".wrath"), agility(
						".agility"), swiftness(".swiftness"), vitality(".vitality"), color(".color"), isdead(
								".isdead"), powerlevel(".powerlevel"), style(".style"), variant(".variant"), jump(
										".defaultJump"), sprint(".defaultSpeed"), sex(".sex"), hasChest(".hasChest");
		private String n;

		private Keys(String name) {
			n = name;
		}

		@Override
		public String toString() {
			return n;
		}
	}
}
