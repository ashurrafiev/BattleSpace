package com.xrbpowered.battlespace.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.xrbpowered.battlespace.game.Game;
import com.xrbpowered.battlespace.game.Player;
import com.xrbpowered.battlespace.game.Weapon;
import com.xrbpowered.battlespace.ui.DamageText;
import com.xrbpowered.net.Connection;
import com.xrbpowered.net.NetBase;
import com.xrbpowered.net.NetMessage;

public class MsgPlayerStatus extends NetMessage {
	public static final byte CMD = 12;
	
	public static final int HEALTH = 0;
	public static final int SCORE = 1;
	public static final int RESPAWN_TIMER = 2;
	public static final int EFFECT_TIMER = 3;
	public static final int SECONDARY_WEAPON = 4;
	public static final int AMMO = 5;
	
	public final int index;
	public final int var;
	public final int value;
	
	public MsgPlayerStatus(Player player, int var, int value) {
		super(CMD);
		this.index = player.index;
		this.var = var;
		this.value = value;
	}
	
	public MsgPlayerStatus(DataInputStream in) throws IOException {
		super(CMD);
		this.index = in.readByte();
		this.var = in.readByte();
		this.value = in.readInt();
	}
	
	@Override
	protected void pack(DataOutputStream out) throws IOException {
		out.writeByte(index);
		out.writeByte(var);
		out.writeInt(value);
	}
	
	@Override
	public boolean process(NetBase net, Connection c) {
		if(!net.isServer()) {
			Game game = ((BSNetClient) net).game;
			Player player = game.players[index];
			if(player==null)
				return false;
			switch(var) {
				case HEALTH:
					DamageText.create(game, player, value-player.health);
					player.health = value;
					return true;
				case SCORE:
					player.score = value;
					return true;
				case RESPAWN_TIMER:
					player.getTimer(Player.RESPAWN_TIMER).start(value);
					return true;
				case EFFECT_TIMER:
					player.getTimer(Player.EFFECT_TIMER).start(value);
					return true;
				case SECONDARY_WEAPON:
					player.setSecondaryWeapon(value<0 ? null : Weapon.byId[value]);
					return true;
				case AMMO:
					player.ammo = value;
					return true;
				default:
					return false;
			}
		}
		else
			return false;
	}

}
