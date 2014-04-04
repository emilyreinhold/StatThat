package com.example.statthat;

import android.content.Context;

import com.orm.SugarRecord;

public class StatType extends SugarRecord<StatType> {
	Sport sport;
	String name;
	
	public StatType(Context ctx) {
		super(ctx);
	}
	
	public StatType(Context ctx, Sport sport, String name) {
		super(ctx);
		this.sport = sport;
		this.name = name;
	}
}
