/*
 * Copyright (C) 2011 No Heroes.
 * See readme for license details.
 */
package com.noheroes.seedreward;

import com.noheroes.seedreward.interfaces.Balance;
import com.noheroes.seedreward.internals.DummyBalance;
import com.noheroes.seedreward.internals.Properties;
import com.noheroes.seedreward.internals.SQLStorage;
import com.noheroes.seedreward.internals.SRMessageQueue;
import com.noheroes.seedreward.internals.iConomy5Balance;
import com.noheroes.seedreward.internals.iConomy6Balance;
import com.noheroes.seedreward.listeners.SRPlayerListener;
import com.noheroes.seedreward.listeners.SRPluginListener;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class SeedReward extends JavaPlugin {
    
    private static final Logger logger = Logger.getLogger("Minecraft");
    
    private final SRPlayerListener playerListener = new SRPlayerListener(this);
    private final SRPluginListener pluginListener = new SRPluginListener(this);
    
    private static SRMessageQueue messageQueue;
    private static Balance iConomy = null;
    
    private SQLStorage db;
    private static Server server;

    public void onDisable() {
        PluginDescriptionFile pdf = this.getDescription();
        log(Level.INFO, pdf.getName() + " is disabled");
        messageQueue.stopScheduler();
    }

    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Type.PLAYER_JOIN, playerListener, Priority.Monitor, this);
        pm.registerEvent(Type.PLUGIN_ENABLE, pluginListener, Priority.Monitor, this);
        pm.registerEvent(Type.PLUGIN_DISABLE, pluginListener, Priority.Monitor, this);
        
        server = getServer();   
        
        loadSRConfig(this.getConfig());
        this.saveConfig();

        messageQueue = new SRMessageQueue(this);
        messageQueue.startScheduler();
        
        db = new SQLStorage(this);
        
        //We're using a softdepend, so the following should hook an iconomy.  
        //We do have a plugin listener active, in case the iconomy plugin is enabled
        //or disabled after this loads. We try 6, then 5, then dummy, in case more than
        //one is active.
        if(pm.isPluginEnabled("iConomy")){
            if(pm.getPlugin("iConomy").getClass().getName().equals("com.iCo6.iConomy")) {
                iConomy = new iConomy6Balance(this, (com.iCo6.iConomy)pm.getPlugin("iConomy"));
                SeedReward.log(Level.INFO, "Hooked iConomy 6.");
            } 

            else if(pm.getPlugin("iConomy").getClass().getName().equals("com.iConomy.iConomy")) {
                iConomy = new iConomy5Balance(this, (com.iConomy.iConomy)pm.getPlugin("iConomy"));
                SeedReward.log(Level.INFO, "Hooked iConomy 5.");
            } 
            
            else {
                //May be iconomy 4 or other unsupported version.
                iConomy = new DummyBalance(this);
                SeedReward.log(Level.INFO, "No economy plugin found. Using Dummy economy.");
            }
            
        } else {
            //No iconomy found.  Just use dummy balance until iconomy is loaded.
            iConomy = new DummyBalance(this);
            SeedReward.log(Level.INFO, "No economy plugin found. Using Dummy economy.");
        }
        
        PluginDescriptionFile pdf = this.getDescription();
        log(Level.INFO, pdf.getName() + " version " + pdf.getVersion() + " is enabled.");
    }
    
    public static void log(Level level, String msg) {
        logger.log(level, msg);
    }
    
    public static void broadcast(String msg) {
        server.broadcastMessage(msg);
    }   
        
    public SQLStorage getDB(){
        return this.db;
    }
    
    public static Balance getBalanceHandler() {
        return iConomy;
    }
    
    public static void setBalanceHandler(Balance handler) {
        iConomy = handler;
    }
    
    private void loadSRConfig(FileConfiguration config) {
        config.options().copyDefaults(true);

        Properties.playerDBURL = config.getString("PlayerDB.url");
        Properties.playerDBUser = config.getString("PlayerDB.User");
        Properties.playerDBPass = config.getString("PlayerDB.Pass");
        
        Properties.rewardDBURL = config.getString("RewardDB.url");
        Properties.rewardDBUser = config.getString("RewardDB.User");
        Properties.rewardDBPass = config.getString("RewardDB.Pass");
        
        Properties.ConnectionURL = config.getString("LookupURL");
    }  
}
