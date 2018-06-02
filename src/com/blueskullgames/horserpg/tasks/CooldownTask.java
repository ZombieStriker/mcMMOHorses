package com.blueskullgames.horserpg.tasks;

import org.bukkit.scheduler.BukkitRunnable;

import com.blueskullgames.horserpg.HorseRPG;
import com.blueskullgames.horserpg.RPGHorse;

public class CooldownTask extends BukkitRunnable {
	
	@Override
    public void run() {
    	for (RPGHorse h : HorseRPG.horses) {
	    	if (h.isBanished || h.isDead)
	    		h.tick();
	    	else {
	    		if (!h.swiftness.refreshed)
	    			h.swiftness.tick();
	    		if (!h.wrath.refreshed)
	    			h.wrath.tick();
	    	}
    	}
    }
    
}