package com.blueskullgames.horserpg.skills;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.blueskullgames.horserpg.HorseRPG;
import com.blueskullgames.horserpg.RPGHorse;

public class Wrath extends Skill {

	public static final String NAME 		= "Wrath";
	public static final String PASSIVE_1 	= "Trample";
	public static final String PASSIVE_2	= "Infuriate";
	
	public boolean refreshed;

	public double infuriateChance;
	public int infuriateCD;
	public int duration;
	
	/** Constructs a wrath skill
	 * @param h is the horse to add the skill to
	 * @param xp is the current xp
	 */
	public Wrath(RPGHorse h, int xp) {
		super(h, NAME, xp);
		refreshed = true;
		infuriateCD = HorseRPG.infuriateCooldown;
		update();
	}
	
	@SuppressWarnings("deprecation")
	public void infuriate() {
		h.godmode = true;
		h.horse.setFireTicks(duration);
		h.horse.getWorld().strikeLightningEffect(h.horse.getLocation());
		((LivingEntity) h.horse).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duration, 2, true));
		refreshed = false;
		try{
			addXP(10, !h.horse.getPassengers().isEmpty() ? (Player)h.horse.getPassengers().get(0) : null);			
		}catch(Exception|Error e){
		addXP(10, h.horse.getPassenger() != null ? (Player)h.horse.getPassenger() : null);
		}
	}

	@SuppressWarnings("deprecation")
	public void tick() {
		infuriateCD--;
		if (infuriateCD < 120 - (duration/20) && h.godmode)
			h.godmode = false;
		
		if (infuriateCD <= 0) {
			infuriateCD = HorseRPG.infuriateCooldown;
			refreshed = true;
			
			try{
				if (h.horse != null && !h.horse.getPassengers().isEmpty())
					HorseRPG.msg((Player)h.horse.getPassengers().get(0), HorseRPG.SKILL_REFRESH.replaceAll("%ability%",PASSIVE_2));				
			}catch(Exception|Error e){
			if (h.horse != null && h.horse.getPassenger() != null)
				HorseRPG.msg((Player)h.horse.getPassenger(),HorseRPG.SKILL_REFRESH.replaceAll("%ability%",PASSIVE_2));
			}
		}
	}
	
	@Override
	public void stats(CommandSender sender) {
		sender.sendMessage("");
		HorseRPG.msg(sender, "&c-----[]&a" + NAME.toUpperCase() + "&c[]-----");
		HorseRPG.msg(sender, "&8XP GAIN:&f Taking Damage and Using Fury");
		HorseRPG.msg(sender, "&8LVL:&a " + level + "&3 XP&e(&6" + xp + "&e/&6" + (levelToXP(level+1)+1) + "&e)");
		
		HorseRPG.msg(sender, "&c-----[]&aEFFECTS&c[]-----");
		HorseRPG.msg(sender, "&3" + PASSIVE_2 + ":&a Unleashes horse fury");
		
		HorseRPG.msg(sender, "&c-----[]&a" + h.name.toUpperCase() + " STATS&c[]-----");
		HorseRPG.msg(sender, String.format("&c%s Chance:&e %.3f%% (%ds)", PASSIVE_2, infuriateChance*100, duration/20));
	}

	@Override
	public void update() {
		infuriateChance = level / 1000.0;
		duration = 80 + (int)(level / 10.0);
	}
	
}
