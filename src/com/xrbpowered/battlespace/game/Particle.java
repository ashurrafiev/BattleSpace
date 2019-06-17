package com.xrbpowered.battlespace.game;

import com.xrbpowered.battlespace.ui.EntityRenderer;
import com.xrbpowered.battlespace.ui.SmokeRenderer;
import com.xrbpowered.utils.TweenUtils;

public class Particle extends Entity<Particle> {

	public final EntityRenderer<Particle> renderer;
	public final long duration;
	public final float size;
	
	private Timer timer;
	
	public Particle(Game game, float size, EntityRenderer<Particle> renderer, long duration) {
		super(game);
		this.renderer = renderer;
		this.duration = duration;
		this.timer = new Timer(duration);
		timer.start();
		this.size = size;
	}

	@Override
	public EntityRenderer<Particle> renderer() {
		return renderer;
	}
	
	public double getS() {
		return TweenUtils.tween(duration-timer.remaining(), 0, duration);
	}
	
	@Override
	public void update(long dt) {
		timer.update(dt);
		if(timer.isActive())
			super.update(dt);
		else
			destroy();
	}
	
	public static Particle createSmoke(Game game, Entity<?> origin, float power) {
		Particle p = (Particle) new Particle(game, power*0.5f+0.5f, SmokeRenderer.instance, 750L).sling(origin, Game.random.nextFloat()*360f, 0.005f, 3f*power);
		game.newEntities.add(p);
		return p;
	}

}
