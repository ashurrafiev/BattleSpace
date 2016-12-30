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
