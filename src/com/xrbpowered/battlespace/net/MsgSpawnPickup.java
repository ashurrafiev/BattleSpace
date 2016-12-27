package com.xrbpowered.battlespace.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.xrbpowered.battlespace.game.Game;
import com.xrbpowered.battlespace.game.Pickup;
import com.xrbpowered.battlespace.game.PickupFactory;
import com.xrbpowered.net.Connection;
import com.xrbpowered.net.NetBase;
import com.xrbpowered.net.NetMessage;

public class MsgSpawnPickup extends NetMessage {
	public static final byte CMD = 18;
	
	public final int uid;
	public final int type;
	public final float x, y;
	
	public MsgSpawnPickup(Pickup p) {
		super(CMD);
		this.uid = p.uid;
		this.type = p.type;
		this.x = p.x;
		this.y = p.y;
	}
	
	public MsgSpawnPickup(DataInputStream in) throws IOException {
		super(CMD);
		this.uid = in.readInt();
		this.type = in.readByte();
		this.x = in.readFloat();
		this.y = in.readFloat();
	}
	
	@Override
	protected void pack(DataOutputStream out) throws IOException {
		out.writeInt(uid);
		out.writeByte(type);
		out.writeFloat(x);
		out.writeFloat(y);
	}
	
	@Override
	public boolean process(NetBase net, Connection c) {
		if(!net.isServer()) {
			Game game = ((BSNetClient) net).game;
			Pickup p = PickupFactory.create(game, type);
			p.uid = uid;
			p.setPosition(x, y);
			game.addEntity(p);
			return true;
		}
		else
			return false;
	}
	
}
