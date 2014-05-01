package com.example.statthat;

import java.util.ArrayList;
import java.util.List;

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
	}
	
	public static ArrayList<String> getBballStatTypeIds() {
		ArrayList<String> ids = new ArrayList<String>();
		Sport b = Sport.find(Sport.class, "name = ?", "basketball").get(0);
		List<Stat> stats = Stat.find(Stat.class, "sport = ?", b.getId().toString());
		for (Stat s : stats) {
			ids.add(s.getId().toString());
		}
		return ids;
	}

}

