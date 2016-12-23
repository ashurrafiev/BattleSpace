package com.xrbpowered.battlespace.net;

import java.io.DataInputStream;
import java.io.IOException;

import com.xrbpowered.battlespace.game.GameClient;
import com.xrbpowered.net.Connection;
import com.xrbpowered.net.MsgServerInfo;
import com.xrbpowered.net.NetBase;
import com.xrbpowered.net.NetClient;
import com.xrbpowered.net.NetMessage;

public class BSNetClient extends NetClient {

	public GameClient game = null;
	
	public BSNetClient(String host, int port) {
		super(host, port);
	}
	
	public void addGame(GameClient game, String login) {
		this.game = game;
		this.login = login;
		open(); // TODO connection progress display
	}

	public int counter = 0;
	
	@Override
	protected boolean pullStateUpdates(Connection c) {
		if(!isInitSuccessful() || game.player==null)
			return false;
		c.addMessage(new MsgPlayerPos(game.playerIndex, game.player));
		counter++;
		if(counter==20) {
			c.addMessage(new MsgPing());
			counter = 0;
		}
		return true;
	}
	
	@Override
	protected MsgServerInfo getServerInfo(DataInputStream in) throws IOException {
		return new MsgServerInfo(in) {
			@Override
			protected void logError(byte localRes) {
				System.out.printf("Conection error: %d+%d\n", response, localRes);
			}
			@Override
			public boolean process(NetBase net, Connection c) {
				System.out.printf("Response from server: %d\n", response);
				return super.process(net, c);
			}
		};
	}
	
	@Override
	protected NetMessage translateMessage(byte cmd, DataInputStream in) throws IOException {
		switch(cmd) {
			case MsgPlayerAddRemove.CMD_ADD:
				return new MsgPlayerAddRemove(in, true);
			case MsgPlayerAddRemove.CMD_REMOVE:
				return new MsgPlayerAddRemove(in, false);
			case MsgPlayerPos.CMD:
				return new MsgPlayerPos(in);
			case MsgPlayerDestroyed.CMD:
				return new MsgPlayerDestroyed(in);
			case MsgPlayerStatus.CMD:
				return new MsgPlayerStatus(in);
				
			case MsgSpawnProjectile.CMD:
				return new MsgSpawnProjectile(in);
			case MsgRemoveProjectile.CMD:
				return new MsgRemoveProjectile(in);
				
			case MsgChat.CMD:
				return new MsgChat(in.readUTF());
			case MsgPing.CMD_PING:
			case MsgPing.CMD_PONG:
				return new MsgPing(cmd, in);
			default:
				return super.translateMessage(cmd, in);
		}
	}

}
