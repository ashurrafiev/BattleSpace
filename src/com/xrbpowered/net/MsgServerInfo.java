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
