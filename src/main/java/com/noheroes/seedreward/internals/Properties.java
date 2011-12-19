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
    
    public static String playerDBURL;
    public static String playerDBUser;
    public static String playerDBPass;
    
    public static String rewardDBURL;
    public static String rewardDBUser;
    public static String rewardDBPass;
    
    public static final String userSteamQuery = "SELECT steamID from tbl_test WHERE name = ?";
    public static final String userRewardQuery = "SELECT reward from tbl_test WHERE steamID = ?";
    public static final String userRewardReset = "UPDATE tbl_test SET reward = 0 WHERE steamID = ?";
    
    public static final long defaultDelay = 60L;
}
