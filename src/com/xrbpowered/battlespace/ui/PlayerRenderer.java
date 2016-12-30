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
package com.xrbpowered.battlespace.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import com.xrbpowered.battlespace.game.ClientPlayer;
import com.xrbpowered.battlespace.game.Player;

public class PlayerRenderer implements EntityRenderer<Player> {
	
	@Override
	public void render(Graphics2D g2, Player player) {
		g2.setStroke(new BasicStroke(2f));
		if(!player.isRespawning()) {
			g2.setColor(player instanceof ClientPlayer ? Color.WHITE : Color.RED);
			g2.drawOval((int)(player.x-Player.RADIUS), (int)(player.y-Player.RADIUS), (int)(Player.RADIUS*2f), (int)(Player.RADIUS*2f));
			g2.drawLine((int)(player.x), (int)(player.y),
					(int)(player.x+Player.RADIUS*1.2f*Math.cos(player.angle)),
					(int)(player.y+Player.RADIUS*1.2f*Math.sin(player.angle)));
			FontMetrics fm = g2.getFontMetrics();
			g2.drawString(player.name, (int)(player.x-fm.stringWidth(player.name)/2), (int)(player.y+Player.RADIUS+15));
			if(player.isShielded()) {
				g2.setColor(Color.CYAN);
				g2.drawOval((int)(player.x-Player.RADIUS-7), (int)(player.y-Player.RADIUS-7), (int)(Player.RADIUS*2f+14), (int)(Player.RADIUS*2f+14));
			}
		}
		else {
			long ts = player.getTimer(Player.RESPAWN_TIMER).remaining();
			if(ts<500L) {
				float r = Player.RADIUS*2f*ts/500f;
				g2.setColor(Color.CYAN);
				g2.drawOval((int)(player.x-r), (int)(player.y-r), (int)(r*2f), (int)(r*2f));
			}
		}
	};

}
