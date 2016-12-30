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
package com.xrbpowered.battlespace.game;

import java.awt.Color;

import com.xrbpowered.battlespace.ui.EntityRenderer;
import com.xrbpowered.battlespace.ui.PickupRenderer;

public class HealthPickup extends Pickup {

	private static final Color COLOR = new Color(0x00bb00);
	private static final Color MEGA_COLOR = new Color(0x77ff33);
	
	public final int hp;
	private final String label;
	
	public HealthPickup(Game game, int type, int hp) {
		super(game, type);
		this.hp = hp;
		this.label = String.format("+%d hp", hp);
	}
	
	@Override
	public EntityRenderer<Pickup> renderer() {
		return PickupRenderer.healthRenderer;
	}
	
	@Override
	public void onOverlapPlayer(Player player) {
		if(!destroyed) {
			player.restoreHealth(hp);
		}
		super.onOverlapPlayer(player);
	}

	@Override
	public float getRadius() {
		return hp>=100 ? LARGE_RADIUS : SMALL_RADIUS;
	}
	
	@Override
	public Color getColor() {
		return hp>=100 ? MEGA_COLOR : COLOR;
	}

	@Override
	public String getLabel() {
		return label;
	}
	
}
