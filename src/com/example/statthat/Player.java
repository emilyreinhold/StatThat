package com.example.statthat;

import java.util.List;

import android.content.Context;

import com.orm.SugarRecord;

/* 
 * Example usage of database objects:
 * New Entities:
 * Context context = getApplicationContext();
 * Player player = new Player(context, "Bob", "Lawblah",...);
 * player.save()
 * 
 * Find Entity:
 * Player.findById(Player.class, 1);
 * 
 * Update Entity:
 * player.firstName = "Bawb";
 * player.save();
 * 
 * Delete Entity:
 * player.delete();
 * 
 */

public class Player extends SugarRecord<Player> {
	String firstName;
	String lastName;
	Team team;
	int number;
	String position;
	int heightInches;
	int heightFeet;
	double weight;
	String year;
	
	public Player(Context ctx) {
		super(ctx);
	}
	
	public Player(Context ctx, String firstName, String lastName, Team team, int number,
					String position, int heightInches, int heightFeet, double weight, String year) {
		super(ctx);
		this.firstName = firstName;
		this.lastName = lastName;
		this.team = team;
		this.number = number;
		this.position = position;
		this.heightInches = heightInches;
		this.heightFeet = heightFeet;
		this.weight = weight;
		this.year = year;
	}
	
	// Helper Methods
	
	public List<Stat> getStats() {
		return Stat.find(Stat.class, "player = ?", getId().toString());
	}
	
	public List<Stat> getStatsForGame(Game game) {
		return Stat.find(Stat.class, "player = ? and game = ?", getId().toString(), game.getId().toString());
	}
	
	public List<Stat> getStatsForStatType(StatType statType) {
		return Stat.find(Stat.class, "player = ? and stat_type = ?", getId().toString(), statType.getId().toString());
	}
	
	public Integer getCountForStatType(StatType statType) {
		return getStatsForStatType(statType).size();
	}
	
}
