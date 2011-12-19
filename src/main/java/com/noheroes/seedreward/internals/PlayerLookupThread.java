/*
 * Copyright (C) 2011 No Heroes.
 * See readme for license details.
 */
package com.noheroes.seedreward.internals;
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
    SRPlayerListener pl;
    
    public PlayerLookupThread(PlayerJoinEvent playerEvent, SRPlayerListener pl) {
        this.playerEvent = playerEvent;
        this.player = playerEvent.getPlayer();
        this.pl = pl;
    }
    
    @Override
    public void run(){
        //SeedReward.broadcast("Player Joined Test Message");
        SRMessageQueue.addMessage(new SRMessage(player, 
                player.getName() + " has earned no ducats"));
        
        // Removes the task ID from the hashmap, this should be the last line so that it is executed right before the thread finishes
        pl.removeTask(player);
    }
}
