package com.blueskullgames.horserpg.skills;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.blueskullgames.horserpg.HorseRPG;
import com.blueskullgames.horserpg.RPGHorse;

public abstract class Skill {
	
	public final RPGHorse h;
	public final String name;
	
	public int xp;
	public int level;
	
	/** Calculates and returns the level given xp
	 * @param xp is the experience points
	 * @return the level for the given xp
	 */
	public static int xpToLevel(int xp) {
		return (int)(1000 * Math.log10(xp + 2040) - 3310);
	}
	
	/** Calculates and returns the xp given the level
	 * @param level is the level
	 * @return the experience points required for that level
	 */
	public static int levelToXP(int level) {
		return (int)(Math.pow(10, (level/1000.0) + (331/100.0)) - 2040);
	}
	
	/** Constructs a skill object
	 * @param h is the horse
	 * @param name is the skill name
	 * @param cooldown is the ability cooldown (in seconds)
	 * @param xp is the current amount of xp
	 */
	public Skill(RPGHorse h, String name, int xp) {
		this.h = h;
		this.name = name;
		this.xp = xp;

		level = xpToLevel(xp);
	}
	
	/** Adds xp to the skill with player specified
	 * @param newXP is the xp to add
	 * @param p is the horse owner
	 * @return true if leveled up
	 */
	public void addXP(int newXP, Player p) {
		xp += newXP;

		int oldLevel = level;
		level = xpToLevel(xp);
		int difference = level-oldLevel;
		if (level != oldLevel) {
			if (p != null)
				HorseRPG.msg(p, "&e" + name + " skill increased by " + difference + ". Total (" + level + ")" );
			h.powerLevel += difference;
			update();
		}
	}

	public abstract void stats(CommandSender sender);
	
	public abstract void update();
	
}
