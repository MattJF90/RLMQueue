package com.rlminecraft.RLMQueue;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;

/**
 * <b>Listener</b><br>
 * Listens for any events required by RLMShop
 * @author Matt Fielding
 */
public class LoginListener implements Listener {
	
	private RLMQueue plugin;
	
	/**
	 * <b>ShopListener Constructor</b><br>
	 * Listens for any events required by RLMShop
	 * @param instance
	 */
	public LoginListener(RLMQueue instance) {
		plugin = instance;
	}
	
	@EventHandler
	public void onPlayerInteract (AsyncPlayerPreLoginEvent event) {
		plugin.cleanupQueue();
		int online = plugin.getServer().getOnlinePlayers().length;
		int cap = plugin.getServer().getMaxPlayers();
		if (online >= cap - plugin.loginQueue.size() - 1) {
			// Determine if player is in queue
			int pos = plugin.getQueuePosition(event.getName());
			
			// New Player
			if (pos == 0) {
				plugin.addToQueue(event.getName());
				event.disallow(Result.KICK_FULL,
					"You have been added to the login queue (position " + plugin.loginQueue.size() + ")");
			
			// Queued Player
			} else {
				// Check if player is close enough to the front of the queue
				if (online + pos < cap - plugin.loginQueue.size()) { //Login
					plugin.removeFromQueue(event.getEventName());
				} else {
					plugin.addToQueue(event.getName());
					event.disallow(Result.KICK_FULL,
						"You are at position " + plugin.loginQueue.size() + " / "
						+ plugin.loginQueue.size() + " in the login queue. Be"
						+ "sure to keep trying at least every " + (int)(plugin.timeout/1000)
						+ " seconds.");
				}
			}
		} else {
			if (!plugin.loginQueue.isEmpty()) plugin.removeFromQueue(event.getName());
		}
	}
	
}
