package main;

import processing.core.*;
import processing.data.*;
import scenes.Scene;
import scenes.StartMenu;
import scenes.BossLevel1;
import scenes.DemoGameScene;
import scenes.MapScene;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class Main extends PApplet {

	/**
	 * Create a tree structure to handle unlocking of levels and skills
	 * Track user levels and experience points Create
	 * 4 different levels, and a map to move back and forth between. 3 normal
	 * enemies and 1 boss Add animations for enemies and attacks Adjust animations
	 * for clearing runes + add combos? Turn runes into sprites Add backgrounds +
	 * spruce up user interface replace buttons with sprites?
	 * add additional stats to Player 
	 * Improve customization options 
	 * add a "view character" screen so that players can see their stats? 
	 * add items - shops on map? 
	 **/

	// 1. Global Variables

	public static Scene s; // Keeps track of the current screen
	public static float screenX = 375; // Tracks the width of the screen
	public static float screenY = 667; // Tracks the height of the screen
	public static boolean debug = false; // Flag used to view debug features such as mouse position
	public static int targetFPS = 60; // The desired frame-per-second rate
	long oldT; // This variable, along with newT, will be used to keep track of the amount of
				// time between frames
	long newT;
	long diff; // This will hold the difference from oldT to newT
	int frameLength; // This variable holds the desired amount of time between frames rendered
	public static Player p; // Holds information about the current player. For now, simply create a blank
							// player. In the future, player information will be loaded from a save file

	// 3. This function will be called once to set up the scene
	public void setup() {
		/**
		 * a. Set the size. This size was chosen because it is the same aspect ratio as
		 * an Iphone screen, but at a slightly lower resolution. The box appears on my
		 * screen as about the same size as an actual Iphone screen, which will allow me
		 * to test if the game works well on that sized screen. TODO: Implement function
		 * to adjust display based on size and resolution of the screen, while
		 * maintaining aspect ratio
		 **/

		// b. Initialize Global Variables
		// i. The first scene should always be the start menu
		//s = new StartMenu(this);
		s = new StartMenu(this);
		p = new Player(this);
		// These variables will be used to track time and keep the game running at the
		// target frames per second
		oldT = millis();
		newT = oldT;
		frameLength = 1000 / targetFPS;
	}

	public void draw() // This function draws everything to the screen. For the most part, will simply
						// draw the current scene. Has potential for some debugging information
	{
		newT = millis();
		diff = newT - oldT; // Only draws the scene if enough time has passed
		if (diff > frameLength) {
			s.drawScene(diff);
			if (debug) // If the debug flag is set, draws additional information to the screen
			{
				textAlign(LEFT);
				textSize(screenY * 0.03f);
				text("MouseX = " + mouseX, 0, screenY * 0.05f);
				text("MouseY = " + mouseY, 0, screenY * 0.1f);
			}
		}
	}

	public void savePlayer() // This function is used to save the player to a file
	{
		if (Main.p != null) {
			File listFile = new File("Data/Characters.txt");
			File characterFile = new File("Data/" + p.name + ".json");
			JSONObject savePlayer = new JSONObject();
			savePlayer.setString("name", p.name);
			savePlayer.setFloat("maxHealth", p.maxHealth);
			savePlayer.setFloat("health", p.health);
			savePlayer.setFloat("maxEnergy", p.maxEnergy);
			savePlayer.setFloat("energy", p.energy);
			savePlayer.setFloat("x", p.x);
			savePlayer.setFloat("y", p.y);
			String characterClass  = null; 
			if(p.characterClass == CharacterClass.WIZARD)
			{
				characterClass = "Wizard";
			}
			else if(p.characterClass == CharacterClass.KNIGHT)
			{
				characterClass = "Knight";
			}
			savePlayer.setString("characterClass", characterClass);
			JSONArray unlockedRunes = new JSONArray();
			int i = 0;
			for (String r : p.unlockedRunes) {
				unlockedRunes.setString(i, r);
				i++;
			}
			savePlayer.setJSONArray("unlockedRunes", unlockedRunes);
			savePlayer.setInt("currentStage", p.currentStage);
			JSONArray unlockedLevels = new JSONArray(); 
			unlockedLevels.setBoolean(0, p.unlockedStages.get(1));
			unlockedLevels.setBoolean(1, p.unlockedStages.get(2));
			unlockedLevels.setBoolean(2, p.unlockedStages.get(3));
			savePlayer.setJSONArray("unlockedStages", unlockedLevels);
			
			if (!listFile.isFile()) // if listFile doesn't exist create it, add the character's name, and create a
									// file for the character
			{
				//println("list file doesn't exist");
				PrintWriter writeList = createWriter(listFile);
				writeList.println(p.name);
				writeList.flush();
				writeList.close();
				PrintWriter writeCharacter = createWriter(characterFile);
				// saveJSONObject(savePlayer, "Data/" + p.name + ".json");
				writeCharacter.print(savePlayer);
				writeCharacter.flush();
				writeCharacter.close();
			} else {
				//println(listFile.getAbsolutePath());
				String[] fileStrings = loadStrings(listFile);
				ArrayList<String> fileList = new ArrayList<String>(Arrays.asList(fileStrings)); 
				if(!fileList.contains(p.name))
				{
					fileList.add(p.name);
				}
				fileStrings = fileList.toArray(fileStrings);
				saveStrings(listFile, fileStrings);
				PrintWriter writeCharacter = createWriter(characterFile);
				// saveJSONObject(savePlayer, "Data/" + p.name + ".json");
				writeCharacter.print(savePlayer);
				writeCharacter.flush();
				writeCharacter.close();
				//print(savePlayer);
			}
		}
	}
	
	/*
	 * This function will load a player character from the JSON file
	 */
	public void loadPlayer(String name)
	{
		File characterFile = new File("Data/" + name + ".json");
		JSONObject playerJSON = loadJSONObject(characterFile);
		float maxHealth = playerJSON.getFloat("maxHealth");
		float health = playerJSON.getFloat("health");
		float energy = playerJSON.getFloat("energy"); 
		float maxEnergy = playerJSON.getFloat("maxEnergy");
		String[] unlockedRunes = playerJSON.getJSONArray("unlockedRunes").getStringArray();
		String characterString = playerJSON.getString("characterClass");
		CharacterClass characterClass = null; 
		if(characterString.equals("Wizard"))
		{
			characterClass = CharacterClass.WIZARD; 
		}
		else if(characterString.equals("Knight"))
		{
			characterClass = CharacterClass.KNIGHT;
		}
		int currentLevel = playerJSON.getInt("currentStage");
		boolean unlockedLevels[] = playerJSON.getJSONArray("unlockedStages").getBooleanArray();
		p = new Player(maxHealth, health, maxEnergy, energy, unlockedRunes,characterClass, name,
				currentLevel, unlockedLevels[0], unlockedLevels[1], unlockedLevels[2]);
		s = new MapScene(this);
	}

	// For all input event functions, simply call the corresponding function in the
	// current scene

	public void mouseClicked() {
		s.mouseClicked();
	}

	public void mousePressed() {
		s.mousePressed();
	}

	public void mouseReleased() {
		s.mouseReleased();
	}

	public void mouseDragged() {
		s.mouseDragged();
	}

	public void keyPressed() {
		s.keyPressed();
	}

	public void keyReleased() {
		s.keyReleased();
	}

	public void settings() {
		size(375, 667);
	}

	static public void main(String[] passedArgs) {
		String[] appletArgs = new String[] { "main.Main" };
		if (passedArgs != null) {
			PApplet.main(concat(appletArgs, passedArgs));
		} else {
			PApplet.main(appletArgs);
		}
	}
}
