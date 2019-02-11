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
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.*;
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
		config.set("Horses." + horse.owners_name + "." + horse.rpgUUID.toString() + path, var);
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Object getVariable(RPGHorse horse, String path) {
		return config.get("Horses." + horse.owners_name + "." + horse.rpgUUID.toString() + path);
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
		config.set("Horses." + horse.owners_name + "." + horse.rpgUUID.toString() + Keys.name, horse.name);
		config.set("Horses." + horse.owners_name + "." + horse.rpgUUID.toString() + Keys.wrath, horse.wrath.xp);
		config.set("Horses." + horse.owners_name + "." + horse.rpgUUID.toString() + Keys.agility, horse.agility.xp);
		config.set("Horses." + horse.owners_name + "." + horse.rpgUUID.toString() + Keys.swiftness, horse.swiftness.xp);
		config.set("Horses." + horse.owners_name + "." + horse.rpgUUID.toString() + Keys.vitality, horse.vitality.xp);
		config.set("Horses." + horse.owners_name + "." + horse.rpgUUID.toString() + Keys.color, horse.color.name());
		config.set("Horses." + horse.owners_name + "." + horse.rpgUUID.toString() + Keys.isdead, horse.isDead);
		config.set("Horses." + horse.owners_name + "." + horse.rpgUUID.toString() + Keys.godmode, horse.godmode);
		config.set("Horses." + horse.owners_name + "." + horse.rpgUUID.toString() + Keys.powerlevel, horse.powerLevel);
		config.set("Horses." + horse.owners_name + "." + horse.rpgUUID.toString() + Keys.style, horse.style.name());
		config.set("Horses." + horse.owners_name + "." + horse.rpgUUID.toString() + Keys.variant, horse.variant.name());
		config.set("Horses." + horse.owners_name + "." + horse.rpgUUID.toString() + Keys.hassaddle, horse.hasSaddle);
		config.set("Horses." + horse.owners_name + "." + horse.rpgUUID.toString() + Keys.sex, horse.isMale);
		config.set("Horses." + horse.owners_name + "." + horse.rpgUUID.toString() + Keys.SPAWNED, horse.spawned);
		if ((horse.getHorse() != null && !((Ageable) horse.getHorse()).isAdult()) || horse.isBaby) {
			config.set("Horses." + horse.owners_name + "." + horse.rpgUUID.toString() + Keys.isBaby,
					(horse.getHorse() != null ? !((Ageable) horse.getHorse()).isAdult() : horse.isBaby));
			config.set("Horses." + horse.owners_name + "." + horse.rpgUUID.toString() + Keys.AGE, horse.babyAge);
		} else {
			config.set("Horses." + horse.owners_name + "." + horse.rpgUUID.toString() + Keys.isBaby, null);
			config.set("Horses." + horse.owners_name + "." + horse.rpgUUID.toString() + Keys.AGE, null);
		}
		if (horse.hasChest)
			config.set("Horses." + horse.owners_name + "." + horse.rpgUUID.toString() + Keys.hasChest, horse.hasChest);
		if (horse.getHorse() != null && !horse.isDead && !horse.isBanished) {
			config.set("Horses." + horse.owners_name + "." + horse.rpgUUID.toString() + Keys.entityslastUUID,
					horse.getHorse().getUniqueId().toString());
			config.set("Horses." + horse.owners_name + "." + horse.rpgUUID.toString() + Keys.entityslastWorld,
					horse.getHorse().getWorld().getName());
			if (horse.generic_speed < 0) {
				if (horse.getHorse() != null)
					config.set("Horses." + horse.owners_name + "." + horse.rpgUUID.toString() + Keys.sprint,
							RPGHorse.attributeUtil.getSpeed(horse.getHorse()));
			} else {
				config.set("Horses." + horse.owners_name + "." + horse.rpgUUID.toString() + Keys.sprint,
						horse.generic_speed);
			}
			if (horse.generic_speed <= 0) {
				if (horse.getHorse() != null)
					config.set("Horses." + horse.owners_name + "." + horse.rpgUUID.toString() + Keys.jump,
							RPGHorse.attributeUtil.getJumpHeight(horse.getHorse()));
			} else {
				config.set("Horses." + horse.owners_name + "." + horse.rpgUUID.toString() + Keys.jump,
						horse.generic_jump);
			}
			try {
				config.set("Horses." + horse.owners_name + "." + horse.rpgUUID.toString() + Keys.inventory,
						((HorseInventory) ((AbstractHorse) horse.getHorse()).getInventory()).getContents());
			} catch (Exception | Error e) {
				try {
					config.set("Horses." + horse.owners_name + "." + horse.rpgUUID.toString() + Keys.inventory,
							((HorseInventory) ((Horse) horse.getHorse()).getInventory()).getContents());
				} catch (Exception | Error e2) {
					config.set("Horses." + horse.owners_name + "." + horse.rpgUUID.toString() + Keys.inventory,
							((Inventory) ((AbstractHorse) horse.getHorse()).getInventory()).getContents());
				}
			}

		} else {
			config.set("Horses." + horse.owners_name + "." + horse.rpgUUID.toString() + Keys.entityslastUUID,
					horse.holderOverUUID == null ? null : horse.holderOverUUID.toString());
			config.set("Horses." + horse.owners_name + "." + horse.rpgUUID.toString() + Keys.entityslastWorld, null);
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
					final String horsename = config.getString("Horses." + owner + "." + rpguuids + Keys.name);
					UUID uuid = UUID.fromString(rpguuids);

					double jumpPow = config.contains("Horses." + owner + "." + rpguuids + Keys.jump)
							? config.getDouble("Horses." + owner + "." + rpguuids + Keys.jump)
							: RPGHorse.getRandomJump();
					double sprintPow = config.contains("Horses." + owner + "." + rpguuids + Keys.sprint)
							? config.getDouble("Horses." + owner + "." + rpguuids + Keys.sprint)
							: RPGHorse.getRandomSpeed();

					if (jumpPow > RPGHorse.maxJump)
						jumpPow = RPGHorse.getRandomJump();
					if (sprintPow > RPGHorse.maxSpeed)
						sprintPow = RPGHorse.getRandomSpeed();

					boolean sex = config.contains("Horses." + owner + "." + rpguuids + Keys.sex)
							? config.getBoolean("Horses." + owner + "." + rpguuids + Keys.sex)
							: Math.random() < 0.5;

					final RPGHorse rpgHorse = new RPGHorse(horsename, owner, c, s, v, gm, sswift, sagil, svit, swrath,
							uuid, jumpPow, sprintPow, sex);
					if (config.contains("Horses." + owner + "." + rpguuids + Keys.SPAWNED)) {
						rpgHorse.spawned = config.getBoolean("Horses." + owner + "." + rpguuids + Keys.SPAWNED);
					}
					if (config.contains("Horses." + owner + "." + rpguuids + Keys.isBaby)) {
						rpgHorse.isBaby = true;
					}
					if (config.contains("Horses." + owner + "." + rpguuids + Keys.AGE)) {
						rpgHorse.babyAge = config.getInt("Horses." + owner + "." + rpguuids + Keys.AGE);
					}

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
					rpgHorse.hasSaddle = config.getBoolean("Horses." + owner + "." + rpguuids + Keys.hassaddle);
					if (config.contains("Horses." + owner + "." + rpguuids + Keys.entityslastUUID.toString())) {
						final UUID uuid2 = UUID.fromString(
								config.getString("Horses." + owner + "." + rpguuids + Keys.entityslastUUID));
						final String sss = config.getString("Horses." + owner + "." + rpguuids + Keys.entityslastWorld);
						final World world2 = Bukkit.getWorld(sss);
						rpgHorse.isBanished = false;
						new BukkitRunnable() {
							World world = world2;

							@Override
							public void run() {
								if (rpgHorse.getHorse() != null) {
									cancel();
									return;
								}
								if (world == null) {
									world = Bukkit.getWorld(sss);
									if (world == null)
										return;
								}
								for (Entity e : world.getEntities()) {
									if (e.getUniqueId().equals(uuid2)) {
										rpgHorse.setHorse(e);
										break;
									}
								}
								if (rpgHorse.getHorse() != null) {
									HorseRPG.addSpawnedHorse(rpgHorse.getHorse(), rpgHorse);
									if (Bukkit.getPlayer(owner) != null)
										HorseRPG.pCurrentHorse.put(Bukkit.getPlayer(owner).getUniqueId(), rpgHorse);
									cancel();
								}
							}
						}.runTaskTimer(HorseRPG.instance, 2, 5 * 20);
					}
					//HorseRPG.instance.getLogger().info("Loading " + rpgHorse.name);
					return rpgHorse;
				}
			} catch (Error | Exception e45) {
			}
		}
		HorseRPG.instance.getLogger().warning("Could not load horse " + horseUUID);
		return null;
	}

	public enum Keys {
		entityslastUUID(".lastUUIDinstance"), entityslastWorld(".lastWorldname"), name(".name"), godmode(
				".godmode"), inventory(".inventory"), hassaddle(".hassaddle"), wrath(".wrath"), agility(
						".agility"), swiftness(".swiftness"), vitality(".vitality"), color(".color"), isdead(
								".isdead"), powerlevel(".powerlevel"), style(".style"), variant(".variant"), jump(
										".defaultJump"), sprint(".defaultSpeed"), sex(".sex"), hasChest(
												".hasChest"), isBaby(".isBaby"), AGE(".age"), SPAWNED(".spawned");
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
