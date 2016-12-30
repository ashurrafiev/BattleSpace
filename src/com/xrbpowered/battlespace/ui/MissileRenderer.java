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
	};
}
