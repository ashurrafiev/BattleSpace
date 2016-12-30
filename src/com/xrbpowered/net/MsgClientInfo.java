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
