package com.xrbpowered.battlespace.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.xrbpowered.utils.RandomUtils;

public class PickupFactory {

	public static final int HEALTH_25 = 0;
	public static final int HEALTH_50 = 1;
	public static final int HEALTH_100 = 2;
	public static final int AMMO = 3;
	
	public static final int WEAPON_BASE = 4;
	
	private static final int[] weights = {6, 3, 1, 4, 1, 1, 1}; 

	private static final int MAX_PICKUPS = 5;
	private static final long RESPAWN_TIME = 5000L;

	private static List<Pickup> active = new ArrayList<>();
	private static Timer respawnTimer = new Timer(RESPAWN_TIME);
	
	public static Pickup create(Game game, int type) {
		switch(type) {
			case HEALTH_25:
				return new HealthPickup(game, type, 25);
			case HEALTH_50:
				return new HealthPickup(game, type, 50);
			case HEALTH_100:
				return new HealthPickup(game, type, 100);
			case AMMO:
				return new AmmoPickup(game, type);
			default:
				return new WeaponPickup(game, type, Weapon.byId[type-WEAPON_BASE]);
		}
	}
	
	public static Pickup createRandom(Game game, Random random) {
		return create(game, RandomUtils.weighted(random, weights));
	}
	
	public static Pickup spawn(Game game, long dt, Random random) {
		if(!game.isServer())
			return null;
		respawnTimer.update(dt);
		if(active.size()<MAX_PICKUPS && !respawnTimer.isActive()) {
			Pickup p = createRandom(game, random);
			if(p!=null) {
				game.addEntity(p);
				active.add(p);
				respawnTimer.start();
			}
			return p;
		}
		else
			return null;
	}
	
	public static void remove(Pickup p) {
		active.remove(p);
	}
	
}
