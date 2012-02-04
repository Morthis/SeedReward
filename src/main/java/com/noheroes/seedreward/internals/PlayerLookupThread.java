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
            sr.log(Level.WARNING, "SeedReward - Unable to look up SteamID of player " + player.getName());
            SRMessageQueue.addMessage(new SRMessage(r + "[Server]" + w + 
                    " Unable to look up your SteamID.  The database may be temporarily offline or your steamID is not added to your profile.", player));
            taskCleanup();
            return;
        }
        
        reward = sr.getDB().getPlayerReward(steamID);       
        
        if (reward == null) {
            sr.log(Level.WARNING, "SeedReward - Unable to load reward value from database");
            SRMessageQueue.addMessage(new SRMessage(r + "[Server]" + w + 
                    " Unable to look up your rewards earned for seeding.  The rewards database may be temporarily offline.", player));
            taskCleanup();
            return;
        }
        
        if (reward == 0) {
            SRMessageQueue.addMessage(new SRMessage(
                    r + "[Server]" + w + " You have not earned any ducats from seeding. To learn more type /help seeding.", player));
            taskCleanup();
            return;
        }
        
        if (sr.getDB().rewardPlayer(player, reward)) {
            if (sr.getDB().resetPlayerReward(steamID)) {
                // Rewarded player successfully and reset DB successfully               
                
                if (Properties.showRewardMsg)
                    SRMessageQueue.addMessage(new SRMessage( r + "[Server]" + w + 
                            fillInMessage(Properties.rewardMsg, player, reward), player));
                if (Properties.showRewardServerMsg)
                    SRMessageQueue.addMessage(new SRMessage(r + "[Server] " + w + 
                            fillInMessage(Properties.rewardServerMsg, player, reward)));                    
            }
            else {
                // Rewarded player successfully but DB reset failed
                if (sr.getDB().chargePlayer(player, reward)) {
                    // Player's balance reset back to normal following a DB reset failure
                    SRMessageQueue.addMessage(new SRMessage(r + "[Server]" + w + 
                            " You have earned " + reward.toString() + 
                            " ducats from seeding.  The ducats could not be awarded, likely because the database is temporarily offline." + 
                            "  They will be awarded next time you log on.", player));
                    sr.log(Level.WARNING, "SeedReward - Unable to reset the reward database for player " + player.getName());
                }
                else {
                    // Player's balance did not reset back to normal following a DB reset failure
                    sr.log(Level.SEVERE, "SeedReward - Player " + player.getName() + " received his seeding reward when the reward database reset failed.");
                }
            }
        }
        else {
            // Failed to reward player, database reset not attempted
            SRMessageQueue.addMessage(new SRMessage(r + "[Server]" + w + 
                    " You have earned " + reward.toString() + " ducats from seeding.  Because the economy plugin is currently not working or disabled " + 
                    "these will be awarded to you next time you log in."));
            sr.log(Level.WARNING, "SeedReward - Unable to award player " + player.getName() + " his seeding reward.");
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
        newMessage = message.replace("$R$", reward.toString());   
        newMessage = newMessage.replace("$P$", player.getName());
        return newMessage;
    }
}
