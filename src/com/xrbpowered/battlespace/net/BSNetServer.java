package com.xrbpowered.battlespace.net;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;

import com.xrbpowered.battlespace.game.Entity;
import com.xrbpowered.battlespace.game.GameServer;
import com.xrbpowered.battlespace.game.Player;
import com.xrbpowered.net.Connection;
import com.xrbpowered.net.MsgClientInfo;
import com.xrbpowered.net.NetMessage;
import com.xrbpowered.net.NetServer;

public class BSNetServer extends NetServer {

	public GameServer game = null;
	
	public BSNetServer(int port) {
		super(port);
	}
	
	public void addGame(GameServer game) {
		this.game = game;
		open();
	}
	
	@Override
	protected MsgClientInfo getClientInfo(DataInputStream in) throws IOException {
		return new MsgBSClientInfo(in);
	}
	
	@Override
	protected NetMessage translateMessage(byte cmd, DataInputStream in) throws IOException {
		switch(cmd) {
			case MsgPlayerPos.CMD:
				return new MsgPlayerPos(in);
			case MsgTrigger.CMD:
				return new MsgTrigger(in);
			case MsgChat.CMD:
				return new MsgChat(in.readUTF());
			case MsgPing.CMD_PING:
			case MsgPing.CMD_PONG:
				return new MsgPing(cmd, in);
			default:
				return super.translateMessage(cmd, in);
		}
	}
	
	@Override
	public void sendState(Connection target) {
		for(Connection c : connections) {
			MsgBSClientInfo info = (MsgBSClientInfo) c.getInfo();
			if(c!=target && info.initSuccessful && info.player!=null) {
				target.addMessage(new MsgPlayerAddRemove(info.playerIndex, info.login, true));
				target.addMessage(new MsgPlayerStatus(info.player, MsgPlayerStatus.HEALTH, info.player.health));
				target.addMessage(new MsgPlayerStatus(info.player, MsgPlayerStatus.SCORE, info.player.score));
				target.addMessage(new MsgPlayerStatus(info.player, MsgPlayerStatus.RESPAWN_TIMER, (int) info.player.getTimer(Player.RESPAWN_TIMER).remaining()));
				target.addMessage(new MsgPlayerStatus(info.player, MsgPlayerStatus.EFFECT_TIMER, (int) info.player.getTimer(Player.EFFECT_TIMER).remaining()));
				target.addMessage(new MsgPlayerStatus(info.player, MsgPlayerStatus.SECONDARY_WEAPON, info.player.secondaryWeapon==null ? -1 : info.player.secondaryWeapon.id));
				target.addMessage(new MsgPlayerStatus(info.player, MsgPlayerStatus.AMMO, info.player.ammo));
			}
		}
		for(Entity<?> e : game.entities) {
			if(e.uid>=0)
				target.addMessage(e.createSpawnMessage());
		}
	}
	
	@Override
	protected Connection addConnection(InetAddress address, int port) {
		System.out.printf("Added %s:%d\n", address.getHostName(), port);
		return super.addConnection(address, port);
	}
	
	@Override
	protected boolean removeConnection(Connection c) {
		if(super.removeConnection(c)) {
			System.out.printf("Removed %s:%d\n", c.address.getHostName(), c.port);
			return true;
		}
		else
			return false;
	}
	
}
