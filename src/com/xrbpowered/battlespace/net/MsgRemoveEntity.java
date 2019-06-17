package com.xrbpowered.battlespace.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.xrbpowered.battlespace.game.Entity;
import com.xrbpowered.battlespace.game.Game;
import com.xrbpowered.net.Connection;
import com.xrbpowered.net.NetBase;
import com.xrbpowered.net.NetMessage;

public class MsgRemoveEntity extends NetMessage {
	public static final byte CMD = 16;
	
	public final int uid;

	public MsgRemoveEntity(Entity<?> e) {
		super(CMD);
		this.uid = e.uid;
	}
	
	public MsgRemoveEntity(DataInputStream in) throws IOException {
		super(CMD);
		this.uid = in.readInt();
	}
	
	@Override
	protected void pack(DataOutputStream out) throws IOException {
		out.writeInt(uid);
	}
	
	@Override
	public boolean process(NetBase net, Connection c) {
		if(!net.isServer()) {
			Game game = ((BSNetClient) net).game;
			Entity<?> e = game.entityUidMap.get(uid);
			if(e!=null)
				e.destroyed = true;
			return true;
		}
		else
			return false;
	}
}
