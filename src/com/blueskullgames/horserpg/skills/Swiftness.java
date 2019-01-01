package com.blueskullgames.horserpg.skills;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.blueskullgames.horserpg.HorseRPG;
import com.blueskullgames.horserpg.RPGHorse;

public class Swiftness extends Skill {
	
	public static final String NAME 	= "Swiftness";
	public static final String ABILITY 	= "Super Speed";

	public boolean refreshed;
	
	public int sprintCd;
	
	public int amplitude;
	public int duration;
	
	/** Constructs a swiftness skill
	 * @param h is the horse to add the skill to
	 * @param xp is the current xp
	 */
	public Swiftness(RPGHorse h, int xp) {
		super(h, NAME, xp);
		refreshed = true;
		sprintCd = HorseRPG.sprintCooldown;
		update();
	}

	@SuppressWarnings("deprecation")
	public void sprint() {
		if (h.getHorse() != null) {
			((LivingEntity) h.getHorse()).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duration, amplitude, true));
			refreshed = false;
			try{
				addXP(5, !h.getHorse().getPassengers().isEmpty() ? (Player)h.getHorse().getPassengers().get(0) : null);				
			}catch(Exception|Error e){			
			addXP(5, h.getHorse().getPassenger() != null ? (Player)h.getHorse().getPassenger() : null);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public void tick() {
		sprintCd--;
		if (sprintCd <= 0) {
			sprintCd = HorseRPG.sprintCooldown;
			refreshed = true;

			try{
				if (h.getHorse() != null && !h.getHorse().getPassengers().isEmpty())
					HorseRPG.msg((Player)h.getHorse().getPassengers().get(0), HorseRPG.SKILL_REFRESH.replaceAll("%ability%",ABILITY));		
			}catch(Exception|Error e){			
			if (h.getHorse() != null && h.getHorse().getPassenger() != null)
				HorseRPG.msg((Player)h.getHorse().getPassenger(), HorseRPG.SKILL_REFRESH.replaceAll("%ability%",ABILITY));
			}
		}
	}
	
	@Override
	public void stats(CommandSender sender) {
		sender.sendMessage("");
		HorseRPG.msg(sender, "&c-----[]&a" + NAME.toUpperCase() + "&c[]-----");
		HorseRPG.msg(sender, "&8XP GAIN:&f Sprinting and Riding Horse");
		HorseRPG.msg(sender, "&8LVL:&a " + level + "&3 XP&e(&6" + xp + "&e/&6" + (levelToXP(level+1)+1) + "&e)");
		
		HorseRPG.msg(sender, "&c-----[]&aEFFECTS&c[]-----");
		HorseRPG.msg(sender, "&3" + ABILITY + ":&a Temporary Speed Boost");
		
		HorseRPG.msg(sender, "&c-----[]&a" + h.name.toUpperCase() + " STATS&c[]-----");
		HorseRPG.msg(sender, String.format("&c%s Boost:&e +", ABILITY, amplitude));
		HorseRPG.msg(sender, String.format("&c%s Length:&e %ds", ABILITY, duration/20));
	}
	
	@Override
	public void update() {
		amplitude = 1 + (int)(level / 125.0);
		duration = 40 + (int)(level / 10.0);
	}
}
