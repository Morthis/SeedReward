/*
 * Copyright (C) 2011 No Heroes.
 * See readme for license details.
 */
package com.noheroes.seedreward.internals;

import com.noheroes.seedreward.SeedReward;
import com.noheroes.seedreward.interfaces.StorageInterface;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.logging.Level;
import org.bukkit.entity.Player;

/**
 *
 * @author Sorklin <sorklin at gmail.com>
 */
public class SQLStorage implements StorageInterface{

    private SeedReward sr;
    
    private Connection userCon;
    private Connection rewardCon;
    private PreparedStatement userSteamQuery;
    private PreparedStatement userRewardQuery;
    private PreparedStatement userResetReward;
    
    public SQLStorage(SeedReward plugin){
        this.sr = plugin;
        
        try {
            userCon = DriverManager.getConnection(Properties.playerDBURL, 
                    Properties.playerDBUser, Properties.playerDBPass);
            userSteamQuery = userCon.prepareStatement(Properties.userSteamQuery);
        } catch (SQLException ex) {
            SeedReward.log(Level.SEVERE, "Exception connecting to userDB.");
            SeedReward.log(Level.SEVERE, ex.getMessage());
        }
        
        try {
            rewardCon = DriverManager.getConnection(Properties.rewardDBURL, 
                    Properties.rewardDBUser, Properties.rewardDBPass);
            userRewardQuery = rewardCon.prepareStatement(Properties.userRewardQuery);
            userResetReward = rewardCon.prepareStatement(Properties.userRewardReset);
        } catch (SQLException ex) {
            SeedReward.log(Level.SEVERE, "Exception connecting to reward DB.");
            SeedReward.log(Level.SEVERE, ex.getMessage());
        }
    }
    
    public String getPlayerSteam(Player player) {
        String steamID = null;
        ResultSet rs = null;
        
        if (userCon == null)
            return steamID;

        try {
            if(userCon.isValid(2)){
                userSteamQuery.clearParameters();
                userSteamQuery.setString(1, player.getName());
                rs = userSteamQuery.executeQuery();
                steamID = rs.getString(1);
            }
        } catch (SQLException ex) {
            SeedReward.log(Level.SEVERE, "Exception connecting to userDB.");
            SeedReward.log(Level.SEVERE, ex.getMessage());
        }
      
        return steamID;
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
