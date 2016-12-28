package com.xrbpowered.battlespace.ui;

import java.awt.Color;
import java.awt.Graphics2D;

import com.xrbpowered.battlespace.game.ClientPlayer;
import com.xrbpowered.battlespace.game.GasCloud;
import com.xrbpowered.utils.ColorUtils;
import com.xrbpowered.utils.TweenUtils;

public class GasCloudRenderer implements EntityRenderer<GasCloud> {

	public static final Color DAMAGE_COLOR = new Color(0x99dd00); 
	public static final Color HEAL_COLOR = new Color(0x00bb33); 
	
	@Override
	public void render(Graphics2D g2, GasCloud gas) {
		long age = gas.getAge();
		double w = TweenUtils.wave(age, 750L);
		double r = GasCloud.RADIUS * (0.9+w*0.2);
		double a = 1.0;
		if(age<500L) {
			r *= TweenUtils.tween(age, 0, 500L);
		}
		else if(age>GasCloud.DURATION-2000L) {
			a = 1.0 - TweenUtils.tween(age, GasCloud.DURATION-2000L, 2000L);
		}
		g2.setColor(ColorUtils.alpha(gas.owner instanceof ClientPlayer ? HEAL_COLOR : DAMAGE_COLOR, a*(0.5-w*0.1)));
		g2.fillOval((int)(gas.x-r), (int)(gas.y-r), (int)(r*2.0), (int)(r*2.0));

	};
}
