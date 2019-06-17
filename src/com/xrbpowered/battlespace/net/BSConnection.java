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
