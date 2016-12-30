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
import com.xrbpowered.battlespace.ui.GasCloudRenderer;
import com.xrbpowered.net.NetMessage;

public class GasCloud extends Entity<GasCloud> {

	public static final long DURATION = 10000L;
	public static final long TICK = 250L;
	public static final float DPS = 10f;
	public static final float HPS = 5f;
	public static final float RADIUS= 30f;

	public final Player owner;
	private long age = 0;
	private Timer tickTimer = new Timer(TICK);
	
	private float damage = 0f;
	private float healing = 0f;
	
	public GasCloud(Game game, Player owner, long age) {
		super(game);
		this.owner = owner;
		this.age = age;
	}

	private static GasCloudRenderer renderer = new GasCloudRenderer();
	
	@Override
	public EntityRenderer<GasCloud> renderer() {
		return renderer;
	}

	@Override
	public NetMessage createSpawnMessage() {
		return new MsgSpawnStaticEntity(this);
	}
	
	public long getAge() {
		return age;
	}
	
	@Override
	public boolean canTriggerOverlapPlayer(Player player) {
		return true;
	}
	
	@Override
	public void onOverlapPlayer(Player player) {
		if(player==owner)
			player.restoreHealth((int)healing);
		else
			player.receiveDamage((int)damage, owner);
	}
	
	@Override
	public void update(long dt) {
		if(game.isServer()) {
			tickTimer.update(dt);
			if(!tickTimer.isActive()) {
				damage += DPS*(float)TICK/1000f;
				healing += HPS*(float)TICK/1000f;
				checkPlayerOverlaps(RADIUS+Player.RADIUS/2f);
				tickTimer.start();
				damage -= (int)damage;
				healing -= (int)healing;
			}
		}
		age += dt;
		if(age>=DURATION)
			destroy();
	}
	
}
