package com.xrbpowered.battlespace.game;

import com.xrbpowered.battlespace.net.MsgEntityPos;
import com.xrbpowered.battlespace.net.MsgSpawnMissile;
import com.xrbpowered.battlespace.ui.EntityRenderer;
import com.xrbpowered.battlespace.ui.MissileRenderer;
import com.xrbpowered.net.NetMessage;

public class Missile extends Entity<Missile> {
	
	public static final int TYPE_ROCKET = 0;
	public static final int TYPE_HOMING = 1;
	
	public static class MissileInfo {
		public final boolean homing;
		public final long maxAge;
		public final float speed;
		public final float steer;
		public final float triggerRadiusFactor;
		public final float power;
		public MissileInfo(boolean homing, long maxAge, float speed, float steer, float power, float triggerRadiusFactor) {
			this.homing = homing;
			this.maxAge = maxAge;
			this.speed = speed;
			this.steer = (float) (steer*Math.PI/180f);
			this.triggerRadiusFactor = triggerRadiusFactor;
			this.power = power;
		}
	}
	
	public static final MissileInfo[] INFO = {
		new MissileInfo(false, 10000L, 0.5f, 0f, 0.75f, 0.333f),
		new MissileInfo(true, 5000L, 0.5f, 0.2f, 0.25f, 0.25f),
	};
	
	public final Player owner;
	public final int type;
	private long age;
	
	public Missile(Game game, Player owner, int type) {
		super(game);
		this.owner = owner;
		this.type = type;
		this.age = 0;
	}
	
	private static MissileRenderer renderer = new MissileRenderer();
	
	@Override
	public EntityRenderer<Missile> renderer() {
		return renderer;
	}
	
	@Override
	public NetMessage createSpawnMessage() {
		return new MsgSpawnMissile(this);
	}
	
	public Missile shoot(Player player, float da) {
		sling(player, da, INFO[type].speed, Player.RADIUS*2f+INFO[type].speed*25f);
		return this;
	}
	
	public float getTriggerRadius() {
		MissileInfo info = INFO[type];
		return info.triggerRadiusFactor*info.power*Explosion.BASE_HIT_RADIUS;
	}
	
	protected void detonate() {
		if(!destroyed) {
			game.addEntity(new Explosion(game, owner, x, y, INFO[type].power, 0L));
			destroy();
		}
	}
	
	@Override
	public boolean canTriggerOverlapPlayer(Player player) {
		return player!=owner;
	}
	
	@Override
	public void onOverlapPlayer(Player player) {
		detonate();
	}
	
	private void controlHoming(long dt) {
		MissileInfo info = INFO[type];
		Player target = null;
		float distTarget = 0f;
		for(Player player : game.players)
			if(player!=null && player!=owner && !player.isRespawning()) {
				float dist = distBetween(player);
				if(target==null || dist<distTarget) {
					target = player;
					distTarget = dist;
				}
			}
		if(target!=null) {
			float targetAngle = (float) Math.atan2(target.y-y, target.x-x);
			if(Math.abs(targetAngle-angle)>Math.abs(targetAngle-Math.PI*2f-angle))
				targetAngle -= Math.PI*2f;
			else if(Math.abs(targetAngle-angle)>Math.abs(targetAngle+Math.PI*2f-angle))
				targetAngle += Math.PI*2f;
			float a = angle;
			if(a>targetAngle)
				a -= Math.min(info.steer*dt, Math.abs(angle-targetAngle));
			else
				a += Math.min(info.steer*dt, Math.abs(angle-targetAngle));
			setAngleSpeed(a, info.speed);
			game.net.broadcastMessage(new MsgEntityPos(this), null);
		}
	}
	
	@Override
	public void update(long dt) {
		MissileInfo info = INFO[type];
		checkPlayerOverlaps(Player.RADIUS+getTriggerRadius());
		if(!destroyed) {
			if(game.isServer()) {
				if(info.homing)
					controlHoming(dt);
				age += dt;
				if(age>info.maxAge)
					detonate();
			}
			else {
				Particle.createSmoke(game, this, info.power);
			}
		}
		super.update(dt);
	}
}
