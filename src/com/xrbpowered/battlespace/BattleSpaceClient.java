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
package com.xrbpowered.battlespace;

import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.xrbpowered.battlespace.game.ClientPlayer;
import com.xrbpowered.battlespace.game.Game;
import com.xrbpowered.battlespace.game.GameClient;
import com.xrbpowered.battlespace.net.MsgChat;
import com.xrbpowered.battlespace.ui.BattlePane;
import com.xrbpowered.battlespace.ui.KeyStateTracker;
import com.xrbpowered.battlespace.ui.MessageLog;
import com.xrbpowered.battlespace.ui.StatusPane;

public class BattleSpaceClient extends JFrame {

	public static final String DEFAULT_HOST = "127.0.0.1";
	public static final int DEFAULT_PORT = 3377;
	public static final String DEFAULT_PLAYER_NAME = "NewPlayer";
	
	private BattlePane battlePane;
	private StatusPane[] statusPanes = new StatusPane[Game.MAX_PLAYERS];
	
	private GameClient game;
	
	public static KeyStateTracker keys = new KeyStateTracker();
	
	public BattleSpaceClient(String playerName, String host, int port) {
		setTitle("Battle Space");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		
		game = new GameClient(playerName, host, port);
		
		JPanel cp = new JPanel() {
			public void doLayout() {
				battlePane.setBounds(StatusPane.WIDTH, 0, BattlePane.SIZE, BattlePane.SIZE);
				for(int i=0; i<statusPanes.length; i+=2) {
					statusPanes[i].setBounds(0, i/2*StatusPane.HEIGHT, StatusPane.WIDTH, StatusPane.HEIGHT);
					statusPanes[i+1].setBounds(StatusPane.WIDTH+BattlePane.SIZE, i/2*StatusPane.HEIGHT, StatusPane.WIDTH, StatusPane.HEIGHT);
				}
			}
		};
		cp.setPreferredSize(new Dimension(1500, 900));
		cp.add(battlePane = new BattlePane(this));
		for(int i=0; i<statusPanes.length; i++) {
			cp.add(statusPanes[i] = new StatusPane(this, i));
		}
		
		addKeyListener(keys);
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(java.awt.event.KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER && e.isControlDown()) {
					String msg = JOptionPane.showInputDialog(null, "Chat message:", "");
					if(msg!=null && !msg.isEmpty()) {
						BattlePane.messageLog.add(MessageLog.CHAT_SELF, "You: "+msg);
						game.net.broadcastMessage(new MsgChat(msg), null);
					}
				}
			}
		});
		
		setContentPane(cp);
		pack();
		setVisible(true);
		
		UpdateThread thread = new UpdateThread(this);
		thread.game = game;
		thread.start();
	}
	
	public GameClient getGame() {
		return game;
	}
	
	public ClientPlayer getPlayer() {
		return game.player;
	}
	
	public static void main(String[] args) {
		String playerName = DEFAULT_PLAYER_NAME;
		String host = DEFAULT_HOST;
		int port = DEFAULT_PORT;
		if(args.length>0) {
			String[] s = args[0].split("\\:", 2);
			host = s[0];
			port = Integer.parseInt(s[1]);
			if(port<=0 || port>65535)
				port = DEFAULT_PORT;
		}
		if(args.length>1) {
			playerName = args[1].trim();
			if(!playerName.matches("[A-Za-z0-9_]{3,}"))
				playerName = DEFAULT_PLAYER_NAME;
		}
		System.out.printf("Login: %s\nServer: %s\nPort: %d\n", playerName, host, port);
		
		new BattleSpaceClient(playerName, host, port);
	}
	
}
