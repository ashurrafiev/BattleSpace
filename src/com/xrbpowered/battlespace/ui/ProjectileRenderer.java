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
import java.awt.Graphics2D;
import java.awt.Stroke;

import com.xrbpowered.battlespace.game.Projectile;
import com.xrbpowered.battlespace.game.Projectile.ProjectileInfo;

public class ProjectileRenderer implements EntityRenderer<Projectile> {
	
	private static final Stroke STROKE = new BasicStroke(1.5f); 
	
	@Override
	public void render(Graphics2D g2, Projectile proj) {
		ProjectileInfo info = Projectile.INFO[proj.type];
		g2.setColor(info.color);
		if(info.circle) {
			g2.fillOval((int)(proj.x-info.size), (int)(proj.y-info.size), (int)(info.size*2f), (int)(info.size*2f));
		}
		else {
			g2.setStroke(STROKE);
			g2.drawLine((int)(proj.x), (int)(proj.y),
					(int)(proj.x-info.size*Math.cos(proj.angle)),
					(int)(proj.y-info.size*Math.sin(proj.angle)));
		}
	}
}
