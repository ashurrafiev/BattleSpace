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
	public List<Entity<?>> newEntities = new ArrayList<>();
	public List<Entity<?>> entities = new ArrayList<>();
	public HashMap<Integer, Entity<?>> entityUidMap = new HashMap<>();
	
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

	public void addEntity(Entity<?> e) {
		if(e.uid<0) {
			int uid = nextProjUid++;
			if(nextProjUid<0)
				nextProjUid = 0;
			e.uid = uid;
		}
		newEntities.add(e);
		entityUidMap.put(e.uid, e);		
	}
	
	public void update(long dt) {
		for(Player player : players) {
			if(player!=null)
				player.update(dt);
		}
		for(Iterator<Entity<?>> i = entities.iterator(); i.hasNext();) {
			Entity<?> e = i.next();
			if(!e.destroyed)
				e.update(dt);
			if(e.destroyed) {
				entityUidMap.remove(e.uid);
				i.remove();
			}
		}
		
		entities.addAll(newEntities);
		newEntities.clear();
	}
	
	public Point getNewPickupLocation() {
		int x = random.nextInt(16);
		int y = random.nextInt(16);
		return new Point(x*50-375, y*50-375);
	}
	
}
