package com.xrbpowered.battlespace.game;

import java.awt.Point;

import com.xrbpowered.battlespace.net.MsgRemoveEntity;
import com.xrbpowered.battlespace.ui.EntityRenderer;
import com.xrbpowered.net.NetMessage;

public abstract class Entity<T extends Entity<T>> {

	public float x, y, angle;
	public float vx, vy;
	public boolean destroyed = false;

	public final Game game;
	public int uid = -1;

	public Entity(Game game) {
		this.game = game;
	}
	
	public abstract EntityRenderer<T> renderer();
	
	public NetMessage createSpawnMessage() {
		return null;
	}

	public Entity<T> setPosition(Point p) {
		this.x = p.x;
		this.y = p.y;
		return this;
	}

	public Entity<T> setPosition(float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}

	public Entity<T> setVelocity(float vx, float vy) {
		this.vx = vx;
		this.vy = vy;
		updateAngle();
		return this;
	}
	
	protected void updateAngle() {
		this.angle = (float) Math.atan2(vy, vx);
	}
	
	protected void destroy() {
		if(game.isServer() && uid>=0) {
			game.net.broadcastMessage(new MsgRemoveEntity(this), null);
		}
		this.destroyed = true;
	}
	
	protected boolean checkClip() {
		return !Game.CLIP_RECT.contains(this.x, this.y);
	}
	
	protected void updateLocation(long dt) {
		this.x += vx*dt;
		this.y += vy*dt;
		if(checkClip())
			destroy();
	}
	
	public void update(long dt) {
		updateLocation(dt);
	}
	
	public float distBetween(Entity<?> other) {
		float dx = this.x-other.x;
		float dy = this.y-other.y;
		return (float)Math.sqrt(dx*dx+dy*dy);
	}
	
	public boolean canTriggerOverlapPlayer(Player player) {
		return false;
	}
	
	public void onOverlapPlayer(Player player) {
	}
	
	protected void checkPlayerOverlaps(float dist) {
		if(game.isServer()) {
			for(Player player : game.players) {
				if(player!=null && !player.isRespawning() && canTriggerOverlapPlayer(player) && distBetween(player)<=dist) {
					onOverlapPlayer(player);
				}
			}
		}
	}
	
}
