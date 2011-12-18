/*
 * Copyright (C) 2011 No Heroes.
 * See readme for license details.
 */
package com.noheroes.seedreward.internals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author PIETER
 */
public class SRConfig {
    
    private YamlConfiguration config;
    private HashMap<String, Object> configDefaults = new HashMap<String, Object>();
    
    public SRConfig(File configFile)
    {
        config = new YamlConfiguration();
     
        // Set defaults for config file
        configDefaults.put("ForumDB", "Link");
        configDefaults.put("RewardDB", "Link");
        
        if (!configFile.exists())
        {
            for (String key : configDefaults.keySet())
            {
                config.set(key, configDefaults.get(key));
            }
                
            try {       
                config.save(configFile);
            } catch (IOException ex) {
                Logger.getLogger(SRConfig.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
        {
            try {
                config.load(configFile);
            } catch (Exception ex) {
                Logger.getLogger(SRConfig.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public String getString(String key)
    {
        return config.getString(key);
    }
}
