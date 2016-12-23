package com.xrbpowered.battlespace.game;

public class Entity {

	public float x, y, angle;
	public float vx, vy;
	public boolean destroyed = false;

	public final Game game;
	
	public Entity(Game game) {
		this.game = game;
	}
	
	protected void updateAngle() {
		this.angle = (float) Math.atan2(vy, vx);
	}
	
	protected void destroy() {
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
	
	public float distBetween(Entity other) {
		float dx = this.x-other.x;
		float dy = this.y-other.y;
		return (float)Math.sqrt(dx*dx+dy*dy);
	}
	
}
