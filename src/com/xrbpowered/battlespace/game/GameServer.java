package com.xrbpowered.battlespace.game;

import com.xrbpowered.battlespace.net.BSNetServer;
import com.xrbpowered.battlespace.net.MsgSpawnProjectile;

public class GameServer extends Game {

	public GameServer(int port) {
		BSNetServer net = new BSNetServer(port);
		net.addGame(this);
		this.net = net;
	}
	
	@Override
	public void addEntity(Entity<?> e) {
		super.addEntity(e);
		// FIXME entity spawn packet
		if(e instanceof Projectile) {
			net.broadcastMessage(new MsgSpawnProjectile((Projectile) e), null);
		}
	}

}
