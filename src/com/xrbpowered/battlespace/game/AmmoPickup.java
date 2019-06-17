package com.xrbpowered.battlespace.game;

import java.awt.Color;

import com.xrbpowered.battlespace.ui.EntityRenderer;
import com.xrbpowered.battlespace.ui.PickupRenderer;

public class AmmoPickup extends Pickup {
	
	private static final Color COLOR = new Color(0xccaa77);
	
	public AmmoPickup(Game game, int type) {
		super(game, type);
	}
	
	@Override
	public EntityRenderer<Pickup> renderer() {
		return PickupRenderer.ammoRenderer;
	}
	
	@Override
	public void onOverlapPlayer(Player player) {
		if(!destroyed) {
			player.fillAmmo();
		}
		super.onOverlapPlayer(player);
	}

	@Override
	public Color getColor() {
		return COLOR;
	}

	@Override
	public String getLabel() {
		return "ammo";
	}
}
