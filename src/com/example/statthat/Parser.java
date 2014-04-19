package com.example.statthat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Parser {
	
	public String[] shotStats = { "two point", "three point", "free throw" };
	public String[] otherStats = { "offensive rebound", "defensive rebound", "assist", "steal", "block", "turnover", "foul" };
	
	public HashMap<String, Integer> ones = new HashMap<String, Integer>();
	public HashMap<String, Integer> teens = new HashMap<String, Integer>();
	public HashMap<String, Integer> tens = new HashMap<String, Integer>();
	String oneMapping = "one:1,two:2,three:3,four:4,five:5,six:6,seven:7,eight:8,nine:9";
	String teenMapping = "ten:10,eleven:11,twelve:12,thirteen:13,fourteen:14,fifteen:15,sixteen:16,seventeen:17,eighteen:18,ninteen:19";
	String tenMapping = "twenty:20,thirty:30,fourty:40,fifty:50,sixty:60,seventy:70,eighty:80,ninety:90";
	
	public Parser() {
		// Populate Number to Int mappings
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
	}
	
	public int saveStat(ArrayList<String> matches) {
		return 0;
	}
	
	public int stringToInt(String[] words) throws NumberFormatException {
		int result = 0;
		for (int i = 1; i < 3; i++) {
			String word = words[i];
			if (ones.containsKey(word)) {
				result += ones.get(word);
			} else if (teens.containsKey(word)) {
				result += teens.get(word);
			} else if (tens.containsKey(word)) {
				result += tens.get(word);
			}
		}
		return result;
	}
	
	public String parseMatch(ArrayList<String> matches) {
		String output = "";
		
		for (String match: matches) {
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
			int number;
			try {
				// Speech recognizer returned Ints in string
				number = Integer.parseInt(sentence[1]);
			} catch (NumberFormatException e) {
				
				try {
					// Speech recognizer returned the word versions of numbers
					number = stringToInt(sentence);
				} catch (NumberFormatException ex) {
					// Something went completely wrong
					number = 0;
					System.out.println("Unable to parse player number:" + sentence[1]);
				}
			}

			// Find action
		
			// If shot, find success bool
		}
		
	}
}
