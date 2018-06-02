package com.blueskullgames.horserpg.skills;

import org.bukkit.command.CommandSender;

import com.blueskullgames.horserpg.HorseRPG;
import com.blueskullgames.horserpg.RPGHorse;

public class Agility extends Skill {

	public static final String NAME 		= "Agility";
	public static final String PASSIVE_1	= "Roll";
	public static final String PASSIVE_2 	= "Graceful Roll";
	public static final String PASSIVE_3 	= "Dodge";
	
	public double rollChance;
	public double gracefulChance;
	public double dodgeChance;
	
	/** Constructs an agility skill
	 * @param h is the horse to add the skill to
	 * @param xp is the current xp
	 */
	public Agility(RPGHorse h, int xp) {
		super(h, NAME, xp);
		update();
	}

	@Override
	public void stats(CommandSender sender) {
		sender.sendMessage("");
		HorseRPG.msg(sender, "&c-----[]&a" + NAME.toUpperCase() + "&c[]-----");
		HorseRPG.msg(sender, "&8XP GAIN:&f Falling");
		HorseRPG.msg(sender, "&8LVL:&a " + level + "&3 XP&e(&6" + xp + "&e/&6" + (levelToXP(level+1)+1) + "&e)");
		
		HorseRPG.msg(sender, "&c-----[]&aEFFECTS&c[]-----");
		HorseRPG.msg(sender, "&3" + PASSIVE_1 + ":&a Reduces fall damage");
		HorseRPG.msg(sender, "&3" + PASSIVE_2 + ":&a Negates almost all fall damage");
		HorseRPG.msg(sender, "&3" + PASSIVE_3 + ":&a Reduce attack damage by half");
		
		HorseRPG.msg(sender, "&c-----[]&a" + h.name.toUpperCase() + " STATS&c[]-----");
		HorseRPG.msg(sender, String.format("&c%s Chance:&e %.2f%%", PASSIVE_1, rollChance*100));
		HorseRPG.msg(sender, String.format("&c%s Chance:&e %.2f%%", PASSIVE_2, gracefulChance*100));
		HorseRPG.msg(sender, String.format("&c%s Chance:&e %.2f%%", PASSIVE_3, dodgeChance*100));
	}
	
	@Override
	public void update() {
		rollChance = level / 500.0;
		gracefulChance = level / 1000.0;
		dodgeChance = level / 4000.0;
	}
	
}
