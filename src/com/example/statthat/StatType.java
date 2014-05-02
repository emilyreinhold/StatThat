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
	
	public static ArrayList<String> getBballBoolStats() {
		ArrayList<String> bools = new ArrayList<String>();
		bools.add("free throw");
		bools.add("two point");
		bools.add("three point");
		return bools;
	}
	
	public static ArrayList<String> getBballStatTypeIds() {
		ArrayList<String> ids = new ArrayList<String>();
		System.out.println(Sport.listAll(Sport.class).size());
		List<Sport> bList = Sport.find(Sport.class, "name = ?", "basketball");
		if (bList.size() < 1) {
			return null;
		}
		Sport b = bList.get(0);
		List<StatType> statTypes = StatType.find(StatType.class, "sport = ?", b.getId().toString());
		for (StatType s : statTypes) {
			ids.add(s.getId().toString());
		}
		return ids;
	}
	
	public static ArrayList<String> getBballStatTypeNames() {
		ArrayList<String> names = new ArrayList<String>();
		List<Sport> bList = Sport.find(Sport.class, "name = ?", "basketball");
		if (bList.size() < 1) {
			return null;
		}
		Sport b = bList.get(0);
		ArrayList<String> bools = getBballBoolStats();
		List<StatType> statsTypes = StatType.find(StatType.class, "sport = ?", b.getId().toString());
		for (StatType s : statsTypes) {
			if (bools.contains(s.name)) {
				names.add(s.name + " attempted");
				names.add(s.name + " made");
			} else {
				names.add(s.name);
			}
		}
		return names;
	}

}

