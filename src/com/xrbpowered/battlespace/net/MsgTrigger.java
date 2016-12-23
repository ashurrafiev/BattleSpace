package com.xrbpowered.battlespace.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.xrbpowered.battlespace.game.Game;
import com.xrbpowered.battlespace.game.Player;
import com.xrbpowered.net.Connection;
import com.xrbpowered.net.NetBase;
import com.xrbpowered.net.NetMessage;

public class MsgTrigger extends NetMessage {
	public static final byte CMD = 13;
	
	public final int index;
	public final int trigger;
	public final boolean state;
	
	public MsgTrigger(int index, int trigger, boolean state) {
		super(CMD);
		this.index = index;
		this.trigger = trigger;
		this.state = state;
	}
	
	public MsgTrigger(DataInputStream in) throws IOException {
		super(CMD);
		this.index = in.readByte();
		int b = ((int) in.readByte()) & 0xff;
		this.trigger = b & 0x7f;
		this.state = b > 127;
	}
	
	@Override
	protected void pack(DataOutputStream out) throws IOException {
		out.writeByte(index);
		int b = trigger & 0x7f;
		if(state) b += 128;
		out.writeByte(b);
	}
	
	@Override
	public boolean process(NetBase net, Connection c) {
		if(net.isServer()) {
			Game game = ((BSNetServer) net).game;
			Player player = game.players[index];
			if(((MsgBSClientInfo)c.getInfo()).playerIndex!=index || player==null)
				return false;
			player.setTrigger(trigger, state);
			return true;
		}
		else
			return false;
	}
	
}
