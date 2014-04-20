package com.example.statthat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;

public class Parser {
	
	public String[] shotStatsArray = { "two point", "three point", "free throw" };
	public String[] otherStatsArray = { "offensive rebound", "defensive rebound", "assist", "steal", "block", "turnover", "foul" };
	public ArrayList<String> shotStats = new ArrayList<String>();
	public ArrayList<String> otherStats = new ArrayList<String>();
	
	public HashMap<String, Integer> ones = new HashMap<String, Integer>();
	public HashMap<String, Integer> teens = new HashMap<String, Integer>();
	public HashMap<String, Integer> tens = new HashMap<String, Integer>();
	String oneMapping = "one:1,two:2,three:3,four:4,five:5,six:6,seven:7,eight:8,nine:9";
	String teenMapping = "ten:10,eleven:11,twelve:12,thirteen:13,fourteen:14,fifteen:15,sixteen:16,seventeen:17,eighteen:18,nineteen:19";
	String tenMapping = "twenty:20,thirty:30,fourty:40,fifty:50,sixty:60,seventy:70,eighty:80,ninety:90";
	
	// Constants that specify the voice command for making/missing shots
	public static String SUCCESS_COMMAND = "made";
	public static String FAILURE_COMMAND = "missed";
	
	Game game;
	Context context;
	
	// Parser is constructed for each game
	public Parser(Game game, Context context) {
		// Populate Number to Int mappings
		this.game = game;
		this.context = context;
		
		String[] pairs = oneMapping.split(",");
		String[] keyValue;
		for (int i=0; i < pairs.length; i++) {
		    keyValue = pairs[i].split(":");
		    ones.put(keyValue[0], Integer.parseInt(keyValue[1]));
		}
		pairs = teenMapping.split(",");
		for (int i=0; i < pairs.length; i++) {
		    keyValue = pairs[i].split(":");
		    teens.put(keyValue[0], Integer.parseInt(keyValue[1]));
		}
		pairs = tenMapping.split(",");
		for (int i=0; i < pairs.length; i++) {
		    keyValue = pairs[i].split(":");
		    tens.put(keyValue[0], Integer.parseInt(keyValue[1]));
		}
		// Put stats into arraylists
		for (String s : shotStatsArray) {
			shotStats.add(s);
		}
		for (String s : otherStatsArray) {
			otherStats.add(s);
		}
	}
	
	// Save the stat and return its ID
	public String saveStat(int number, String action, boolean result, int period, double time) {
		Stat s;
		try {
		List<Player> players = Player.find(Player.class, "number = ? AND team = ?", String.valueOf(number), game.team.getId().toString());
		if (players.size() < 1) {
			System.out.println("Unable to find player number: " + String.valueOf(number)+ ", on team: " + game.team.name);
		}
		// String actionName = DBHelper.bballStatMap.get(action);
		StatType type = StatType.find(StatType.class, "name = ?", action).get(0);
		s = new Stat(context, players.get(0), game, type, time, period, result);
		s.save();
		} catch (Exception e) {
			System.out.println("Unable to save stat.");
			return null;
		}
		return s.getId().toString();
	}
	
	// Turn word form of number into int
	// ex: stringToInt(["twenty", "three"]) => 23
	// Note: currently broken for things like player three two point made
	public int[] stringToInt(String[] words) throws NumberFormatException {
		int result = 0;
		int pos = 0;
		int limit;
		boolean oneUsed = false;
		
		if (words.length < 3) {
			int[] dummy = {0, 1};
			return dummy;
		}
		
		limit = 3; // TODO: change to func call to remove action from array
		
		for (int i = 1; i < limit; i++) {
			String word = words[i];
			if (ones.containsKey(word) && !oneUsed) {
				result += ones.get(word);
				pos += 1;
				oneUsed = true;
			} else if (teens.containsKey(word)) {
				result += teens.get(word);
				pos += 1;
			} else if (tens.containsKey(word)) {
				result += tens.get(word);
				pos += 1;
			}
		}
		int[] resultArray = {result, pos + 1};
		return resultArray;
	}
	
	// Parse the text and separate into pieces needed to save stat
	public String parseMatch(ArrayList<String> matches, double time, int period) {
		String stat_id = null;
		
		String output = "";
		
		for (String match: matches) {
			int currentPos = 1;
			int number;
			String action = "";
			boolean result = true;
			
			output += "Original: " + match + "\n";
			String[] sentence = match.split(" ");
			
			if (sentence.length < 3) {
				continue; // not enough words, try next match
			}
			
			for (int i = 0; i < sentence.length; i++) {
				sentence[i] = sentence[i].toLowerCase();
			}
			
			if (!sentence[0].equals("player")) {
				continue; // command didn't begin with player, try next match
			}
			
			// Find player number
			try {
				// Speech recognizer returned Ints in string
				number = Integer.parseInt(sentence[1]);
				currentPos += 1;
			} catch (NumberFormatException e) {
				
				try {
					// Speech recognizer returned the word versions of numbers
					int[] nums = stringToInt(sentence);
					number = nums[0];
					currentPos = nums[1];
				} catch (NumberFormatException ex) {
					// Something went completely wrong
					number = 0;
					currentPos += 1; // ? dont know what to do here
					System.out.println("Unable to parse player number:" + sentence[1]);
				}
			}
			
			output += "Player: " + String.valueOf(number) + ", ";

			// Find action
			if (sentence.length == 5 || sentence.length == 6) {
				// Look for shot stats
				String testAction = sentence[3] + " " + sentence[4];//sentence[currentPos] + " " + sentence[currentPos + 1];
				if (shotStats.contains(testAction)) {
					action = testAction;
					if(sentence[2].equals(SUCCESS_COMMAND)) { //(sentence[currentPos + 2].equals(SUCCESS_COMMAND)) {
						result = true;
					} else if (sentence[2].equals(FAILURE_COMMAND)) { //(sentence[currentPos + 2].equals(FAILURE_COMMAND)) {
						result = false;
					} else {
						System.out.println("Unable to parse made/missed shot");
					}
				} else if (otherStats.contains(testAction)) {
					// Other stats that have 2 words
					action = testAction;
				}
			} else {
				if (otherStats.contains(sentence[currentPos])) {
					action = sentence[currentPos];
				}
			}
			
			output += "Stat: " + action + ", Result: " + String.valueOf(result) + "\n";
			
			if (!action.equals("")) {
				output += "Made it into action\n";
				stat_id = saveStat(number, action, result, period, time);
				if(stat_id != null)
					return stat_id;
				else
					output += "\n**Stat retrival failed**\n";
			}
			
		} // end of loop
		
		return output;
	}
}
