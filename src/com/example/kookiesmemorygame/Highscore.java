package com.example.kookiesmemorygame;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.Intent;

public class Highscore {
	 private SharedPreferences preferences;
	 private SharedPreferences.Editor editor;
	 private String names[];
	 private long score[];
	 private String dates[];
	 public static final String PREFS_NAME = "Highscore";
	// Context
	 Context _context;
	     
	 
    
	    // Shared pref mode
	    int PRIVATE_MODE = 0;


	    // Constructor
	 public Highscore(Context context)
	 {
		 preferences = context.getSharedPreferences(PREFS_NAME, 0);
		 names = new String[10];
		 dates = new String[10];
		 score = new long[10];

		 for (int x=0; x<10; x++)
		 {
			names[x] = preferences.getString("name"+x, "...");
			dates[x] = preferences.getString("dates"+x, "...");
			score[x] = preferences.getLong("score"+x, 0);
		 }

	 }

	 public String getName(int x)
	 {
		 //get the name of the x-th position in the Highscore-List
		 return names[x];
	 }

	 public String getDt(int x)
	 {
		 //get the dates of the x-th position in the Highscore-List
		 return dates[x];
	 }
	 
	 public long getScore(int x)
	 {
		 //get the score of the x-th position in the Highscore-List
		 return score[x];
	 }
	 
	 public boolean inHighscore(long score)
	 {
		 //test, if the score is in the Highscore-List
		 int position;
		 for (position=0;  position<10&&this.score[position]>score;  position++);

		 if (position==10) return false;
		 return true;
	 }

	 public boolean addScore(String name, String date, long score)
	 {
		//add the score with the name and date to the Highscore-List
		
		int position;
		for (position=0; position<10&&this.score[position]>score; position++);

		if (position==10) return false;

		for (int x=9; x>position; x--)
		{
			names[x]=names[x-1];
			dates[x]=dates[x-1];
			this.score[x]=this.score[x-1];
		}

		this.names[position] = new String(name);	
		this.dates[position] = new String(date); //new Date().toString();	
		this.score[position] = score;
	
//SharedPreferences.Editor editor = preferences.edit();
		 editor = preferences.edit();
		for (int x=0; x<10; x++)
		{
			editor.putString("name"+x, this.names[x]);
			editor.putString("dates"+x, this.dates[x]);
			editor.putLong("score"+x, this.score[x]);
			
		}

		editor.commit();
		return true;

	 }
	


	 public void clearHS(Context context) {
		 //preferences = context.getSharedPreferences(PREFS_NAME, 0);
		 names = new String[10];
		 dates = new String[10];
		 score = new long[10];
		 
		 editor = preferences.edit();
		 for (int x=0; x<10; x++)
		{
			 editor.remove("name"+x);
			 editor.remove("dates"+x);
			 editor.remove("score"+x);
			 editor.commit();
		}
		 

		 for (int x=0; x<10; x++)
		 {
			names[x] = preferences.getString("name"+x, "...");
			dates[x] = preferences.getString("dates"+x, "...");
			score[x] = preferences.getLong("score"+x, 0);
		 }
		 //editor.commit();
		 //*/
	
	
	 }


	
	 
	
}