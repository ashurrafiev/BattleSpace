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

import com.xrbpowered.battlespace.net.BSNetServer;
import com.xrbpowered.net.NetMessage;

public class GameServer extends Game {

	public GameServer(int port) {
		BSNetServer net = new BSNetServer(port);
		net.addGame(this);
		this.net = net;
	}
	
	@Override
	public void addEntity(Entity<?> e) {
		super.addEntity(e);
		NetMessage msg = e.createSpawnMessage();
		if(msg!=null)
			net.broadcastMessage(msg, null);
	}
	
	@Override
	public void update(long dt) {
		PickupFactory.spawn(this, dt, random);
		super.update(dt);
	}

}
