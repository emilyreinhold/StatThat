package com.example.statthat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;

public class Parser {
	
	public ArrayList<String> stats = new ArrayList<String>();
	
	public HashMap<String, Integer> ones = new HashMap<String, Integer>();
	public HashMap<String, Integer> teens = new HashMap<String, Integer>();
	public HashMap<String, Integer> tens = new HashMap<String, Integer>();
	String oneMapping = "one:1,two:2,three:3,four:4,five:5,six:6,seven:7,eight:8,nine:9";
	String teenMapping = "ten:10,eleven:11,twelve:12,thirteen:13,fourteen:14,fifteen:15,sixteen:16,seventeen:17,eighteen:18,nineteen:19";
	String tenMapping = "twenty:20,thirty:30,fourty:40,fifty:50,sixty:60,seventy:70,eighty:80,ninety:90";
	
	// Constants that specify the voice command for making/missing shots
	public static String[] SUCCESS_COMMANDS = {"made", "maid"};
	public static String[] FAILURE_COMMANDS = {"missed", "miss", "mist"};
	
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
		for (String s : DBHelper.bballStatTypes) {
			stats.add(s);
		}
	}
	
	// Save the stat and return its ID
	public String saveStat(int number, String action, boolean result, int period, double time, Flag flag, Team team) {
		Stat s;
		try {
			
			List<Player> players = Player.find(Player.class, "number = ? and team = ?", String.valueOf(number), team.getId().toString());//, game.team.getId().toString());
			if (players.size() < 1) {
				System.out.println("Unable to find player number: " + String.valueOf(number)+ ", on team: " + game.team.name);
				return null;
			}
			
			StatType type = StatType.find(StatType.class, "name = ?", action).get(0);
			s = new Stat(context, players.get(0), game, type, time, period, result, flag);
			s.save();
		} catch (Exception e) {
			System.out.println("Error: Unable to save stat.");
			return null;
		}
		return s.getId().toString();
	}
	
	// Find the last possible index that could contain the text for
	// a player number.
	// ex: findLimit([player, one, two, point, miss]) => 1
	// 	   findLimit([player, twenty, two, foul]) => 2
	public int findLimit(String[] words) {
		int pos = 1;
		int curr = 2;
		while (words.length > curr) {
			String action = words[curr] + words[curr + 1];
			if (stats.contains(action)) {
				pos = curr - 1;
				break;
			}
			if (stats.contains(words[curr])) {
				pos = curr - 1;
				break;
			}
			if (stats.contains(words[curr + 1])) {
				pos = curr;
				break;
			}
			curr++;
		}
		
		return pos;
	}
	
	// Turn word form of number into int
	// ex: stringToInt(["twenty", "three"]) => 23
	public int[] stringToInt(String[] words) throws NumberFormatException {
		int result = 0;
		int pos = 0;
		boolean oneUsed = false;
		
		if (words.length < 3) {
			int[] dummy = {0, 1};
			return dummy;
		}
		
		for (int i = 1; i < 3; i++) {
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
	
	public String[] replaceHomophones(String[] words) {
		String mapping = "ate:eight,hate:eight,weight:eight,won:one,too:two,to:two,for:four";
		HashMap<String, String> matches = new HashMap<String, String>();
		String[] pairs = mapping.split(",");
		String[] keyValue;
		for (int i=0; i < pairs.length; i++) {
		    keyValue = pairs[i].split(":");
		    matches.put(keyValue[0], keyValue[1]);
		}
		
		for (int i = 0; i < words.length; i++) {
			String word = words[i];
			if (matches.containsKey(word)) {
				words[i] = matches.get(word);
			}
		}
		return words;
	}
	
	public boolean matchCommand(String word, boolean isSuccess) {
		String[] cmds;
		if (isSuccess) {
			cmds = SUCCESS_COMMANDS;
		} else {
			cmds = FAILURE_COMMANDS;
		}
		for (String command : cmds) {
			if (word.equals(command)) {
				return true;
			}
		}
		return false;
	}
	
	// Parse the text and separate into pieces needed to save stat
	public String parseMatch(ArrayList<String> matches, double time, int period, Team team) {
		String stat_id = null;
		
		String output = "";
		
		for (String match: matches) {
			int currentPos = 1;
			int number;
			String action = "";
			boolean result = true;
			
			output += "Original: " + match + "\n";
			String[] sentence = match.split(" ");
			sentence = replaceHomophones(sentence);
			
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
			
			if (matchCommand(sentence[currentPos], true)) {
				result = true;
				currentPos++;
			} else if (matchCommand(sentence[currentPos], false)) {
				result = false;
				currentPos++;
			}
			
			// Find action
			int nextPos = currentPos + 1;
			while (action.equals("") && nextPos <= sentence.length) {
				String testAction = "";
				for (int i = currentPos; i < nextPos; i++) {
					if (i != currentPos && i != sentence.length) {
						testAction += " ";
					}
					testAction += sentence[i];
					
					if (stats.contains(testAction)) {
						action = testAction;
					}
				}
				
				nextPos++;
			}
			
			output += "Stat: " + action + ", Result: " + String.valueOf(result) + "\n";
			
			if (!action.equals("")) {
				output += "Made it into action\n";
				stat_id = saveStat(number, action, result, period, time, null, team);
				if(stat_id != null) {
					output += "Stat saved!";
					System.out.print(output);
					return stat_id; // Successful stat saving
				} else {
					output += "\n**Stat retrival failed**\n";
				}
			} else {
				Flag flag = new Flag(context, match);
				stat_id = saveStat(number, action, result, period, time, flag, team);
				System.out.print("Erroneous stat saved");
				return stat_id;
			}
			
		} // end of loop
		
		// should never reach this point. now stats are flagged
		// output = "Error occurred, try again."; // Failure message for now
		return output;
	}
}
