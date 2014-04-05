package com.example.statthat;

// Helper Class to store database seed data

public class DBHelper {
	
	// Basketball data
	
	static String[] bballStatTypes = { "3 pt made", "3 pt missed", "2 pt made", "2 pt missed", 
		   "Free throw made", "Free throw missed", "Offensive rebound", "Defensive rebound",
		   "Assist", "Steal", "Block", "Turnover", "Personal foul" };

	static String[] bballStatAbbr = { "3FGM", "3FG", "FGM", "FG",
		  "FTM", "FT", "OREB", "DREB",
		  "AST", "STL", "BLK", "TO", "PF" };
}
