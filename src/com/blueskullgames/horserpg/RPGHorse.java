package com.blueskullgames.horserpg;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.inventory.ItemStack;

import com.blueskullgames.horserpg.skills.Agility;
import com.blueskullgames.horserpg.skills.Swiftness;
import com.blueskullgames.horserpg.skills.Vitality;
import com.blueskullgames.horserpg.skills.Wrath;
import com.blueskullgames.horserpg.utils.NewHorseUtil;
import com.blueskullgames.horserpg.utils.NewHorseVarientUtil;
import com.blueskullgames.horserpg.utils.ReflectionUtil;
import com.blueskullgames.horserpg.utils.atributeutils.AtributeUtilAbstractHorse;
import com.blueskullgames.horserpg.utils.atributeutils.AtributeUtilHorse;
import com.blueskullgames.horserpg.utils.atributeutils.BaseAtributeUtil;

@SuppressWarnings("deprecation")
public class RPGHorse implements Comparable<RPGHorse> {

	public static String[] FIRST_NAMES = { "Big", "Boomer", "Bubba", "Bubble", "Candy", "Chicken", "Chubby", "Chunky",
			"Cinnamon", "Daisy", "Fluffy", "Lil'", "Little", "Muffin", "Peachy", "Pooky", "Rainbow", "Sir", "Snuggle",
			"Sprinkle", "Stinker", "Swag", "Tickle", "Tinkle", "Tootsie", "Twinkle"

			, "Gary", "Henry", "King", "Foal Ball", "Apple", "Carrot", "Potato", "Sugar", "Butter", "Silver" };
	public static String[] LAST_NAMES = { "Blossom", "Booty", "Bottoms", "Boy", "Bunches", "Buttercup", "Cucumber",
			"Cumquat", "Daddy", "Freckles", "Girl", "Horsey", "Hugs A Lot", "Marshmallow", "McFluffems", "McGiggles",
			"McNuggs", "McShowoff", "McSnuggles", "Noodles", "Pancake", "Poops A Lot", "Potato"

			, "McSwifty", "The Brave", "The Noble", "The First", "Johnson", "Grimes", "McSnuffles" };

	public boolean godmode, isBanished, isDead;
	public double distance;
	public int respawnTimer;

	public int powerLevel;
	public double price;

	public Swiftness swiftness;
	public Agility agility;
	public Vitality vitality;
	public Wrath wrath;

	public String name;
	public String owners_name;

	private Entity horse;
	public UUID holderOverUUID = null;

	public Color color;
	public Style style;
	public Variant variant;

	public boolean hasSaddle;

	public boolean hasChest = false;

	public UUID rpgUUID;

	public ItemStack[] inventory;

	public boolean isMale;
	public boolean allowBreeding = false;
	public boolean isBaby = false;
	public int babyAge = 0;

	public double generic_speed = 0.25f;
	public double generic_jump = 0.7f;

	public static double minSpeed = 0.1125;
	public static double maxSpeed = 0.33;
	public static double minJump = 0.4;
	public static double maxJump = 1.0;

//	public boolean spawned = false;

	public static double getRandomSpeed() {
		return ((maxSpeed - minSpeed) * Math.random()) + minSpeed;
	}

	public static double getRandomJump() {
		return ((maxJump - minJump) * Math.random()) + minJump;
	}

	public static BaseAtributeUtil attributeUtil = null;
	static {
		try {
			attributeUtil = new AtributeUtilAbstractHorse();
		} catch (Error | Exception e4) {
			attributeUtil = new AtributeUtilHorse();
		}
	}

	/**
	 * Makes a unique random name
	 * 
	 * @return a unique random name
	 */
	public static String randomName(Player p) {
		String name = "Glitch";
		top: while (true) {
			name = FIRST_NAMES[(int) (Math.random() * FIRST_NAMES.length)] + " "
					+ LAST_NAMES[(int) (Math.random() * LAST_NAMES.length)];
			if (!HorseRPG.ownedHorses.containsKey(p.getName()))
				return name;
			for (RPGHorse h : HorseRPG.ownedHorses.get(p.getName()))
				if (name.equalsIgnoreCase(h.name))
					continue top;
			break;
		}
		return name;
	}

	/**
	 * Gets a random color
	 * 
	 * @return a random color
	 */
	public static Color randomColor() {
		return Color.values()[(int) (Math.random() * Color.values().length)];
	}

	/**
	 * Gets a random style
	 * 
	 * @return a random style
	 */
	public static Style randomStyle() {
		return Style.values()[(int) (Math.random() * Style.values().length)];
	}

	/**
	 * Gets a random variant
	 * 
	 * @return a random variant
	 */
	public static Variant randomVariant() {
		return Variant.values()[(int) (Math.random() * Variant.values().length)];
	}

