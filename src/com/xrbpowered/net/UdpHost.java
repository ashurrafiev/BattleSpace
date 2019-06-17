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
