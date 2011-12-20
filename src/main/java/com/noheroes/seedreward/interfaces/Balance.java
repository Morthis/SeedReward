/*
 * Copyright (C) 2011 No Heroes.
 * See readme for license details.
 */
package com.noheroes.seedreward.interfaces;

import org.bukkit.entity.Player;

public interface Balance {
	public String  getClassName ();
	public boolean isEnabled ();
	public void add (Player p, double amount);
	public void add (String p, double amount);
        public String format(double amount);
}
