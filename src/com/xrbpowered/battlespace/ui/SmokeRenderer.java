package com.xrbpowered.battlespace.ui;

import java.awt.Color;
import java.awt.Graphics2D;

import com.xrbpowered.battlespace.game.Particle;
import com.xrbpowered.utils.ColorUtils;

public class SmokeRenderer implements EntityRenderer<Particle> {

	public static final Color COLOR = new Color(0x999999);
	public static final float BASE_SIZE = 20f;
	
	public static final SmokeRenderer instance = new SmokeRenderer();
	
	@Override
	public void render(Graphics2D g2, Particle e) {
		double s = e.getS();
		g2.setColor(ColorUtils.alpha(COLOR, 0.25-s*0.25));
		double r = e.size * BASE_SIZE * (s + 0.25); 
		g2.fillOval((int)(e.x-r), (int)(e.y-r), (int)(r*2f), (int)(r*2f));
	}
	
}
