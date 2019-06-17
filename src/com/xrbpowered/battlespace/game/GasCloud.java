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
