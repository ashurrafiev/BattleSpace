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
	};
}
