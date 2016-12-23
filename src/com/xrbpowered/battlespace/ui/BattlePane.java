package com.xrbpowered.battlespace.ui;

import java.awt.BasicStroke;
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
import com.xrbpowered.battlespace.game.Game;
import com.xrbpowered.battlespace.game.Player;
import com.xrbpowered.battlespace.game.Projectile;
import com.xrbpowered.battlespace.game.Projectile.ProjectileInfo;
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
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(bgImg, 0, 0, null);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		g2.setFont(StatusPane.FONT);
		FontMetrics fm = g2.getFontMetrics();
		
		for(int i=0; i<Game.MAX_PLAYERS; i++) {
			Player player = client.getGame().players[i];
			if(player==null)
				continue;
			g2.setStroke(new BasicStroke(2f));
			if(!player.isRespawning()) {
				g2.setColor(player==client.getPlayer() ? Color.WHITE : Color.RED);
				g2.drawOval((int)(player.x+SIZE/2-Player.RADIUS), (int)(player.y+SIZE/2-Player.RADIUS), (int)(Player.RADIUS*2f), (int)(Player.RADIUS*2f));
				g2.drawLine((int)(player.x+SIZE/2), (int)(player.y+SIZE/2),
						(int)(player.x+SIZE/2+Player.RADIUS*1.2f*Math.cos(player.angle)),
						(int)(player.y+SIZE/2+Player.RADIUS*1.2f*Math.sin(player.angle)));
				g2.drawString(player.name, (int)(player.x+SIZE/2-fm.stringWidth(player.name)/2), (int)(player.y+SIZE/2+Player.RADIUS+15));
				if(player.isShielded()) {
					g2.setColor(Color.CYAN);
					g2.drawOval((int)(player.x+SIZE/2-Player.RADIUS-7), (int)(player.y+SIZE/2-Player.RADIUS-7), (int)(Player.RADIUS*2f+14), (int)(Player.RADIUS*2f+14));
				}
			}
			else {
				long ts = player.getTimer(Player.RESPAWN_TIMER).remaining();
				if(ts<500L) {
					float r = Player.RADIUS*2f*ts/500f;
					g2.setColor(Color.CYAN);
					g2.drawOval((int)(player.x+SIZE/2-r), (int)(player.y+SIZE/2-r), (int)(r*2f), (int)(r*2f));
				}
			}
		}
		
		g2.setStroke(new BasicStroke(1.5f));
		for(Projectile proj : client.getGame().projectiles) {
			ProjectileInfo info = Projectile.INFO[proj.type];
			g2.setColor(info.color);
			g2.drawLine((int)(proj.x+SIZE/2), (int)(proj.y+SIZE/2),
					(int)(proj.x+SIZE/2-info.traceLength*Math.cos(proj.angle)),
					(int)(proj.y+SIZE/2-info.traceLength*Math.sin(proj.angle)));
		}
		
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
