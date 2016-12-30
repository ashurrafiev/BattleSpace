/*******************************************************************************
 * MIT License
 *
 * Copyright (c) 2016 Ashur Rafiev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *******************************************************************************/
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

	public static final int VERSION = 100;
	
	public GameServer game = null;
	
	public BSNetServer(int port) {
		super(port);
	}
	
	@Override
	public int getVersion() {
		return VERSION;
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
