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
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import com.xrbpowered.battlespace.game.Pickup;

public class PickupRenderer implements EntityRenderer<Pickup> {

	protected void renderShape(Graphics2D g2, Pickup p, float r) {
	}
	
	@Override
	public void render(Graphics2D g2, Pickup p) {
		g2.setStroke(new BasicStroke(1f));
		g2.setColor(p.getColor());
		float r = p.getRadius();
		g2.drawOval((int)(p.x-r), (int)(p.y-r), (int)(r*2f), (int)(r*2f));
		FontMetrics fm = g2.getFontMetrics();
		g2.drawString(p.getLabel(), (int)(p.x-fm.stringWidth(p.getLabel())/2), (int)(p.y-r-4));
		renderShape(g2, p, r);
	};
	
	public static final PickupRenderer healthRenderer = new PickupRenderer() {
		@Override
		protected void renderShape(Graphics2D g2, Pickup p, float r) {
			g2.setStroke(new BasicStroke(3f));
			g2.drawLine((int)(p.x), (int)(p.y-r+5), (int)(p.x), (int)(p.y+r-5));
			g2.drawLine((int)(p.x-r+5), (int)(p.y), (int)(p.x+r-5), (int)(p.y));
		}
	};
	
	public static final PickupRenderer ammoRenderer = new PickupRenderer() {
		@Override
		protected void renderShape(Graphics2D g2, Pickup p, float r) {
			g2.fillRect((int)(p.x-r+5), (int)(p.y-r+5), (int)(r*2f-10), (int)(r*2f-10));
		}
	};
	
	public static final PickupRenderer weaponRenderer = new PickupRenderer() {
		@Override
		protected void renderShape(Graphics2D g2, Pickup p, float r) {
			g2.setStroke(new BasicStroke(2f));
			g2.drawLine((int)(p.x), (int)(p.y-r), (int)(p.x), (int)(p.y+r));
			g2.drawLine((int)(p.x-r), (int)(p.y), (int)(p.x+r), (int)(p.y));
			g2.drawOval((int)(p.x-r+8), (int)(p.y-r+8), (int)(r*2f-16), (int)(r*2f-16));
		}
	};
	
}
