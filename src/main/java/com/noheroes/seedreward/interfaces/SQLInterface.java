/*
 * Copyright (C) 2011 Sorklin <sorklin at gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.noheroes.seedreward.interfaces;

import org.bukkit.entity.Player;

public interface SQLInterface {
    
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
    
}
