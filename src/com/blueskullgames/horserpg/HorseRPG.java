package com.blueskullgames.horserpg;

import java.io.File;
import java.sql.*;
import java.util.*;
import java.util.Map.Entry;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Donkey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.*;
import org.bukkit.entity.Mule;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.*;

import com.blueskullgames.horserpg.configs.HorseConfigHandler;
import com.blueskullgames.horserpg.configs.MessagesConfigHandler;
import com.blueskullgames.horserpg.listeners.*;
import com.blueskullgames.horserpg.tasks.*;
import com.blueskullgames.horserpg.utils.*;

@SuppressWarnings("deprecation")
public class HorseRPG extends JavaPlugin {

	public static final String H_HELP = "mcmmohorses.help";
	public static final String H_ME = "mcmmohorses.me";
	public static final String H_STATS = "mcmmohorses.stats";
	public static final String H_SKILL = "mcmmohorses.skills.info";
	public static final String H_CLAIM = "mcmmohorses.claim";
	public static final String H_BUY = "mcmmohorses.buy";
	public static final String H_SELL = "mcmmohorses.sell";
	public static final String H_SHOP = "mcmmohorses.shop";
	public static final String H_SUMMON = "mcmmohorses.summon";
	public static final String H_BANISH = "mcmmohorses.banish";
	public static final String H_DELETE = "mcmmohorses.kill";
	public static final String H_PROTECT = "mcmmohorses.admin.protect";
	public static final String H_UNPROTECT = "mcmmohorses.admin.unprotect";
	public static final String H_BANISH_FOR_PLAYER = "mcmmohorses.admin.banishforplayer";
	public static final String H_ADDXP = "mcmmohorses.admin.addxp";
	public static final String H_DELXP = "mcmmohorses.admin.delxp";
	public static final String H_SET = "mcmmohorses.admin.set";
	public static final String H_SET_NAME = "mcmmohorses.admin.set.name";
	public static final String H_SET_SPEED = "mcmmohorses.admin.set.speed";
	public static final String H_SET_JUMP = "mcmmohorses.admin.set.jump";
	public static final String H_SET_COLOR = "mcmmohorses.admin.set.color";
	public static final String H_SET_STYLE = "mcmmohorses.admin.set.style";
	public static final String H_SET_TYPE = "mcmmohorses.admin.set.type";
	public static final String H_CREATE = "mcmmohorses.admin.breed";
	public static final String H_SAVE = "mcmmohorses.admin.save";
	public static final String H_DB = "mcmmohorses.admin.db";
	public static final String H_RELOAD = "mcmmohorses.admin.reload";
	public static final String H_LEADERBOARD = "mcmmohorses.leaderboard";
	public static final String H_BREED = "mcmmohorses.breed";

	public static String BANISH_HORSE = "&aPlease banish your current horse first.";
	public static String HORSE_BRED = "&aHorse successfully bred!";
	public static String HORSES_SAVED = "&aAll horses saved to database.";
	public static String INVALID_COLOR = "&aPlease specify a valid horse color.";
	public static String INVALID_COMMAND = "&aInvalid command.";
	public static String INVALID_NAME = "&aPlease specify a horse name.";
	public static String INVALID_PAGE = "&aInvalid page number.";
	public static String INVALID_STYLE = "&aPlease specify a valid horse style.";
	public static String INVALID_VARIANT = "&aPlease specify a valid horse variant.";
	public static String MAX_HORSES = "&aYou already claimed the max number of horses!";
	public static String NO_ECONOMY = "&aEconomy features are disabled!";
	public static String NO_HORSES = "&aYou don't have a horse yet!";
	public static String NO_MORE_HORSES = "&aYou don't have any more horses!";
	public static String NO_HORSE_SUMMONED = "&aYou don't have a horse summoned!";
	public static String NO_PERMISSION = "&aYou are not allowed to use this command!";
	public static String NO_PERMISSION_HORSE = "&aYou are not allowed to ride this horse!";
	public static String PAGE_DOES_NOT_EXIST = "&aPage does not exist.";
	public static String PLAYER_ONLY = "&aThis command can only be run by a &bplayer.";

	public static String NO_WILD_HORSES = "&aYou cannot interact with wild horses.";
	public static String NO_BREED_HORSES = "&aYou cannot breed these two horses: ";
	public static String ALLOW_BREEDING = "&aYou have set breeding for %0% to %1% ";

	public static String NOT_ENOUGH_MONEY = "&aYou do not have enough money to buy this horse!";
	public static String BOUGHT_HORSE = "&aYou have just bought the horse ";
	public static String BOUGHT_HORSE_EXP1 = "&aTo summon your new horse, use the command /h summon <Your Horse>";

	// new messages
	public static String HORSE_NEEDS_TAME = "&aThis horse needs to be tamed and saddled first!";
	public static String RenameHorse = "&aHorse %oldname% has been changed to %newname%";
	public static String ChangeSpeedHorse = "&aHorse %oldname% has had their speed change to %speed%";
	public static String ChangeJumpHorse = "&aHorse %oldname% has had their jump height change to %jump%";
	public static String PURCHASED_FOR_CLAIM = "&aHorse purchased for &e%amount%";
	public static String NEXT_HORSE_COST = "&aNext horse costs: &e%amount%";
	public static String CLAIM_NAME = "&b %name% &a claimed!";
	public static String SKILL_INCREASED_BY = "&e %name% skill increased by %difference%. Total (%level%)";
	public static String NOTENOUGHMONEY = "&aYou don't have enough money!";
	public static String NEED_TIME_TO_RECHARGE = "&e%name%&c needs some time to recharge.";
	public static String SKILL_REFRESH = "&a**Your &e%ability%&a is refreshed*";

	public static String TOO_TIRED = "&c%name% is too tired to use that ability. &e(%cd%s)";

	public static String INVALID_PRICE = "&aInvalid horse price.";
	public static String HORSE_DOES_NOT_EXIST = "&b%name%&a does not exist.";
	public static String OFFER_DECLINED = "&aOffer declined.";
	public static String MUST_RIDE_HORSE_TO_CLAIM = "&aYou must be riding your horse to claim it!";
	public static String NO_OFFER_PENDING = "&aYou don't have an offer pending.";
	// public static String CLOSERTHAN100 = "&a You need to be within 100 blocks of
	// your horse to banish them!";

	public static String BANISH_LOADED_CHUNK = "&a The chunk your horse is in must be loaded before bansishement!";
	public static String BANISH_OTHER_NOT_ONLINE = "&a That player does not exist!";

	public static String FOALBREED = "&a The foal &e%name%&a has been born.*";

	public static BukkitTask saveTask, cooldownTask;
	public static ConfigAccessor config;
	public static Economy econ;
	public static HashMap<Entity, RPGHorse> hSpawnedHorses;
	public static HashMap<UUID, RPGHorse> pCurrentHorse;
	public static HashMap<UUID, RPGHorse> offers;
	public static HashMap<String, TreeSet<RPGHorse>> ownedHorses;
	public static HashSet<RPGHorse> horses;
	public static HorseRPG instance;
	public static LinkedHashMap<String, String> help;
	public static LinkedHashMap<String, String> setHelp;

	public static MessagesConfigHandler messages;

	public static HorseConfigHandler h_config;
	public static List<String> playersWithScoreboards = new ArrayList<>();
	public static boolean nobanishment = false;
	public static boolean banishondisable = true;
	public static boolean banishonquit = true;
	public static boolean disableBreeding = false;
	public static boolean claimBreeding = true;

	public static boolean disableTamedHorses = false;

	public static int savetype = 2;
	public static boolean logWhenSave = true;

	public static boolean freeHorse;
	public static boolean permanentDeath;
	public static int deathTimer;
	public static int banishTimer;
	public static int sprintCooldown;
	public static int infuriateCooldown;

	public static HashMap<String, Integer> groups;

	public static boolean useEconomy;
	public static int claimCost;
	public static int claimCostMultiplier;
	public static int summonCost;
	public static int banishCost;

	public static boolean forceClaimOnTaim = false;
	public static boolean forcePermToRideUnclaimed = false;

	public static boolean DisableshowStatsInChat = false;
	public static boolean DisableshowStatsinInScoreboard = false;

	public static HashMap<Variant, Double> costForHorse = new HashMap<>();
	public static ShopManager shop;// = new ShopManager();

	/**
	 * Messages a sender
	 * 
	 * @param sender
	 *            is the sender to message
	 * @param message
	 *            is the message to send
	 */
	public static void msg(CommandSender sender, String message, String... args) {
		String tempmess = message;
		for (int i = 0; i < args.length; i++) {
			tempmess = tempmess.replaceAll("%" + i + "%", args[i]);
		}
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', tempmess));
	}

	/**
	 * Checks if the sender can execute the command
	 * 
	 * @param sender
	 *            is the sender to check
	 * @param permission
	 *            is the permission node required
	 * @param playerOnly
	 *            is true if the command is player only
	 * @return true if the sender is not allowed to execute the command
	 */
	public static boolean notAllowed(CommandSender sender, String permission, boolean playerOnly) {
		if (!sender.hasPermission(permission)) {
			msg(sender, NO_PERMISSION);
			return true;
		}
		if (playerOnly && !(sender instanceof Player)) {
			msg(sender, PLAYER_ONLY);
			return true;
		}
		return false;
	}

