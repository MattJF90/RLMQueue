package com.rlminecraft.RLMQueue;

public class QueuedPlayer {
	private String name;
	private long lastLogin;
	
	public QueuedPlayer (String player) {
		this.name = player;
		this.lastLogin = System.currentTimeMillis();
	}
	
	public void updateLoginTime () {
		this.lastLogin = System.currentTimeMillis();
	}
	
	public boolean isExpired (long timeout) {
		return (System.currentTimeMillis() > this.lastLogin + timeout);
	}
	
	public String getPlayer () {
		return this.name;
	}
	
}
