/*
 * Copyright (C) 2011 No Heroes.
 * See readme for license details.
 */
package com.noheroes.seedreward.internals;

import com.noheroes.seedreward.SeedReward;
import com.noheroes.seedreward.listeners.SRPlayerListener;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 *
 * @author PIETER
 */
public class PlayerLookupThread implements Runnable {
    
    PlayerJoinEvent playerEvent;
    Player player;
    
    public PlayerLookupThread(PlayerJoinEvent playerEvent) {
        this.playerEvent = playerEvent;
        this.player = playerEvent.getPlayer();
    }
    
    @Override
    public void run(){
        //SeedReward.broadcast("Player Joined Test Message");
        
        SeedReward.addMessage(playerEvent.getPlayer().getName() + " has earned no ducats");
        
        // Removes the task ID from the hashmap, this should be the last line so that it is executed right before the thread finishes
        SRPlayerListener.removeTask(playerEvent.getPlayer().getName());
    }
}
