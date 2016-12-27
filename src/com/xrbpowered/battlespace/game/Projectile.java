package com.xrbpowered.battlespace.game;

import java.awt.Color;

import com.xrbpowered.battlespace.net.MsgSpawnProjectile;
import com.xrbpowered.battlespace.ui.EntityRenderer;
import com.xrbpowered.battlespace.ui.ProjectileRenderer;
import com.xrbpowered.net.NetMessage;

public class Projectile extends Entity<Projectile> {

	public static final int TYPE_MINI = 0;
	public static final int TYPE_SPREAD = 1;
	
	public static class ProjectileInfo {
		public final float speed;
		public final Color color;
		public final float traceLength;
		public final int damage;
		public ProjectileInfo(float speed, Color color, float traceLength, int damage) {
			this.speed = speed;
			this.color = color;
			this.traceLength = traceLength;
			this.damage = damage;
		}
	}
	
	public static final ProjectileInfo[] INFO = {
		new ProjectileInfo(0.75f, new Color(0xffaa00), 20f, 7),
		new ProjectileInfo(0.9f, new Color(0xee7700), 25f, 12),
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
		da = (float)Math.PI*da/180f;
		float pvx = (float)Math.cos(player.angle+da);
		float pvy = (float)Math.sin(player.angle+da);
		setVelocity(INFO[type].speed*pvx, INFO[type].speed*pvy);
		setPosition(player.x+Player.RADIUS*2f*pvx+vx*25f, player.y+Player.RADIUS*2f*pvy+vy*25f);
		updateAngle();
		return this;
	}
	
	@Override
	public boolean canTriggerOverlapPlayer(Player player) {
		return player!=owner;
	}
	
	@Override
	public void onOverlapPlayer(Player player) {
		player.receiveDamage(INFO[type].damage, owner);
		destroy();
	}
	
	@Override
	public void update(long dt) {
		checkPlayerOverlaps(Player.RADIUS);
		super.update(dt);
	}
	
}
