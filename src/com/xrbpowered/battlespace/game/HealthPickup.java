package com.xrbpowered.battlespace.game;

import java.awt.Color;

import com.xrbpowered.battlespace.ui.EntityRenderer;
import com.xrbpowered.battlespace.ui.PickupRenderer;

public class HealthPickup extends Pickup {

	private static final Color COLOR = new Color(0x0099ff);
	
	public final int hp;
	private final String label;
	
	public HealthPickup(Game game, int type, int hp) {
		super(game, type);
		this.hp = hp;
		this.label = String.format("+%d hp", hp);
	}
	
	private static PickupRenderer renderer = new PickupRenderer();
	
	@Override
	public EntityRenderer<Pickup> renderer() {
		return renderer;
	}
	
	@Override
	public void onOverlapPlayer(Player player) {
		if(!destroyed) {
			player.restoreHealth(hp);
		}
		super.onOverlapPlayer(player);
	}

	@Override
	public Color getColor() {
		return COLOR;
	}

	@Override
	public String getLabel() {
		return label;
	}
	
}
