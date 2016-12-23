package com.xrbpowered.battlespace.net;

import java.io.DataOutputStream;
import java.io.IOException;

import com.xrbpowered.battlespace.ui.BattlePane;
import com.xrbpowered.battlespace.ui.MessageLog;
import com.xrbpowered.net.Connection;
import com.xrbpowered.net.MsgClientInfo;
import com.xrbpowered.net.NetBase;
import com.xrbpowered.net.NetMessage;

public class MsgChat extends NetMessage {
	public static final byte CMD = 64;
	
	public final String msg;
	
	public MsgChat(String msg) {
		super(CMD);
		this.msg = msg;
	}
	
	@Override
	protected void pack(DataOutputStream out) throws IOException {
		out.writeUTF(msg);
	}
	
	@Override
	public boolean process(NetBase net, Connection c) {
		String msg = this.msg;
		if(net.isServer()) {
			msg = String.format("%s: %s", ((MsgClientInfo) c.getInfo()).login, msg);
			net.broadcastMessage(new MsgChat(msg), c);
		}
		else {
			BattlePane.messageLog.add(MessageLog.CHAT, msg);
			System.out.println(msg);
		}
		return true;
	};

}
