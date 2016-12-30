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
