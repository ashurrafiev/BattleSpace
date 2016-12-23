package com.xrbpowered.battlespace.net;

import java.io.DataInputStream;
import java.io.IOException;

import com.xrbpowered.battlespace.game.Game;
import com.xrbpowered.battlespace.game.Player;
import com.xrbpowered.net.Connection;
import com.xrbpowered.net.MsgClientInfo;
import com.xrbpowered.net.NetBase;

public class MsgBSClientInfo extends MsgClientInfo {

	public int playerIndex = -1;
	public Player player = null;
	
	public MsgBSClientInfo(NetBase net, String login) {
		super(net, login);
	}

	public MsgBSClientInfo(DataInputStream in) throws IOException {
		super(in);
	}

	@Override
	protected boolean createUserObject(NetBase net) {
		Game game = ((BSNetServer) net).game;
		this.player = new Player(game, login);
		this.playerIndex = game.addPlayer(player);
		return this.playerIndex>=0;
	}
	
	@Override
	protected void broadcastUserAdd(NetBase net, Connection c) {
		net.broadcastMessage(new MsgPlayerAddRemove(playerIndex, login, true), c);
		c.addMessage(new MsgPlayerAddRemove(playerIndex, null, true));
	}
	
	@Override
	protected void broadcastUserRemove(NetBase net, Connection c) {
		Game game = ((BSNetServer) net).game;
		game.players[playerIndex] = null;
		net.broadcastMessage(new MsgPlayerAddRemove(playerIndex, null, false), null);
	}
	
}
