/*
 * Copyright (C) 2011 No Heroes.
 * See readme for license details.
 */
package com.noheroes.seedreward.listeners;

import com.noheroes.seedreward.SeedReward;
import com.noheroes.seedreward.internals.PlayerLookupThread;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

/**
 *
 * @author PIETER
 */
public class SRPlayerListener extends PlayerListener {
    
    SeedReward instance;
    // Stores task ID's associated with each player's login
    private final HashMap<Player, Integer> playerTaskID = new HashMap<Player, Integer>();
    
    public SRPlayerListener(SeedReward instance){
        this.instance = instance;
    }
    
    @Override
    public void onPlayerJoin(PlayerJoinEvent playerEvent)
    {
        Player player = playerEvent.getPlayer();
        Integer taskID;
        
        // Checks if a task for the player is already running to prevent multiple 
        // tasks from triggering when logging in and out in quick succession
        if (playerTaskID.containsKey(player)) {
            taskID = playerTaskID.get(player);
            if(instance.getServer().getScheduler().isCurrentlyRunning(taskID) 
                    || instance.getServer().getScheduler().isQueued(taskID))
                return;
        }
        
        PlayerLookupThread lookupThread = new PlayerLookupThread(playerEvent, this);
        taskID = instance.getServer().getScheduler().scheduleAsyncDelayedTask(instance, lookupThread);
        synchronized (playerTaskID) {
            playerTaskID.put(player, taskID);
        }
    }
    
    public synchronized void removeTask(Player player) {
        playerTaskID.remove(player);
    }
}
