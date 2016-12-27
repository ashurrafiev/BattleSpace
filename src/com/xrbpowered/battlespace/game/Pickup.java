package com.xrbpowered.battlespace.game;

import java.awt.Color;

import com.xrbpowered.battlespace.net.MsgSpawnPickup;
import com.xrbpowered.net.NetMessage;

public abstract class Pickup extends Entity<Pickup> {

	public static final float RADIUS = 10.0f;
	public static final float PICK_RADIUS = RADIUS * 1.5f;
	
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
		checkPlayerOverlaps(Player.RADIUS+PICK_RADIUS);
	}
}
