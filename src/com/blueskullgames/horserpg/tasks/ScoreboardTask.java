package com.blueskullgames.horserpg.tasks;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import com.blueskullgames.horserpg.HorseRPG;

public class ScoreboardTask implements Runnable {

	private Player p;
	private Scoreboard oldsb;
	public ScoreboardTask(Player p,Scoreboard oldsb) {
		this.p = p;
		this.oldsb = oldsb;
	}
	
	@Override
	public void run() {
		p.setScoreboard(oldsb);
		HorseRPG.playersWithScoreboards.remove(p.getName());
	}
}