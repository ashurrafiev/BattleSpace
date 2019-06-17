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
	
	public static final Weapon plasma = new Weapon(0, "plasma", 75L, 50) {
		@Override
		public boolean fire(Player player) {
			player.game.addEntity(new Projectile(player.game, player, Projectile.TYPE_PLASMA).shoot(player, 0f));
			return true;
		}
	};

	public static final Weapon spread = new Weapon(1, "spread", 500L, 20) {
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
	
	public static final Weapon homing = new Weapon(3, "homing", 500L, 5) {
		@Override
		public boolean fire(Player player) {
			player.game.addEntity(new Missile(player.game, player, Missile.TYPE_HOMING).shoot(player, 0f));
			return true;
		}
	};

	public static final Weapon gas = new Weapon(4, "gas", 50L, 30) {
		@Override
		public boolean fire(Player player) {
			player.game.addEntity(new GasCloud(player.game, player, 0L).offset(player, Game.random.nextFloat()*360f, Player.RADIUS*(0.5f+Game.random.nextFloat())));
			return true;
		}
	};

	public static final Weapon mines = new Weapon(5, "mines", 500L, 3) {
		@Override
		public boolean fire(Player player) {
			player.game.addEntity(new Mine(player.game, player, 0L).setPosition(player.x, player.y));
			return true;
		}
	};

}