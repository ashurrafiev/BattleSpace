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

import com.xrbpowered.battlespace.game.Game;
import com.xrbpowered.battlespace.game.Missile;
import com.xrbpowered.battlespace.game.Player;
import com.xrbpowered.net.Connection;
import com.xrbpowered.net.NetBase;
import com.xrbpowered.net.NetMessage;

public class MsgSpawnMissile extends NetMessage {
	public static final byte CMD = 20;
	
	public final int uid;
	public final int ownerIndex, type;
	public final float x, y, vx, vy;
	
	public MsgSpawnMissile(Missile m) {
		super(CMD);
		this.uid = m.uid;
		this.ownerIndex = m.owner.index;
		this.type = m.type;
		this.x = m.x;
		this.y = m.y;
		this.vx = m.vx;
		this.vy = m.vy;
	}
	
	public MsgSpawnMissile(DataInputStream in) throws IOException {
		super(CMD);
		this.uid = in.readInt();
		this.ownerIndex = in.readByte();
		this.type = in.readByte();
		this.x = in.readFloat();
		this.y = in.readFloat();
		this.vx = in.readFloat();
		this.vy = in.readFloat();
	}
	
	@Override
	protected void pack(DataOutputStream out) throws IOException {
		out.writeInt(uid);
		out.writeByte(ownerIndex);
		out.writeByte(type);
		out.writeFloat(x);
		out.writeFloat(y);
		out.writeFloat(vx);
		out.writeFloat(vy);
	}
	
	@Override
	public boolean process(NetBase net, Connection c) {
		if(!net.isServer()) {
			Game game = ((BSNetClient) net).game;
			Player owner = game.players[ownerIndex];
			Missile m = new Missile(game, owner, type);
			m.uid = uid;
			m.setPosition(x, y);
			m.setVelocity(vx, vy);
			game.addEntity(m);
			return true;
		}
		else
			return false;
	}
}
