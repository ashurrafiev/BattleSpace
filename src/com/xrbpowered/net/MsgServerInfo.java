package com.xrbpowered.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MsgServerInfo extends NetMessage implements ConnectionInfo {
	public static final byte CMD = 6;
	
	public static final byte ERR_VERSION = 1;
	
	public final byte response;
	public final int version;
	public final long time;
	public final long deltaTime;
	
	public MsgServerInfo(byte response, NetBase net) {
		super(CMD);
		this.response = response;
		this.version = net.getVersion();
		this.time = System.currentTimeMillis();
		this.deltaTime = 0L;
	}
	
	public MsgServerInfo(DataInputStream in) throws IOException {
		super(CMD);
		this.response = in.readByte();
		this.version = in.readInt();
		this.time = in.readLong();
		this.deltaTime = this.time - System.currentTimeMillis();
	}
	
	@Override
	protected void pack(DataOutputStream out) throws IOException {
		out.writeByte(response);
		out.writeInt(version);
		out.writeLong(time);
	}
	
	protected void logError(byte localRes) {
	}
	
	@Override
	public boolean process(NetBase net, Connection c) {
		if(net.isServer())
			return false;
		byte res = c.setInfo(this);
		if(response!=SUCCESS || res!=SUCCESS) {
			logError(res);
			if(response!=SUCCESS) {
				net.removeConnection(c);
				return false;
			}
			else {
				net.disconnect(c);
				return true;
			}
		}
		else
			return true;
	}
	
}
