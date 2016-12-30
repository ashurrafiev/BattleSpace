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
