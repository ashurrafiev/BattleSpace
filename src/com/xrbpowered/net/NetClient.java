/*******************************************************************************
 * MIT License
 *
 * Copyright (c) 2016 Ashur Rafiev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *******************************************************************************/
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
