package com.xrbpowered.battlespace.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

import com.xrbpowered.battlespace.game.ClientPlayer;
import com.xrbpowered.battlespace.game.Missile;
import com.xrbpowered.battlespace.game.Missile.MissileInfo;

public class MissileRenderer implements EntityRenderer<Missile> {

	public static final Stroke STROKE = new BasicStroke(1f); 
	public static final Color TRIGGER_COLOR = new Color(0x99990000, true);
	public static final Color OWNER_TRIGGER_COLOR = new Color(0x99777777, true);
	
	public void render(Graphics2D g2, Missile e) {
		boolean owner = e.owner instanceof ClientPlayer;
		MissileInfo info = Missile.INFO[e.type];
		g2.setColor(Color.LIGHT_GRAY);
		float r = info.power * 5f+3f; 
		g2.fillOval((int)(e.x-r), (int)(e.y-r), (int)(r*2f), (int)(r*2f));
		g2.setStroke(STROKE);
		g2.setColor(owner ? OWNER_TRIGGER_COLOR : TRIGGER_COLOR);
		r = e.getTriggerRadius();
		g2.drawOval((int)(e.x-r), (int)(e.y-r), (int)(r*2f), (int)(r*2f));
	}
}
