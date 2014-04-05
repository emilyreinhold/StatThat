package com.example.statthat;

import java.util.List;

import android.content.Context;

import com.orm.SugarRecord;

public class Team extends SugarRecord<Team> {
	String name;
	Sport sport;
	
	public Team(Context ctx) {
		super(ctx);
	}
	
	public Team(Context ctx, String name, Sport sport) {
		super(ctx);
		this.name = name;
		this.sport = sport;
	}
	
	// Helper methods
	
	public List<Player> getPlayers() {
		return Player.find(Player.class, "team = ?", getId().toString());
	}
	
	public List<Game> getGames() {
		return Game.find(Game.class, "team = ?", getId().toString());
	}
	
	public List<Team> getAll() {
		return Team.listAll(Team.class);
	}
}
