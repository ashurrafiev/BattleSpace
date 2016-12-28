package com.xrbpowered.battlespace.ui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import com.xrbpowered.battlespace.game.Entity;
import com.xrbpowered.battlespace.game.Game;
import com.xrbpowered.battlespace.game.Particle;
import com.xrbpowered.utils.ColorUtils;

public class DamageText extends Particle {

	public static final Color COLOR_HEAL = new Color(0x00ff00);
	public static final Color COLOR_DAMAGE = new Color(0xff0000);

	private static final EntityRenderer<Particle> renderer = new EntityRenderer<Particle>() {
		@Override
		public void render(Graphics2D g2, Particle e) {
			double s = e.getS();
			DamageText t = (DamageText) e;
			g2.setColor(ColorUtils.alpha(t.color, 1.0-s));
			FontMetrics fm = g2.getFontMetrics();
			g2.drawString(t.label, (int)(t.x-fm.stringWidth(t.label)/2), (int)(t.y+4));
		}
	};
	
	public final Color color;
	public final String label;
	
	public DamageText(Game game, int hp) {
		super(game, 0f, renderer, 2000L);
		if(hp<0) {
			color = COLOR_DAMAGE;
			label = String.format("%d", -hp);
		}
		else {
			color = COLOR_HEAL;
			label = String.format("+%d", hp);
		}
	}
	
	public static DamageText create(Game game, Entity<?> origin, int hp) {
		if(hp==0)
			return null;
		DamageText p = (DamageText) new DamageText(game, hp).sling(origin, Game.random.nextFloat()*360f, 0.03f, 10f);
		game.newEntities.add(p);
		return p;
	}
	
}
