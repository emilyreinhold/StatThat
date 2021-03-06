package com.example.statthat;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

// Helper Class to store database seed data

public class DBHelper {
	
	// Basketball data
	
	static String[] bballStatTypes = { "three point", "two point", "free throw", "offensive rebound", "defensive rebound",
		   "assist", "steal", "block", "turnover", "personal foul" };

	static String[] bballStatAbbr = { "3FGM", "3FG", "FGM", "FG",
		  "FTM", "FT", "OREB", "DREB",
		  "AST", "STL", "BLK", "TO", "PF" };
	
	static String[] parseTestText = {"playeR three three point missed", "player twenty two offensive rebound", "player five foul", "player 6 steal", "player 33 free throw made"};
	
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
	
	public static Stat getAStat() {
		return Stat.listAll(Stat.class).get(0);
	}
	
	private static List<Player> populateTestPlayers(Context c, Team team) {
		List<Player> players = new ArrayList<Player>();
		List<Player> queryResult = Player.find(Player.class, "first_name = ? or first_name = ? or first_name = ?", "Tobias", "George Michael", "Maebe");
		
		if (queryResult.size() == 3) {
			return queryResult;
		} else {
			
			Player default_player = new Player(c, "Arnold", "Palmer", team, 9, "guard", 8, 5, 160.0, "Freshman");
			default_player.save();
			players.add(default_player);
			
			Player p = new Player(c, "Tobias", "Funke", team, 7, "guard", 8, 5, 160.0, "sophomore");
			p.save();
			players.add(p);
			Player p2 = new Player(c, "George Michael", "Bluth", team, 5, "forward", 1, 6, 180.0, "senior");
			p2.save();
			players.add(p2);
			Player p3 = new Player(c, "Tom", "Funke", team, 1, "guard", 8, 5, 160.0, "sophomore");
			p3.save();
			players.add(p3);
			Player p4 = new Player(c, "Annyong", "Bluth", team, 25, "guard", 6, 4, 124.0, "freshman");
			p4.save();
			players.add(p4);
			Player p5 = new Player(c, "Buster", "Bluth", team, 47, "guard", 5, 5, 100.0, "senior");
			p5.save();
			players.add(p5);
			Player p6 = new Player(c, "Steve", "Holt", team, 6, "guard", 9, 5, 160.0, "sophomore");
			p6.save();
			players.add(p6);
			Player p7 = new Player(c, "Onyango", "Funke", team, 2, "guard", 5, 10, 105.0, "sophomore");
			p7.save();
			players.add(p7);

			
			return players;
		}
	}
	
	private static void populateStatsForGame(Context c2, Game game2, List<Player> players2) {
	
		final String gameId = game2.getId().toString();
		final Context c = c2;

		new Thread(new Runnable() {
			public void run() {
				Game game = Game.findById(Game.class, Long.parseLong(gameId));
				List<Player> players = game.team.getPlayers();
				StatType type1 = StatType.find(StatType.class, "name = ?", "three point").get(0);
				StatType type2 = StatType.find(StatType.class, "name = ?", "free throw").get(0);
				StatType type3 = StatType.find(StatType.class, "name = ?", "personal foul").get(0);
				StatType type4 = StatType.find(StatType.class, "name = ?", "two point").get(0);
				StatType type5 = StatType.find(StatType.class, "name = ?", "turnover").get(0);
				StatType type6 = StatType.find(StatType.class, "name = ?", "assist").get(0);
				
				new Stat(c, players.get(0), game, type1, 11.0, 1, true, null).save();
				new Stat(c, players.get(0), game, type1, 13.0, 1, true, null).save();
				new Stat(c, players.get(0), game, type1, 20.0, 2, false, null).save();
				new Stat(c, players.get(1), game, type1, 2.0, 1, true, null).save();
				new Stat(c, players.get(1), game, type2, 41.0, 3, false, null).save();
				new Stat(c, players.get(2), game, type2, 21.0, 2, false, null).save();
				new Stat(c, players.get(2), game, type1, 17.0, 1, true, null).save();
				new Stat(c, players.get(2), game, type3, 50.0, 4, true, null).save();
				new Stat(c, players.get(3), game, type1, 13.0, 1, true, null).save();
				new Stat(c, players.get(3), game, type1, 20.0, 2, false, null).save();
				new Stat(c, players.get(4), game, type1, 2.0, 1, true, null).save();
				new Stat(c, players.get(4), game, type2, 41.0, 3, false, null).save();
				new Stat(c, players.get(4), game, type2, 21.0, 2, false, null).save();
				new Stat(c, players.get(5), game, type1, 17.0, 1, true, null).save();
				new Stat(c, players.get(5), game, type3, 50.0, 4, true, null).save();
				new Stat(c, players.get(5), game, type1, 13.0, 1, true, null).save();
				new Stat(c, players.get(6), game, type1, 20.0, 2, false, null).save();
				new Stat(c, players.get(6), game, type1, 2.0, 1, true, null).save();
				new Stat(c, players.get(6), game, type2, 41.0, 3, false, null).save();
				new Stat(c, players.get(6), game, type2, 21.0, 2, false, null).save();
				new Stat(c, players.get(5), game, type1, 17.0, 1, true, null).save();
				new Stat(c, players.get(4), game, type4, 50.0, 4, true, null).save();
				new Stat(c, players.get(0), game, type4, 11.0, 1, true, null).save();
				new Stat(c, players.get(0), game, type4, 13.0, 1, true, null).save();
				new Stat(c, players.get(0), game, type5, 20.0, 2, false, null).save();
				new Stat(c, players.get(1), game, type5, 2.0, 1, true, null).save();
				new Stat(c, players.get(1), game, type4, 41.0, 3, false, null).save();
				
				new Stat(c, players.get(2), game, type5, 21.0, 2, false, null).save();
				new Stat(c, players.get(2), game, type6, 17.0, 1, true, null).save();
				new Stat(c, players.get(2), game, type5, 50.0, 4, true, null).save();
				new Stat(c, players.get(3), game, type4, 13.0, 1, true, null).save();
				new Stat(c, players.get(3), game, type4, 20.0, 2, false, null).save();
				new Stat(c, players.get(2), game, type4, 2.0, 1, true, null).save();
				new Stat(c, players.get(3), game, type4, 41.0, 3, false, null).save();
				new Stat(c, players.get(1), game, type4, 21.0, 2, false, null).save();
				new Stat(c, players.get(0), game, type5, 17.0, 1, true, null).save();
				new Stat(c, players.get(0), game, type5, 50.0, 4, true, null).save();
				new Stat(c, players.get(1), game, type6, 13.0, 1, true, null).save();
				new Stat(c, players.get(0), game, type6, 20.0, 2, false, null).save();
				new Stat(c, players.get(5), game, type6, 2.0, 1, true, null).save();
				new Stat(c, players.get(5), game, type4, 41.0, 3, false, null).save();
				new Stat(c, players.get(5), game, type5, 21.0, 2, false, null).save();
				new Stat(c, players.get(4), game, type4, 17.0, 1, true, null).save();
				new Stat(c, players.get(4), game, type4, 50.0, 4, true, null).save();
				
			}
		}).start();
	}

}
