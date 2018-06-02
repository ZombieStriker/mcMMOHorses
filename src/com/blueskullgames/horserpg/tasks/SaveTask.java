package com.blueskullgames.horserpg.tasks;

import org.bukkit.scheduler.BukkitRunnable;

import com.blueskullgames.horserpg.HorseRPG;

public class SaveTask extends BukkitRunnable {

    public void run() {
    	HorseRPG.saveHorses(null);
    }
    
}