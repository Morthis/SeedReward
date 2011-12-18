/*
 * Copyright (C) 2011 No Heroes.
 * See readme for license details.
 */
package com.noheroes.seedreward.internals;

import org.bukkit.event.player.PlayerJoinEvent;

/**
 *
 * @author PIETER
 */
public class PlayerLookupThread implements Runnable {
    
    PlayerJoinEvent playerEvent;
    
    public PlayerLookupThread(PlayerJoinEvent playerEvent)
    {
        this.playerEvent = playerEvent;
    }
    
    @Override
    public void run()
    {
        
    }
}
