package com.xrbpowered.battlespace.ui;

import java.awt.Graphics2D;

import com.xrbpowered.battlespace.game.Entity;

public interface EntityRenderer<T extends Entity<T>> {
	public void render(Graphics2D g2, T e);
}
