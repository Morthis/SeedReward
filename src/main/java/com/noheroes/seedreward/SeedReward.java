/*
 * Copyright (C) 2011 No Heroes.
 * See readme for license details.
 */
package com.noheroes.seedreward;

import com.noheroes.seedreward.internals.SQLStorage;
import com.noheroes.seedreward.internals.SRConfig;
import com.noheroes.seedreward.internals.SRMessageQueue;
import com.noheroes.seedreward.listeners.SRPlayerListener;
import com.noheroes.seedreward.listeners.SRPluginListener;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class SeedReward extends JavaPlugin {
    
    private static final Logger logger = Logger.getLogger("Minecraft");
    
    private final SRPlayerListener playerListener = new SRPlayerListener(this);
    private final SRPluginListener pluginListener = new SRPluginListener(this);
    private static SRMessageQueue messageQueue;
    
    private SQLStorage db;
    
    private static Server server;
    public SRConfig config;
    private String pluginPath;
    public File configFile;

    public void onDisable() {
        log(Level.INFO, "Plugin SeedRewards disabled");
        messageQueue.stopScheduler();
    }

    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Type.PLAYER_JOIN, playerListener, Priority.Monitor, this);
        pm.registerEvent(Type.PLUGIN_ENABLE, pluginListener, Priority.Monitor, this);
        
        //Needs rewrite:
        pluginPath = getDataFolder().getAbsolutePath();
        configFile = new File(pluginPath + File.separatorChar + "config.yml");
        log(Level.INFO, "Plugin SeedRewards enabled");
        config = new SRConfig(configFile);

        server = getServer();   

        messageQueue = new SRMessageQueue(this);
        messageQueue.startScheduler();
        
        db = new SQLStorage(this);
    }
    
    public static void log(Level level, String msg)
    {
        logger.log(level, msg);
    }
    
    public static void broadcast(String msg)
    {
        server.broadcastMessage(msg);
    }
    
    public static void addMessage(String msg)
    {
        messageQueue.addMessage(msg);
    }
        
    protected SQLStorage getDB(){
        return this.db;
    }
}
