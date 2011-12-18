/*
 * Copyright (C) 2011 No Heroes.
 * See readme for license details.
 */
package com.noheroes.seedreward.listeners;

import com.noheroes.seedreward.SeedReward;
import com.noheroes.seedreward.internals.PlayerLookupThread;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

/**
 *
 * @author PIETER
 */
public class SRPlayerListener extends PlayerListener {
    
    SeedReward instance;
    
    public SRPlayerListener(SeedReward instance)
    {
        this.instance = instance;
    }
    
    public void onPlayerJoin(PlayerJoinEvent playerEvent)
    {
        PlayerLookupThread lookupThread = new PlayerLookupThread(playerEvent);
        instance.getServer().getScheduler().scheduleAsyncDelayedTask(instance, lookupThread);
    }
}
