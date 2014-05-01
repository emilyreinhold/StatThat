package com.example.statthat;

import android.content.Context;

import com.orm.SugarRecord;

public class Flag extends SugarRecord<Flag> {
	boolean isResolved;
	String originalParsing;
	
	public Flag(Context ctx) {
		super(ctx);
	}
	
	public Flag(Context ctx, String originalParsing) {
		super(ctx);
		this.originalParsing = originalParsing;
		this.isResolved = false;
	}
}
