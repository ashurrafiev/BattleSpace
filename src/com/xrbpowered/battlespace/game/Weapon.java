package com.xrbpowered.battlespace.game;

public abstract class Weapon {

	public static final Weapon[] byId = new Weapon[16];
	
	public final int id;
	public final String name;
	public final long cooldown;
	public final int maxAmmo;
	
	public Weapon(int id, String name, long cooldown, int maxAmmo) {
		this.id = id;
		this.name = name;
		this.cooldown = cooldown;
		this.maxAmmo = maxAmmo;
		byId[id] = this;
	}
	
	public abstract boolean fire(Player player);
	
	public static final Weapon plasma = new Weapon(0, "plasma", 75L, 75) {
		@Override
		public boolean fire(Player player) {
			player.game.addEntity(new Projectile(player.game, player, Projectile.TYPE_PLASMA).shoot(player, 0f));
			return true;
		}
	};

	public static final Weapon spread = new Weapon(1, "spread", 500L, 15) {
		@Override
		public boolean fire(Player player) {
			for(int i=-3; i<=3; i++) {
				player.game.addEntity(new Projectile(player.game, player, Projectile.TYPE_SPREAD).shoot(player, i*5f));
			}
			return true;
		}
	};
	
	public static final Weapon rocket = new Weapon(2, "rocket", 750L, 5) {
		@Override
		public boolean fire(Player player) {
			player.game.addEntity(new Missile(player.game, player, Missile.TYPE_ROCKET).shoot(player, 0f));
			return true;
		}
	};

}