package com.xrbpowered.net;

import java.io.DataInputStream;
import java.io.IOException;

public class NetServer extends NetBase {

	public static final int DEF_PORT = 3377;
	
	public final int port;

	public NetServer(int port) {
		this.port = port<=0 ? DEF_PORT : port;
	}
	
	@Override
	public boolean isServer() {
		return true;
	}
	
	@Override
	public boolean open() {
		host.open(port);
		System.out.println("Started...");
		return true;
	}
	
	@Override
	protected boolean pullStateUpdates(Connection c) {
		MsgClientInfo info = (MsgClientInfo) c.getInfo();
		return info!=null && info.initSuccessful;
	}
	
	protected MsgClientInfo getClientInfo(DataInputStream in) throws IOException {
		return new MsgClientInfo(in);
	}
	
	@Override
	protected NetMessage translateMessage(byte cmd, DataInputStream in) throws IOException {
		switch(cmd) {
			case MsgClientInfo.CMD:
				return getClientInfo(in);
			default:
				return super.translateMessage(cmd, in);
		}
	}
	
	public void sendState(Connection target) {
		// TODO send state
	}
	
}
