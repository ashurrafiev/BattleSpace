package com.xrbpowered.battlespace.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.xrbpowered.battlespace.game.ClientPlayer;
import com.xrbpowered.battlespace.game.Game;
import com.xrbpowered.battlespace.game.Player;
import com.xrbpowered.net.Connection;
import com.xrbpowered.net.NetBase;
import com.xrbpowered.net.NetMessage;

public class MsgPlayerPos extends NetMessage {
	public static final byte CMD = 10;
	
	public final int index;
	public final float x, y, angle;
	
	public MsgPlayerPos(int index, Player player) {
		super(CMD);
		this.index = index;
		this.x = player.x;
		this.y = player.y;
		this.angle = player.angle;
	}
	
	public MsgPlayerPos(DataInputStream in) throws IOException {
		super(CMD);
		this.index = in.readByte();
		this.x = in.readFloat();
		this.y = in.readFloat();
		this.angle = in.readFloat();
	}
	
	@Override
	protected void pack(DataOutputStream out) throws IOException {
		out.writeByte(index);
		out.writeFloat(x);
		out.writeFloat(y);
		out.writeFloat(angle);
	}
	
	@Override
	public boolean process(NetBase net, Connection c) {
		Player player;
		if(net.isServer()) {
			Game game = ((BSNetServer) net).game;
			player = game.players[index];
			if(((MsgBSClientInfo)c.getInfo()).playerIndex!=index || player==null)
				return false;
			net.broadcastMessage(this, c);
		}
		else {
			Game game = ((BSNetClient) net).game;
			player = game.players[index];
			if(player==null)
				return false;
		}
		if(player instanceof ClientPlayer)
			return true;
		if(!player.isRespawning()) {
			player.x = x;
			player.y = y;
			player.angle = angle;
		}
		return true;
	}
	
}
