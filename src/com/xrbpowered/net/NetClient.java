package com.xrbpowered.net;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;

public class NetClient extends NetBase {

	public static final int DEF_PORT = 3377;
	
	public final String serverHost;
	public final int serverPort;
	
	public String login = "";

	protected Connection serverConnection = null;
	protected boolean initSuccessful = false;

	public NetClient(String host, int port) {
		this.serverHost = host;
		this.serverPort = port<=0 ? DEF_PORT : port;
	}
	
	@Override
	public boolean isServer() {
		return false;
	}
	
	@Override
	public boolean open() {
		host.open();
		host.setRemote(serverHost, serverPort);
		connections.add(serverConnection = new Connection(host.remoteHost, host.remotePort));
		serverConnection.addMessage(NetMessage.CMD_HELLO);
		serverConnection.addMessage(createInfo());
		return true;
	}
	
	protected MsgClientInfo createInfo() {
		return new MsgClientInfo(this, login);
	}
	
	public boolean isConnected() {
		return serverConnection!=null;
	}
	
	public boolean isInitSuccessful() {
		return initSuccessful;
	}
	
	public Connection getServerConnection() {
		return serverConnection;
	}
	
	protected MsgServerInfo getServerInfo(DataInputStream in) throws IOException {
		return new MsgServerInfo(in);
	}
	
	@Override
	protected NetMessage translateMessage(byte cmd, DataInputStream in) throws IOException {
		switch(cmd) {
			case MsgServerInfo.CMD:
				return getServerInfo(in);
			case MsgClientInfo.CMD_DONE_INIT:
				return new NetMessage(cmd) {
					@Override
					public boolean process(NetBase net, Connection c) {
						initSuccessful = true;
						return true;
					}
				};
			default:
				return super.translateMessage(cmd, in);
		}
	}
	
	@Override
	protected Connection addConnection(InetAddress address, int port) {
		return null;
	}
	
	@Override
	protected boolean removeConnection(Connection c) {
		if(super.removeConnection(c)) {
			serverConnection = null;
			return true;
		}
		else
			return false;
	}
	
}
