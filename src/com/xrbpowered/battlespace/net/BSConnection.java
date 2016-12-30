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

import java.net.InetAddress;

import com.xrbpowered.net.Connection;
import com.xrbpowered.net.ConnectionInfo;

public class BSConnection extends Connection {

	public static final byte ERR_BAD_NICKNAME = 2;
	
	public BSConnection(InetAddress address, int port) {
		super(address, port);
	}
	
	@Override
	protected byte setInfo(ConnectionInfo info) {
		if(info instanceof MsgBSClientInfo) {
			MsgBSClientInfo cinfo = (MsgBSClientInfo) info;
			if(cinfo.login.isEmpty() || !cinfo.login.trim().equals(cinfo.login))
				return ERR_BAD_NICKNAME;
		}
		else
			return ConnectionInfo.ERR_UNKNOWN;
		return super.setInfo(info);
	}

}
