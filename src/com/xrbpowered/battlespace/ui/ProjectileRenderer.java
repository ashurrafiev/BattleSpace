package com.xrbpowered.battlespace.ui;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;

import com.xrbpowered.battlespace.game.Projectile;
import com.xrbpowered.battlespace.game.Projectile.ProjectileInfo;

public class ProjectileRenderer implements EntityRenderer<Projectile> {
	
	private static final Stroke STROKE = new BasicStroke(1.5f); 
	
	@Override
	public void render(Graphics2D g2, Projectile proj) {
		ProjectileInfo info = Projectile.INFO[proj.type];
		g2.setColor(info.color);
		if(info.circle) {
			g2.fillOval((int)(proj.x-info.size), (int)(proj.y-info.size), (int)(info.size*2f), (int)(info.size*2f));
		}
		else {
			g2.setStroke(STROKE);
			g2.drawLine((int)(proj.x), (int)(proj.y),
					(int)(proj.x-info.size*Math.cos(proj.angle)),
					(int)(proj.y-info.size*Math.sin(proj.angle)));
		}
	}
}
