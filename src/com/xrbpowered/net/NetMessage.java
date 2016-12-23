package com.xrbpowered.net;

import java.io.DataOutputStream;
import java.io.IOException;

public class NetMessage {

	public static final byte CMD_END = 0;
	public static final byte CMD_HELLO = 1;
	public static final byte CMD_DISCONNECT = 2;
	public static final byte CMD_ACK = 3;
	
	public final byte cmd;
//	protected int id = -1;
	
	public NetMessage(byte cmd) {
		this.cmd = cmd;
	}

	protected void pack(DataOutputStream out) throws IOException {
	}

	public boolean process(NetBase net, Connection c) {
		return true;
	}

	protected void acknowledged(NetBase net, Connection c) {
	}
	
}
