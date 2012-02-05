/*
 * Copyright (C) 2011 No Heroes.
 * See readme for license details.
 */
package com.noheroes.seedreward.internals;
import com.noheroes.seedreward.SeedReward;
import com.noheroes.seedreward.listeners.SRPlayerListener;

import java.util.logging.Level;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 *
 * @author PIETER
 */
public class PlayerLookupThread implements Runnable {
    
    PlayerJoinEvent playerEvent;
    Player player;
    SRPlayerListener pl;
    SeedReward sr;
    String steamID;
    Long reward;
    
    ChatColor r = ChatColor.RED;
    ChatColor w = ChatColor.WHITE;
    
    public PlayerLookupThread(PlayerJoinEvent playerEvent, SRPlayerListener pl, SeedReward sr) {
        this.playerEvent = playerEvent;
        this.player = playerEvent.getPlayer();
        this.pl = pl;
        this.sr = sr;
    }
    
    @Override
    public void run(){ 
                
        steamID = sr.getDB().getPlayerSteam(player);
        
        
        if(steamID == null) {
            if (Properties.showNoSteamID)
                sendMessage(Properties.noSteamIDMsg, player);
            taskCleanup();
            return;
        }
        
        if  (steamID.equals("Database access error"))
        {
            if (Properties.showDBError)
                sendMessage(Properties.DBErrorMsg, player);
            taskCleanup();
            return;
        }
        
        reward = sr.getDB().getPlayerReward(steamID);       
        
        if (reward == null) {
            if (Properties.showDBError)
                sendMessage(Properties.DBErrorMsg, player);
            taskCleanup();
            return;
        }
        
        if (reward == 0) {
            if (Properties.showNoRewardMsg)
                sendMessage(Properties.noRewardMsg, player, reward);
            if (Properties.showNoRewardServerMsg)
                sendMessage(Properties.noRewardServerMsg, player, reward, true);
            taskCleanup();
            return;
        }
        
        if (sr.getDB().rewardPlayer(player, reward)) {
            if (sr.getDB().resetPlayerReward(steamID)) {
                // Rewarded player successfully and reset DB successfully                              
                if (Properties.showRewardMsg)
                    sendMessage(Properties.rewardMsg, player, reward);
                if (Properties.showRewardServerMsg)
                    sendMessage(Properties.rewardServerMsg, player, reward, true);
            }
            else {
                // Rewarded player successfully but DB reset failed
                if (sr.getDB().chargePlayer(player, reward)) {
                    // Player's balance reset back to normal following a DB reset failure
                    if (Properties.showDBError)
                        sendMessage(Properties.DBErrorMsg, player, reward);
                }
                else {
                    // Player's balance did not reset back to normal following a DB reset failure
                    sr.log(Level.SEVERE, "SeedReward - Player " + player.getName() + " received his seeding reward when the reward database reset failed.");
                }
            }
        }
        else {
            // Failed to reward player, database reset not attempted
            if (Properties.showDBError)
                sendMessage(Properties.DBErrorMsg, player);
        }                
        
        // Removes the task ID from the hashmap, this should be the last line so that it is executed right before the thread finishes
        taskCleanup();
    }
    
    private void taskCleanup()
    {
        pl.removeTask(player);
    }
    
    private String fillInMessage(String message, Player player, Long reward)
    {
        String newMessage;
        
        newMessage = message.replace("$P$", player.getName());
        if (reward != null)
            newMessage = newMessage.replace("$R$", reward.toString());   
        return newMessage;
    }
    
    // Used for sending error messages, reward value defaults to null because none could be looked up and displaying it would make no sense
    private void sendMessage(String message, Player player)
    {
        sendMessage(message, player, null, false);
    }
    
    // Default to player whisper if serverWideAnnouncement isn't given
    private void sendMessage(String message, Player player, Long reward)
    {
        sendMessage(message, player, reward, false);
    }
    
    private void sendMessage(String message, Player player, Long reward, boolean serverWideAnnouncement)
    {
        if (serverWideAnnouncement)
            SRMessageQueue.addMessage(new SRMessage(r + "[Server] " + w + 
                fillInMessage(message, player, reward)));   
        else
            SRMessageQueue.addMessage(new SRMessage(r + "[Server] " + w + 
                fillInMessage(message, player, reward), player));   
    }
}
