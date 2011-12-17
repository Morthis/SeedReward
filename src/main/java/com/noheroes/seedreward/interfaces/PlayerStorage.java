/*
 * Copyright (C) 2011 No Heroes.
 * See readme for license details.
 */
package com.noheroes.seedreward.interfaces;

import org.bukkit.entity.Player;


public interface PlayerStorage {
    
    public void setPlayer(Player player);
    public Player getPlayer();
    public void setSteamID(String steamID);
    public String getSteamID();
    public void setReward(long amount);
    public long getReward();
    
}
