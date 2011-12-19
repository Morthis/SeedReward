/*
 * Copyright (C) 2011 No Heroes.
 * See readme for license details.
 */
package com.noheroes.seedreward.internals;

import com.noheroes.seedreward.SeedReward;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author PIETER
 */
public class SRMessageQueue {
    
    private SeedReward instance;
    private static Queue<String> messageQueue = new LinkedList<String>();
    private static checkMessages msgThread;
    private static int taskID;
    private static final long defaultDelay = 200L;
    
    public SRMessageQueue(SeedReward instance)
    {
        this.instance = instance;
    }
    
    public static boolean addMessage(String msg)
    {
        return messageQueue.offer(msg);
    }
    
    public static Object retrieveMessage()
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
            taskID = instance.getServer().getScheduler().scheduleSyncRepeatingTask(instance, msgThread, 0, defaultDelay);   
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
    
    Object msg;

    public void run() {
        
        msg = SRMessageQueue.retrieveMessage();
        while(msg != null)
        {
            SeedReward.broadcast(msg.toString());
            msg = SRMessageQueue.retrieveMessage();
        }         
    }    
}
