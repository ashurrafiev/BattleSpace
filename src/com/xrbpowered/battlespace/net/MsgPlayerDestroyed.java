package com.xrbpowered.battlespace.net;

import java.awt.Point;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.xrbpowered.battlespace.game.Game;
import com.xrbpowered.battlespace.game.Player;
import com.xrbpowered.net.Connection;
import com.xrbpowered.net.NetBase;
import com.xrbpowered.net.NetMessage;

public class MsgPlayerDestroyed extends NetMessage {
	public static final byte CMD = 11;
	
	public final int index;
	public final float x, y;
	
	public MsgPlayerDestroyed(Player player) {
		super(CMD);
		this.index = player.index;
		this.x = player.x;
		this.y = player.y;
	}
	
	public MsgPlayerDestroyed(DataInputStream in) throws IOException {
		super(CMD);
		this.index = in.readByte();
		this.x = in.readFloat();
		this.y = in.readFloat();
	}
	
	@Override
	protected void pack(DataOutputStream out) throws IOException {
		out.writeByte(index);
		out.writeFloat(x);
		out.writeFloat(y);
	}
	
	@Override
	public boolean process(NetBase net, Connection c) {
		if(!net.isServer()) {
			Game game = ((BSNetClient) net).game;
			Player player = game.players[index];
			if(player==null)
				return false;
			player.destroy(0, new Point((int) x, (int) y));
			return true;
		}
		else
			return false;
	}
}
