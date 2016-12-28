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
