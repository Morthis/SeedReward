/*
 * Copyright (C) 2011 No Heroes.
 * See readme for license details.
 */
package com.noheroes.seedreward.listeners;

import com.noheroes.seedreward.SeedReward;
import com.noheroes.seedreward.internals.PlayerLookupThread;
import java.util.HashMap;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

/**
 *
 * @author PIETER
 */
public class SRPlayerListener extends PlayerListener {
    
    SeedReward instance;
    // Stores task ID's associated with each player's login
    static HashMap<String, Integer> playerTaskID = new HashMap<String, Integer>();
    
    public SRPlayerListener(SeedReward instance)
    {
        this.instance = instance;
    }
    
    public void onPlayerJoin(PlayerJoinEvent playerEvent)
    {
        String playerName = playerEvent.getPlayer().getName();
        Integer taskID;
        
        // Checks if a task for the player is already running to prevent multiple tasks from triggering when logging in and out in quick succession
        if (playerTaskID.get(playerName) != null) {
            taskID = playerTaskID.get(playerName);
            if(instance.getServer().getScheduler().isCurrentlyRunning(taskID) || instance.getServer().getScheduler().isQueued(taskID))
                return;
        }
        
        PlayerLookupThread lookupThread = new PlayerLookupThread(playerEvent);
        taskID = instance.getServer().getScheduler().scheduleAsyncDelayedTask(instance, lookupThread);
        playerTaskID.put(playerName, taskID);
    }
     
    
    public static void removeTask(String playerName)
    {
        playerTaskID.remove(playerName);
    }
}
