package com.xrbpowered.battlespace;

import com.xrbpowered.battlespace.game.Game;
import com.xrbpowered.battlespace.game.GameServer;

public class BattleSpaceServer {

	public static final int DEFAULT_PORT = 3377;
	
	public static void main(String[] args) {
		int port = DEFAULT_PORT;
		if(args.length>0) {
			port = Integer.parseInt(args[0]);
			if(port<=0 || port>65535)
				port = DEFAULT_PORT;
		}
		System.out.printf("Port: %d\n", port);
		
		Game game = new GameServer(port);
		UpdateThread thread = new UpdateThread(null);
		thread.game = game;
		thread.start();
	}

}
