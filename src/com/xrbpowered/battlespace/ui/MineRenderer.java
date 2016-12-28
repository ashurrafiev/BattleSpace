package com.xrbpowered.battlespace.ui;

import java.awt.Color;
import java.awt.Graphics2D;

import com.xrbpowered.battlespace.game.ClientPlayer;
import com.xrbpowered.battlespace.game.Mine;
import com.xrbpowered.utils.ColorUtils;
import com.xrbpowered.utils.MathUtils;
import com.xrbpowered.utils.TweenUtils;

public class MineRenderer implements EntityRenderer<Mine> {

	public void render(Graphics2D g2, Mine e) {
		boolean owner = e.owner instanceof ClientPlayer;
		double s = MathUtils.snap(TweenUtils.tween(e.getAge(), 0L, Mine.ARMING_DURATION));
		if(owner || s<0.99) {
			g2.setColor(owner ? ColorUtils.blend(Color.RED, Color.DARK_GRAY, s) : ColorUtils.alpha(Color.RED, 1.0-s));
			float r = 5f; 
			g2.fillOval((int)(e.x-r), (int)(e.y-r), (int)(r*2f), (int)(r*2f));
			g2.setStroke(MissileRenderer.STROKE);
			g2.setColor(owner ? MissileRenderer.OWNER_TRIGGER_COLOR : ColorUtils.alpha(MissileRenderer.TRIGGER_COLOR, 1.0-s));
			r = e.getTriggerRadius();
			g2.drawOval((int)(e.x-r), (int)(e.y-r), (int)(r*2f), (int)(r*2f));
		}
	};
}