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
	
	// Add basketball stat types to db, if not there already
	public static void populateBballStatTypes(Context c) {
		Sport bball = Sport.populateBball(c);
		boolean isPopulated = StatType.listAll(StatType.class).size() > 0;
		if (isPopulated) {
			return;
		} else {
			for (String name : DBHelper.bballStatTypes) {
				StatType type = new StatType(c, bball, name);
				type.save();
			}
		}
		
		String[] pairs = DBHelper.mapping.split(",");
		for (int i=0; i < pairs.length; i++) {
		    String[] keyValue = pairs[i].split(":");
		    DBHelper.bballStatMap.put(keyValue[0], keyValue[1]);
		}
	}

}

