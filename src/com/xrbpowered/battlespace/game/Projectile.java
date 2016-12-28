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
/*		da = (float)Math.PI*da/180f;
		float pvx = (float)Math.cos(player.angle+da);
		float pvy = (float)Math.sin(player.angle+da);
		setVelocity(INFO[type].speed*pvx, INFO[type].speed*pvy);
		setPosition(player.x+Player.RADIUS*2f*pvx+vx*25f, player.y+Player.RADIUS*2f*pvy+vy*25f);
		updateAngle();*/
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
