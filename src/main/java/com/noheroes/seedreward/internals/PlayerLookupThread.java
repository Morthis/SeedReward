/*
 * Copyright (C) 2011 No Heroes.
 * See readme for license details.
 */
package com.noheroes.seedreward.internals;
import com.noheroes.seedreward.SeedReward;
import com.noheroes.seedreward.listeners.SRPlayerListener;

import java.util.logging.Level;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 *
 * @author PIETER
 */
public class PlayerLookupThread implements Runnable {
    
    PlayerJoinEvent playerEvent;
    Player player;
    SRPlayerListener pl;
    SeedReward sr;
    String steamID;
    Long reward;
    
    ChatColor r = ChatColor.RED;
    ChatColor w = ChatColor.WHITE;
    
    public PlayerLookupThread(PlayerJoinEvent playerEvent, SRPlayerListener pl, SeedReward sr) {
        this.playerEvent = playerEvent;
        this.player = playerEvent.getPlayer();
        this.pl = pl;
        this.sr = sr;
    }
    
    @Override
    public void run(){ 
        if (!sr.EconomyHooked())
        {
            taskCleanup();
            return;
        }
        
        steamID = sr.getDB().getPlayerSteam(player);
        
        if(steamID == null)
        {
            taskCleanup();
            return;
        }
        
        reward = sr.getDB().getPlayerReward(steamID);
        
        if (reward == 0)
        {
            SRMessageQueue.addMessage(new SRMessage(
                    r + "[Server]" + w + " You have not earned any ducats from seeding. To learn more type /help seeding.", player));
        }
        else
        {
            if (sr.getDB().resetPlayerReward(steamID))
            {
                if (sr.getDB().rewardPlayer(player, reward)) {
                    SRMessageQueue.addMessage(new SRMessage(
                            r + "[Server]" + w + " You have earned " + reward.toString() + " ducats from seeding.", player));
                    SRMessageQueue.addMessage(new SRMessage(
                            r + "[Server] " + w + player.getName() + " has earned " + reward.toString() +
                            " ducats from seeding.  Type /help seeding to learn more."));
                }
                else
                {
                    sr.log(Level.SEVERE, "Failed to add " + reward.toString() + " ducats to " + player.getName() + "'s account.");
                }
            }
        }
        
        // Removes the task ID from the hashmap, this should be the last line so that it is executed right before the thread finishes
        taskCleanup();
    }
    
    private void taskCleanup()
    {
        pl.removeTask(player);
    }
}
