package com.example.statthat;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

// Helper Class to store database seed data

public class DBHelper {
	
	// Basketball data
	
	static String[] bballStatTypes = { "3 pt", "2 pt", "Free throw", "Offensive rebound", "Defensive rebound",
		   "Assist", "Steal", "Block", "Turnover", "Personal foul" };

	static String[] bballStatAbbr = { "3FGM", "3FG", "FGM", "FG",
		  "FTM", "FT", "OREB", "DREB",
		  "AST", "STL", "BLK", "TO", "PF" };
	
	public static void populateTestData(Context c) {
		StatType.populateBballStatTypes(c);
		
		Team team = new Team(c, "Golden Bears", Sport.populateBball(c));
		team.save();
		List<Player> players = populateTestPlayers(c, team);
		Game game = new Game(c, team, "Stanfurd", "Cal", "03-01-2014");
		game.save();
		populateStatsForGame(c, game, players);
	}
	
	// Use with CAUTION! Will delete entire database!!
	public static void deleteAllData() {
		Player.deleteAll(Player.class);
		Team.deleteAll(Team.class);
		Game.deleteAll(Game.class);
		Stat.deleteAll(Stat.class);
		Sport.deleteAll(Sport.class);
		StatType.deleteAll(StatType.class);
	}
	
	private static List<Player> populateTestPlayers(Context c, Team team) {
		List<Player> players = new ArrayList<Player>();
		List<Player> queryResult = Player.find(Player.class, "first_name = ? or first_name = ? or first_name = ?", "Tobias", "George Michael", "Maebe");
		
		if (queryResult.size() == 3) {
			return queryResult;
		} else {
			Player p = new Player(c, "Tobias", "Funke", team, 0, "guard", 8, 5, 160.0, "sophomore");
			p.save();
			players.add(p);
			
			Player p2 = new Player(c, "George Michael", "Bluth", team, 5, "forward", 1, 6, 180.0, "senior");
			p2.save();
			players.add(p2);
			
			Player p3 = new Player(c, "Tobias", "Funke", team, 0, "guard", 8, 5, 160.0, "sophomore");
			p3.save();
			players.add(p3);
			
			return players;
		}
	}
	
	private static void populateStatsForGame(Context c, Game game, List<Player> players) {
		StatType type1 = StatType.find(StatType.class, "name = ?", "3 pt").get(0);
		StatType type2 = StatType.find(StatType.class, "name = ?", "Free throw").get(0);
		StatType type3 = StatType.find(StatType.class, "name = ?", "Personal foul").get(0);
		
		new Stat(c, players.get(0), game, type1, 11.0, true).save();
		new Stat(c, players.get(0), game, type1, 13.0, true).save();
		new Stat(c, players.get(0), game, type1, 20.0, false).save();
		new Stat(c, players.get(1), game, type1, 2.0, true).save();
		new Stat(c, players.get(1), game, type2, 41.0, false).save();
		new Stat(c, players.get(2), game, type2, 21.0, false).save();
		new Stat(c, players.get(2), game, type1, 17.0, true).save();
		new Stat(c, players.get(2), game, type3, 50.0, true).save();
		
	}

}