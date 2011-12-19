/*
 * Copyright (C) 2011 No Heroes.
 * See readme for license details.
 */
package com.noheroes.seedreward.listeners;

import com.noheroes.seedreward.SeedReward;

import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;

/**
 *
 * @author PIETER
 */
public class SRPluginListener extends ServerListener {
    
    SeedReward instance;
    
    public SRPluginListener(SeedReward instance){
        this.instance = instance;
    }

    @Override
    public void onPluginEnable(PluginEnableEvent event) {
        super.onPluginEnable(event);
        //iconomy late loading here.
    }

    @Override
    public void onPluginDisable(PluginDisableEvent event) {
        super.onPluginDisable(event);
        //iconomy unloading here (if necessary).
    }
    
    
}
