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

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class UdpHost {

	protected InetAddress remoteHost = null;
	protected int remotePort = 0;
	protected DatagramSocket socket = null;
	
	public void setRemote(String remoteHost, int remotePort) {
		try {
			this.remoteHost = InetAddress.getByName(remoteHost);
			this.remotePort = remotePort;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setRemote(InetAddress remoteHost, int remotePort) {
		this.remoteHost = remoteHost;
		this.remotePort = remotePort;
	}
	
	public void open() {
		try {
			socket = new DatagramSocket();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void open(int port) {
		try {
			socket = new DatagramSocket(port);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void close() {
		if(socket!=null) {
			socket.close();
			socket = null;
		}
	}
	
	public void send(byte[] message, int length) {
		try {
			DatagramPacket packet = new DatagramPacket(message, length, remoteHost, remotePort);
			socket.send(packet);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public DatagramPacket receive(int maxLength, int timeout) {
		try {
			byte[] buf = new byte[maxLength];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			socket.setSoTimeout(timeout);
			socket.receive(packet);
			return packet;
		}
		catch(SocketTimeoutException e) {
			return null;
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
