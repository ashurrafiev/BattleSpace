package com.xrbpowered.battlespace.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.xrbpowered.net.Connection;
import com.xrbpowered.net.NetBase;
import com.xrbpowered.net.NetMessage;

public class MsgPing extends NetMessage {
	public static final byte CMD_PING = 65;
	public static final byte CMD_PONG = 66;
	
	public final long time;
	
	public MsgPing() {
		super(CMD_PING);
		this.time = System.currentTimeMillis();
	}

	private MsgPing(long time) {
		super(CMD_PONG);
		this.time = time;
	}

	public MsgPing(byte cmd, DataInputStream in) throws IOException {
		super(cmd);
		this.time = in.readLong();
	}
	
	@Override
	protected void pack(DataOutputStream out) throws IOException {
		out.writeLong(time);
	}
	
	@Override
	public boolean process(NetBase net, Connection c) {
		if(cmd==CMD_PING) {
			c.addMessage(new MsgPing(time));
		}
		else {
			c.statLatency = System.currentTimeMillis() - time;
		}
		return true;
	}
}
