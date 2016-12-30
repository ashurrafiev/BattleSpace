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

import com.xrbpowered.battlespace.game.Explosion;
import com.xrbpowered.battlespace.game.Game;
import com.xrbpowered.battlespace.game.Player;
import com.xrbpowered.net.Connection;
import com.xrbpowered.net.NetBase;
import com.xrbpowered.net.NetMessage;

public class MsgSpawnExplosion extends NetMessage {
	public static final byte CMD = 19;
	
	public final int uid;
	public final int ownerIndex, age;
	public final float x, y, power;
	
	public MsgSpawnExplosion(Explosion exp) {
		super(CMD);
		this.uid = exp.uid;
		this.ownerIndex = exp.owner==null ? -1 : exp.owner.index;
		this.age = (int) exp.getAge();
		this.x = exp.x;
		this.y = exp.y;
		this.power = exp.power;
	}
	
	public MsgSpawnExplosion(DataInputStream in) throws IOException {
		super(CMD);
		this.uid = in.readInt();
		this.ownerIndex = in.readByte();
		this.age = in.readInt();
		this.x = in.readFloat();
		this.y = in.readFloat();
		this.power = in.readFloat();
	}
	
	@Override
	protected void pack(DataOutputStream out) throws IOException {
		out.writeInt(uid);
		out.writeByte(ownerIndex);
		out.writeInt(age);
		out.writeFloat(x);
		out.writeFloat(y);
		out.writeFloat(power);
	}
	
	@Override
	public boolean process(NetBase net, Connection c) {
		if(!net.isServer()) {
			Game game = ((BSNetClient) net).game;
			Player owner = ownerIndex<0 ? null : game.players[ownerIndex];
			Explosion exp = new Explosion(game, owner, x, y, power, age);
			exp.uid = uid;
			game.addEntity(exp);
			return true;
		}
		else
			return false;
	}
}
