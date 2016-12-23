package com.xrbpowered.battlespace.game;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.xrbpowered.battlespace.ui.BattlePane;
import com.xrbpowered.net.NetBase;

public class Game {

	public static final int MAX_PLAYERS = 4;
	public static final Rectangle CLIP_RECT = new Rectangle(
			(int)(-BattlePane.SIZE/2-Player.RADIUS*2),
			(int)(-BattlePane.SIZE/2-Player.RADIUS*2),
			(int)(BattlePane.SIZE+Player.RADIUS*4),
			(int)(BattlePane.SIZE+Player.RADIUS*4)
		);

	public static Random random = new Random();

	public NetBase net = null;
	
	public Player[] players = new Player[MAX_PLAYERS];
	
	private int nextProjUid = 0;
	public List<Projectile> projectiles = new ArrayList<>();
	public HashMap<Integer, Projectile> projectileUidMap = new HashMap<>();
	
	public int addPlayer(Player player) {
		for(int i=0; i<players.length; i++) {
			if(players[i]==null) {
				players[i] = player;
				player.index = i;
				return i;
			}
		}
		return -1;
	}
	
	public boolean isServer() {
		return true;
	}

	public void addProjectile(Projectile proj) {
		if(proj.uid<0) {
			int uid = nextProjUid++;
			if(nextProjUid<0)
				nextProjUid = 0;
			proj.uid = uid;
		}
		projectiles.add(proj);
		projectileUidMap.put(proj.uid, proj);		
	}
	
	public void update(long dt) {
		for(Player player : players) {
			if(player!=null)
				player.update(dt);
		}
		for(Iterator<Projectile> i = projectiles.iterator(); i.hasNext();) {
			Projectile proj = i.next();
			if(!proj.destroyed)
				proj.update(dt);
			if(proj.destroyed) {
				projectileUidMap.remove(proj.uid);
				i.remove();
			}
		}
	}
	
	public Point getNewPickupLocation() {
		int x = random.nextInt(16);
		int y = random.nextInt(16);
		return new Point(x*50-375, y*50-375);
	}
	
}
