package com.example.statthat;

import android.content.Context;

import com.orm.SugarRecord;

public class Stat extends SugarRecord<Stat> {
	Player player;
	Game game;
	StatType statType;
	double time;
	boolean result;
	
	public Stat(Context ctx) {
		super(ctx);
	}
	
	public Stat(Context ctx, Player player, Game game, StatType statType, double time, boolean result) {
		super(ctx);
		this.player = player;
		this.game = game;
		this.statType = statType;
		this.time = time;
		this.result = result;
	}
}
