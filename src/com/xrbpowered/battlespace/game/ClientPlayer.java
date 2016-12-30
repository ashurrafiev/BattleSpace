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

import java.awt.Point;
import java.awt.event.KeyEvent;

import com.xrbpowered.battlespace.BattleSpaceClient;
import com.xrbpowered.battlespace.net.MsgTrigger;
import com.xrbpowered.battlespace.ui.KeyStateTracker;

public class ClientPlayer extends Player {

	private static final float ACCELERATION = 0.002f;
	private static final float MAX_SPEED = 0.5f;
	private static final float INERTIA = 0.999f;
	
	public String deathMessage = "You have joined.";
	
	public ClientPlayer(Game game, String name) {
		super(game, name);
	}
	
	public void updateMouse(Point p) {
		this.mouse.setLocation(p);
		updateAngle();
	}
	
	@Override
	public void setTrigger(int trigger, boolean state) {
		if(index>=0) {
			super.setTrigger(trigger, state);
			game.net.broadcastMessage(new MsgTrigger(index, trigger, state), null);
		}
	}
	
	protected Point getControlVector() {
		KeyStateTracker keys = BattleSpaceClient.keys;
		int dx = 0;
		int dy = 0;
		if(keys.isKeyPressed(KeyEvent.VK_W))
			dy -= 1;
		if(keys.isKeyPressed(KeyEvent.VK_S))
			dy += 1;
		if(keys.isKeyPressed(KeyEvent.VK_A))
			dx -= 1;
		if(keys.isKeyPressed(KeyEvent.VK_D))
			dx += 1;
		return new Point(dx, dy);
	}
	
	public void receiveDamage(int damage, Player owner) {
		super.receiveDamage(damage, owner);
		if(isRespawning())
			deathMessage = owner==this ? "Cought your own bullet." : (owner==null ? "Killed by accident." : "Killed by "+owner.name+".");
	}
	
	@Override
	public void push(float px, float py) {
		if(!isRespawning()) {
			vx += px;
			vy += py;
		}
	}
	
	@Override
	protected boolean checkClip() {
		if(super.checkClip()) {
			deathMessage = "Lost in space.";
			return true;
		}
		else
			return false;
	}
	
	@Override
	public void update(long dt) {
		if(!isRespawning()) {
			Point cv = getControlVector();
			vx *= 1.0 - (1.0-INERTIA)*dt;
			vy *= 1.0 - (1.0-INERTIA)*dt;
			if(cv.x!=0 || cv.y!=0) {
				double a = Math.atan2(cv.y, cv.x);
				vx += ACCELERATION*Math.cos(a)*dt;
				vy += ACCELERATION*Math.sin(a)*dt;
				double v = Math.sqrt(vx*vx+vy*vy);
				if(v>MAX_SPEED) {
					vx /= v/MAX_SPEED;
					vy /= v/MAX_SPEED;
				}
			}
		}
		super.update(dt);
		updateAngle();	
	}

}
