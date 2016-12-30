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

import com.xrbpowered.battlespace.net.MsgSpawnProjectile;
import com.xrbpowered.battlespace.ui.EntityRenderer;
import com.xrbpowered.battlespace.ui.ProjectileRenderer;
import com.xrbpowered.net.NetMessage;

public class Projectile extends Entity<Projectile> {

	public static final int TYPE_MINI = 0;
	public static final int TYPE_SPREAD = 1;
	public static final int TYPE_PLASMA = 2;
	
	public static class ProjectileInfo {
		public final float speed;
		public final Color color;
		public final float size;
		public final boolean circle;
		public final int damage;
		public final float pushFactor;
		public ProjectileInfo(float speed, Color color, float size, boolean circle, int damage, float pushFactor) {
			this.speed = speed;
			this.color = color;
			this.size = size;
			this.circle = circle;
			this.damage = damage;
			this.pushFactor = pushFactor;
		}
	}
	
	public static final ProjectileInfo[] INFO = {
		new ProjectileInfo(0.75f, new Color(0xffaa00), 20f, false, 7, 0.05f),
		new ProjectileInfo(1.0f, new Color(0xee7700), 25f, false, 15, 0.15f),
		new ProjectileInfo(0.6f, new Color(0x7755ff), 5f, true, 10, 0.5f),
	};
	
	public final Player owner;
	public final int type;
	
	public Projectile(Game game, Player owner, int type) {
		super(game);
		this.owner = owner;
		this.type = type;
	}
	
	private static ProjectileRenderer renderer = new ProjectileRenderer();
	
	@Override
	public EntityRenderer<Projectile> renderer() {
		return renderer;
	}
	
	@Override
	public NetMessage createSpawnMessage() {
		return new MsgSpawnProjectile(this);
	}
	
	public Projectile shoot(Player player, float da) {
		sling(player, da, INFO[type].speed, Player.RADIUS*2f+INFO[type].speed*25f);
		return this;
	}
	
	@Override
	public boolean canTriggerOverlapPlayer(Player player) {
		return player!=owner;
	}
	
	@Override
	public void onOverlapPlayer(Player player) {
		ProjectileInfo info = INFO[type];
		if(info.pushFactor>0f)
			player.push(vx*info.pushFactor, vy*info.pushFactor);
		player.receiveDamage(info.damage, owner);
		destroy();
	}
	
	@Override
	public void update(long dt) {
		checkPlayerOverlaps(Player.RADIUS);
		super.update(dt);
	}
	
}
