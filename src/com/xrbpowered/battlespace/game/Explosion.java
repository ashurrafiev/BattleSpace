package com.xrbpowered.battlespace.game;

import com.xrbpowered.battlespace.net.MsgSpawnExplosion;
import com.xrbpowered.battlespace.ui.EntityRenderer;
import com.xrbpowered.battlespace.ui.ExplosionRenderer;
import com.xrbpowered.net.NetMessage;

public class Explosion extends Entity<Explosion> {
	
	public static final long DURATION = 250L;
	public static final float BASE_DAMAGE = 150f;
	public static final float BASE_HIT_RADIUS= 300f;
	public static final float BASE_PUSH = 1f;
	
	public final float power;
	public final Player owner;
	
	private long age = 0;
	private boolean processed = false;
	
	public Explosion(Game game, Player owner, float x, float y, float power, long age) {
		super(game);
		setPosition(x, y);
		this.owner = owner;
		this.power = power;
		this.age = age;
	}

	private static ExplosionRenderer renderer = new ExplosionRenderer();
	
	@Override
	public EntityRenderer<Explosion> renderer() {
		return renderer;
	}
	
	@Override
	public NetMessage createSpawnMessage() {
		return new MsgSpawnExplosion(this);
	}

	public int getMaxDamage() {
		return (int)(power*BASE_DAMAGE);
	}
	
	public float getHitRadius() {
		return power*BASE_HIT_RADIUS;
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
		float s = 1f - distBetween(player)/getHitRadius();
		int damage = (int)Math.round(getMaxDamage()*s);
		if(damage>0) {
			float px = player.x - x;
			float py = player.y - y;
			float len = (float) Math.sqrt(px*px+py*py);
			px *= power * s / (len+1f);
			py *= power * s / (len+1f);
			player.push(px*BASE_PUSH, py*BASE_PUSH);
			player.receiveDamage(damage, owner);
		}
	}
	
	@Override
	public void update(long dt) {
		if(game.isServer() && !processed) {
			checkPlayerOverlaps(getHitRadius());
			processed = true;
		}
		age += dt;
		if(age>=DURATION)
			destroy();
	}

}