	public static boolean hasHorses(Player p) {
		if (ownedHorses.containsKey(p.getName()) && ownedHorses.get(p.getName()).size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * Returns the sender's horse THAT IS NOT CURRENTLY SPAWNED
	 * 
	 * @param p
	 * @param args
	 * @param offset
	 * @return
	 */
	public static RPGHorse getHorseNotSpawned(Player p, String[] args, int offset) {
		if (args.length <= offset) {
			if (ownedHorses.containsKey(p.getName()) && ownedHorses.get(p.getName()).size() > 0) {
				for (RPGHorse h : ownedHorses.get(p.getName())) {
					if (!hSpawnedHorses.containsKey(h.getHorse())) {
						return h;
					}
				}
			}
			return null;
		}

		String hName = args[offset];
		for (int i = offset + 1; i < args.length; i++)
			hName += " " + args[i];
		hName = hName.replaceAll("_", " ");

		if (ownedHorses.containsKey(p.getName()))
			for (RPGHorse h : ownedHorses.get(p.getName()))
				if (h.name.equalsIgnoreCase(hName))
					return h;

		msg(p, HORSE_DOES_NOT_EXIST.replace("%name%", hName));
		return null;
	}

	/**
	 * Returns the sender's horse THAT IS SPAWNED
	 * 
	 * @param p
	 * @param args
	 * @param offset
	 * @return
	 */
	public static RPGHorse getHorseSpawned(Player p, String[] args, int offset) {
		if (args.length <= offset) {
			if (ownedHorses.containsKey(p.getName()) && ownedHorses.get(p.getName()).size() > 0) {
				for (RPGHorse h : ownedHorses.get(p.getName())) {
					if (hSpawnedHorses.containsKey(h.getHorse())) {
						return h;
					}
				}
			}
			return null;
		}

		String hName = args[offset];
		for (int i = offset + 1; i < args.length; i++)
			hName += " " + args[i];
		hName = hName.replaceAll("_", " ");

		if (ownedHorses.containsKey(p.getName()))
			for (RPGHorse h : ownedHorses.get(p.getName()))
				if (hSpawnedHorses.containsKey(h.getHorse()))
					if (h.name.equalsIgnoreCase(hName))
						return h;
		msg(p, HORSE_DOES_NOT_EXIST.replace("%name%", hName));
		return null;
	}

	/**
	 * Returns the sender's associated horse
	 * 
	 * @param p
	 *            is the command sender
	 * @param args
	 *            are the command arguments
	 * @param offset
	 *            is the argument index offset of the horse name
	 */
	public static RPGHorse currentHorse(Player p, String[] args, int offset) {
		if (args.length <= offset) {
			if (pCurrentHorse.containsKey(p.getUniqueId())) {
				return pCurrentHorse.get(p.getUniqueId());
			} else if (ownedHorses.containsKey(p.getName()) && ownedHorses.get(p.getName()).size() > 0)
				return ownedHorses.get(p.getName()).first();

			return null;
		}

		String hName = args[offset];
		for (int i = offset + 1; i < args.length; i++)
			hName += " " + args[i];
		hName = hName.replaceAll("_", " ");
		if (ownedHorses.containsKey(p.getName()))
			for (RPGHorse h : ownedHorses.get(p.getName()))
				if (h.name.equalsIgnoreCase(hName))
					return h;

		msg(p, HORSE_DOES_NOT_EXIST.replace("%name%", hName));
		return null;
	}

	/**
	 * Gets the max horses a sender can own
	 * 
	 * @param sender
	 *            is the sender to check
	 * @return the max horses the sender can own
	 */
	public static int maxHorses(CommandSender sender) {
		int max = 0;
		for (Entry<String, Integer> group : groups.entrySet()) {
			if (sender.hasPermission(group.getKey())) {
				if (group.getValue() == -1)
					return -1;
				max = group.getValue();
			}
		}
		return max;
	}

	/**
	 * Displays the help message
	 * 
	 * @param sender
	 *            is the sender to message
	 * @param args
	 *            are the command arguments
	 */
	public static void showHelp(CommandSender sender, String[] args) {
		if (notAllowed(sender, H_HELP, false))
			return;
		int page = 1;
		if (args.length >= 2) {
			try {
				page = Integer.parseInt(args[1]);
			} catch (NumberFormatException ex) {
				msg(sender, INVALID_PAGE);
				return;
			}
		}
		ArrayList<String> customHelp = new ArrayList<String>();
		for (Entry<String, String> entry : help.entrySet())
			if (sender.hasPermission(entry.getKey()))
				customHelp.add(entry.getValue());

		int maxPages = customHelp.size() / 8 + (customHelp.size() % 8 > 0 ? 1 : 0);
		if (page <= 0 || maxPages < page) {
			msg(sender, PAGE_DOES_NOT_EXIST);
			return;
		}

		page *= 8;
		for (int i = page - 8; i < page; i++) {
			if (i % 8 == 0) {
				sender.sendMessage("");
				msg(sender, "&aHelp Page " + (page / 8) + ":");
			}
			if (i < customHelp.size())
				msg(sender, customHelp.get(i));
		}
	}

	/**
	 * Displays the player's stats
	 * 
	 * @param sender
	 *            is the sender to message
	 */
	public static void showMe(CommandSender sender) {
		if (notAllowed(sender, H_ME, true))
			return;
		Player p = (Player) sender;

		TreeSet<RPGHorse> myHorses = ownedHorses.get(p.getName());
		if (myHorses == null || myHorses.size() <= 0) {
			msg(sender, NO_HORSES);
			return;
		}

		String horseNames = myHorses.toString();
		msg(sender, "&aOwned: &7" + myHorses.size() + " / " + (maxHorses(p) == -1 ? "Unlimited" : maxHorses(p)));
		msg(sender, "&aHorses:&7 " + horseNames.substring(1, horseNames.length() - 1));
		if (!playersWithScoreboards.contains(p.getName())) {
			ScoreboardManager manager = Bukkit.getScoreboardManager();
			Scoreboard board = manager.getNewScoreboard();
			Objective obj = board.registerNewObjective("horsemestats", "dummy");
			obj.setDisplayName(ChatColor.GOLD + sender.getName() + "'s stats");
			obj.setDisplaySlot(DisplaySlot.SIDEBAR);
			int ownedHor = myHorses.size();
			int showedHorses = Math.min(ownedHor, 10);
			try {
				obj.getScore(Bukkit.getOfflinePlayer(ChatColor.translateAlternateColorCodes('&',
						"&aOwned: &7" + myHorses.size() + " / " + (maxHorses(p) == -1 ? "Unlimited" : maxHorses(p)))))
						.setScore(showedHorses + 2);
				obj.getScore(Bukkit.getOfflinePlayer(ChatColor.translateAlternateColorCodes('&', "&aHorses:")))
						.setScore(showedHorses + 1);
				for (int i = 0; i < showedHorses; i++) {
					obj.getScore(Bukkit.getOfflinePlayer(
							ChatColor.translateAlternateColorCodes('&', ((RPGHorse) myHorses.toArray()[i]).name)))
							.setScore(i);
				}
				if (showedHorses < ownedHor)
					obj.getScore(Bukkit.getOfflinePlayer(ChatColor.translateAlternateColorCodes('&',
							"Plus " + (ownedHor - showedHorses) + " more..."))).setScore(-1);
			} catch (Error | Exception e54) {
				obj.getScore(ChatColor.translateAlternateColorCodes('&',
						"&aOwned: &7" + myHorses.size() + " / " + (maxHorses(p) == -1 ? "Unlimited" : maxHorses(p))))
						.setScore(showedHorses + 3);
				obj.getScore(ChatColor.translateAlternateColorCodes('&',
						"&aMax-Owned: &7" + (maxHorses(p) == -1 ? "Unlimited" : maxHorses(p))))
						.setScore(showedHorses + 2);
				obj.getScore((ChatColor.translateAlternateColorCodes('&', "&aHorses:"))).setScore(showedHorses + 1);
				for (int i = 0; i < showedHorses; i++) {
					obj.getScore((ChatColor.translateAlternateColorCodes('&', ((RPGHorse) myHorses.toArray()[i]).name)))
							.setScore(i);
				}
				if (showedHorses < ownedHor)
					obj.getScore((ChatColor.translateAlternateColorCodes('&',
							"Plus " + (ownedHor - showedHorses) + " more"))).setScore(-1);
			}
			Scoreboard oldsb = p.getScoreboard();
			p.setScoreboard(board);
			playersWithScoreboards.add(p.getName());

			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(instance, new ScoreboardTask(p, oldsb), 200);
		}

	}

	/**
	 * Displays the player's horse stats
	 * 
	 * @param sender
	 *            is the sender to update
	 */
	public static void showHorse(CommandSender sender, String[] args) {
		if (notAllowed(sender, H_STATS, true))
			return;

		Player p = (Player) sender;
		if (playersWithScoreboards.contains(p.getName())) {
			p.sendMessage(ChatColor.GREEN + "Already showing the horse's stats.");
			return;
		}

		RPGHorse h = currentHorse(p, args, 1);
		if (h == null) {
			msg(p, NO_HORSES);
			return;
		}

		if (!DisableshowStatsInChat) {
			p.sendMessage(ChatColor.YELLOW + "\"" + h.name + "\" Stats");
			p.sendMessage((ChatColor.GREEN + "Swiftness") + ChatColor.WHITE + "  " + h.swiftness.level);
			p.sendMessage((ChatColor.DARK_GRAY + "Sprint: ") + ChatColor.GRAY + "= " + ((int) (h.swiftness.amplitude))
					+ (ChatColor.DARK_GRAY + "Time: ") + ChatColor.GRAY + "= " + ((int) (h.swiftness.duration / 20))
					+ "s");
			p.sendMessage((ChatColor.GREEN + "Agility") + ChatColor.WHITE + "  " + h.agility.level);
			p.sendMessage((ChatColor.DARK_GRAY + "Dodge:") + ChatColor.GRAY + "= "
					+ ((int) (h.agility.dodgeChance * 100)) + "%" + (ChatColor.DARK_GRAY + "Roll:") + ChatColor.GRAY
					+ "= " + ((int) (h.agility.rollChance * 100)) + "%");
			p.sendMessage((ChatColor.GREEN + "Vitality") + ChatColor.WHITE + "  " + h.vitality.level);
			// TODO: Check if correct: 44 (22 hears) is the base health for all horses
			p.sendMessage((ChatColor.DARK_GRAY + "Base") + ChatColor.GRAY + "= " + ((44 - h.vitality.healthBonus)) / 2
					+ (ChatColor.DARK_GRAY + "  Bonus") + ChatColor.GRAY + "= " + ((int) (h.vitality.healthBonus / 2)));
			p.sendMessage((ChatColor.GREEN + "Wrath") + ChatColor.WHITE + "  " + h.wrath.level);
			p.sendMessage((ChatColor.DARK_GRAY + "Infuriate: ") + ChatColor.GRAY + "= "
					+ ((int) (h.wrath.infuriateChance * 100)) + "%");
			p.sendMessage((ChatColor.GOLD + "Power Level") + ChatColor.WHITE + "  " + h.powerLevel);
			p.sendMessage((ChatColor.GOLD + "Sex") + ChatColor.WHITE + "  " + (h.isMale ? "Male" : "Female")
					+ (h.variant == Variant.DONKEY || h.variant == Variant.MULE ? "(Gelding)" : ""));
			p.sendMessage((ChatColor.GREEN + "Base-Speed") + ChatColor.WHITE + "  " + h.generic_speed);
			p.sendMessage((ChatColor.GREEN + "Base-Jump") + ChatColor.WHITE + "  " + h.generic_jump);
		}
		if (!DisableshowStatsinInScoreboard) {

			ScoreboardManager manager = Bukkit.getScoreboardManager();
			Scoreboard board = manager.getNewScoreboard();

			Objective obj = board.registerNewObjective("horsestats", "dummy");
			obj.setDisplaySlot(DisplaySlot.SIDEBAR);
			obj.setDisplayName(ChatColor.YELLOW + "\"" + h.name + "\" Stats");
			try {
				obj.getScore((ChatColor.GREEN + "Swiftness") + ChatColor.WHITE + "  " + h.swiftness.level).setScore(8);
				obj.getScore((ChatColor.DARK_GRAY + "Sprint: ") + ChatColor.GRAY + "= "
						+ ((int) (h.swiftness.amplitude)) + (ChatColor.DARK_GRAY + "Time: ") + ChatColor.GRAY + "= "
						+ ((int) (h.swiftness.duration / 20)) + "s").setScore(7);
				obj.getScore((ChatColor.GREEN + "Agility") + ChatColor.WHITE + "  " + h.agility.level).setScore(6);
				obj.getScore((ChatColor.DARK_GRAY + "Dodge:") + ChatColor.GRAY + "= "
						+ ((int) (h.agility.dodgeChance * 100)) + "%" + (ChatColor.DARK_GRAY + "Roll:") + ChatColor.GRAY
						+ "= " + ((int) (h.agility.rollChance * 100)) + "%").setScore(5);
				obj.getScore((ChatColor.GREEN + "Vitality") + ChatColor.WHITE + "  " + h.vitality.level).setScore(4);
				// TODO: Check if correct: 44 (22 hears) is the base health for all horses
				obj.getScore((ChatColor.DARK_GRAY + "Base") + ChatColor.GRAY + "= "
						+ ((44 - h.vitality.healthBonus)) / 2 + (ChatColor.DARK_GRAY + "  Bonus") + ChatColor.GRAY
						+ "= " + ((int) (h.vitality.healthBonus / 2))).setScore(3);
				obj.getScore((ChatColor.GREEN + "Wrath") + ChatColor.WHITE + "  " + h.wrath.level).setScore(2);
				obj.getScore((ChatColor.DARK_GRAY + "Infuriate: ") + ChatColor.GRAY + "= "
						+ ((int) (h.wrath.infuriateChance * 100)) + "%").setScore(1);
				obj.getScore((ChatColor.GOLD + "Power Level") + ChatColor.WHITE + "  " + h.powerLevel).setScore(0);

				obj.getScore((ChatColor.GOLD + "Sex") + ChatColor.WHITE + "  " + (h.isMale ? "Male" : "Female")
						+ (h.variant == Variant.DONKEY || h.variant == Variant.MULE ? "(Gelding)" : "")).setScore(-1);
				obj.getScore((ChatColor.GREEN + "Base-Speed") + ChatColor.WHITE + "  " + h.generic_speed).setScore(-2);
				obj.getScore((ChatColor.GREEN + "Base-Jump") + ChatColor.WHITE + "  " + h.generic_jump).setScore(-3);

			} catch (Exception e) {
				obj.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Swiftness")).setScore(h.swiftness.level);
				obj.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Agility")).setScore(h.agility.level);
				obj.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Vitality")).setScore(h.vitality.level);
				obj.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Wrath")).setScore(h.wrath.level);
				obj.getScore(Bukkit.getOfflinePlayer(ChatColor.GOLD + "Power Level")).setScore(h.powerLevel);

				obj.getScore(Bukkit.getOfflinePlayer((ChatColor.DARK_GRAY + "Sprint: ") + ChatColor.GRAY + "= "
						+ ((int) (h.swiftness.amplitude)) + (ChatColor.DARK_GRAY + "Time: ") + ChatColor.GRAY + "= "
						+ ((int) (h.swiftness.duration / 20)) + "s")).setScore(7);

				obj.getScore(Bukkit.getOfflinePlayer((ChatColor.DARK_GRAY + "Dodge:") + ChatColor.GRAY + "= "
						+ ((int) (h.agility.dodgeChance * 100)) + "%" + (ChatColor.DARK_GRAY + "Roll:") + ChatColor.GRAY
						+ "= " + ((int) (h.agility.rollChance * 100)) + "%")).setScore(5);
				// TODO: Check if correct: 44 (22 hears) is the base health for all horses
				obj.getScore(Bukkit.getOfflinePlayer((ChatColor.DARK_GRAY + "Base") + ChatColor.GRAY + "= "
						+ (((44 - h.vitality.healthBonus) / 2)) + ChatColor.DARK_GRAY + "  Bonus" + ChatColor.GRAY
						+ "= " + ((int) (h.vitality.healthBonus / 2)))).setScore(3);

				obj.getScore(Bukkit.getOfflinePlayer((ChatColor.DARK_GRAY + "Infuriate: ") + ChatColor.GRAY + "= "
						+ ((int) (h.wrath.infuriateChance * 100)) + "%")).setScore(1);
				obj.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Base-Speed")).setScore((int) h.generic_speed);
				obj.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Base-Jump")).setScore((int) h.generic_jump);

			}

			Scoreboard oldsb = p.getScoreboard();
			p.setScoreboard(board);
			playersWithScoreboards.add(p.getName());

			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(instance, new ScoreboardTask(p, oldsb), 200);
		}
	}

	/**
	 * Displays the skill statistics message
	 * 
	 * @param sender
	 *            is the sender to message
	 * @param skill
	 *            is the skill to show
	 * @param args
	 *            are the command arguments
	 */
	public static void showSkill(CommandSender sender, String skill, String[] args) {
		if (notAllowed(sender, H_SKILL, true))
			return;
		Player p = (Player) sender;
		RPGHorse h = currentHorse(p, args, 1);
		if (h == null)
			return;

		switch (skill) {
		case "swiftness":
			h.swiftness.stats(sender);
			break;
		case "agility":
			h.agility.stats(sender);
			break;
		case "vitality":
			h.vitality.stats(sender);
			break;
		case "wrath":
			h.wrath.stats(sender);
			break;
		}
	}

	/**
	 * Attempts to claim a horse
	 * 
	 * @param sender
	 *            is the sender to message
	 */
	public static void claimHorse(CommandSender sender) {
		if (notAllowed(sender, H_CLAIM, true))
			return;
		Player p = (Player) sender;

		int senderHorses = ownedHorses.containsKey(p.getName()) ? ownedHorses.get(p.getName()).size() : 0;

		if (maxHorses(p) != -1 && senderHorses >= maxHorses(p)) {
			msg(p, MAX_HORSES);
			return;
		}
		/*
		 * if (pCurrentHorse.containsKey(p)) { msg(p, BANISH_HORSE); return; }
		 */

		boolean aHorse = false;
		try {
			aHorse = p.getVehicle() instanceof org.bukkit.entity.AbstractHorse;
		} catch (Error | Exception e) {
			aHorse = p.getVehicle() instanceof Horse;
		}

		if (!aHorse) {
			msg(p, MUST_RIDE_HORSE_TO_CLAIM);
			return;
		}

		Entity horse = p.getVehicle();

		for (RPGHorse rpg : horses) {
			if (rpg.getHorse() != null && !rpg.getHorse().isValid()) {
				if (horse.getUniqueId().equals(rpg.getHorse().getUniqueId())
						|| horse.getUniqueId().equals(rpg.holderOverUUID)) {
					HorseRPG.hSpawnedHorses.put(horse, HorseRPG.hSpawnedHorses.remove(rpg.getHorse()));
					rpg.getHorse().remove();
					rpg.setHorse(horse);
					break;
				}
			}
			if (rpg.spawned && ChatColor.translateAlternateColorCodes('&', rpg.name).equals(horse.getCustomName())) {
				msg(p, "&aHorses name is the same as an already spawned horse. Canceling.");
				return;
			}
		}

		if (hSpawnedHorses.containsKey(horse)) {
			if (hSpawnedHorses.get(horse).owners_name.equals(sender.getName())) {
				msg(p, "&aYou already own this horse!");
			} else {
				msg(p, "&aThis horse is already owned!");
			}
			return;
		}
		boolean kl = false;
		try {
			kl = !((Tameable) horse).isTamed() || ((Horse) horse).getInventory().getSaddle() == null;
		} catch (Error | Exception e) {
			ItemStack sad = null;
			for (ItemStack is : ((org.bukkit.entity.AbstractHorse) horse).getInventory().getContents())
				if (is != null && (is.getType() == (Material.SADDLE) || is.getType().name().contains("CARPET"))) {
					sad = is;
					break;
				}
			// = ((org.bukkit.entity.AbstractHorse) horse).getInventory().getItem(0);
			kl = (!((Tameable) horse).isTamed()) || sad == null;
		}
		if (kl) {
			msg(p, HORSE_NEEDS_TAME);
			return;
		}

		if (useEconomy && claimCost > 0) {
			double currentCost = Math.pow(claimCostMultiplier, senderHorses) * claimCost;
			EconomyResponse r;
			try {
				r = econ.withdrawPlayer(p, currentCost);
			} catch (Exception e) {
				r = econ.withdrawPlayer(p.getName(), currentCost);
			}
			if (r.transactionSuccess()) {
				try {
					msg(p, PURCHASED_FOR_CLAIM.replace("%amount%", "" + econ.format(currentCost)));
				} catch (Exception e4) {
					msg(p, PURCHASED_FOR_CLAIM.replace("%amount%", "[ERROR]"));
				}
				msg(p, NEXT_HORSE_COST.replace("%amount%",
						econ.format(Math.pow(claimCostMultiplier, senderHorses + 1) * claimCost)));
			} else {
				msg(p, NOTENOUGHMONEY);
				msg(p, "&aYou need: &e" + econ.format(currentCost));
				return;
			}
		}

		RPGHorse h;
		try {
			h = new RPGHorse(p, (Horse) horse);
			h.setVariant(((Horse) horse).getVariant());
		} catch (Error | Exception e) {
			h = new RPGHorse(p, (org.bukkit.entity.AbstractHorse) horse);
			h.setVariant(((org.bukkit.entity.AbstractHorse) horse).getVariant());
		}

		try {
			if (h instanceof Donkey) {
				((Donkey) h).isCarryingChest();
			}
			if (h instanceof Mule) {
				((Mule) h).isCarryingChest();
			}
		} catch (Error | Exception e) {
		}

		h.setHorse(horse);
		h.setName(h.name);

		pCurrentHorse.put(p.getUniqueId(), h);
		hSpawnedHorses.put(horse, h);
		if (!ownedHorses.containsKey(p.getName()))
			ownedHorses.put(p.getName(), new TreeSet<RPGHorse>());
		ownedHorses.get(p.getName()).add(h);
		horses.add(h);

		msg(p, CLAIM_NAME.replaceAll("%name%", h.name));
	}

	/**
	 * Accepts or declines a horse offer
	 * 
	 * @param sender
	 *            is the sender to message
	 */
	public static void buyHorse(CommandSender sender, boolean buy) {
		if (notAllowed(sender, H_BUY, true))
			return;
		if (!useEconomy) {
			msg(sender, NO_ECONOMY);
			return;
		}
		Player p = (Player) sender;

		if (!offers.containsKey(p.getUniqueId())) {
			msg(p, NO_OFFER_PENDING);
			return;
		}
		if (!buy) {
			offers.remove(p.getUniqueId());
			msg(p, OFFER_DECLINED);
			return;
		}
		if (pCurrentHorse.containsKey(p.getUniqueId())) {
			msg(p, BANISH_HORSE);
			return;
		}

		RPGHorse h = offers.get(p.getUniqueId());
		EconomyResponse er1;
		try {
			er1 = econ.withdrawPlayer(p, h.price);
		} catch (Exception e) {
			er1 = econ.withdrawPlayer(p.getName(), h.price);
		}
		if (!er1.transactionSuccess()) {
			msg(p, "&c" + er1.errorMessage);
			return;
		}
		EconomyResponse er2;
		try {
			er2 = econ.depositPlayer(Bukkit.getOfflinePlayer(h.owners_name), h.price);
		} catch (Exception e) {
			er2 = econ.depositPlayer(h.owners_name, h.price);
		}
		if (!er2.transactionSuccess()) {
			econ.depositPlayer(p.getName(), h.price);
			return;
		}

		for (Player seller : instance.getServer().getOnlinePlayers()) {
			if (seller.getName().equalsIgnoreCase(h.owners_name)) {
				pCurrentHorse.remove(seller.getUniqueId());
				msg(seller, "&b" + h.name + "&a sold to &b" + p.getName() + "&a for &b" + econ.format(h.price));
				break;
			}
		}
		h.banish();
		msg(p, "&b" + h.name + "&a bought from &b" + h.owners_name + "&a for &b" + econ.format(h.price));
		h.owners_name = p.getName();
		offers.remove(p.getUniqueId());
	}

	/**
	 * Attempts to sell your horse
	 * 
	 * @param sender
	 *            is the sender to message
	 * @param args
	 *            are the command arguments
	 */
	public static void sellHorse(CommandSender sender, String[] args) {
		if (notAllowed(sender, H_SELL, true))
			return;
		if (!useEconomy) {
			msg(sender, NO_ECONOMY);
			return;
		}
		Player p = (Player) sender;
		if (!pCurrentHorse.containsKey(p.getUniqueId())) {
			msg(p, "&aPlease summon the horse first.");
			return;
		}

		double cost = 0;
		try {
			cost = Double.parseDouble(args[1]);
		} catch (Exception ex) {
			msg(p, INVALID_PRICE);
			return;
		}

		if (args.length < 3) {
			msg(p, "&aPlease specify the potential buyer.");
			return;
		}

		String buyerName = args[2];
		for (int i = 3; i < args.length; i++)
			buyerName += " " + args[i];

		Player buyer = null;
		for (Player player : instance.getServer().getOnlinePlayers()) {
			if (player.getName().equalsIgnoreCase(buyerName)) {
				buyer = player;
				break;
			}
		}
		if (buyer == null) {
			msg(p, "&aCould not find &b" + buyerName + "&a online.");
			return;
		}

		RPGHorse h = pCurrentHorse.get(p.getUniqueId());
		h.price = cost;
		offers.put(buyer.getUniqueId(), h);
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(instance, new OfferTask(p), 1200);

		msg(p, "&aOffering &b" + h.name + "&a at &b" + econ.format(cost) + "&a for &b60 seconds.");
		msg(buyer, "&aType &b/h buy&a within &b60s&a to buy &b" + h.name + "&a for &b" + econ.format(cost));
		msg(buyer, "&aType &b/h decline&a to decline the offer.");
	}

	/**
	 * Summons the specified horse
	 * 
	 * @param sender
	 *            is the sender to message
	 * @param args
	 *            are the command arguments
	 */
	public static void summonHorse(CommandSender sender, String[] args) {
		if (notAllowed(sender, H_SUMMON, true))
			return;
		Player p = (Player) sender;

		/**
		 * if (pCurrentHorse.containsKey(p)) { msg(p, "&b" + pCurrentHorse.get(p).name +
		 * "&a is already summoned."); return; }
		 */

		RPGHorse h = null;// getHorseNotSpawned(p, args, 1);
		if (!hasHorses(p)) {
			h = null;
			if (freeHorse) {
				msg(p, "&aHere's one for free...");
				h = new RPGHorse(p);
				if (!ownedHorses.containsKey(p.getName()))
					ownedHorses.put(p.getName(), new TreeSet<RPGHorse>());
				ownedHorses.get(p.getName()).add(h);
				horses.add(h);
			} else {
				if (ownedHorses.containsKey(p.getName())) {
					if (ownedHorses.get(p.getName()).size() >= 0) {
						msg(p, NO_MORE_HORSES);
					} else {
						msg(p, NO_HORSES);
					}
				} else {
					msg(p, NO_HORSES);
				}
			}
		} else {
			h = getHorseNotSpawned(p, args, 1);
		}
		if (h != null) {
			if (hSpawnedHorses.containsKey(h.getHorse())) {
				msg(p, "&e" + h.name + "&a has already been summoned.");
				return;
			}
			if (h.spawned) {
				msg(p, "&e" + h.name + "&a has already been spawned.");
				return;
			}
			if (h.isBanished || h.isDead) {
				HorseRPG.msg(p, NEED_TIME_TO_RECHARGE.replaceAll("%name%", h.name));
			} else {
				if (useEconomy && summonCost > 0) {
					boolean b = false;
					try {
						b = econ.withdrawPlayer(p, summonCost).transactionSuccess();
					} catch (Exception e) {
						b = econ.withdrawPlayer(p.getName(), summonCost).transactionSuccess();
					}
					if (b)
						msg(p, "&aHorse summoned for &e" + econ.format(summonCost));
					else {
						msg(p, "&aYou need &e" + econ.format(summonCost) + "&a to summon a horse.");
						return;
					}
				}
				h.summon(p);
				pCurrentHorse.put(p.getUniqueId(), h);
			}
		}
	}

	/**
	 * Banishes the player's horse
	 * 
	 * @param sender
	 *            is the sender to message
	 */
	public static void banishHorse(CommandSender sender, String[] args, boolean forced) {
		if (forced) {
			if (notAllowed(sender, H_BANISH_FOR_PLAYER, true))
				return;
		} else {
			if (notAllowed(sender, H_BANISH, true))
				return;
		}
		Player p = null;
		if (forced) {
			if (args.length < 2)
				return;
			p = Bukkit.getPlayer(args[1]);
			if (p == null) {
				msg(sender, BANISH_OTHER_NOT_ONLINE);
				return;
			}
		} else {
			p = (Player) sender;
		}
		RPGHorse horse = null;
		if (!pCurrentHorse.containsKey(p.getUniqueId())) {
			if (horse == null) {
				msg(sender, NO_HORSE_SUMMONED);
				return;
			}
		}
		horse = (forced ? args.length > 2 : args.length > 1) ? getHorseSpawned(p, args, forced ? 2 : 1)
				: pCurrentHorse.get(p.getUniqueId());

		if (horse == null) {
			msg(sender, NO_HORSE_SUMMONED);
			return;
		}

		if (!horse.getHorse().isValid()) {
			for (RPGHorse rpg : horses) {
				if (rpg.getHorse() != null && !rpg.getHorse().isValid()) {
					for (Entity e : rpg.getHorse().getNearbyEntities(60, 30, 60)) {
						if (e.getUniqueId().equals(rpg.getHorse().getUniqueId())) {
							HorseRPG.hSpawnedHorses.put(e, HorseRPG.hSpawnedHorses.remove(rpg.getHorse()));
							rpg.getHorse().remove();
							rpg.setHorse(e);
							break;
						}
					}
				}
			}
		}

		if (horse.getHorse() == null || !horse.getHorse().getLocation().getChunk().isLoaded()) {
			msg(sender, BANISH_LOADED_CHUNK);
			return;
		}
		if (forced) {
			if (p.getWorld() == horse.getHorse().getWorld()) {
				if (p.getLocation().distanceSquared(horse.getHorse().getLocation()) >= 10000) {
					horse.getHorse().teleport(p.getLocation());
				}
			}
			if (((Player) sender).getWorld() == horse.getHorse().getWorld()) {
				if (((Player) sender).getLocation().distanceSquared(horse.getHorse().getLocation()) >= 10000) {
					horse.getHorse().teleport(((Player) sender).getLocation());
				}
			}
		} else {
			if (p.getLocation().distanceSquared(horse.getHorse().getLocation()) >= 10000) {
				horse.getHorse().teleport(p.getLocation());
			}
		}
		if (!forced)
			if (useEconomy && banishCost > 0) {
				boolean b = false;
				try {
					b = econ.withdrawPlayer(p, banishCost).transactionSuccess();
				} catch (Exception e) {
					b = econ.withdrawPlayer(p.getName(), banishCost).transactionSuccess();
				}

				if (b)
					msg(p, "&aHorse banished for &e" + econ.format(banishCost));
				else {
					msg(p, "&aYou need &e" + econ.format(summonCost) + "&a to banish your horse.");
					return;
				}
			}
		horse.banish();
		if (pCurrentHorse.get(p.getUniqueId()) == horse) {
			pCurrentHorse.remove(p.getUniqueId());
			for (RPGHorse others : ownedHorses.get(p.getName())) {
				if (hSpawnedHorses.containsKey(others.getHorse())) {
					pCurrentHorse.put(p.getUniqueId(), others);
					break;
				}
			}
		}
		msg(p, "&a" + horse.name + " banished.");
	}

	/**
	 * Makes a horse invincible
	 * 
	 * @param sender
	 *            is the sender to message
	 * @param args
	 *            are the command arguments
	 */
	public static void protectHorse(CommandSender sender, String[] args, boolean useGodMode) {
		if (useGodMode && notAllowed(sender, H_PROTECT, true))
			return;
		if (!useGodMode && notAllowed(sender, H_UNPROTECT, true))
			return;
		Player p = (Player) sender;
		RPGHorse h = currentHorse(p, args, 1);
		if (h == null)
			return;
		h.godmode = useGodMode;
		msg(p, useGodMode ? "&aHorse protected." : "&aHorse unprotected.");
	}

	/**
	 * Adds/removes xp from a horse
	 * 
	 * @param sender
	 *            is the sender to message
	 * @param args
	 *            are the command arguments
	 */
	public static void addHorseXP(CommandSender sender, String[] args) {
		boolean addXP = args[0].equalsIgnoreCase("addxp");
		if (addXP && notAllowed(sender, H_ADDXP, true))
			return;
		if (!addXP && notAllowed(sender, H_DELXP, true))
			return;
		Player p = (Player) sender;

		if (args.length < 2) {
			msg(p, "&aPlease specify a valid skill.");
			return;
		}
		if (args.length < 3) {
			msg(p, "&aPlease specify an xp amount.");
			return;
		}
		RPGHorse h = currentHorse(p, args, 3);
		if (h == null)
			return;

		int xp = 0;
		try {
			xp = Integer.parseInt(args[2]);
		} catch (NumberFormatException ex) {
			msg(p, "&aPlease specify an xp amount.");
			return;
		}

		String op = "&aAdded &b";
		if (!addXP) {
			xp *= -1;
			op = "&aRemoved &b";
		}

		switch (args[1]) {
		case "swiftness":
			h.swiftness.addXP(xp, p);
			break;
		case "agility":
			h.agility.addXP(xp, p);
			break;
		case "vitality":
			h.vitality.addXP(xp, p);
			break;
		case "wrath":
			h.wrath.addXP(xp, p);
			break;
		default:
			msg(p, "&aPlease specify a valid skill.");
			return;
		}
		msg(p, op + xp + "&a from &b" + h.name);
	}

	/**
	 * Displays the set help message
	 * 
	 * @param sender
	 *            is the sender to message
	 */
	public static void showSetHelp(CommandSender sender) {
		if (notAllowed(sender, H_SET, false))
			return;
		msg(sender, "");
		msg(sender, "&aSet Help:");
		for (Entry<String, String> entry : setHelp.entrySet())
			if (sender.hasPermission(entry.getKey()))
				msg(sender, entry.getValue());
	}

	/**
	 * Changes the currently spawned horse's name
	 * 
	 * @param sender
	 *            is the sender to message
	 * @param args
	 *            are the command arguments
	 */
	public static void setHorseName(CommandSender sender, String[] args) {
		if (notAllowed(sender, H_SET_NAME, true))
			return;
		Player p = (Player) sender;
		if (args.length <= 2) {
			msg(p, INVALID_NAME);
			return;
		}

		RPGHorse h = pCurrentHorse.get(p.getUniqueId());
		if (h == null) {
			msg(p, NO_HORSE_SUMMONED);
			return;
		}
		String oldname = h.name;

		if (args[2].equalsIgnoreCase("random"))
			h.setName(RPGHorse.randomName(p));
		else {
			String hName = args[2];
			for (int i = 3; i < args.length; i++)
				hName += " " + args[i];
			hName = hName.replaceAll("_", " ");
			h.setName(hName);
		}
		// "&aHorse %oldname% has been changed to %newname%"
		msg(p, RenameHorse.replace("%oldname%", oldname).replace("%newname%", h.name));
	}

	/**
	 * Changes the currently spawned horse's name
	 * 
	 * @param sender
	 *            is the sender to message
	 * @param args
	 *            are the command arguments
	 */
	public static void setHorseSpeed(CommandSender sender, String[] args) {
		if (notAllowed(sender, H_SET_SPEED, true))
			return;
		Player p = (Player) sender;
		if (args.length <= 2) {
			msg(p, INVALID_NAME);
			return;
		}

		RPGHorse h = pCurrentHorse.get(p.getUniqueId());
		if (h == null) {
			msg(p, NO_HORSE_SUMMONED);
			return;
		}
		double speed = RPGHorse.getRandomSpeed();
		// String oldname = h.name;

		if (args[2].equalsIgnoreCase("random"))
			h.generic_speed = speed;
		else {
			h.generic_speed = Double.parseDouble(args[2]);
		}
		if (h.getHorse() != null) {
			RPGHorse.attributeUtil.setSpeed(h.getHorse(), h.generic_speed);
		}
		// "&aHorse %oldname% has been changed to %newname%"
		msg(p, ChangeSpeedHorse.replace("%oldname%", h.name).replace("%speed%", "" + h.generic_speed));
	}

	/**
	 * Changes the currently spawned horse's name
	 * 
	 * @param sender
	 *            is the sender to message
	 * @param args
	 *            are the command arguments
	 */
	public static void setHorseJump(CommandSender sender, String[] args) {
		if (notAllowed(sender, H_SET_SPEED, true))
			return;
		Player p = (Player) sender;
		if (args.length <= 2) {
			msg(p, INVALID_NAME);
			return;
		}

		RPGHorse h = pCurrentHorse.get(p.getUniqueId());
		if (h == null) {
			msg(p, NO_HORSE_SUMMONED);
			return;
		}
		double jump = RPGHorse.getRandomJump();
		// String oldname = h.name;

		if (args[2].equalsIgnoreCase("random"))
			h.generic_jump = jump;
		else {
			h.generic_jump = Double.parseDouble(args[2]);
		}

		if (h.getHorse() != null) {
			RPGHorse.attributeUtil.setJumpHeight(h.getHorse(), h.generic_jump);
		}

		// "&aHorse %oldname% has been changed to %newname%"
		msg(p, ChangeJumpHorse.replace("%oldname%", h.name).replace("%jump%", "" + h.generic_jump));
	}

	/**
	 * Changes the current spawned horse's color
	 * 
	 * @param sender
	 *            is the sender to message
	 * @param args
	 *            are the command arguments
	 */
	public static void setHorseColor(CommandSender sender, String[] args) {
		if (notAllowed(sender, H_SET_NAME, true))
			return;
		Player p = (Player) sender;
		if (args.length <= 2) {
			msg(p, INVALID_COLOR);
			return;
		}

		RPGHorse h = pCurrentHorse.get(p.getUniqueId());
		if (h == null) {
			msg(p, NO_HORSE_SUMMONED);
			return;
		}

		if (args[2].equalsIgnoreCase("random"))
			h.setColor(RPGHorse.randomColor());
		else {
			try {
				h.setColor(Color.valueOf(args[2]));
			} catch (Exception ex) {
				msg(p, INVALID_COLOR);
				StringBuilder sb = new StringBuilder();
				sb.append("Valid colors: ");
				for (Color c : Color.values()) {
					sb.append(c.toString() + ", ");
				}
				msg(p, sb.toString());
			}
		}
	}

	/**
	 * Changes the currently spawned horse's style
	 * 
	 * @param sender
	 *            is the sender to message
	 * @param args
	 *            are the command arguments
	 */
	public static void setHorseStyle(CommandSender sender, String[] args) {
		if (notAllowed(sender, H_SET_NAME, true))
			return;
		Player p = (Player) sender;
		if (args.length <= 2) {
			msg(p, INVALID_STYLE);
			return;
		}

		RPGHorse h = pCurrentHorse.get(p.getUniqueId());
		if (h == null) {
			msg(p, NO_HORSE_SUMMONED);
			return;
		}

		if (args[2].equalsIgnoreCase("random"))
			h.setStyle(RPGHorse.randomStyle());
		else {
			try {
				h.setStyle(Style.valueOf(args[2]));
			} catch (Exception ex) {
				msg(p, INVALID_STYLE);
				StringBuilder sb = new StringBuilder();
				sb.append("Valid styles: ");
				for (Style c : Style.values()) {
					sb.append(c.toString() + ", ");
				}
				msg(p, sb.toString());
			}
		}
	}

	/**
	 * Changes the currently spawned horse's variant
	 * 
	 * @param sender
	 *            is the sender to message
	 * @param args
	 *            are the command arguments
	 */
	public static void setHorseVariant(CommandSender sender, String[] args) {
		if (notAllowed(sender, H_SET_TYPE, true))
			return;
		Player p = (Player) sender;

		if (args.length <= 2) {
			msg(p, INVALID_VARIANT);
			return;
		}

		RPGHorse h = pCurrentHorse.get(p.getUniqueId());
		if (h == null) {
			msg(p, NO_HORSE_SUMMONED);
			return;
		}

		switch (args[2].toLowerCase()) {
		case "donkey":
			h.setVariant(Variant.DONKEY);
			break;
		case "horse":
			h.setVariant(Variant.HORSE);
			break;
		case "mule":
			h.setVariant(Variant.MULE);
			break;
		case "skele":
			h.setVariant(Variant.SKELETON_HORSE);
			break;
		case "zombie":
			h.setVariant(Variant.UNDEAD_HORSE);
			break;
		case "rand":
			h.setVariant(RPGHorse.randomVariant());
			break;
		default:
			msg(p, INVALID_VARIANT);
		}
	}

	/**
	 * Allows the horse to be breed with by other players
	 * 
	 * @param sender
	 *            is the sender to message
	 * @param args
	 *            are the command arguments
	 */
	private void allowBreeding(CommandSender sender, String[] args) {
		if (notAllowed(sender, H_BREED, true))
			return;
		Player p = (Player) sender;
		String name = p.getName();

		if (!ownedHorses.containsKey(name))
			ownedHorses.put(name, new TreeSet<RPGHorse>());
		if (maxHorses(p) != -1 && ownedHorses.get(p.getName()).size() >= maxHorses(p)) {
			msg(p, MAX_HORSES);
			return;
		}

		RPGHorse h = currentHorse(p, args, 1);
		if (h == null) {
			msg(p, NO_HORSE_SUMMONED);
			return;
		}
		h.allowBreeding = !h.allowBreeding;
		msg(p, ALLOW_BREEDING, h.name, h.allowBreeding + "");
	}

	/**
	 * Creates a new horse
	 * 
	 * @param sender
	 *            is the sender to message
	 * @param args
	 *            are the command arguments
	 */
	public static void createHorse(CommandSender sender, String[] args, boolean gift) {
		if (notAllowed(sender, H_CREATE, true))
			return;
		Player p = (Player) sender;
		int offset = 0;
		if (gift) {
			if (args.length >= 2) {
				p = Bukkit.getPlayer(args[1]);
				offset++;
			}
		}
		if (p == null) {
			sender.sendMessage("[MCMMO] Player is not online");
			return;
		}
		String name = p.getName();

		if (!ownedHorses.containsKey(name))
			ownedHorses.put(name, new TreeSet<RPGHorse>());
		if (maxHorses(p) != -1 && ownedHorses.get(p.getName()).size() >= maxHorses(p)) {
			msg(p, MAX_HORSES);
			return;
		}

		RPGHorse h = new RPGHorse(p);
		ownedHorses.get(name).add(h);
		horses.add(h);

		if (args.length >= 2 + offset) {
			String hName = args[1];
			for (int i = 2 + offset; i < args.length; i++)
				hName += " " + args[i];
			h.setName(hName);
		}
		if (!pCurrentHorse.containsKey(p.getUniqueId())) {
			h.summon(p);
			pCurrentHorse.put(p.getUniqueId(), h);
		}
		msg(p, HORSE_BRED);
	}

	/**
	 * Deletes the specified horse
	 * 
	 * @param sender
	 *            is the sender to message
	 */
	public static void deleteHorse(CommandSender sender, String[] args) {
		if (notAllowed(sender, H_DELETE, true))
			return;
		Player p = (Player) sender;

		if (args.length <= 1) {
			msg(p, INVALID_NAME);
			return;
		}

		RPGHorse h = currentHorse(p, args, 1);
		if (h == null)
			return;

		pCurrentHorse.remove(p.getUniqueId());
		h.banish();
		ownedHorses.get(p.getName()).remove(h);
		horses.remove(h);
		// Test if this removes horses.
		h_config.removeHorse(h);
		if (hSpawnedHorses.containsKey(h.getHorse())) {
			h.getHorse().remove();
			hSpawnedHorses.remove(h.getHorse());
		}

		msg(p, "&b" + h.name + "&a won't be bothering you anymore.");
	}

	/**
	 * Reloads the horse database
	 * 
	 * @param sender
	 *            is the sender to message
	 */
	public void loadDatabase(CommandSender sender) {
		if (notAllowed(sender, H_DB, false))
			return;

		for (RPGHorse h : hSpawnedHorses.values())
			h.banish();

		ownedHorses.clear();
		pCurrentHorse.clear();
		hSpawnedHorses.clear();
		horses.clear();

		initHorses();

		msg(sender, "&aHorse database reloaded.");

	}

	/**
	 * Reloads the plugin
	 * 
	 * @param sender
	 *            is the sender to message
	 */
	public void reloadPlugin(CommandSender sender) {
		if (notAllowed(sender, H_RELOAD, false))
			return;
		HorseRPG.h_config = new HorseConfigHandler(new File(getDataFolder(), "horsedata.yml"));
		onDisable();
		onEnable();
		msg(sender, "&bmcMMO Horses&a reloaded.");
	}

	@Override
	public void onEnable() {
		instance = this;
		HorseRPG.h_config = new HorseConfigHandler(new File(getDataFolder(), "horsedata.yml"));

		initVariables();
		initMessages();
		initConfig();
		initHelp();
		// This is just a work around. This makes sure that we can
		// transfer db horses to the file
		// Object o = h_config.getGlobalVariable(Keys.G_dbtransfermode.toString());
		// if (o == null || (int) o < 1) {
		// h_config.setGlobalVar(Keys.G_dbtransfermode.toString(), 1);
		// }
		initHorses();

		// if (((int) h_config.getGlobalVariable(Keys.G_dbtransfermode.toString())) < 2)
		// {
		// h_config.setGlobalVar(Keys.G_dbtransfermode.toString(), 2);
		// }

		new BukkitRunnable() {
			@Override
			public void run() {
				for (RPGHorse h : hSpawnedHorses.values()) {
					h_config.saveHorse(h, false);
				}
				h_config.save();
			}
		}.runTaskTimer(this, 20 * 60 * 15, 20 * 60 * 15);

		cooldownTask = new CooldownTask().runTaskTimer(this, 0, 20);

		if (useEconomy && !setupEconomy()) {
			getLogger().severe("Plugin disabled due to Vault dependency!");
			getLogger().severe("Set 'enable-economy' to false in config to disable dependency.");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new HorseListener(), this);
		pm.registerEvents(new PlayerListener(), this);
		pm.registerEvents(shop = new ShopManager(), this);
		try {
			if (forceClaimOnTaim)
				pm.registerEvents(new TameToClaimListener(), this);
		} catch (Error | Exception e5) {
			e5.printStackTrace();
		}
		try {
			if (forcePermToRideUnclaimed)
				pm.registerEvents(new PermToRideListener(), this);
		} catch (Error | Exception e5) {
			e5.printStackTrace();
		}

		if (!getConfig().contains("auto-update")) {
			getConfig().set("auto-update", true);
			saveConfig();
		}

		// new Updater(instance, 61609, getConfig().getBoolean("auto-update"));
		if (getConfig().getBoolean("auto-update"))
			GithubUpdater.autoUpdate(this, "ZombieStriker", "mcMMOHorses", "mcMMOHorses.jar");

		// Download the API dependancy
		if (Bukkit.getPluginManager().getPlugin("PluginConstructorAPI") == null) {
			// new `encyDownloader(this, 276723);
			GithubDependDownloader.autoUpdate(this,
					new File(getDataFolder().getParentFile(), "PluginConstructorAPI.jar"), "ZombieStriker",
					"PluginConstructorAPI", "PluginConstructorAPI.jar");
		}

		Metrics metrics = new Metrics(this);
		// Optional: Add custom charts
		metrics.addCustomChart(new Metrics.SimplePie("horsecount", new java.util.concurrent.Callable<String>() {
			@Override
			public String call() throws Exception {
				return horses.size() + "";
			}
		}));
	}

	/**
	 * Attempts to set up the economy
	 * 
	 * @return true if economy is set up successfully
	 */
	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null)
			return false;
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null)
			return false;
		econ = rsp.getProvider();
		return econ != null;
	}

	/**
	 * Initializes the variables
	 */
	private void initVariables() {
		config = new ConfigAccessor(this, "config.yml");
		config.saveDefaultConfig();

		if (!config.getConfig().contains("enable-economy"))
			config.overwriteConfig();

		offers = new HashMap<UUID, RPGHorse>();
		ownedHorses = new HashMap<String, TreeSet<RPGHorse>>();
		pCurrentHorse = new HashMap<UUID, RPGHorse>();
		hSpawnedHorses = new HashMap<Entity, RPGHorse>();
		horses = new HashSet<RPGHorse>();
	}

	private void initMessages() {
		messages = new MessagesConfigHandler(new File(getDataFolder(), "messages.yml"));

		BANISH_HORSE = messages.a("Banish_horse", BANISH_HORSE);
		HORSE_BRED = messages.a("Horse_bred", HORSE_BRED);
		HORSES_SAVED = messages.a("Horses_saved", HORSES_SAVED);
		INVALID_COLOR = messages.a("Invalid_color", INVALID_COLOR);
		INVALID_COMMAND = messages.a("Invalid_command", INVALID_COMMAND);
		INVALID_NAME = messages.a("Invalid_name", INVALID_NAME);
		INVALID_PAGE = messages.a("Invalid_page", INVALID_PAGE);
		INVALID_STYLE = messages.a("Invalid_style", INVALID_STYLE);
		INVALID_VARIANT = messages.a("Invalid_variant", INVALID_VARIANT);
		MAX_HORSES = messages.a("Max_horses", MAX_HORSES);
		NO_ECONOMY = messages.a("NoEconomy", NO_ECONOMY);
		NO_HORSES = messages.a("No_horses", NO_HORSES);
		NO_MORE_HORSES = messages.a("No_more_horses", NO_MORE_HORSES);
		NO_HORSE_SUMMONED = messages.a("No_horses_summoned", NO_HORSE_SUMMONED);
		NO_PERMISSION = messages.a("No_Permission", NO_PERMISSION);
		NO_PERMISSION_HORSE = messages.a("No_Permission_Horse", NO_PERMISSION_HORSE);
		PAGE_DOES_NOT_EXIST = messages.a("Page_does_not_exist", PAGE_DOES_NOT_EXIST);
		PLAYER_ONLY = messages.a("Player_only", PLAYER_ONLY);

		FOALBREED = messages.a("Breed_New_Foal", FOALBREED);

		NO_WILD_HORSES = messages.a("No_wild_horses", NO_WILD_HORSES);
		NO_BREED_HORSES = messages.a("No_breeding_horses", NO_BREED_HORSES);
		ALLOW_BREEDING = messages.a("Allow_breeding", ALLOW_BREEDING);

		NOT_ENOUGH_MONEY = messages.a("Not_enough_money", NOT_ENOUGH_MONEY);
		BOUGHT_HORSE = messages.a("Bought_horse", BOUGHT_HORSE);
		BOUGHT_HORSE_EXP1 = messages.a("Bought_horse_exp1", BOUGHT_HORSE_EXP1);

		HORSE_NEEDS_TAME = messages.a("Horse_needs_tame", HORSE_NEEDS_TAME);
		PURCHASED_FOR_CLAIM = messages.a("PurchasedHorse", PURCHASED_FOR_CLAIM);
		NEXT_HORSE_COST = messages.a("Next_cost", NEXT_HORSE_COST);
		CLAIM_NAME = messages.a("Claimed_name", CLAIM_NAME);
		SKILL_INCREASED_BY = messages.a("Skill_Increased_By", SKILL_INCREASED_BY);
		NOTENOUGHMONEY = messages.a("Not_enough_money", NOTENOUGHMONEY);
		NEED_TIME_TO_RECHARGE = messages.a("Need_Time_To_Recharge", NEED_TIME_TO_RECHARGE);
		SKILL_REFRESH = messages.a("Skill_Refreshed", SKILL_REFRESH);
		RenameHorse = messages.a("Renamed_horse", RenameHorse);
		ChangeSpeedHorse = messages.a("Change_Speed_horse", ChangeSpeedHorse);
		ChangeJumpHorse = messages.a("Change_Jump_horse", ChangeJumpHorse);

		TOO_TIRED = messages.a("Horse_Too_Tired", TOO_TIRED);
		INVALID_PRICE = messages.a("Invalid_price", INVALID_PRICE);
		HORSE_DOES_NOT_EXIST = messages.a("Horse_Does_Not_Exist", HORSE_DOES_NOT_EXIST);
		OFFER_DECLINED = messages.a("Offer_Declined", OFFER_DECLINED);
		MUST_RIDE_HORSE_TO_CLAIM = messages.a("MUST_RIDE_HORSE_TO_CLAIM", MUST_RIDE_HORSE_TO_CLAIM);
		NO_OFFER_PENDING = messages.a("No_Offer_Pending", NO_OFFER_PENDING);
		// CLOSERTHAN100 = messages.a("Must_be_closer_than_100", CLOSERTHAN100);

		BANISH_LOADED_CHUNK = messages.a("Banished_In_Unloaded_Chunk", BANISH_LOADED_CHUNK);

		RPGHorse.FIRST_NAMES = messages.b("VALID_FIRST_NAMES", RPGHorse.FIRST_NAMES);
		RPGHorse.LAST_NAMES = messages.b("VALID_LAST_NAMES", RPGHorse.LAST_NAMES);

		ShopManager.title = messages.a("Shop Title", ShopManager.title);
	}

	/**
	 * Initializes config.yml
	 */
	private void initConfig() {
		try {
			FileConfiguration fc = config.getConfig();
			int saveInterval = fc.getInt("save-interval");
			if (saveInterval > 0)
				saveTask = new SaveTask().runTaskTimer(this, saveInterval, saveInterval);

			freeHorse = fc.getBoolean("free-horse");
			permanentDeath = fc.getBoolean("permanent-death");
			deathTimer = fc.getInt("death-timer");
			banishTimer = fc.getInt("banish-timer");
			sprintCooldown = fc.getInt("sprint-cooldown");
			infuriateCooldown = fc.getInt("infuriate-cooldown");

			groups = new HashMap<String, Integer>();
			Map<String, Object> groupsRaw = fc.getConfigurationSection("groups").getValues(false);
			for (Entry<String, Object> entry : groupsRaw.entrySet())
				groups.put("mcmmohorses.groups." + entry.getKey(), (int) entry.getValue());

			useEconomy = fc.getBoolean("enable-economy");
			claimCost = fc.getInt("claim-cost");
			claimCostMultiplier = fc.getInt("claim-cost-multiplier");
			summonCost = fc.getInt("summon-cost");
			banishCost = fc.getInt("banish-cost");

			if (fc.contains("disable_StatsInScoreboard"))
				DisableshowStatsinInScoreboard = fc.getBoolean("disable_StatsInScoreboard");
			if (fc.contains("disable_StatsInChat"))
				DisableshowStatsInChat = fc.getBoolean("disable_StatsInChat");

			if (fc.contains("horsestats.default_min_speed"))
				RPGHorse.minSpeed = fc.getDouble("horsestats.default_min_speed");
			if (fc.contains("horsestats.default_max_speed"))
				RPGHorse.maxSpeed = fc.getDouble("horsestats.default_max_speed");
			if (fc.contains("horsestats.default_min_jump"))
				RPGHorse.minJump = fc.getDouble("horsestats.default_min_jump");
			if (fc.contains("horsestats.default_max_jump"))
				RPGHorse.maxJump = fc.getDouble("horsestats.default_max_jump");

			if (fc.contains("claimFoalOnBreed"))
				HorseRPG.claimBreeding = fc.getBoolean("claimFoalOnBreed");

			/*
			 * if (!h_config.containsGlobalVariable(Keys.G_nobanish.toString())) {
			 * h_config.setGlobalVar(Keys.G_nobanish.toString(), false); }else {
			 * 
			 * }
			 * 
			 * if (!h_config.containsGlobalVariable(Keys.G_banishOnDisable.toString())) {
			 * h_config.setGlobalVar(Keys.G_banishOnDisable.toString(), true); }
			 * 
			 * if (!h_config.containsGlobalVariable(Keys.G_DisableBreeding.toString())) {
			 * h_config.setGlobalVar(Keys.G_DisableBreeding.toString(), false); }
			 * 
			 * if (!h_config.containsGlobalVariable(Keys.G_banishonquit.toString())) {
			 * h_config.setGlobalVar(Keys.G_banishonquit.toString(), true); } if
			 * (!h_config.containsGlobalVariable(Keys.G_ClameOnTame.toString())) {
			 * h_config.setGlobalVar(Keys.G_ClameOnTame.toString(), false); }
			 * 
			 * if (!h_config.containsGlobalVariable(Keys.G_PermToRide.toString())) {
			 * h_config.setGlobalVar(Keys.G_PermToRide.toString(), false); } if
			 * (!h_config.containsGlobalVariable(Keys.G_REMOVETAME.toString())) {
			 * h_config.setGlobalVar(Keys.G_REMOVETAME.toString(), false); } for (Variant v
			 * : Variant.values()) { if
			 * (!h_config.containsGlobalVariable(Keys.G_horseCost.toString() + "." +
			 * v.name())) { h_config.setGlobalVar(Keys.G_horseCost.toString() + "." +
			 * v.name(), 100.0); } costForHorse.put(v, (Double)
			 * h_config.getGlobalVariable(Keys.G_horseCost.toString() + "." + v.name())); }
			 */
			/*
			 * G_dbtransfermode(".savetype"),
			 * G_DisableBreeding(".disable_basegame_breeding"), G_banishOnDisable(
			 * ".banish_on_disable"),
			 * G_ClameOnTame(".Force_claim_on_tame"),G_PermToRide(".RequirePermissionToRide"
			 * ), G_REMOVETAME(".disable_basegame_taming"),G_banishonquit(
			 * ".banish_on_player_quit"), G_nobanish(
			 * ".disable_banishment"),G_horseCost(".priceForHorse"),
			 */
			if (!fc.contains("savetype")) {
				config.overwriteConfig();
				fc = config.getConfig();
			}
			savetype = fc.getInt("savetype");

			logWhenSave = fc.getBoolean("logWhenSaved");

			nobanishment = fc.getBoolean("disable_banishment");
			banishondisable = fc.getBoolean("banish_on_disable");
			banishonquit = fc.getBoolean("banish_on_player_quit");
			disableBreeding = fc.getBoolean("disable_basegame_breeding");

			disableTamedHorses = fc.getBoolean("disable_basegame_taming");

			forceClaimOnTaim = fc.getBoolean("Force_claim_on_tame");
			forcePermToRideUnclaimed = fc.getBoolean("RequirePermissionToRide");
			for (Variant v : Variant.values()) {
				costForHorse.put(v, fc.getDouble("priceForHorse." + v.name()));// (Double)
																				// h_config.getGlobalVariable(Keys.G_horseCost.toString()
																				// + "." + v.name()));
			}
		} catch (Exception e) {
			getLogger().info("Error loading 'config.yml' file!");
			e.printStackTrace();
		}
	}

	/**
	 * Initializes help messages
	 */
	private void initHelp() {
		help = new LinkedHashMap<String, String>();
		setHelp = new LinkedHashMap<String, String>();

		help.put(H_HELP, "&b/h help &7[page] &a- Displays horse commands.");
		help.put(H_ME, "&b/h me &a- Displays your info.");
		help.put(H_CLAIM, "&b/h claim &a- Claims the horse you are riding.");
		help.put(H_STATS, "&b/h stats &7[horse] &a- Displays your horse info.");
		help.put(H_SUMMON, "&b/h summon &a- Spawns your horse.");
		help.put(H_BANISH, "&b/h banish &a- Despawns your horse.");
		help.put(H_DELETE, "&b/h kill &e<name> &a- Removes an owned horse for good.");
		if (useEconomy) {
			help.put(H_BUY, "&b/h buy &a- Buys a horse offered to you.");
			help.put(H_SELL, "&b/h sell &e<money> <player> &a- Sells the summoned horse.");
			help.put(H_SHOP, "&b/h shop &a- Opens the shop GUI to buy horses.");
		}

		// fhelp.put(H_GIFT, "&b/h gift &7[name] [reciever] &a- Gifts a horse to another
		// player.");
		help.put(H_CREATE, "&b/h create &7[name] &a- Breeds a horse.");
		help.put(H_ADDXP, "&b/h addxp &e<skill> <xp> &7[name] &a- Adds xp to horse.");
		help.put(H_DELXP, "&b/h delxp &e<skill> <xp> &7[name] &a- Removes xp from horse.");
		help.put(H_PROTECT, "&b/h protect &a- Protects your horse from damage.");
		help.put(H_UNPROTECT, "&b/h unprotect &a- Unprotects your horse.");
		help.put(H_SET, "&b/h set &a- Displays a list of set commands.");
		help.put(H_SAVE, "&b/h save &a- Saves all horse data.");
		help.put(H_DB, "&b/h db &a- Reloads the horse database.");
		help.put(H_RELOAD, "&b/h reload &a- Reloads the configuration files.");

		setHelp.put(H_SET_NAME, "&b/h set name &a<name|random>");
		setHelp.put(H_SET_COLOR, "&b/h set color &a<color|random>");
		setHelp.put(H_SET_STYLE, "&b/h set style &a<style|random>");
		setHelp.put(H_SET_STYLE, "&b/h set speed &a<<0.1 - 0.3>|random>");
		setHelp.put(H_SET_STYLE, "&b/h set jump &a<<0.7>|random>");
		setHelp.put(H_SET_TYPE, "&b/h set type &a<donkey|horse|mule|skele|zombie|rand>");

	}

	/**
	 * Initializes horses.db
	 */
	private void initHorses() {
		if (savetype == 2) {
			if (h_config.anyOwners()) {
				int amount = 0;
				for (String owner : h_config.getOwners()) {
					for (String name : h_config.getHorses(owner)) {
						RPGHorse h = h_config.getHorse(owner, name);
						if (owner == null || h == null || horses == null)
							continue;
						if (!ownedHorses.containsKey(owner))
							ownedHorses.put(owner, new TreeSet<RPGHorse>());
						ownedHorses.get(owner).add(h);
						horses.add(h);
						amount++;
					}
				}
				HorseRPG.instance.getLogger().info("Finished loading "+amount+" horses.");
			}
		} else {
			try {
				Class.forName("org.sqlite.JDBC");
				Connection connection = DriverManager.getConnection("jdbc:sqlite:horses.db");
				Statement statement = connection.createStatement();
				statement.setQueryTimeout(30);

				statement.executeUpdate("create table if not exists horses (	name string, " + "owner string, "
						+ "color string, " + "style string, " + "variant string, " + "protected integer, "
						+ "swiftnessXP integer, " + "agilityXP integer, " + "vitalityXP integer, "
						+ "wrathXP integer, sex integer)");

				ResultSet rs = statement.executeQuery("select * from horses");

				while (rs.next()) {
					String owner = rs.getString("owner");
					Color color = Color.valueOf(rs.getString("color"));
					Style style = Style.valueOf(rs.getString("style"));
					Variant variant = Variant.valueOf(rs.getString("variant"));

					double speed = rs.getDouble("defaultSpeed");
					double jump = rs.getDouble("defaultJump");
					if (jump <= 0 || jump > RPGHorse.maxJump)
						jump = RPGHorse.getRandomJump();
					if (speed <= 0 || speed > RPGHorse.maxSpeed)
						speed = RPGHorse.getRandomSpeed();

					RPGHorse h = new RPGHorse(rs.getString("name"), owner, color, style, variant,
							rs.getInt("godmode") == 1, rs.getInt("swiftnessXP"), rs.getInt("agilityXP"),
							rs.getInt("vitalityXP"), rs.getInt("wrathXP"), null, jump, speed, rs.getInt("sex") == 0);
					// TODO:Tempfix. Since I don't want users to use the sql, just set the dfefault
					// value to 2.25

					if (!ownedHorses.containsKey(owner))
						ownedHorses.put(owner, new TreeSet<RPGHorse>());

					ownedHorses.get(owner).add(h);
					horses.add(h);
				}
				connection.close();

			} catch (Exception e) {
				getLogger().info("Error loading 'horses.db' file!");
				System.out.println(e.getMessage());
			}
		}
	}

	/**
	 * Saves all horses in the sql database
	 * 
	 * @param sender
	 *            is the sender to message
	 */
	public static void saveHorses(CommandSender sender) {
		if (sender != null && !notAllowed(sender, H_SAVE, false))
			return;
		try {
			if (savetype == 2) {
				for (TreeSet<RPGHorse> horseSet : ownedHorses.values()) {
					for (RPGHorse h : horseSet) {
						try {
							h_config.saveHorse(h, false);
						} catch (Error | Exception ed4) {
							ed4.printStackTrace();
						}
					}
				}
				h_config.save();
			} else {
				try (Connection connect = DriverManager.getConnection("jdbc:sqlite:horses.db")) {
					try (Statement statement = connect.createStatement()) {
						statement.setQueryTimeout(30);
						statement.executeUpdate("drop table if exists horses");
					}

					try (Statement statement = connect.createStatement()) {
						statement.setQueryTimeout(30);
						statement.executeUpdate("create table horses (	name string, " + "owner string, " + "color string, "
								+ "style string, " + "variant string, " + "godmode integer, " + "swiftnessXP integer, "
								+ "agilityXP integer, " + "vitalityXP integer, "
								+ "wrathXP integer, sex integer, defaultSpeed integer, defaultJump integer)");
					}

					try (PreparedStatement statement = connect.prepareStatement("insert into horses values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);")) {
						statement.setQueryTimeout(30);
						for (TreeSet<RPGHorse> horseSet : ownedHorses.values()) {
							for (RPGHorse h : horseSet)
								try {
									statement.setString(1, h.name);
									statement.setString(1, h.owners_name);
									statement.setString(1, h.color.toString());
									statement.setString(1, h.style.toString());
									statement.setString(1, h.variant.toString());
									statement.setInt(1, h.godmode ? 1 : 0);
									statement.setInt(1, h.swiftness.xp);
									statement.setInt(1, h.agility.xp);
									statement.setInt(1, h.vitality.xp);
									statement.setInt(1, h.wrath.xp);
									statement.setInt(1, h.isMale ? 0 : 1);
									statement.setInt(1, (int) h.generic_speed);
									statement.setInt(1, (int) h.generic_jump);
									statement.addBatch();
								} catch (Error | Exception ed4) {
									ed4.printStackTrace();
								}
						}

						statement.executeBatch();
					}

					if (!connect.getAutoCommit()) {
						connect.commit();
					}
				}
			}

			if (sender != null)
				msg(sender, HORSES_SAVED);
			if (instance != null)
				if (logWhenSave)
					msg(Bukkit.getConsoleSender(), HORSES_SAVED);
		} catch (Exception e) {
			System.err.println(e);
			msg(sender,
					"&a A problem has occured. Report the error message in the console to Zombie_Striker on spigot:");
			sender.sendMessage("https://www.spigotmc.org/resources/mcmmohorses.46301/");
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> r = new ArrayList<>();
		if (args.length == 1) {
			a(r, "help", args[0]);
			a(r, "me", args[0]);
			a(r, "claim", args[0]);
			a(r, "stats", args[0]);
			a(r, "summon", args[0]);
			a(r, "banish", args[0]);
			a(r, "banishFor", args[0]);
			a(r, "kill", args[0]);
			a(r, "buy", args[0]);
			a(r, "sell", args[0]);
			a(r, "shop", args[0]);

			// TODO: REENABLE BREEDING
			// a(r, "allowbreeding", args[0]);
			a(r, "addxp", args[0]);
			a(r, "delxo", args[0]);
			a(r, "protect", args[0]);
			a(r, "unprotect", args[0]);
			a(r, "set", args[0]);
			a(r, "save", args[0]);
			a(r, "db", args[0]);
			a(r, "leaderboard", args[0]);
			a(r, "reload", args[0]);
			a(r, "gift", args[0]);
		} else {
			if (args[0].equalsIgnoreCase("set")) {
				a(r, "name", args[1]);
				a(r, "speed", args[1]);
				a(r, "jump", args[1]);
				a(r, "type", args[1]);
				a(r, "color", args[1]);
				a(r, "style", args[1]);
			}
			if (args[0].equalsIgnoreCase("banishFor")) {
				if (args.length == 2) {
					for (UUID player : pCurrentHorse.keySet()) {
						a(r, Bukkit.getPlayer(player).getName(), args[1]);
					}
				} else {
					if (ownedHorses.containsKey(sender.getName()))
						for (RPGHorse h : ownedHorses.get(sender.getName())) {
							a(r, h.name, args[2]);
						}
				}
			}
			if (args[0].equalsIgnoreCase("stats") || args[0].equalsIgnoreCase("breed")
					|| args[0].equalsIgnoreCase("summon") || args[0].equalsIgnoreCase("allowbreeding")
					|| args[0].equalsIgnoreCase("kill") || args[0].equalsIgnoreCase("banish")) {
				if (ownedHorses.containsKey(sender.getName()))
					for (RPGHorse h : ownedHorses.get(sender.getName())) {
						a(r, h.name, args[1]);
					}
			}
			if (args[0].equalsIgnoreCase("addxp") || args[0].equalsIgnoreCase("delxp")) {
				if (args.length == 2) {
					a(r, "agility", args[1]);
					a(r, "swiftness", args[1]);
					a(r, "vitality", args[1]);
					a(r, "wrath", args[1]);
				}
				if (args.length == 3) {
					a(r, "1", args[2]);
				}
				if (args.length == 4) {
					if (ownedHorses.containsKey(sender.getName()))
						for (RPGHorse h : ownedHorses.get(sender.getName())) {
							a(r, h.name, args[3]);
						}
				}

			}
		}
		return r;
	}

	public void a(List<String> l, String s, String arg) {
		if (s.toLowerCase().startsWith(arg.toLowerCase()))
			l.add(s.replaceAll(" ", "_"));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			showHelp(sender, args);
			return true;
		}
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (player.getVehicle() != null && hSpawnedHorses.containsKey(player.getVehicle())) {

			}
		}

		String hCmd = args[0].toLowerCase();
		String subcmd = "";
		if (args.length >= 2)
			subcmd = args[1].toLowerCase();

		switch (hCmd) {
		case "help":
			showHelp(sender, args);
			return true;
		case "me":
			showMe(sender);
			return true;
		case "info":
		case "stats":
		case "horse":
			showHorse(sender, args);
			return true;
		case "shop":
			shop.openInventory(sender);
			return true;
		case "swiftness":
		case "agility":
		case "vitality":
		case "wrath":
			showSkill(sender, hCmd, args);
			return true;
		case "claim":
			claimHorse(sender);
			return true;
		case "buy":
		case "accept":
			buyHorse(sender, true);
			return true;
		case "deny":
		case "decline":
			buyHorse(sender, false);
			return true;
		case "sell":
			sellHorse(sender, args);
			return true;
		case "leaderboard":
			showLeaderBoard(sender, args);
			return true;
		case "spawn":
		case "s":
		case "summon":
			summonHorse(sender, args);
			return true;
		case "b":
		case "banish":
		case "stable":
			banishHorse(sender, args, false);
			return true;
		case "banishfor":
			banishHorse(sender, args, true);
			return true;
		case "addxp":
		case "delxp":
			addHorseXP(sender, args);
			return true;
		case "lock":
		case "protect":
			protectHorse(sender, args, true);
			return true;
		case "unlock":
		case "unprotect":
			protectHorse(sender, args, false);
			return true;
		case "save":
			saveHorses(sender);
			return true;
		case "gift":
		case "create":
			createHorse(sender, args, hCmd.equals("gift"));
			return true;
		case "allowbreeding":
			allowBreeding(sender, args);
			return true;
		case "delete":
		case "kill":
		case "remove":
			deleteHorse(sender, args);
			return true;
		case "set":
			switch (subcmd) {
			case "name":
				setHorseName(sender, args);
				return true;
			case "speed":
				setHorseSpeed(sender, args);
				return true;
			case "jump":
				setHorseJump(sender, args);
				return true;
			case "type":
				setHorseVariant(sender, args);
				return true;
			case "color":
				setHorseColor(sender, args);
				return true;
			case "style":
				setHorseStyle(sender, args);
				return true;
			default:
				showSetHelp(sender);
				return true;
			}
		case "db":
		case "data":
		case "database":
			loadDatabase(sender);
			return true;
		case "reload":
			reloadPlugin(sender);
			return true;
		default:
			msg(sender, INVALID_COMMAND);
			return true;
		}
	}

	@Override
	public void onDisable() {
		if (banishondisable)
			for (Entry<Entity, RPGHorse> horse : hSpawnedHorses.entrySet()) {
				horse.getValue().banish();
				if (horse.getKey() != null && horse.getKey().isValid()) {
					horse.getKey().remove();
				}
			}

		// Force save all spawned horses.
		for (RPGHorse h : hSpawnedHorses.values())
			h_config.saveHorse(h, false);
		h_config.save();

		HandlerList.unregisterAll(this);
		saveHorses(null);
		saveTask.cancel();
		if (cooldownTask != null)
			cooldownTask.cancel();
	}

	/**
	 * Displays the player's horse stats
	 * 
	 * @param sender
	 *            is the sender to update
	 */
	public static void showLeaderBoard(CommandSender sender, String[] args) {
		if (notAllowed(sender, H_LEADERBOARD, true))
			return;

		Player p = (Player) sender;
		if (playersWithScoreboards.contains(p.getName())) {
			p.sendMessage(ChatColor.GREEN + "Already showing a scoreboard-menu.");
			return;
		}

		NavigableMap<RPGHorse, Integer> map = new TreeMap<RPGHorse, Integer>(Collections.reverseOrder());
		for (TreeSet<RPGHorse> hor : ownedHorses.values()) {
			for (RPGHorse h : hor) {
				map.put(h, Integer.MAX_VALUE - h.agility.xp - h.wrath.xp - h.vitality.xp - h.swiftness.xp);
			}
		}

		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = null;
		Objective obj = null;
		if (!DisableshowStatsinInScoreboard) {
			board = manager.getNewScoreboard();

			obj = board.registerNewObjective("leaderboard", "dummy");
			obj.setDisplaySlot(DisplaySlot.SIDEBAR);
			obj.setDisplayName(ChatColor.YELLOW + "Leaderboard");
			sender.sendMessage(ChatColor.YELLOW + "Leaderboard");
		}

		int index = 0;
		for (RPGHorse h : map.descendingKeySet()) {
			String k = ChatColor.GREEN + "#" + index + "  " + h.name.substring(0, Math.min(h.name.length(), 16));
			String plaeyerName = h.owners_name.substring(0, Math.min(29 - k.length(), h.owners_name.length()));
			k = k + ChatColor.WHITE + " " + plaeyerName;
			if (!DisableshowStatsinInScoreboard)
				try {
					obj.getScore(k).setScore(20 - index);
				} catch (Exception e) {
					obj.getScore(Bukkit.getOfflinePlayer(k)).setScore(20 - index);
				}
			if (!DisableshowStatsInChat)
				sender.sendMessage(
						k + " = " + (h.agility.level + h.wrath.level + h.vitality.level + h.swiftness.level));
			index++;
			if (index >= 20)
				break;
		}

		if (!DisableshowStatsinInScoreboard) {
			Scoreboard oldsb = p.getScoreboard();
			p.setScoreboard(board);
			playersWithScoreboards.add(p.getName());

			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(instance, new ScoreboardTask(p, oldsb), 200);
		}
	}
}
