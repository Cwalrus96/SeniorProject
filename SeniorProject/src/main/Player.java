package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import animations.SpriteAnimation;

public class Player {
	// 1. Global Variables - will expand over time. For right now, need health and runes. 

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
	public String currentStage; //Keeps track of the player's current location on the map (which stage are they on)
	
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
		currentStage = "Start"; 
		main.stages.findNode("Start").setStatus(NodeStatus.UNLOCKED);
		main.stages.findNode("Stage1A").setStatus(NodeStatus.AVAILABLE);
		main.stages.findNode("Stage1B").setStatus(NodeStatus.AVAILABLE);
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
		currentStage = "Start"; 
		main.stages.findNode("Start").setStatus(NodeStatus.UNLOCKED);
		main.stages.findNode("Stage1A").setStatus(NodeStatus.AVAILABLE);
		main.stages.findNode("Stage1B").setStatus(NodeStatus.AVAILABLE);
	}

	// This constructor is used when loading a character from a JSON file
	public Player(float maxHealth, float health, float maxEnergy, float energy, String[] unlockedRunes,
			CharacterClass characterClass, String name, String currentStage) {
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
		this.currentStage = currentStage; 
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