package com.xrbpowered.battlespace.game;

import com.xrbpowered.battlespace.net.MsgSpawnMissile;
import com.xrbpowered.battlespace.ui.EntityRenderer;
import com.xrbpowered.battlespace.ui.MissileRenderer;
import com.xrbpowered.battlespace.ui.SmokeRenderer;
import com.xrbpowered.net.NetMessage;

public class Missile extends Entity<Missile> {
	
	public static final int TYPE_ROCKET = 0;
	
	public static class MissileInfo {
		public final boolean homing; 
		public final float speed;
		public final float triggerRadiusFactor;
		public final float power;
		public MissileInfo(boolean homing, float speed, float power, float triggerRadiusFactor) {
			this.homing = homing;
			this.speed = speed;
			this.triggerRadiusFactor = triggerRadiusFactor;
			this.power = power;
		}
	}
	
	public static final MissileInfo[] INFO = {
		new MissileInfo(false, 0.5f, 0.75f, 0.333f)
	};
	
	public final Player owner;
	public final int type;
	
	public Missile(Game game, Player owner, int type) {
		super(game);
		this.owner = owner;
		this.type = type;
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
	
	@Override
	public boolean canTriggerOverlapPlayer(Player player) {
		return player!=owner;
	}
	
	@Override
	public void onOverlapPlayer(Player player) {
		if(!destroyed) {
			game.addEntity(new Explosion(game, owner, x, y, INFO[type].power, 0L));
			destroy();
		}
	}
	
	@Override
	public void update(long dt) {
		if(game.isServer() && INFO[type].homing) {
			// TODO homing missile
		}
		if(!game.isServer()) {
			game.newEntities.add(new Particle(game, 1f, SmokeRenderer.instance, 750L).sling(this, Game.random.nextFloat()*360f, 0.005f, 3f));
		}
		checkPlayerOverlaps(Player.RADIUS+getTriggerRadius());
		super.update(dt);
	}
}
