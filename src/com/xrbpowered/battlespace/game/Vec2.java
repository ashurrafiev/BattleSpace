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
