package com.xrbpowered.battlespace.ui;

import java.awt.Color;
import java.awt.Graphics2D;

import com.xrbpowered.battlespace.game.Explosion;
import com.xrbpowered.utils.ColorUtils;
import com.xrbpowered.utils.TweenUtils;

public class ExplosionRenderer implements EntityRenderer<Explosion> {

	@Override
	public void render(Graphics2D g2, Explosion exp) {
		double s = TweenUtils.tween(exp.getAge(), 0L, Explosion.DURATION);
		double r = TweenUtils.easeIn(s) * exp.getHitRadius() * 0.25;
		g2.setColor(ColorUtils.alpha(Color.YELLOW, 1.0-s));
		g2.fillOval((int)(exp.x-r), (int)(exp.y-r), (int)(r*2.0), (int)(r*2.0));

	};
}