	/**
	 * Creates a new random horse
	 * 
	 * @param owner
	 *            is the new horse owner
	 */
	public RPGHorse(Player owner) {
		this(randomName(owner), owner.getName(), randomColor(), randomStyle(), Variant.HORSE, false, 0, 0, 0, 0, null,
				getRandomJump(), getRandomSpeed(), Math.random() > 0.5);
	}

	/**
	 * Makes a spawned horse an RPG Horse
	 * 
	 * @param owner
	 *            is the owner of the horse
	 * @param horse
	 *            is the spawned horse
	 */
	public RPGHorse(Player owner, Horse horse) {
		this((horse.getCustomName() != null && horse.getCustomName() != "" ? horse.getCustomName() : randomName(owner)),
				owner.getName(), horse.getColor(), horse.getStyle(), Variant.HORSE, false, 0, 0, 0, 0, null,
				attributeUtil.getJumpHeight(horse), attributeUtil.getSpeed(horse), Math.random() > 0.5);
		this.isBaby = !((Ageable) horse).isAdult();
		this.babyAge = ((Ageable) horse).getAge();
		this.holderOverUUID = horse.getUniqueId();
	//	spawned = true;
	}

	/**
	 * Makes a spawned horse an RPG Horse
	 * 
	 * USE IF HORSE VAR IS NOT ACCEPTABLE
	 * 
	 * @param owner
	 *            is the owner of the horse
	 * @param horse
	 *            is the spawned horse
	 */
	public RPGHorse(Player owner, Entity horse) {
		this((horse.getCustomName() != null && horse.getCustomName() != "" ? horse.getCustomName() : randomName(owner)),
				owner.getName(), Color.BROWN, Style.NONE, Variant.HORSE, false, 0, 0, 0, 0, null,
				attributeUtil.getJumpHeight(horse), attributeUtil.getSpeed(horse), Math.random() > 0.5);
		this.isBaby = !((Ageable) horse).isAdult();
		this.babyAge = ((Ageable) horse).getAge();
		this.holderOverUUID = horse.getUniqueId();
		//spawned = true;
	}

	/**
	 * Creates an RPG horse
	 * 
	 * @param name
	 *            is the name of the horse
	 * @param owner
	 *            is the player owner of the horse
	 * @param color
	 *            is the color of the horse
	 * @param style
	 *            is the style of the horse
	 * @param variant
	 *            is the variant of the horse
	 * @param godmode
	 *            sets whether horse is invisible or not
	 * @param enduranceXP
	 *            is the enduranceXP
	 * @param swiftnessXP
	 *            is the swiftnessXP
	 * @param agilityXP
	 *            is the agilityXP
	 * @param vitalityXP
	 *            is the vitalityXP
	 * @param wrathXP
	 *            is the wrathXP
	 */
	public RPGHorse(String name, String owner, Color color, Style style, Variant variant, boolean godmode,
			int swiftnessXP, int agilityXP, int vitalityXP, int wrathXP, UUID id, double jumpPow, double sprintPow,
			boolean isMale) {

		setName(name);
		this.owners_name = owner;
		this.color = color;
		this.style = style;
		this.variant = variant;
		this.godmode = godmode;

		isBanished = isDead = false;

		this.isMale = isMale;
		distance = 0;
		respawnTimer = 0;

		swiftness = new Swiftness(this, swiftnessXP);
		agility = new Agility(this, agilityXP);
		vitality = new Vitality(this, vitalityXP);
		wrath = new Wrath(this, wrathXP);

		powerLevel = swiftness.level + agility.level + vitality.level + wrath.level;
		price = 0;
		if (id == null) {
			rpgUUID = UUID.randomUUID();
		} else {
			rpgUUID = id;
		}
		this.generic_jump = jumpPow;
		this.generic_speed = sprintPow;
	}

	/**
	 * Sets a new name for the horse
	 * 
	 * @param newName
	 *            is the new name
	 */
	public void setName(String newName) {
		int add = 0;
		String testName = newName.replaceAll("\u00a7", "&");
		if (newName.length() == 0)
			return;
		if (HorseRPG.ownedHorses.containsKey(owners_name))
			whileloop: while (true) {
				for (RPGHorse h : HorseRPG.ownedHorses.get(owners_name)) {
					if (h.name.equals(testName) && h != this) {
						add++;
						testName = newName + "(" + add + ")";
						continue whileloop;
					}
				}
				break;
			}
		name = testName;
		if (horse != null)
			horse.setCustomName(ChatColor.translateAlternateColorCodes('&', name));
	}

	/**
	 * Sets a new color for the horse
	 * 
	 * @param newName
	 *            is the new color
	 */
	public void setColor(Color newColor) {
		color = newColor;
		if (horse != null)
			try {
				((Horse) horse).setColor(color);
			} catch (Exception e) {
				// TODO: Styles are only available for actual horses; Does not
				// apply to mulesm, llamas, ect.
			}
	}

