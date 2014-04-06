package com.example.statthat;

import android.content.Context;

import com.orm.SugarRecord;

public class Stat extends SugarRecord<Stat> {
	Player player;
	Game game;
	StatType statType;
	double time;
	boolean result;
	int period_num;
	
	public Stat(Context ctx) {
		super(ctx);
	}
	
	public Stat(Context ctx, Player player, Game game, StatType statType, double time, int period_num, boolean result) {
		super(ctx);
		this.player = player;
		this.game = game;
		this.statType = statType;
		this.time = time;
		this.period_num = period_num;
		this.result = result;
	}
}
