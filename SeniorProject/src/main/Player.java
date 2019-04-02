package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import animations.SpriteAnimation;

public class Player {
	// 1. Global Variables - will expand over time. For right now, need health and runes.
	// TODO: Expand player - allow customization, keep track of unlocked skills and
	// abilities,
	// Track stats, experience, level, appearance, inventory, equipment, etc.
	//Consider creating a separate "progress" object to use when loading the character? maybe several? 

	String name;
	public float maxHealth; // Keeps track of the player's maximum health value
	public float health; // Tracks the player's health value
	public float maxEnergy; // Keeps track of the player's maximum energy value
	public float energy; // Tracks the player's energy value (gets reduced when player attacks, fills
							// from clearing runes)
	public float x; // represents the x position of the player's bottom left corner
	public float y; // represents the y position of the player's bottom left corner
	public ArrayList<String> unlockedRunes; // Keeps track of what runes the player currently has available
	public CharacterClass characterClass;
	public CharacterStatus status;
	SpriteAnimation sprite;
	public boolean statusChange;
	Main main;
	public int currentStage; //Keeps track of the player's current location on the map (which stage are they on)
	public GameTree stages; //Keeps track of which stages the player has unlocked so far
	
	public Player(Main main) // Used to create a null or default character
	{
		maxHealth = 100.0f;
		health = maxHealth;
		maxEnergy = 50.0f;
		energy = maxEnergy;
		unlockedRunes = new ArrayList<String>();
		unlockedRunes.add("Fire");
		unlockedRunes.add("Slash");
		unlockedRunes.add("Heal");
		x = Main.screenX * 0.2f;
		y = Main.screenY - Main.screenX;
		name = "NULL";
		this.main = main;
		status = CharacterStatus.IDLE;
		this.characterClass = CharacterClass.WIZARD;
		currentStage = 0; 
		stages = new GameTree(); 
		stages.addNode("Start", null, null, NodeStatus.UNLOCKED);
		stages.addNode("Stage1A",new String[] {"Start"} , null, NodeStatus.AVAILABLE); 
		stages.addNode("Stage1B", new String[] {"Start"}, null, NodeStatus.AVAILABLE);
		stages.addNode("Boss1", new String[] {"Stage1A", "Stage1B"}, null, NodeStatus.UNAVAILABLE);
		stages.findNode("Start").addChild(stages.findNode("Stage1A"));
		stages.findNode("Start").addChild(stages.findNode("Stage1B")); 
		stages.findNode("Stage1A").addChild(stages.findNode("Boss1"));
		stages.findNode("Stage1B").addChild(stages.findNode("Boss1"));
	}

	public Player(String name) // Used to create a default character with the given name
	{
		maxHealth = 100.0f;
		health = maxHealth;
		maxEnergy = 50;
		energy = maxEnergy;
		unlockedRunes = new ArrayList<String>();
		unlockedRunes.add("Fire");
		unlockedRunes.add("Slash");
		unlockedRunes.add("Heal");
		x = Main.screenX * 0.2f;
		y = Main.screenY - Main.screenX;
		status = CharacterStatus.IDLE;
		this.name = name;
		currentStage = 0; 
		stages = new GameTree(); 
		stages.addNode("Start", null, null, NodeStatus.UNLOCKED);
		stages.addNode("Stage1A",new String[] {"Start"} , null, NodeStatus.AVAILABLE); 
		stages.addNode("Stage1B", new String[] {"Start"}, null, NodeStatus.AVAILABLE);
		stages.addNode("Boss1", new String[] {"Stage1A", "Stage1B"}, null, NodeStatus.UNAVAILABLE);
		stages.findNode("Start").addChild(stages.findNode("Stage1A"));
		stages.findNode("Start").addChild(stages.findNode("Stage1B")); 
		stages.findNode("Stage1A").addChild(stages.findNode("Boss1"));
		stages.findNode("Stage1B").addChild(stages.findNode("Boss1"));
	}

	// This constructor is used when loading a character from a JSON file
	public Player(float maxHealth, float health, float maxEnergy, float energy, String[] unlockedRunes,
			CharacterClass characterClass, String name, int currentLevel, boolean unlocked1, 
			boolean unlocked2, boolean unlocked3) {
		this.maxHealth = maxHealth;
		this.health = health;
		this.maxEnergy = maxEnergy;
		this.energy = energy;
		this.unlockedRunes = new ArrayList<String>(Arrays.asList(unlockedRunes));
		this.name = name;
		x = Main.screenX * 0.2f;
		y = Main.screenY - Main.screenX;
		this.characterClass = characterClass;
		status = CharacterStatus.IDLE;
		this.currentStage = currentLevel; 
		stages = new HashMap<Integer, Boolean>();
		stages.put(0, true);
		stages.put(1, unlocked1); 
		stages.put(2, unlocked2);
		stages.put(3, unlocked3);
	}

	public String getSpriteFile() {
		if (characterClass == CharacterClass.KNIGHT) 
		{
			if (status == CharacterStatus.IDLE) 
			{
				return "Resources/KnightIdle.png";
			} 
			else if (status == CharacterStatus.ATTACKING) 
			{
				return "Resources/KnightAttacking.png";
			}
		} 
		else if (characterClass == CharacterClass.WIZARD) 
		{
			if (status == CharacterStatus.IDLE)
			{
				return "Resources/WizardIdle.png";
			} 
			else if (status == CharacterStatus.ATTACKING) 
			{
				return "Resources/WizardAttacking.png";
			}
		}
		return null;
	}

}