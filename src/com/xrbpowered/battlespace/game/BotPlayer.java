package com.xrbpowered.battlespace.game;

import java.awt.Point;

public class BotPlayer extends ClientPlayer {

	public BotPlayer(Game game) {
		super(game, String.format("Bot.%02d", Game.random.nextInt(100)));
	}
	
	private Player target = null;
	private int targetTime = 0;
	private float targetDist = 0f;
	
	protected Player pickTarget() {
		Player target = null;
		float distTarget = 0f;
		for(Player player : game.players)
			if(player!=null && player!=this && !player.isRespawning()) {
				float dist = distBetween(player);
				if(target==null || dist<distTarget) {
					target = player;
					distTarget = dist;
				}
			}
		return target;
	}
	
	@Override
	protected Point getControlVector() {
		if(target==null || target.isRespawning() || targetTime<=0) {
			target = pickTarget();
			targetTime = Game.random.nextInt(500)+500;
		}
		else
			targetTime--;
		if(target!=null && !target.isRespawning()) {
			targetDist = distBetween(target);
			updateMouse(new Point(
					(int)(target.x+target.vx*targetDist/Projectile.INFO[0].speed),
					(int)(target.y+target.vy*targetDist/Projectile.INFO[0].speed)
				));
		}
		setTrigger(PRIMARY_TRIGGER, target!=null && !target.isRespawning());
		setTrigger(SECONDARY_TRIGGER, target!=null && !target.isRespawning());
		
		Vec2 res = new Vec2(-x, -y);
		float dist = 450f-res.length();
		if(dist<0f)
			dist = 0f;
		res.scale(50f/(dist*dist*dist+1f));
		Vec2 here = new Vec2(x, y).addScaled(new Vec2(vx, vy), 5f);
		for(Player player : game.players)
			if(player!=null && player!=this && !player.isRespawning()) {
				Vec2 d = Vec2.subtract(null, here, new Vec2(player.x, player.y));
				dist = d.length();
				res.addScaled(d, 0.1f/(dist*dist+1f));
			}
		for(Entity<?> e : game.entities)
			if(e instanceof Projectile) {
				Projectile proj = (Projectile) e;
				if(!proj.destroyed) {
					Vec2 p = new Vec2(proj.x, proj.y).addScaled(new Vec2(proj.vx, proj.vy), 10f);
					Vec2 d = Vec2.subtract(null, here, p);
					dist = d.length();
					if(dist<200f)
						res.addScaled(d, 0.05f/(dist*dist+1f));
				}
			}
		res.normalise();
		
		return new Point(control(res.x), control(res.y));
	}
	
	private int control(float dx) {
		if(dx<0.2f)
			return -1;
		else if(dx>0.2f)
			return 1;
		else
			return 0;
	}
	
}
