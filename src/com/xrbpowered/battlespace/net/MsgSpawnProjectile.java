package com.xrbpowered.battlespace.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.xrbpowered.battlespace.game.Game;
import com.xrbpowered.battlespace.game.Player;
import com.xrbpowered.battlespace.game.Projectile;
import com.xrbpowered.net.Connection;
import com.xrbpowered.net.NetBase;
import com.xrbpowered.net.NetMessage;

public class MsgSpawnProjectile extends NetMessage {
	public static final byte CMD = 17;
	
	public final int uid;
	public final int ownerIndex, type;
	public final float x, y, vx, vy;
	
	public MsgSpawnProjectile(Projectile proj) {
		super(CMD);
		this.uid = proj.uid;
		this.ownerIndex = proj.owner.index;
		this.type = proj.type;
		this.x = proj.x;
		this.y = proj.y;
		this.vx = proj.vx;
		this.vy = proj.vy;
	}
	
	public MsgSpawnProjectile(DataInputStream in) throws IOException {
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
			Projectile proj = new Projectile(game, owner, type);
			proj.uid = uid;
			proj.setPosition(x, y);
			proj.setVelocity(vx, vy);
			game.addEntity(proj);
			return true;
		}
		else
			return false;
	}

}
