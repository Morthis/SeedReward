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
    
    PlayerJoinEvent player;
    
    public PlayerLookupThread(PlayerJoinEvent player)
    {
        this.player = player;
    }
    
    @Override
    public void run()
    {
        
    }
}
