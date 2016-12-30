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
