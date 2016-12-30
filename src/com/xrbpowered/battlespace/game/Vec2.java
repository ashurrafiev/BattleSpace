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

public class Vec2 {

	public float x, y;
	
	public Vec2(float x, float y) {
		set(x, y);
	}

	public Vec2(Vec2 v) {
		set(v);
	}

	public Vec2 set(float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}
	
	public Vec2 set(Vec2 v) {
		this.x = v.x;
		this.y = v.y;
		return this;
	}
	
	public float length() {
		return (float) Math.sqrt(x*x+y*y);
	}
	
	public Vec2 scale(float k) {
		this.x *= k;
		this.y *= k;
		return this;
	}
	
	public Vec2 addScaled(Vec2 v, float k) {
		this.x += v.x*k;
		this.y += v.y*k;
		return this;
	}
	
	public Vec2 normalise() {
		return scale(1f/length());
	}
	
	private static Vec2 result(Vec2 dst, float x, float y) {
		if(dst==null)
			return new Vec2(x, y);
		else
			return dst.set(x, y);
	}
	
	public static Vec2 add(Vec2 dst, Vec2 a, Vec2 b) {
		return result(dst, a.x+b.x, a.y+b.y);
	}

	public static Vec2 subtract(Vec2 dst, Vec2 a, Vec2 b) {
		return result(dst, a.x-b.x, a.y-b.y);
	}
	
	public static float dot(Vec2 a, Vec2 b) {
		return a.x*b.x+a.y*b.y;
	}


}
