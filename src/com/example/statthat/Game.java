package com.example.statthat;

import java.util.List;

import android.content.Context;

import com.orm.SugarRecord;

public class Game extends SugarRecord<Game> {
	Team team;
	String opposingTeamName;
	String location;
	String date;
	// For future reference:
	// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	// String date = sdf.format(new Date());
	
	public Game(Context ctx) {
		super(ctx);
	}
	
	public Game(Context ctx, Team team, String opposingTeamName, String location, String date) {
		super(ctx);
		this.team = team;
		this.opposingTeamName = opposingTeamName;
		this.location = location;
		this.date = date;
	}
	
	// Helper Methods
	
	public List<Stat> getStats() {
		return Stat.find(Stat.class, "game = ?", getId().toString());
	}
	
	public List<Stat> getStatsForPlayer(Player player) {
		return Stat.find(Stat.class, "game = ? and player = ?", getId().toString(), player.getId().toString());
	}
	
	public List<Stat> getStatsForStatType(StatType statType) {
		return Stat.find(Stat.class, "game = ? and stat_type = ?", getId().toString(), statType.getId().toString());
	}
	
	public Integer getCountForStatType(StatType statType) {
		return getStatsForStatType(statType).size();
	}
}
