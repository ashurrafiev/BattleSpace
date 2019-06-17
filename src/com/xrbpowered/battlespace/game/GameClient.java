package com.xrbpowered.battlespace.game;

import com.xrbpowered.battlespace.net.BSNetClient;

public class GameClient extends Game {

	public ClientPlayer player = null;
	public int playerIndex = -1;

	public GameClient(String playerName, String host, int port) {
		BSNetClient net = new BSNetClient(host, port);
		net.addGame(this, playerName);
		this.net = net;

	}
	
	@Override
	public boolean isServer() {
		return false;
	}

}
