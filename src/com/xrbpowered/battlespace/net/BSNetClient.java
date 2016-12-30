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

	@Override
	public int getVersion() {
		return BSNetServer.VERSION;
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
			case MsgPlayerPush.CMD:
				return new MsgPlayerPush(in);
				
			case MsgEntityPos.CMD:
				return new MsgEntityPos(in);
			case MsgRemoveEntity.CMD:
				return new MsgRemoveEntity(in);
			case MsgSpawnProjectile.CMD:
				return new MsgSpawnProjectile(in);
			case MsgSpawnPickup.CMD:
				return new MsgSpawnPickup(in);
			case MsgSpawnExplosion.CMD:
				return new MsgSpawnExplosion(in);
			case MsgSpawnMissile.CMD:
				return new MsgSpawnMissile(in);
			case MsgSpawnStaticEntity.CMD:
				return new MsgSpawnStaticEntity(in);
				
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
