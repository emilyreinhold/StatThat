package com.example.statthat;

import java.util.List;

import android.content.Context;

import com.orm.SugarRecord;

public class Sport extends SugarRecord<Sport> {
	
	String name;
	
	public Sport(Context ctx) {
		super(ctx);
	}
	
	public Sport(Context ctx, String name) {
		super(ctx);
		this.name = name;
	}
	
	// Helper Methods
	
	public List<StatType> getStatTypes() {
		return StatType.find(StatType.class, "sport", getId().toString());
	}
	
	public List<Sport> getAll() {
		return Sport.listAll(Sport.class);
	}
}
