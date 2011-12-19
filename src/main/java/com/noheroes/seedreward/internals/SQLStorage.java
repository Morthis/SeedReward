/*
 * Copyright (C) 2011 No Heroes.
 * See readme for license details.
 */
package com.noheroes.seedreward.internals;

import com.noheroes.seedreward.SeedReward;
import com.noheroes.seedreward.interfaces.StorageInterface;
import org.bukkit.entity.Player;

/**
 *
 * @author Sorklin <sorklin at gmail.com>
 */
public class SQLStorage implements StorageInterface{

    SeedReward sr;
    
    public SQLStorage(SeedReward plugin){
        this.sr = plugin;
    }
    
    public String getPlayerSteam(Player player) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public long getPlayerReward(String steamID) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean resetPlayerReward(String steamID) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean rewardPlayer(Player player, long amount) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
