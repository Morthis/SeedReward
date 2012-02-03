/*
 * Copyright (C) 2011 No Heroes.
 * See readme for license details.
 */
package com.noheroes.seedreward.internals;

import com.noheroes.seedreward.SeedReward;
import com.noheroes.seedreward.interfaces.StorageInterface;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;
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
        /*
         * I'm leaving two options in here, depending on what we go with.
         */
        /*
        //PHP:
        String steamID = null;
        
        try {
            URL url = new URL(Properties.ConnectionURL.replace("%name%", player.getName()));
            URLConnection conn = url.openConnection();

            InputStream istream = conn.getInputStream();
            
            int ch;
            byte[] bytes = new byte[1];
            while ((ch = istream.read()) != -1) {
                bytes[0] = (byte) ch;
                steamID = steamID + new String(bytes);
            }
            istream.close();
        } catch (IOException e) {
            SeedReward.log(Level.WARNING, "Exception trying to read from website.");
            e.printStackTrace(); //We'll remove this line when its debugged.  
            //MC restarts on severe codes, which we don't want if the website is down.
        }*/
        
        //SQL:
//        String steamID = null;
//        ResultSet rs = null;
//        
//        if (userCon == null)
//            return steamID;
//
//        try {
//            if(userCon.isValid(2)){
//                userSteamQuery.clearParameters();
//                userSteamQuery.setString(1, player.getName());
//                rs = userSteamQuery.executeQuery();
//                steamID = rs.getString(1);
//            }
//        } catch (SQLException ex) {
//            SeedReward.log(Level.SEVERE, "Exception connecting to userDB.");
//            SeedReward.log(Level.SEVERE, ex.getMessage());
//        }
        
        String steamID = null;
        steamID = "STEAM_0:1:37108831";
      
        return steamID;
    }

    public Long getPlayerReward(String steamID) {
        Long reward = null;
        ResultSet rs = null;
        
        try {
            userRewardQuery.setString(1, steamID);
        } catch (SQLException ex) {
            SeedReward.log(Level.SEVERE, "SeedReward - Error updating prepared statement");
            SeedReward.log(Level.SEVERE, ex.toString());
            return reward;
        }
        
        try {
            rs = userRewardQuery.executeQuery();
        } catch (SQLException ex) {
            SeedReward.log(Level.SEVERE, "SeedReward - Error querying rewards database");
            SeedReward.log(Level.SEVERE, ex.toString());
            return reward;
        }
        
        if (rs == null)
            return reward;
                
        try {
            if (!rs.first())
                return reward;
            reward = rs.getLong(1);
        } catch (SQLException ex) {
            SeedReward.log(Level.SEVERE, "SeedReward - Error accessing result set");
            SeedReward.log(Level.SEVERE, ex.toString());
            return null;
        }
        
        
        return reward;
    }

    public boolean resetPlayerReward(String steamID) {
        return false;
    }

    public boolean rewardPlayer(Player player, long amount) {
        if (!sr.EconomyHooked())
            return false;
        
        EconomyResponse response;
        response = sr.econ.depositPlayer(player.getName(), amount);
        if (response.type == ResponseType.SUCCESS)
            return true;
        else
            return false;
    }
    
    public boolean chargePlayer(Player player, long amount) {
        if (!sr.EconomyHooked())
            return false;

        EconomyResponse response;
        response = sr.econ.withdrawPlayer(player.getName(), amount);
        if (response.type == ResponseType.SUCCESS)
            return true;
        else
            return false;
    }
}
