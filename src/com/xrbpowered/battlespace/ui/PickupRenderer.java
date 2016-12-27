package com.xrbpowered.battlespace.ui;

import java.awt.BasicStroke;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import com.xrbpowered.battlespace.game.Pickup;

public class PickupRenderer implements EntityRenderer<Pickup> {

	@Override
	public void render(Graphics2D g2, Pickup p) {
		g2.setStroke(new BasicStroke(1f));
		g2.setColor(p.getColor());
		g2.drawOval((int)(p.x-Pickup.RADIUS), (int)(p.y-Pickup.RADIUS), (int)(Pickup.RADIUS*2f), (int)(Pickup.RADIUS*2f));
		FontMetrics fm = g2.getFontMetrics();
		g2.drawString(p.getLabel(), (int)(p.x-fm.stringWidth(p.getLabel())/2), (int)(p.y-Pickup.RADIUS-4));
	};
	
}
