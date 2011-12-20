/*
 * Copyright (C) 2011 No Heroes.
 * See readme for license details.
 */
package com.noheroes.seedreward.internals;

import com.noheroes.seedreward.SeedReward;
import com.noheroes.seedreward.interfaces.Balance;

import org.bukkit.entity.Player;


public class DummyBalance implements Balance {
    
    public DummyBalance (SeedReward instance) {
    }

    @Override
    public void add (Player p, double amount) {}
    @Override
    public void add (String p, double amount) {}

    @Override
    public String getClassName () {
            return this.getClass().getName();
    }

    @Override
    public boolean isEnabled () {
            return true;
    }

    @Override
    public String format(double amount) {
        return String.valueOf(amount);
    }
}
