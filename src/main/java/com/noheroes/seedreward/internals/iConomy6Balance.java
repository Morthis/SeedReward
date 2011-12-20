/*
 * Copyright (C) 2011 No Heroes.
 * See readme for license details.
 */
package com.noheroes.seedreward.internals;

import org.bukkit.entity.Player;

import com.iCo6.iConomy;
import com.iCo6.system.Accounts;
import com.noheroes.seedreward.SeedReward;
import com.noheroes.seedreward.interfaces.Balance;
import java.util.logging.Level;

public class iConomy6Balance implements Balance {
    
    private iConomy iconomy;
    private Accounts accounts;
    private SeedReward sr;

    public iConomy6Balance (SeedReward db, iConomy iconomy) {
            this.iconomy = iconomy;
            this.sr = db;
            accounts = new Accounts ();
    }

    @Override
    public String getClassName() {
            return iconomy.getClass().getName();
    }

    @Override
    public boolean isEnabled() {
            return iconomy.isEnabled();
    }

    @Override
    public void add(Player p, double amount) {
            this.add(p.getName(), amount);		
    }

    @Override
    public void add(String p, double amount) {
        if (accounts.exists(p))
            accounts.get(p).getHoldings().add(amount);
        else
            SeedReward.log(Level.WARNING, "iconomy 6 add failed with player " + p);
    }

    @Override
    public String format(double amount) {
        return iConomy.format(amount);
    }	
}
