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
		g2.setStroke(STROKE);
		ProjectileInfo info = Projectile.INFO[proj.type];
		g2.setColor(info.color);
		g2.drawLine((int)(proj.x), (int)(proj.y),
				(int)(proj.x-info.traceLength*Math.cos(proj.angle)),
				(int)(proj.y-info.traceLength*Math.sin(proj.angle)));
	}
}
