package com.blueskullgames.horserpg.tasks;

import org.bukkit.entity.Player;

import com.blueskullgames.horserpg.HorseRPG;

public class OfferTask implements Runnable {

	private Player p;
	
	/** Constructs an offer task
	 * @param p is the player being offered a horse
	 */
	public OfferTask(Player p) {
		this.p = p;
	}
	
	@Override
	public void run() {
		HorseRPG.offers.remove(p.getUniqueId());
	}
}