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
import com.xrbpowered.battlespace.game.GasCloud;
import com.xrbpowered.battlespace.game.Mine;
import com.xrbpowered.battlespace.game.Player;
import com.xrbpowered.net.Connection;
import com.xrbpowered.net.NetBase;
import com.xrbpowered.net.NetMessage;

public class MsgSpawnStaticEntity extends NetMessage {
	public static final byte CMD = 21;
	
	public static final int GAS_CLOUD = 0;
	public static final int MINE = 1;
	
	public final int uid;
	public final int ownerIndex, type, age;
	public final float x, y;
	
	private MsgSpawnStaticEntity(int type, Entity<?> e, Player owner, long age) {
		super(CMD);
		this.uid = e.uid;
		this.type = type;
		this.ownerIndex = owner==null ? -1 : owner.index;
		this.age = (int) age;
		this.x = e.x;
		this.y = e.y;
	}
	
	public MsgSpawnStaticEntity(GasCloud e) {
		this(GAS_CLOUD, e, e.owner, e.getAge());
	}

	public MsgSpawnStaticEntity(Mine e) {
		this(MINE, e, e.owner, e.getAge());
	}

	public MsgSpawnStaticEntity(DataInputStream in) throws IOException {
		super(CMD);
		this.uid = in.readInt();
		this.type = in.readByte();
		this.ownerIndex = in.readByte();
		this.age = in.readInt();
		this.x = in.readFloat();
		this.y = in.readFloat();
	}
	
	@Override
	protected void pack(DataOutputStream out) throws IOException {
		out.writeInt(uid);
		out.writeByte(type);
		out.writeByte(ownerIndex);
		out.writeInt(age);
		out.writeFloat(x);
		out.writeFloat(y);
	}
	
	@Override
	public boolean process(NetBase net, Connection c) {
		if(!net.isServer()) {
			Game game = ((BSNetClient) net).game;
			Player owner = ownerIndex<0 ? null : game.players[ownerIndex];
			Entity<?> e = null;
			switch(type) {
				case GAS_CLOUD:
					e = new GasCloud(game, owner, age);
					break;
				case MINE:
					e = new Mine(game, owner, age);
					break;
				default:
					return false;
			}
			e.uid = uid;
			e.setPosition(x, y);
			game.addEntity(e);
			return true;
		}
		else
			return false;
	}
}
