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
