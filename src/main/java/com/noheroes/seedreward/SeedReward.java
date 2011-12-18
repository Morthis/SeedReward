/*
 * Copyright (C) 2011 No Heroes.
 * See readme for license details.
 */
package com.noheroes.seedreward;

import com.noheroes.seedreward.internals.SRConfig;
import com.noheroes.seedreward.listeners.SRPlayerListener;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class SeedReward extends JavaPlugin {
    
    private static final Logger logger = Logger.getLogger("Minecraft");
    private final SRPlayerListener playerListener = new SRPlayerListener(this);
    public SRConfig config;
    private String pluginPath;
    public File configFile;

    public void onDisable() {
        log(Level.INFO, "Plugin SeedRewards disabled");
    }

    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Type.PLAYER_JOIN, playerListener, Priority.Normal, this);
        pluginPath = getDataFolder().getAbsolutePath();
        configFile = new File(pluginPath + File.separatorChar + "config.yml");
        log(Level.INFO, "Plugin SeedRewards enabled");
        config = new SRConfig(configFile);
        
        String test = config.getString("Hehe");
        log(Level.INFO, test);
    }
    
    public void log(Level level, String msg)
    {
        logger.log(level, msg);
    }
}
