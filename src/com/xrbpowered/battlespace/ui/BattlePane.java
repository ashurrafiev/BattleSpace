package com.xrbpowered.battlespace.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.JPanel;

import com.xrbpowered.battlespace.BattleSpaceClient;
import com.xrbpowered.battlespace.game.ClientPlayer;
import com.xrbpowered.battlespace.game.Entity;
import com.xrbpowered.battlespace.game.Game;
import com.xrbpowered.battlespace.game.Player;
import com.xrbpowered.battlespace.ui.MessageLog.MessageItem;

public class BattlePane extends JPanel {

	public static final int SIZE = 900;

	public static MessageLog messageLog = new MessageLog();
	
	private BufferedImage bgImg;
	
	private final BattleSpaceClient client;
	
	public BattlePane(final BattleSpaceClient client) {
		this.client = client;
		setPreferredSize(new Dimension(SIZE, SIZE));
		setBackground(Color.BLACK);
		setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		setFont(StatusPane.FONT);
		
		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				ClientPlayer player = client.getPlayer();
				if(player!=null)
					player.updateMouse(new Point(e.getX()-SIZE/2, e.getY()-SIZE/2));
			}
			@Override
			public void mouseDragged(MouseEvent e) {
				ClientPlayer player = client.getPlayer();
				if(player!=null)
					player.updateMouse(new Point(e.getX()-SIZE/2, e.getY()-SIZE/2));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				ClientPlayer player = client.getPlayer();
				if(player!=null) {
					player.setTrigger(Player.PRIMARY_TRIGGER, false);
					player.setTrigger(Player.SECONDARY_TRIGGER, false);
				}
			}
		});
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				ClientPlayer player = client.getPlayer();
				if(player!=null) {
					if(e.getButton()==MouseEvent.BUTTON1)
						player.setTrigger(Player.PRIMARY_TRIGGER, true);
					else if(e.getButton()==MouseEvent.BUTTON3)
						player.setTrigger(Player.SECONDARY_TRIGGER, true);
				}
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				ClientPlayer player = client.getPlayer();
				if(player!=null) {
					if(e.getButton()==MouseEvent.BUTTON1)
						player.setTrigger(Player.PRIMARY_TRIGGER, false);
					else if(e.getButton()==MouseEvent.BUTTON3)
						player.setTrigger(Player.SECONDARY_TRIGGER, false);
				}
			}
		});
		
		bgImg = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = (Graphics2D) bgImg.getGraphics();
		Random random = new Random(123L);
		for(int i=0; i<3000; i++) {
			int x = random.nextInt(SIZE);
			int y = random.nextInt(SIZE);
			int c = random.nextInt(random.nextInt(160)+8)+8;
			g2.setColor(new Color(c, c, c));
			g2.fillRect(x, y, 1, 1);
		}
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	private void renderEntities(Graphics2D g2) {
		for(Entity e : client.getGame().entities) {
			e.renderer().render(g2, e);
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(bgImg, 0, 0, null);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		g2.setFont(StatusPane.FONT);
		FontMetrics fm = g2.getFontMetrics();
		
		// render entities
		g2.translate(SIZE/2, SIZE/2);
		for(int i=0; i<Game.MAX_PLAYERS; i++) {
			Player player = client.getGame().players[i];
			if(player==null)
				continue;
			player.renderer().render(g2, player);
		}
		renderEntities(g2);
		g2.translate(-SIZE/2, -SIZE/2);
		
		// render mid-screen message
		Player player = client.getPlayer();
		if(player!=null && player.isRespawning()) {
			g2.setColor(Color.WHITE);
			g2.setFont(StatusPane.BOLD_FONT);
			fm = g2.getFontMetrics();
			String s = ((ClientPlayer) player).deathMessage;
			g2.drawString(s, SIZE/2-fm.stringWidth(s)/2, SIZE/2-30);
			g2.drawString("Spawning in", SIZE/2-fm.stringWidth("Spawning in")/2, SIZE/2-10);
			g2.setFont(StatusPane.BIG_FONT);
			fm = g2.getFontMetrics();
			s = Long.toString(player.getTimer(Player.RESPAWN_TIMER).remaining()/1000L+1L);
			g2.drawString(s, SIZE/2-fm.stringWidth(s)/2, SIZE/2+30);
		}
		if(player==null) {
			g2.setColor(Color.WHITE);
			g2.setFont(StatusPane.BOLD_FONT);
			fm = g2.getFontMetrics();
			String s = "Connecting...";
			g2.drawString(s, SIZE/2-fm.stringWidth(s)/2, SIZE/2-30);
		}
		
		// render message log
		long t = System.currentTimeMillis();
		int y = 30;
		g2.setFont(StatusPane.CHAT_FONT);
		for(MessageItem msg : messageLog.log) {
			Color c = msg.getColor(t);
			if(c!=null) {
				g2.setColor(c);
				g2.drawString(msg.msg, 30, y);
			}
			y += 16;
		}
	}
	
}
