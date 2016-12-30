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
import java.io.DataOutputStream;
import java.io.IOException;

import com.xrbpowered.battlespace.game.ClientPlayer;
import com.xrbpowered.battlespace.game.GameClient;
import com.xrbpowered.battlespace.game.Player;
import com.xrbpowered.battlespace.ui.BattlePane;
import com.xrbpowered.battlespace.ui.MessageLog;
import com.xrbpowered.net.Connection;
import com.xrbpowered.net.NetBase;
import com.xrbpowered.net.NetMessage;

public class MsgPlayerAddRemove extends NetMessage {
	public static final byte CMD_ADD = 8;
	public static final byte CMD_REMOVE = 9;
	
	public final int index;
	public final String name;
	
	public MsgPlayerAddRemove(int index, String name, boolean add) {
		super(add ? CMD_ADD : CMD_REMOVE);
		this.index = index;
		this.name = name;
	}
	
	public MsgPlayerAddRemove(DataInputStream in, boolean add) throws IOException {
		super(add ? CMD_ADD : CMD_REMOVE);
		this.index = in.readByte();
		this.name = in.readUTF(); //add ? in.readUTF() : null;
	}
	
	@Override
	protected void pack(DataOutputStream out) throws IOException {
		out.writeByte(index);
		{ //if(cmd==CMD_ADD) {
			out.writeUTF(name==null ? "" : name);
		}
	}
	
	@Override
	public boolean process(NetBase net, Connection c) {
		GameClient game = ((BSNetClient) net).game;
		// TODO message
		if(cmd==CMD_ADD) {
			if(name.isEmpty()) {
				game.players[index] = game.player = new ClientPlayer(game, ((BSNetClient) net).login);
				game.playerIndex = index;
				BattlePane.messageLog.add(MessageLog.SYSTEM, "You have joined the battle.");
			}
			else {
				game.players[index] = new Player(game, name);
				BattlePane.messageLog.add(MessageLog.SYSTEM, game.players[index].name+" joins the battle.");
			}
			game.players[index].index = index;
		}
		else {
			BattlePane.messageLog.add(MessageLog.SYSTEM, game.players[index].name+" disconnected.");
			game.players[index] = null;
		}
		return true;
	}

}
