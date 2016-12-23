package com.xrbpowered.battlespace.game;

import java.awt.Point;

import com.xrbpowered.battlespace.net.MsgPlayerDestroyed;
import com.xrbpowered.battlespace.net.MsgPlayerStatus;

public class Player extends Entity {

	public static final float RADIUS = 20.0f;
	
	public static final long RESPAWN_TIME = 3000L;
	public static final long RESPAWN_SHIELD_TIME = 1000L;
	public static final long PRIMARY_COOLDOWN_TIME = 125L;
	public static final long SPREAD_COOLDOWN_TIME = 500L;
	
	public static final int RESPAWN_TIMER = 0;
	public static final int EFFECT_TIMER = 1;
	public static final int PRIMARY_COOLDOWN = 2;
	public static final int SECONDARY_COOLDOWN = 3;
	
	public static final int PRIMARY_TRIGGER = 0;
	public static final int SECONDARY_TRIGGER = 1;
	
	public String name;
	public int index = -1;
	public int health;
	public Point mouse = new Point();
	public int score = 0;
	
	private boolean[] triggers = new boolean[2];
	
	private Timer[] timers = {
		new Timer(RESPAWN_TIME),
		new Timer(),
		new Timer(PRIMARY_COOLDOWN_TIME),
		new Timer()
	};
	
	public Player(Game game, String name) {
		super(game);
		this.name = name;
		destroy(0, game.getNewPickupLocation());
	}
	
	@Override
	protected void updateAngle() {
		angle = (float) Math.atan2(mouse.y-y, mouse.x-x);
	}
	
	public void setTrigger(int trigger, boolean state) {
		if(trigger>=0 && trigger<triggers.length)
			triggers[trigger] = state;
	}
	
	public void destroy(long dt, Point spawn) {
		this.x = spawn.x;
		this.y = spawn.y;
		this.vx = 0;
		this.vy = 0;
		this.health = 100;
		timers[RESPAWN_TIMER].start();
		timers[EFFECT_TIMER].start(RESPAWN_TIME+RESPAWN_SHIELD_TIME);
		timers[PRIMARY_COOLDOWN].reset();
		timers[SECONDARY_COOLDOWN].reset();
		updateAngle();
	}
	
	@Override
	protected void destroy() {
		if(game.isServer() && index>=0) {
			destroy(0, game.getNewPickupLocation());
			game.net.broadcastMessage(new MsgPlayerDestroyed(this), null);
		}
	}
	
	public Timer getTimer(int id) {
		return timers[id];
	}
	
	public boolean isRespawning() {
		return timers[RESPAWN_TIMER].isActive();
	}
	
	public boolean isShielded() {
		return timers[EFFECT_TIMER].isActive();
	}
	
	protected void addScore(int add) {
		if(game.isServer()) {
			this.score += add;
			game.net.broadcastMessage(new MsgPlayerStatus(this, MsgPlayerStatus.SCORE, this.score), null);
		}
	}
	
	@Override
	protected boolean checkClip() {
		if(super.checkClip()) {
			addScore(-1);
			return true;
		}
		else
			return false;
	}
	
	public void receiveDamage(int damage, Player owner) {
		if(!game.isServer())
			return;
		if(isShielded())
			return;
		health -= damage;
		if(health<=0) {
			health = 0;
			if(owner!=null) {
				if(owner!=this)
					owner.addScore(1);
				else
					this.addScore(-1);
			}
			destroy();
		}
		else {
			game.net.broadcastMessage(new MsgPlayerStatus(this, MsgPlayerStatus.HEALTH, this.health), null);
		}
	}
	
	protected void updateTimers(long dt) {
		for(Timer timer : timers)
			timer.update(dt);
	}
	
	@Override
	public void update(long dt) {
		if(!isRespawning()) {
			if(game.isServer() && !getTimer(PRIMARY_COOLDOWN).isActive() && triggers[PRIMARY_TRIGGER]) {
				game.addProjectile(new Projectile(game, this, Projectile.TYPE_MINI).shoot(this, 0f));
				getTimer(PRIMARY_COOLDOWN).start();
			}
			/*if(game.isServer() && !getTimer(SECONDARY_COOLDOWN).isActive() && triggers[SECONDARY_TRIGGER]) {
				for(int i=-3; i<=3; i++) {
					game.addProjectile(new Projectile(game, this, Projectile.TYPE_SPREAD).shoot(this, i*5f));
				}
				getTimer(SECONDARY_COOLDOWN).start(SPREAD_COOLDOWN_TIME);
			}*/
		}
		
		updateTimers(dt);
		super.update(dt);
	}
	
}
