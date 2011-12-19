/*
 * Copyright (C) 2011 No Heroes.
 * See readme for license details.
 */
package com.noheroes.seedreward.internals;

import com.noheroes.seedreward.SeedReward;

import java.util.LinkedList;
import java.util.Queue;

import org.bukkit.entity.Player;

/**
 *
 * @author PIETER
 */
public class SRMessageQueue {
    
    private SeedReward instance;
    private static Queue<SRMessage> messageQueue = new LinkedList<SRMessage>();
    private static checkMessages msgThread;
    private static int taskID;
    
    public SRMessageQueue(SeedReward instance)
    {
        this.instance = instance;
    }
    
    public static boolean addMessage(SRMessage msg)
    {
        return messageQueue.offer(msg);
    }
    
    public static SRMessage retrieveMessage()
    {
        return messageQueue.poll();
    }
    
    public void stopScheduler()
    {
        instance.getServer().getScheduler().cancelTask(taskID);
    }
    
    public boolean startScheduler()
    {
        if (instance.getServer().getScheduler().isCurrentlyRunning(taskID))
            return false;
        else
        {
            msgThread = new checkMessages();
            taskID = instance.getServer().getScheduler().scheduleSyncRepeatingTask(instance, msgThread, 0, Properties.defaultDelay);   
            return true;
        }
    }
    
    public boolean startScheduler(long delay)
    {
        if (instance.getServer().getScheduler().isCurrentlyRunning(taskID))
            return false;
        else
        {
            msgThread = new checkMessages();
            taskID = instance.getServer().getScheduler().scheduleSyncRepeatingTask(instance, msgThread, 0, delay);   
            return true;
        }
    }    
}

class checkMessages implements Runnable {
    
    SRMessage msg;

    public void run() {
        
        msg = SRMessageQueue.retrieveMessage();
        while(msg != null){
            String m = msg.getMessage();
            Player p = msg.getPlayer();
            //Most important:
            if(p.isOnline())
                p.sendMessage(m);
            //Not so important (if it doesn't hit everyone).
            SeedReward.broadcast(m);
            msg = SRMessageQueue.retrieveMessage();
        }         
    }    
}
