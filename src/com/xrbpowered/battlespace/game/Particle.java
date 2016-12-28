package com.xrbpowered.battlespace.game;

import com.xrbpowered.battlespace.ui.EntityRenderer;
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

}
