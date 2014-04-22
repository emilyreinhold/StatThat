package com.example.statthat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;

// Helper Class to store database seed data

public class DBHelper {
	
	// Basketball data
	
	static String[] bballStatTypes = { "three point", "two pointt", "free throw", "offensive rebound", "defensive rebound",
		   "assist", "steal", "block", "turnover", "personal foul" };

	static String[] bballStatAbbr = { "3FGM", "3FG", "FGM", "FG",
		  "FTM", "FT", "OREB", "DREB",
		  "AST", "STL", "BLK", "TO", "PF" };
	
	static String[] parseTestText = {"playeR three three point missed", "player twenty two offensive rebound", "player five foul", "player 6 steal", "player 33 free throw made"};
	
	static HashMap<String, String> bballStatMap = new HashMap<String, String>();
	static String mapping = "two point:2 pt,three point:3 pt, free throw:Free throw,offensive rebound:Offensive rebound,defensive rebound:Defensive rebound,assist:Assist,steal:Steal,block:Block,turnover:Turnover,foul:Personal foul";
	
	public static void populateTestData(Context c) {
		StatType.populateBballStatTypes(c);
		
		if (Team.find(Team.class, "name = ?", "Golden Bears").size() > 0) {
			return;
		}
		
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
			
			Player default_player = new Player(c, "Default", "Player", team, 9, "guard", 8, 5, 160.0, "Freshman");
			default_player.save();
			players.add(default_player);
			
			Player p = new Player(c, "Tobias", "Funke", team, 0, "guard", 8, 5, 160.0, "sophomore");
			p.save();
			players.add(p);
			
			Player p2 = new Player(c, "George Michael", "Bluth", team, 5, "forward", 1, 6, 180.0, "senior");
			p2.save();
			players.add(p2);
			
			Player p3 = new Player(c, "Tom", "Funke", team, 1, "guard", 8, 5, 160.0, "sophomore");
			p3.save();
			players.add(p3);
			
			return players;
		}
	}
	
	private static void populateStatsForGame(Context c, Game game, List<Player> players) {
		StatType type1 = StatType.find(StatType.class, "name = ?", "3 pt").get(0);
		StatType type2 = StatType.find(StatType.class, "name = ?", "free throw").get(0);
		StatType type3 = StatType.find(StatType.class, "name = ?", "personal foul").get(0);
		
		new Stat(c, players.get(0), game, type1, 11.0, 1, true).save();
		new Stat(c, players.get(0), game, type1, 13.0, 1, true).save();
		new Stat(c, players.get(0), game, type1, 20.0, 2, false).save();
		new Stat(c, players.get(1), game, type1, 2.0, 1, true).save();
		new Stat(c, players.get(1), game, type2, 41.0, 3, false).save();
		new Stat(c, players.get(2), game, type2, 21.0, 2, false).save();
		new Stat(c, players.get(2), game, type1, 17.0, 1, true).save();
		new Stat(c, players.get(2), game, type3, 50.0, 4, true).save();
		
	}

}
