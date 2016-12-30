/*******************************************************************************
 * MIT License
 *
 * Copyright (c) 2016 Ashur Rafiev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *******************************************************************************/
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
