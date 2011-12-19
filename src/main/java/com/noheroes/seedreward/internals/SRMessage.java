/*
 * Copyright (C) 2011 No Heroes.
 * See readme for license details.
 */
package com.noheroes.seedreward.internals;

import org.bukkit.entity.Player;

/**
 *
 * @author Sorklin <sorklin at gmail.com>
 */
public class SRMessage {
    private Player player;
    private String message;
    
    public SRMessage(Player player, String message){
        this.player = player;
        this.message = message;
    }
    
    public String getMessage(){
        return this.message;
    }
    
    public Player getPlayer(){
        return this.player;
    }
}
