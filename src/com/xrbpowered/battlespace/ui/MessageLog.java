package com.xrbpowered.battlespace.ui;

import java.awt.Color;
import java.util.LinkedList;

import com.xrbpowered.utils.ColorUtils;
import com.xrbpowered.utils.TweenUtils;

public class MessageLog {

	public static final int SYSTEM = 0;
	public static final int CHAT = 1;
	public static final int CHAT_SELF = 2;
	
	public static final Color[] COLORS = {Color.WHITE, new Color(0xff00ddff), new Color(0xff0099dd)};
	public static final long TIME_STAY = 10000L;
	public static final long TIME_FADE = 2000L;
	
	public static class MessageItem {
		public final int type;
		public final String msg;
		public final long time;
		
		public MessageItem(int type, String msg) {
			this.type = type;
			this.msg = msg;
			this.time = System.currentTimeMillis();
		}
		
		public Color getColor(long t) {
			Color c = COLORS[type];
			if(t<time+TIME_STAY)
				return c;
			else if(t>time+TIME_STAY+TIME_FADE)
				return null;
			else {
				double s = TweenUtils.tween(t, time+TIME_STAY, TIME_FADE);
				return ColorUtils.alpha(c, 1.0-s);
			}
		}
	}
	
	public LinkedList<MessageItem> log = new LinkedList<>();
	
	public void add(int type, String msg) {
		log.add(new MessageItem(type, msg));
		if(log.size()>6)
			log.removeFirst();
	}
	
}
