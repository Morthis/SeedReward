/*
 * Copyright (C) 2011 No Heroes.
 * See readme for license details.
 */
package com.noheroes.seedreward.listeners;

import com.noheroes.seedreward.SeedReward;
import com.noheroes.seedreward.internals.DummyBalance;
import com.noheroes.seedreward.internals.iConomy5Balance;
import com.noheroes.seedreward.internals.iConomy6Balance;

import java.util.logging.Level;

import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;
import org.bukkit.plugin.Plugin;


public class SRPluginListener extends ServerListener {
    
    private SeedReward sr;
    
    public SRPluginListener(SeedReward instance){
        this.sr = instance;
    }

    @Override
    public void onPluginEnable(PluginEnableEvent event) {
        Plugin p = event.getPlugin();
        
        if(p.getClass().getName().equals("com.iCo6.iConomy")) {
            SeedReward.setBalanceHandler(new iConomy6Balance(sr, (com.iCo6.iConomy)p));
            SeedReward.log(Level.INFO, "Hooked iConomy 6.");
        } 

        else if(p.getClass().getName().equals("com.iConomy.iConomy")) {
            SeedReward.setBalanceHandler(new iConomy5Balance(sr, (com.iConomy.iConomy)p));
            SeedReward.log(Level.INFO, "Hooked iConomy 5.");
        }
    }

    @Override
    public void onPluginDisable(PluginDisableEvent event) {
        Plugin p = event.getPlugin();
        if(p.getClass().getName().equals("com.iCo6.iConomy")) {
            SeedReward.setBalanceHandler(new DummyBalance(sr));
            SeedReward.log(Level.INFO, "Unhooked iConomy 6. Using Dummy balance handler.");
        }
        
        else if(p.getClass().getName().equals("com.iConomy.iConomy")) {
            SeedReward.setBalanceHandler(new DummyBalance(sr));
            SeedReward.log(Level.INFO, "Unhooked iConomy 5. Using Dummy balance handler.");
        }
    }
}
