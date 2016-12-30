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

import com.xrbpowered.battlespace.net.MsgSpawnPickup;
import com.xrbpowered.net.NetMessage;

public abstract class Pickup extends Entity<Pickup> {

	public static final float SMALL_RADIUS = 10f;
	public static final float LARGE_RADIUS = 15f;
	
	public static final float PICK_RADIUS_MOD = 10f;
	
	public final int type;

	public Pickup(Game game, int type) {
		super(game);
		this.type = type;
		setPosition(game.getNewPickupLocation());
	}
	
	@Override
	public NetMessage createSpawnMessage() {
		return new MsgSpawnPickup(this);
	}
	
	public abstract Color getColor();
	public abstract String getLabel();

	public float getRadius() {
		return SMALL_RADIUS;
	}
	
	@Override
	protected void destroy() {
		if(game.isServer())
			PickupFactory.remove(this);
		super.destroy();
	}

	@Override
	public boolean canTriggerOverlapPlayer(Player player) {
		return true;
	}
	
	@Override
	public void onOverlapPlayer(Player player) {
		destroy();
	}
	
	@Override
	public void update(long dt) {
		checkPlayerOverlaps(Player.RADIUS+getRadius()+PICK_RADIUS_MOD);
	}
}
