package com.xrbpowered.net;

import java.net.DatagramPacket;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class PacketCollector extends Thread {

	public final UdpHost host;
	public final int maxLength;
	
	private LinkedList<DatagramPacket> queue = new LinkedList<>();
	
	public PacketCollector(UdpHost host, int maxLength) {
		this.host = host;
		this.maxLength = maxLength;
	}	
	
	@Override
	public void run() {
		for(;;) {
			DatagramPacket p = host.receive(maxLength, 100);
			if(p!=null)
				queue.add(p);
			if(Thread.interrupted())
				break;
		}
	}
	
	public DatagramPacket receive() {
		try {
			return queue.removeFirst();
		}
		catch(NoSuchElementException e) {
			return null;
		}
	}
	
}
