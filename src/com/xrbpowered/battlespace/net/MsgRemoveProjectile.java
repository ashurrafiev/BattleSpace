package com.xrbpowered.battlespace.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.xrbpowered.battlespace.game.Game;
import com.xrbpowered.battlespace.game.Projectile;
import com.xrbpowered.net.Connection;
import com.xrbpowered.net.NetBase;
import com.xrbpowered.net.NetMessage;

public class MsgRemoveProjectile extends NetMessage {
	public static final byte CMD = 17;
	
	public final int uid;

	public MsgRemoveProjectile(Projectile proj) {
		super(CMD);
		this.uid = proj.uid;
	}
	
	public MsgRemoveProjectile(DataInputStream in) throws IOException {
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
			Projectile proj = game.projectileUidMap.get(uid);
			if(proj!=null)
				proj.destroyed = true;
			return true;
		}
		else
			return false;
	}
}
