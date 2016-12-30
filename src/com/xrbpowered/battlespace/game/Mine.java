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

import com.xrbpowered.battlespace.net.MsgSpawnStaticEntity;
import com.xrbpowered.battlespace.ui.EntityRenderer;
import com.xrbpowered.battlespace.ui.MineRenderer;
import com.xrbpowered.net.NetMessage;

public class Mine extends Entity<Mine> {
	
	public static final long DURATION = 30000L;
	public static final long ARMING_DURATION = 1000L;
	public static final float POWER = 0.75f;
	public static final float TRIGGER_RADIUS_FACTOR = 0.5f;

	public final Player owner;
	private long age = 0;
	
	public Mine(Game game, Player owner, long age) {
		super(game);
		this.owner = owner;
		this.age = age;
	}

	private static MineRenderer renderer = new MineRenderer();
	
	@Override
	public EntityRenderer<Mine> renderer() {
		return renderer;
	}

	@Override
	public NetMessage createSpawnMessage() {
		return new MsgSpawnStaticEntity(this);
	}
	
	public float getTriggerRadius() {
		return TRIGGER_RADIUS_FACTOR*POWER*Explosion.BASE_HIT_RADIUS;
	}
	
	protected void detonate() {
		if(!destroyed) {
			game.addEntity(new Explosion(game, owner, x, y, POWER, 0L));
			destroy();
		}
	}
	
	
	public long getAge() {
		return age;
	}
	
	@Override
	public boolean canTriggerOverlapPlayer(Player player) {
		return player!=owner;
	}
	
	@Override
	public void onOverlapPlayer(Player player) {
		detonate();
	}
	
	@Override
	public void update(long dt) {
		if(age>ARMING_DURATION)
			checkPlayerOverlaps(Player.RADIUS+getTriggerRadius());
		age += dt;
		if(age>DURATION)
			destroy();
	}
	
}
