package com.xrbpowered.battlespace.game;

import com.xrbpowered.battlespace.net.BSNetServer;
import com.xrbpowered.net.NetMessage;

public class GameServer extends Game {

	public GameServer(int port) {
		BSNetServer net = new BSNetServer(port);
		net.addGame(this);
		this.net = net;
	}
	
	@Override
	public void addEntity(Entity<?> e) {
		super.addEntity(e);
		NetMessage msg = e.createSpawnMessage();
		if(msg!=null)
			net.broadcastMessage(msg, null);
	}
	
	@Override
	public void update(long dt) {
		PickupFactory.spawn(this, dt, random);
		super.update(dt);
	}

}
