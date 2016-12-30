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
package com.xrbpowered.battlespace.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.xrbpowered.battlespace.game.Entity;
import com.xrbpowered.battlespace.game.Game;
import com.xrbpowered.net.Connection;
import com.xrbpowered.net.NetBase;
import com.xrbpowered.net.NetMessage;

public class MsgEntityPos extends NetMessage {
	public static final byte CMD = 15;
	
	public final int uid;
	public final float x, y, vx, vy;
	
	public MsgEntityPos(Entity<?> e) {
		super(CMD);
		this.uid = e.uid;
		this.x = e.x;
		this.y = e.y;
		this.vx = e.vx;
		this.vy = e.vy;
	}
	
	public MsgEntityPos(DataInputStream in) throws IOException {
		super(CMD);
		this.uid = in.readInt();
		this.x = in.readFloat();
		this.y = in.readFloat();
		this.vx = in.readFloat();
		this.vy = in.readFloat();
	}
	
	@Override
	protected void pack(DataOutputStream out) throws IOException {
		out.writeInt(uid);
		out.writeFloat(x);
		out.writeFloat(y);
		out.writeFloat(vx);
		out.writeFloat(vy);
	}
	
	@Override
	public boolean process(NetBase net, Connection c) {
		if(!net.isServer()) {
			Game game = ((BSNetClient) net).game;
			Entity<?> e = game.entityUidMap.get(uid);
			if(e!=null) {
				e.setPosition(x, y);
				e.setVelocity(vx, vy);
			}
			return true;
		}
		else
			return false;
	}
}
