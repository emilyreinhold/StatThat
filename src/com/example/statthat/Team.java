package com.example.statthat;

import java.util.ArrayList;
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
	
	// Returns total counts of stats in order that matches the
	// order of IDs returned in StatType.getBballStatTypeIds()
	public ArrayList<Integer> getBballStatCountsForGame(Game game) {
		String gameId = game.getId().toString();
		ArrayList<Integer> counts = new ArrayList<Integer>();
		List<Player> players = getPlayers();
		ArrayList<String> typeIds = StatType.getBballStatTypeIds();
		ArrayList<String> boolStats = StatType.getBballBoolStats();
		for (String typeId : typeIds) {
			int sumTypeCount = 0;
			for (Player player : players) {
				sumTypeCount += player.getStatCountForTypeForGame(gameId, typeId);
			}
			counts.add(sumTypeCount);
			StatType s = StatType.findById(StatType.class, Long.parseLong(typeId));
			if (boolStats.contains(s.name)) {
				int madeCount = 0;
				for (Player player : players) {
					madeCount += player.getStatCountForTypeTrueForGame(gameId, typeId);
				};
				counts.add(madeCount);
			}
		}
		return counts;
	}
	
	public ArrayList<Integer> getBballStatCountsAllTime() {
		ArrayList<Integer> counts = new ArrayList<Integer>();
		List<Player> players = getPlayers();
		ArrayList<String> typeIds = StatType.getBballStatTypeIds();
		ArrayList<String> boolStats = StatType.getBballBoolStats();
		for (String typeId : typeIds) {
			int sumTypeCount = 0;
			for (Player player : players) {
				sumTypeCount += player.getStatCountForTypeAllTime(typeId);
			}
			counts.add(sumTypeCount);
			StatType s = StatType.findById(StatType.class, Long.parseLong(typeId));
			if (boolStats.contains(s.name)) {
				int madeCount = 0;
				for (Player player : players) {
					madeCount += player.getStatCountForTypeTrueAllTime(typeId);
				}
				counts.add(madeCount);
			}
		}
		return counts;
	}
}
