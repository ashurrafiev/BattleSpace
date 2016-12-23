package com.xrbpowered.battlespace;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import com.xrbpowered.battlespace.game.Game;

public class UpdateThread extends Thread {

	public final BattleSpaceClient client; 
	public Game game = null;
	
	public UpdateThread(BattleSpaceClient client) {
		this.client = client;
	}
	
	public void run() {
		try{
			long t0 = System.currentTimeMillis();
			for(;;) {
				long t = System.currentTimeMillis();
				if(game!=null) {
					game.update(t-t0);
					game.net.update();
				}
				t0 = t;
				if(client!=null) {
					SwingUtilities.invokeAndWait(new Runnable() {
						@Override
						public void run() {
							client.repaint();
						}
					});
				}
				Thread.sleep(20);
			}
		}
		catch(InterruptedException | InvocationTargetException e) {
		}
	}

}
