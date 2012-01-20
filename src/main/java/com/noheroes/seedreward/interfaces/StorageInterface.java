/*
 * Copyright (C) 2011 No Heroes.
 * See readme for license details.
 */
package com.noheroes.seedreward.interfaces;

import org.bukkit.entity.Player;

public interface StorageInterface {
    
    /**
     * Queries db for STEAMID.
     * @param player
     * @return If player is VIP, returns STEAM ID, if not, return null.
     */
    public String getPlayerSteam(Player player);
    
    /**
     * Retrieve the player's reward from the reward db.
     * @param steamID The Player's steamID
     * @return The reward in long.
     */
    public long getPlayerReward(String steamID);
    
    /**
     * Reset the player's reward in the reward db.
     * @param steamID The Player's steamID
     * @return Success/Failure
     */
    public boolean resetPlayerReward(String steamID);
    
    /*
     * The following is optional:
     * We can reward the player through the database, or through a
     * hook to iconomy in-game.  Investigate which is the safest, considering 
     * both thread-safe, and potential db corruption issues.
     * My gut is that we should just hook iconomy and do it in game.
     */
    
    /**
     * Increments the iconomy database for the player.
     * @param player
     * @param amount Amount to increment.
     * @return Success/Failure
     */
    public boolean rewardPlayer(Player player, long amount);
    
     /**
     * Decrements the iconomy database for the player.
     * @param player
     * @param amount Amount to decrement.
     * @return Success/Failure
     */
    public boolean chargePlayer(Player player, long amount);
    
}
