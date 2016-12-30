/*******************************************************************************
 * MIT License
 *
 * Copyright (c) 2016 Ashur Rafiev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *******************************************************************************/
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
