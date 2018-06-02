package com.blueskullgames.horserpg.configs;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.blueskullgames.horserpg.HorseRPG;

public class MessagesConfigHandler {
	
	private File file;
	private FileConfiguration config;
	
	private BukkitTask saveTask;
	
	public MessagesConfigHandler(File mess) {
		this.file = mess;
		if(!this.file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		config = YamlConfiguration.loadConfiguration(file);
	}

	public String a(String path, String a) {
		if(config.contains(path)) {
			return config.getString(path);
		}
		config.set(path, a);
		if(saveTask==null) {
			saveTask = new BukkitRunnable() {
				
				@Override
				public void run() {
					save();
					saveTask = null;					
				}
			}.runTaskLater(HorseRPG.instance, 1);
		}
		return a;
	}
	
	public void save() {
		try {
			config.save(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
