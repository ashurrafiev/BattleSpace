package com.xrbpowered.battlespace.game;

import java.awt.Color;

import com.xrbpowered.battlespace.ui.EntityRenderer;
import com.xrbpowered.battlespace.ui.PickupRenderer;

public class WeaponPickup extends Pickup {
	
	private static final Color COLOR = new Color(0xffbb77);
	
	public final Weapon weapon;
	
	public WeaponPickup(Game game, int type, Weapon weapon) {
		super(game, type);
		this.weapon = weapon;
	}
	
	@Override
	public EntityRenderer<Pickup> renderer() {
		return PickupRenderer.weaponRenderer;
	}
	
	@Override
	public void onOverlapPlayer(Player player) {
		if(!destroyed) {
			player.setSecondaryWeapon(weapon);
		}
		super.onOverlapPlayer(player);
	}

	@Override
	public float getRadius() {
		return LARGE_RADIUS;
	}
	
	@Override
	public Color getColor() {
		return COLOR;
	}

	@Override
	public String getLabel() {
		return weapon.name;
	}
}