	public void setHasChest(boolean hasChest) {
		this.hasChest = hasChest;
	}

	public boolean hasChest() {
		return hasChest;
	}

	/**
	 * Sets a new style for the horse
	 * 
	 * @param newName
	 *            is the new style
	 */
	public void setStyle(Style newStyle) {
		style = newStyle;
		if (horse != null)
			try {
				((Horse) horse).setStyle(style);
			} catch (Exception e) {
				// TODO: Styles are only available for actual horses; Does not
				// apply to mulesm, llamas, ect.
			}
	}

	public Entity getHorse() {
		if (horse == null)
			return null;
		if (horse.isValid())
			return horse;
		for (Entity e : horse.getWorld().getEntities()) {
			if (e.getUniqueId().equals(horse.getUniqueId()) || e.getUniqueId().equals(holderOverUUID)) {
				holderOverUUID = e.getUniqueId();
				HorseRPG.addSpawnedHorse(e, this);
				return horse = e;
			}
		}
		return horse;
	}

	public void setHorse(Entity horse) {
		HorseRPG.updateHorseInstance(horse, this.horse, this);
		this.horse = horse;
		this.holderOverUUID = horse.getUniqueId();
		//spawned = true;
	}

	/**
	 * Sets a new variant for the horse
	 * 
	 * @param newName
	 *            is the new variant
	 */
	public void setVariant(Variant newVariant) {
		variant = newVariant;
		if (horse != null) {
			if (ReflectionUtil.isVersionHigherThan(1, 10)) {
				try {
					if (!isBanished && !isDead) {
						banish(false);
						isBanished = false;
						summon(Bukkit.getPlayer(this.owners_name));
					}
					return;
				} catch (Exception e) {
				}
			}
			Entity temp = horse;
			this.horse = this.horse.getWorld().spawnEntity(horse.getLocation(),
					NewHorseVarientUtil.getHorseByType(variant));
			if (!ReflectionUtil.isVersionHigherThan(1, 10)) {
				((Horse) this.horse).setVariant(newVariant);
			}
			this.holderOverUUID = horse.getUniqueId();
			//spawned = true;
			temp.remove();
			HorseRPG.updateHorseInstance(this.horse, temp, this);
		}
	}

	/**
	 * Ticks the horse's death timer
	 */
	public void tick() {
		respawnTimer--;
		if (respawnTimer <= 0) {
			if ((isBanished && HorseRPG.banishTimer >= 10) || (isDead && HorseRPG.deathTimer >= 10)) {
				for (Player p : HorseRPG.instance.getServer().getOnlinePlayers()) {
					if (p.getName().equalsIgnoreCase(owners_name)) {
						HorseRPG.msg(p, "&a**" + name + " is fully recharged**");
						break;
					}
				}
			}
			isBanished = false;
			isDead = false;
		}
	}

	/**
	 * Adds distance to the horse's odometer
	 * 
	 * @param dist
	 *            is the amount of distance to add
	 */
	public void travel(double dist) {
		distance += dist;
		if (distance > 100) {
			distance -= 100;
			try {
				swiftness.addXP(1, (Player) horse.getPassengers().get(0));
			} catch (Exception | Error e) {
				swiftness.addXP(1, (Player) horse.getPassenger());
			}
		}
	}

	/**
	 * Summons a player's horse
	 * 
	 * @param p
	 *            is the horse owner
	 */
	public Entity summon(Player p) {
		return summon(p, p.getLocation());
	}

