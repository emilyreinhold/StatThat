package com.example.statthat;

import java.util.List;

import android.content.Context;

import com.orm.SugarRecord;

public class Sport extends SugarRecord<Sport> {
	
	String name;
	String periodName;
	
	public Sport(Context ctx) {
		super(ctx);
	}
	
	public Sport(Context ctx, String name, String periodName) {
		super(ctx);
		this.name = name;
		this.periodName = periodName;
	}
	
	// Helper Methods
	
	public List<StatType> getStatTypes() {
		return StatType.find(StatType.class, "sport", getId().toString());
	}
	
	public List<Sport> getAll() {
		return Sport.listAll(Sport.class);
	}
	
	
	// Add basketball to db, if it doesn't exist
	public static Sport populateBball(Context c) {
		List<Sport> bball = Sport.find(Sport.class, "name = ?", "basketball");
		if (bball.size() == 1) {
			return bball.get(0);
		} else {
			Sport s = new Sport(c, "basketball", "quarter");
			s.save();
			return s;
		}
	}
}
