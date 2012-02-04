/*
 * Copyright (C) 2011 No Heroes.
 * See readme for license details.
 */
package com.noheroes.seedreward;


import com.noheroes.seedreward.internals.Properties;
import com.noheroes.seedreward.internals.SQLStorage;
import com.noheroes.seedreward.internals.SRMessageQueue;
import com.noheroes.seedreward.listeners.SRPlayerListener;
import com.noheroes.seedreward.listeners.SRPluginListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;


public class SeedReward extends JavaPlugin {
    
    private static final Logger logger = Logger.getLogger("Minecraft");
    
    private final SRPlayerListener playerListener = new SRPlayerListener(this);
    private final SRPluginListener pluginListener = new SRPluginListener(this);
    
    private static SRMessageQueue messageQueue;
    //private static Balance iConomy = null;
    
    private SQLStorage db;
    private static Server server;
    
    public static Economy econ = null;

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

        PluginDescriptionFile pdf = this.getDescription();
    
        if (!setupEconomy()) {
            log(Level.SEVERE, pdf.getName() + " - Vault failed to hook into server economy plugin");
        }
        
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
    
    public boolean EconomyHooked()
    {
        return econ != null;
    }

    private void loadSRConfig(FileConfiguration config) {
        
        //config.addDefault("Messages.Reward.Send", true);
        //config.addDefault("Messages.Reward.Message", "You earned $R$ ducats today from seeding the TF2 Servers. Good Job!");
        
        
        config.options().copyDefaults(true);

        Properties.playerDBURL = config.getString("PlayerDB.url");
        Properties.playerDBUser = config.getString("PlayerDB.User");
        Properties.playerDBPass = config.getString("PlayerDB.Pass");
        
        Properties.rewardDBURL = config.getString("RewardDB.url");
        Properties.rewardDBUser = config.getString("RewardDB.User");
        Properties.rewardDBPass = config.getString("RewardDB.Pass");
        
        Properties.ConnectionURL = config.getString("LookupURL");
        
        Properties.showRewardMsg = config.getBoolean("Messages.Reward.Send");
        Properties.rewardMsg = config.getString("Messages.Reward.Message");
               
    }     
    
     private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        econ = rsp.getProvider();
        return econ != null;
    }
}