	/**
	 * Summons a player's horse
	 * 
	 * @param p
	 *            is the horse owner
	 */
	public Entity summon(Player p, Location loc) {
		try {
			horse = loc.getWorld().spawnEntity(loc, NewHorseVarientUtil.getHorseByType(variant));
		} catch (Exception | Error e) {
			horse = loc.getWorld().spawnEntity(loc, EntityType.HORSE);
			setVariant(variant);
		}
		try {
			((Horse) horse).setColor(color);
			((Horse) horse).setStyle(style);
		} catch (Exception e) {
			// TODO: Styles are only available for actual horses; Does not apply
			// to mules, llamas, ect.
		}

		try {
			if (hasChest) {
				if (horse instanceof org.bukkit.entity.Donkey) {
					((org.bukkit.entity.Donkey) horse).setCarryingChest(true);
				}
				if (horse instanceof org.bukkit.entity.Mule) {
					((org.bukkit.entity.Mule) horse).setCarryingChest(true);
				}
			}
		} catch (Exception e) {
		}

		if (!isBaby) {
			((Ageable) horse).setAdult();
		} else {
			((Ageable) horse).setBaby();
			((Ageable) horse).setAgeLock(false);
			((Ageable) horse).setAge(babyAge);
		}

		holderOverUUID = horse.getUniqueId();
		HorseRPG.updateHorseInstance(horse, this.horse, this);

		((Tameable) horse).setTamed(true);
		horse.setCustomName(ChatColor.translateAlternateColorCodes('&', name));
		if (inventory == null) {
			inventory = new ItemStack[hasChest ? 16 : 1];
			if (hasSaddle)
				inventory[0] = new ItemStack(Material.SADDLE);
		}
		if (horse instanceof org.bukkit.entity.ChestedHorse) {
			if (hasChest)
				((org.bukkit.entity.ChestedHorse) horse).setCarryingChest(true);
			((org.bukkit.entity.ChestedHorse) horse).getInventory().setContents(inventory);
		} else {
			try {
				// ((HorseInventory) ((AbstractHorse) horse).getInventory())
				// .setSaddle(new ItemStack(Material.SADDLE));
				((AbstractHorse) horse).getInventory().setContents(inventory);
			} catch (Exception | Error e) {
				try {
					// ((HorseInventory) ((Horse) horse).getInventory())
					// .setSaddle(new ItemStack(Material.SADDLE));
					((Horse) horse).getInventory().setContents(inventory);
				} catch (Exception | Error e2) {
					if (p != null) {
						HorseRPG.msg(p,
								" Something went wrong with the horse's inventory. Contact the server owner and tell them to report the error message in the console");
						HorseRPG.msg(p,
								"The contents of the horse's inventory has been given to you (or dropped on the floor if your inventory is full)");
					}
					e.printStackTrace();
					e2.printStackTrace();
					for (ItemStack is : inventory) {
						if (is != null) {
							if (p == null || p.getInventory().firstEmpty() == -1) {
								loc.getWorld().dropItem(loc, is);
							} else {
								p.getInventory().addItem(is);
							}
						}
					}
				}
			}
		}

		isBanished = false;
		//spawned = true;
		try {
			((LivingEntity) horse).getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(44 + vitality.healthBonus);
			((Damageable) horse)
					.setHealth(((LivingEntity) horse).getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
		} catch (Exception | Error e) {
			((Damageable) horse).setMaxHealth(44 + vitality.healthBonus);
			((Damageable) horse).setHealth(((Damageable) horse).getMaxHealth());
		}

		HorseRPG.addSpawnedHorse(horse, this);

		this.vitality.update();
		this.agility.update();
		this.wrath.update();
		this.swiftness.update();

		if (generic_jump > 0)
			attributeUtil.setJumpHeight(horse, generic_jump);
		if (generic_speed > 0)
			attributeUtil.setSpeed(horse, generic_speed);
		return horse;
	}

	/**
	 * Banishes the player's horse
	 */
	public void banish(boolean timer) {
		if (horse != null) {
			if (!horse.getLocation().getChunk().isLoaded()) {
				// This should fix issues if you cannot remove entities from unloaded worlds.
				horse.getWorld().loadChunk(horse.getLocation().getChunk());
			}

			try {
				NewHorseUtil.useNewHorses();
				hasSaddle = (((AbstractHorse) horse).getInventory().getItem(0) != null
						&& (((AbstractHorse) horse).getInventory().getItem(0).getType() == Material.SADDLE));
				inventory = ((AbstractHorse) horse).getInventory().getContents();
			} catch (Exception | Error e) {
				hasSaddle = (((Horse) horse).getInventory().getItem(0) != null
						&& (((Horse) horse).getInventory().getItem(0).getType() == Material.SADDLE));
				inventory = ((Horse) horse).getInventory().getContents();
			}
			HorseRPG.removeHorseInstance(horse);
			try {
				if (horse instanceof org.bukkit.entity.Donkey) {
					setHasChest(((org.bukkit.entity.Donkey) horse).isCarryingChest());
				}
				if (horse instanceof org.bukkit.entity.Mule) {
					setHasChest(((org.bukkit.entity.Mule) horse).isCarryingChest());
				}
			} catch (Exception e) {
			}

			isBaby = !((Ageable) horse).isAdult();
			babyAge = ((Ageable) horse).getAge();
			horse.eject();
			horse.remove();
			HorseRPG.removeHorseInstance(horse);
			horse = null;
			holderOverUUID = null;
		}

		HorseRPG.h_config.saveHorse(this, true);

		isBanished = true;
		//spawned = false;
		if (timer)
			respawnTimer = HorseRPG.banishTimer;
	}

	/**
	 * Banishes the player's horse
	 */
	public void banish() {
		this.banish(true);
	}

	@Override
	public int compareTo(RPGHorse h) {
		int dif = h.powerLevel - powerLevel;
		return dif != 0 ? dif : name.compareToIgnoreCase(h.name);
	}

	@Override
	public String toString() {
		return name;
	}

}
