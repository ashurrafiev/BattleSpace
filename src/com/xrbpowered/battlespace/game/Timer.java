package com.xrbpowered.battlespace.game;

public class Timer {

	private long def = 0L;
	private long remain = 0L;
	
	public Timer() {
	}
	
	public Timer(long def) {
		this.def = def;
	}
	
	public void start() {
		remain = def;
	}
	
	public void start(long time) {
		remain = time;
	}
	
	public void reset() {
		remain = 0L;
	}
	
	public boolean isActive() {
		return remain>0L;
	}
	
	public long remaining() {
		return remain;
	}
	
	public void update(long dt) {
		if(remain>0L) {
			remain -= dt;
			if(remain<0L)
				remain = 0L;
		}
	}
	
}
