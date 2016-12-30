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
