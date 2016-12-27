package com.xrbpowered.battlespace.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.xrbpowered.battlespace.game.Game;
import com.xrbpowered.battlespace.game.Player;
import com.xrbpowered.net.Connection;
import com.xrbpowered.net.NetBase;
import com.xrbpowered.net.NetMessage;

public class MsgPlayerPush extends NetMessage {
	public static final byte CMD = 14;
	
	public final int index;
	public final float px, py;
	
	public MsgPlayerPush(Player player, float px, float py) {
		super(CMD);
		this.index = player.index;
		this.px = px;
		this.py = py;
	}
	
	public MsgPlayerPush(DataInputStream in) throws IOException {
		super(CMD);
		this.index = in.readByte();
		this.px = in.readFloat();
		this.py = in.readFloat();
	}
	
	@Override
	protected void pack(DataOutputStream out) throws IOException {
		out.writeByte(index);
		out.writeFloat(px);
		out.writeFloat(py);
	}
	
	@Override
	public boolean process(NetBase net, Connection c) {
		if(!net.isServer()) {
			Game game = ((BSNetClient) net).game;
			Player player = game.players[index];
			if(player==null)
				return false;
			player.push(px, py);
			return true;
		}
		else
			return false;
	}
}
