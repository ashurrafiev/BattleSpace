package com.xrbpowered.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MsgClientInfo extends NetMessage implements ConnectionInfo {
	public static final byte CMD = 4;
	public static final byte CMD_DONE_INIT = 5;
	
	public final int version;
	public final String login;
	public boolean initSuccessful = false;
	
	public MsgClientInfo(NetBase net, String login) {
		super(CMD);
		this.version = net.getVersion();
		this.login = login;
	}
	
	public MsgClientInfo(DataInputStream in) throws IOException {
		super(CMD);
		this.version = in.readInt();
		this.login = in.readUTF();
	}
	
	@Override
	protected void pack(DataOutputStream out) throws IOException {
		out.writeInt(version);
		out.writeUTF(login);
	}
	
	protected boolean createUserObject(NetBase net) {
		return true;
	}
	
	protected void broadcastUserAdd(NetBase net, Connection c) {
	}
	
	protected void broadcastUserRemove(NetBase net, Connection c) {
	}
	
	@Override
	public boolean process(NetBase net, Connection c) {
		if(net.isServer()) {
			byte res = c.setInfo(this); 
			c.addMessage(new MsgServerInfo(res, net));
			if(res==SUCCESS) {
				((NetServer) net).sendState(c);
				c.addMessage(new NetMessage(CMD_DONE_INIT) {
					@Override
					protected void acknowledged(NetBase net, Connection c) {
						if(createUserObject(net)) {
							initSuccessful = true;
							broadcastUserAdd(net, c);
						}
						else {
							net.disconnect(c);
						}
					}
				});
			}
			else {
				net.disconnect(c);
			}
			return true;
		}
		else
			return false;
	}
	
}
