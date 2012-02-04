/*
 * Copyright (C) 2011 No Heroes.
 * See readme for license details.
 */
package com.noheroes.seedreward.internals;

/**
 *
 * @author Sorklin <sorklin at gmail.com>
 */
public class Properties {
    
    // Prevents instances of this class from being created as it would serve no purpose
    private Properties() {}
    
    public static String playerDBURL;
    public static String playerDBUser;
    public static String playerDBPass;
    
    public static String rewardDBURL;
    public static String rewardDBUser;
    public static String rewardDBPass;
    
    public static String ConnectionURL;
    
    public static boolean showRewardMsg;
    public static String rewardMsg;
    
    public static boolean showNoRewardMsg;
    public static String noRewardMsg;
    
    public static boolean showRewardServerMsg;
    public static String rewardServerMsg;
    
    public static boolean showNoRewardServerMsg;
    public static String noRewardServerMsg;
    
    public static final String userSteamQuery = "SELECT steamID from tbl_test WHERE name = ?";
    public static final String userRewardQuery = "SELECT ducats FROM reward WHERE steamID = ?";
    public static final String userRewardReset = "UPDATE tbl_test SET reward = 0 WHERE steamID = ?";
    
    public static final long defaultDelay = 60L;
}
