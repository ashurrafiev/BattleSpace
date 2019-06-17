package com.xrbpowered.battlespace.game;

import java.awt.Color;

import com.xrbpowered.battlespace.ui.EntityRenderer;
import com.xrbpowered.battlespace.ui.PickupRenderer;

public class HealthPickup extends Pickup {

	private static final Color COLOR = new Color(0x00bb00);
	private static final Color MEGA_COLOR = new Color(0x77ff33);
	
	public final int hp;
	private final String label;
	
	public HealthPickup(Game game, int type, int hp) {
		super(game, type);
		this.hp = hp;
		this.label = String.format("+%d hp", hp);
	}
	
	@Override
	public EntityRenderer<Pickup> renderer() {
		return PickupRenderer.healthRenderer;
	}
	
	@Override
	public void onOverlapPlayer(Player player) {
		if(!destroyed) {
			player.restoreHealth(hp);
		}
		super.onOverlapPlayer(player);
	}

	@Override
	public float getRadius() {
		return hp>=100 ? LARGE_RADIUS : SMALL_RADIUS;
	}
	
	@Override
	public Color getColor() {
		return hp>=100 ? MEGA_COLOR : COLOR;
	}

	@Override
	public String getLabel() {
		return label;
	}
	
}
