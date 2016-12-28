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
