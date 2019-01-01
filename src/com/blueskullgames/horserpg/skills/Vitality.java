package com.blueskullgames.horserpg.skills;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.blueskullgames.horserpg.HorseRPG;
import com.blueskullgames.horserpg.RPGHorse;

public class Vitality extends Skill {
	
	public static final String NAME 		= "Vitality";
	public static final String PASSIVE_1 	= "Revitalize";
	public static final String PASSIVE_2	= "Rapid Revitalize";
	public static final String PASSIVE_3	= "Juggernaut";
	
	public double revitalizeChance;
	public double rapidChance;
	
	public int healthBonus;
	public int duration;
	
	public Vitality(RPGHorse h, int xp) {
		super(h, NAME, xp);
		update();
	}

	@SuppressWarnings("deprecation")
	public void heal(boolean rapid) {
		if (h.getHorse() != null) {
			((LivingEntity) h.getHorse()).addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, duration, rapid ? 3 : 6, true));
			Player p ;
			try{
				p = !h.getHorse().getPassengers().isEmpty()? (Player)h.getHorse().getPassengers().get(0) : null;			
			}catch(Exception|Error e){
			p = h.getHorse().getPassenger() != null ? (Player)h.getHorse().getPassenger() : null;
			}
			addXP(5, p);
			if (p != null)
				HorseRPG.msg(p, "&a**HEALING**");
		}
	}
	
	@Override
	public void stats(CommandSender sender) {
		sender.sendMessage("");
		HorseRPG.msg(sender, "&c-----[]&a" + NAME.toUpperCase() + "&c[]-----");
		HorseRPG.msg(sender, "&8XP GAIN:&f Feeding Horse and Taking Damge On Horse");
		HorseRPG.msg(sender, "&8LVL:&a " + level + "&3 XP&e(&6" + xp + "&e/&6" + (levelToXP(level+1)+1) + "&e)");
		
		HorseRPG.msg(sender, "&c-----[]&aEFFECTS&c[]-----");
		HorseRPG.msg(sender, "&3" + PASSIVE_1 + ":&a Regenerates health after taking damage");
		HorseRPG.msg(sender, "&3" + PASSIVE_2 + ":&a Twice as effective as revitalize");
		HorseRPG.msg(sender, "&3" + PASSIVE_3 + ":&a Increases maximum health");
		
		HorseRPG.msg(sender, "&c-----[]&a" + h.name.toUpperCase() + " STATS&c[]-----");
		HorseRPG.msg(sender, String.format("&c%s Chance:&e %.3f%% (%ds)", PASSIVE_1, revitalizeChance*100, duration/20));
		HorseRPG.msg(sender, String.format("&c%s Chance:&e %.3f%% (%ds)", PASSIVE_2, rapidChance*100, duration/20));
		HorseRPG.msg(sender, String.format("&c%s Bonus:&e +%d", PASSIVE_3, healthBonus));
	}
	
	@Override
	public void update() {
		revitalizeChance = level / 500.0;
		rapidChance = level / 1000.0;
		
		healthBonus = (int)(level / 62.5);
		if (healthBonus > 16)
			healthBonus = 16;
		duration 	= (int)(level / 20.0) + 20;
	}
	
}
