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

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.LinkedList;

public abstract class NetBase {

	public static final int MAX_INCOMING_SIZE = 2048;
	public static final int OUTGOING_SIZE = 1024;
	public static final long INACTIVE_LIMIT = 1000L;

	protected UdpHost host = new UdpHost();
	protected PacketCollector packetCollector = null;
	protected ArrayList<Connection> connections = new ArrayList<>();

	public abstract boolean open();
	public abstract boolean isServer();
	
	public int getVersion() {
		return 0;
	}
	
	protected void startPacketCollector() {
		if(packetCollector==null) {
			packetCollector = new PacketCollector(host, MAX_INCOMING_SIZE);
			packetCollector.start();
		}
	}
	
	protected void stopPacketCollector() {
		if(packetCollector!=null) {
			packetCollector.interrupt();
			packetCollector = null;
		}
	}
	
	public void update() {
		for(;;) {
			DatagramPacket p;
			if(packetCollector!=null) {
				p = packetCollector.receive();
			}
			else {
				p = host.receive(MAX_INCOMING_SIZE, 1);
			}
			if(p==null)
				break;
			processMessages(p);
		}
		dropInactive();
		for(Connection c : connections) {
			pullStateUpdates(c);
			byte[] buf = c.preparePacket(this, OUTGOING_SIZE);
			if(buf!=null) {
				host.setRemote(c.address, c.port);
				host.send(buf, buf.length);
			}
		}
		removeDisconnected();
	}
	
	protected void dropInactive() {
		long now = System.currentTimeMillis();
		for(Connection c : connections) {
			if(!c.isAlive(now, INACTIVE_LIMIT))
				disconnect(c);
		}
	}
	
	protected void removeDisconnected() {
		// this is to support concurrent modification via removeConnection()
		LinkedList<Connection> toRemove = new LinkedList<>();
		for(Connection c : connections) {
			if(c.reqDisconnect)
				toRemove.add(c);
		}
		for(Connection c : toRemove)
			removeConnection(c);
	}
	
	protected boolean pullStateUpdates(Connection c) {
		return true;
	}
	
	public void broadcastMessage(NetMessage msg, Connection exclude) {
		for(Connection c : connections) {
			if(exclude==null || c!=exclude) {
				c.addMessage(msg);
			}
		}
	}
	
	protected NetMessage translateMessage(byte cmd, DataInputStream in) throws IOException {
		return null;
	}
	
	protected void processMessages(DatagramPacket p) {
		try {
			DataInputStream in = new DataInputStream(new ByteArrayInputStream(p.getData()));
			Connection c = null;
			for(;;) {
				byte cmd = in.readByte();
				if(cmd==NetMessage.CMD_END)
					return;
				// System.out.printf("cmd=%d\n", cmd);
				int id = -1;
				if(cmd!=NetMessage.CMD_ACK)
					id = in.readInt();

				if(c==null)
					c = findConnection(p);
				if(cmd==NetMessage.CMD_HELLO) {
					if(c==null)
						c = addConnection(p);
					if(c!=null) {
						c.acknowledgeIncomingId(id);
						continue;
					}
				}
				if(c==null)
					return;
				c.confirmAlive();
				
				if(cmd==NetMessage.CMD_DISCONNECT) {
					removeConnection(c);
					// ack disconnect?
					return;
				}
				else if(cmd==NetMessage.CMD_ACK) {
					c.acknowledgeOutgoingId(in.readInt());
				}
				else {
					NetMessage msg = translateMessage(cmd, in);
					if(msg==null)
						throw new IOException("Unknown net message");
					if(c.acknowledgeIncomingId(id)) {
						if(!msg.process(this, c)) {
							// System.out.println("No process cmd!");
						}
					}
				}
			}
		}
		catch(IOException e) {
			// log? ban client? do something?
			e.printStackTrace();
		}
	}
	
	protected Connection findConnection(DatagramPacket p) {
		for(Connection c : connections) {
			if(c.address.equals(p.getAddress()) && c.port==p.getPort())
				return c;
		}
		return null;
	}
	
	protected final Connection addConnection(DatagramPacket p) {
		return addConnection(p.getAddress(), p.getPort());
	}
	
	protected Connection createConnection(InetAddress address, int port) {
		return new Connection(address, port);
	}
	
	protected Connection addConnection(InetAddress address, int port) {
		Connection client = createConnection(address, port);
		connections.add(client);
		return client;
	}
	
	protected boolean removeConnection(Connection c) {
		return connections.remove(c);
	}
	
	public void disconnect(Connection c) {
		if(isServer()) {
			MsgClientInfo info = (MsgClientInfo) c.getInfo();
			if(info!=null && info.initSuccessful)
				info.broadcastUserRemove(this, c);
		}
		c.addMessage(NetMessage.CMD_DISCONNECT);
		c.reqDisconnect = true;
	}
	
}
