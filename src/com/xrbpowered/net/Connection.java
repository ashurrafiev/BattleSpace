package com.xrbpowered.net;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;

public class Connection {

	public static final int MAX_QUEUE_SIZE = 256;
	
	private static class QueueItem {
		public final int id;
		public final NetMessage msg;
		public QueueItem(int id, NetMessage msg) {
			this.id = id;
			this.msg = msg;
		}
	}
	
	public final InetAddress address;
	public final int port;
	
	// TODO ping packet as a common net message 
	public long statLatency = 0L;
	
	protected ConnectionInfo info = null;
	protected LinkedList<QueueItem> msgQueue = new LinkedList<>();
	
	private int acknowledgedIncomingId = -1;
	private int acknowledgedOutgoingId = -1;
	private long aliveTime = -1;
	private int nextMsgId = 0;
	
	protected boolean reqDisconnect = false;
	
	public Connection(InetAddress address, int port) {
		this.address = address;
		this.port = port;
	}
	
	public ConnectionInfo getInfo() {
		return info;
	}
	
	protected byte setInfo(ConnectionInfo info) {
		this.info = info;
		return MsgServerInfo.SUCCESS;
	}
	
	public void addMessage(NetMessage msg) {
		msgQueue.add(new QueueItem(nextMsgId++, msg));
		if(msgQueue.size()>=MAX_QUEUE_SIZE)
			throw new RuntimeException("Connection queue overflow.");
	}

	public final void addMessage(byte cmd) {
		addMessage(new NetMessage(cmd));
	}

	protected void confirmAlive() {
		aliveTime = System.currentTimeMillis();
	}
	
	public boolean isAlive(long now, long ttl) {
		return aliveTime<0L || ttl<0L || (now-aliveTime)<ttl;
	}
	
	protected boolean acknowledgeOutgoingId(int id) {
		if(id>acknowledgedOutgoingId) { // FIXME && id<nextMsgId) {
			acknowledgedOutgoingId = id;
			return true;
		}
		else
			return false;
	}

	protected boolean acknowledgeIncomingId(int id) {
		if(id>acknowledgedIncomingId) {
			acknowledgedIncomingId = id;
			return true;
		}
		else
			return false;
	}

	protected void refineQueue(NetBase net) {
		for(;;) {
			try {
				for(Iterator<QueueItem> i=msgQueue.iterator(); i.hasNext();) {
					QueueItem qm = i.next();
					if(qm.id<=acknowledgedOutgoingId) { // TODO check TTL
						i.remove();
						qm.msg.acknowledged(net, this);
					}
				}
				return;
			}
			catch(ConcurrentModificationException e) {
				// continue from the beginning
			}
		}
	}
	
	protected byte[] preparePacket(NetBase net, int size) {
		try {
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(bytes);
			
			// ack incoming messages
			if(acknowledgedIncomingId>=0) {
				out.writeByte(NetMessage.CMD_ACK);
				out.writeInt(acknowledgedIncomingId);
			}
			
			// remove processed and outdated messages
			refineQueue(net);
			
			// write messages
			LinkedList<QueueItem> queue = msgQueue;
			LinkedList<QueueItem> tmpQueue = new LinkedList<>(); // to avoid concurrent modification from addMessage
			msgQueue = tmpQueue;
			for(QueueItem qm : queue) {
				if(bytes.size()>=size)
					break;
				out.writeByte(qm.msg.cmd);
				out.writeInt(qm.id);
				qm.msg.pack(out);
			}
			out.writeByte(NetMessage.CMD_END);
			msgQueue = queue;
			queue.addAll(tmpQueue); // in case there were new packets added
			
			return bytes.toByteArray();
		}
		catch(IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj==null || !(obj instanceof Connection))
			return false;
		Connection other = (Connection) obj;
		return port==other.port && address.equals(other.address);
	}
	
}
