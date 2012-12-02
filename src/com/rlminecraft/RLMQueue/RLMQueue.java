package com.rlminecraft.RLMQueue;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

public class RLMQueue extends JavaPlugin {
	Logger console;
	List<QueuedPlayer> loginQueue;
	long timeout;
	
	/**
	 * Called upon enabling of plugin
	 */
	public void onEnable() {
		// Get logger
		console = this.getLogger();
		
		// Initialize queue
		loginQueue = new LinkedList<QueuedPlayer>();
		timeout = getConfig().getInt("timeout", 90) * 1000;
		
		// Register event lister
		this.getServer().getPluginManager().registerEvents(new LoginListener(this), this);
	}
	
	/**
	 * Called upon disabling of plugin
	 */
	public void onDisable() {
		// do nothing
	}
	
	/**
	 * Adds or updates a player in the queue.
	 * @param name
	 */
	public void addToQueue (String name) {
		// Check if already in queue
		ListIterator<QueuedPlayer> playerIterator = loginQueue.listIterator();
		while (playerIterator.hasNext()) {
			int i = playerIterator.nextIndex();
			QueuedPlayer player = playerIterator.next();
			if (player.getPlayer() == name) {
				player.updateLoginTime();
				loginQueue.set(i, player);
				return;
			}
		}
		// At this point, the player is not in the queue.
		// Now, they must be pushed to the end of the queue.
		loginQueue.add(new QueuedPlayer(name));
	}
	
	/**
	 * Removes a player from the queue (due to successful login or other reasons).
	 * This method assumes that each player is only in the queue once.
	 * @param name the player to remove from the queue
	 */
	public void removeFromQueue (String name) {
		// Check if already in queue
		ListIterator<QueuedPlayer> playerIterator = loginQueue.listIterator();
		while (playerIterator.hasNext()) {
			QueuedPlayer player = playerIterator.next();
			if (player.getPlayer() == name) {
				playerIterator.remove();
				return;
			}
		}
	}
	
	/**
	 * Returns the player's position in the queue
	 * @param name the player to check
	 * @return the player's position
	 */
	public int getQueuePosition (String name) {
		ListIterator<QueuedPlayer> playerIterator = loginQueue.listIterator();
		int i = 0;
		while (playerIterator.hasNext()) {
			i++;
			if (playerIterator.next().getPlayer() == name) return i;
		}
		return 0;
	}
	
	/**
	 * Removes all expired players from the queue.
	 */
	public void cleanupQueue () {
		ListIterator<QueuedPlayer> playerIterator = loginQueue.listIterator();
		while (playerIterator.hasNext()) {
			QueuedPlayer player = playerIterator.next();
			if (player.isExpired(timeout)) playerIterator.remove();
		}
	}
}
