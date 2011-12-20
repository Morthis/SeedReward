/*
 * Copyright (C) 2011 No Heroes.
 * See readme for license details.
 */
package com.noheroes.seedreward.internals;

import org.bukkit.entity.Player;
import com.iConomy.iConomy;
import com.noheroes.seedreward.SeedReward;
import com.noheroes.seedreward.interfaces.Balance;
import java.util.logging.Level;

public class iConomy5Balance implements Balance {
	private iConomy iconomy;
	private SeedReward sr;
	
	public iConomy5Balance (SeedReward instance, iConomy iconomy) {
            this.sr = instance;
	}
	
        @Override
	public boolean isEnabled () {
            return iconomy.isEnabled();
	}
	
        @Override
	public void add (Player p, double amount) {
            try {
                this.add(p.getName(), amount);
            } catch (NullPointerException npe) {
                SeedReward.log(Level.SEVERE, "NullPointerException: " + npe.getMessage());
                npe.printStackTrace();
            }
	}
	
        @Override
	public void add (String p, double amount) {
            try {
                iConomy.getAccount(p).getHoldings().add(amount);
            } catch (NullPointerException npe) {
                SeedReward.log(Level.SEVERE, "NullPointerException: " + npe.getMessage());
                npe.printStackTrace();
            }
	}

        @Override
	public String getClassName () {
            return iConomy.class.getName();
	}

        @Override
        public String format(double amount) {
            return iConomy.format(amount);
        }
        
}
