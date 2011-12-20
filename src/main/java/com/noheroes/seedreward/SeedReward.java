/*
 * Copyright (C) 2011 No Heroes.
 * See readme for license details.
 */
package com.noheroes.seedreward;

import com.noheroes.seedreward.internals.Properties;
import com.noheroes.seedreward.internals.SQLStorage;
import com.noheroes.seedreward.internals.SRMessageQueue;
import com.noheroes.seedreward.internals.SRMessage;
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
        
        server = getServer();   
        
        loadSRConfig(this.getConfig());
        this.saveConfig();

        messageQueue = new SRMessageQueue(this);
        messageQueue.startScheduler();
        
        db = new SQLStorage(this);
        
        PluginDescriptionFile pdf = this.getDescription();
        log(Level.INFO, pdf.getName() + " version " + pdf.getVersion() + " is enabled.");
    }
    
    public static void log(Level level, String msg)
    {
        logger.log(level, msg);
    }
    
    public static void broadcast(String msg)
    {
        server.broadcastMessage(msg);
    }   
        
    public SQLStorage getDB(){
        return this.db;
    }
    
    private void loadSRConfig(FileConfiguration config) 
    {
        config.options().copyDefaults(true);

        Properties.playerDBURL = config.getString("PlayerDB.url");
        Properties.playerDBUser = config.getString("PlayerDB.User");
        Properties.playerDBPass = config.getString("PlayerDB.Pass");
        
        Properties.rewardDBURL = config.getString("RewardDB.url");
        Properties.rewardDBUser = config.getString("RewardDB.User");
        Properties.rewardDBPass = config.getString("RewardDB.Pass");
    }    
}
