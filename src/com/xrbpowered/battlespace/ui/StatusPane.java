package com.xrbpowered.battlespace.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import com.xrbpowered.battlespace.BattleSpaceClient;
import com.xrbpowered.battlespace.game.Game;
import com.xrbpowered.battlespace.game.Player;
import com.xrbpowered.net.NetClient;

public class StatusPane extends JPanel {

	public static final int WIDTH = 300;
	public static final int HEIGHT = BattlePane.SIZE*2/Game.MAX_PLAYERS;
	
	public static final Font FONT = new Font("Tahoma", Font.PLAIN, 13);
	public static final Font CHAT_FONT = FONT.deriveFont(Font.BOLD, 14f);
	public static final Font BOLD_FONT = FONT.deriveFont(Font.BOLD, 18f);
	public static final Font BIG_FONT = FONT.deriveFont(Font.BOLD, 35f);
	
	public final int index;
	
	private final BattleSpaceClient client;
	
	public StatusPane(BattleSpaceClient client, int index) {
		this.client = client;
		this.index = index;
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setBackground(new Color(0x222222));
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(0x777777));
		g2.drawRect(1, 1, getWidth()-3, getHeight()-3);
		
		g2.setFont(FONT);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setColor(Color.LIGHT_GRAY);
		g2.drawString(String.format("Player %d", index+1), 10, 20);
		
		g2.setFont(BOLD_FONT);
		Player player = client.getGame().players[index];
		if(player!=null) {
			g2.setColor(player==client.getPlayer() ? Color.WHITE : Color.RED);
			g2.drawString(player.name, 10, 40);
			g2.setFont(FONT);
			g2.setColor(Color.LIGHT_GRAY);
			g2.drawString("Score:", 150, 40);
			g2.setFont(BIG_FONT);
			g2.setColor(Color.WHITE);
			g2.drawString(String.format("%d", player.isRespawning() ? 0 : player.health), 10, 75);
			g2.setColor(Color.LIGHT_GRAY);
			g2.drawString(String.format("%d", player.score), 150, 75);
			
			// weapon
			if(player.secondaryWeapon!=null) {
				g2.setColor(Color.WHITE);
				g2.setFont(BOLD_FONT);
				g2.drawString(player.secondaryWeapon.name, 10, 120);
				g2.setFont(BIG_FONT);
				g2.drawString(String.format("%d", player.ammo), 150, 120);
			}
			
			// latency
			if(player==client.getPlayer()) {
				NetClient net = (NetClient) client.getGame().net;
				if(net.isConnected()) {
					long lat = net.getServerConnection().statLatency;
					g2.setColor(Color.WHITE);
					g2.setFont(FONT);
					g2.drawString(String.format("Latency: %dms", lat), 10, HEIGHT-10);
				}
				else {
					g2.setColor(Color.RED);
					g2.setFont(FONT);
					g2.drawString("Connection lost", 10, HEIGHT-10);
				}
			}
		}
		else {
			g2.setColor(Color.GRAY);
			g2.drawString("Disconnected", 10, 40);
		}
	}
	
}
