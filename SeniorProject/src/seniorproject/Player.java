package seniorproject;

import java.util.ArrayList;

public class Player 
{
  //1. Global Variables - will expand over time. For right now, need health and runes. 
  //TODO: Expand player - allow customization, keep track of unlocked skills and abilities, 
  //Track stats, experience, level, appearance, inventory, equipment, etc. 
  
  String name; 
  public float maxHealth; //Keeps track of the player's maximum health value
  public float health; //Tracks the player's health value
  public float maxEnergy; //Keeps track of the player's maximum energy value
  public float energy; //Tracks the player's energy value (gets reduced when player attacks, fills from clearing runes) 
  public float x; //represents the x position of the player's bottom left corner
  public float y; //represents the y position of the player's bottom left corner
  public ArrayList<String> unlockedRunes; //Keeps track of what runes the player currently has available
  Main main; 
  
  public Player(Main main) //Used to create a null or default character
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
     name = "NULL"; 
     this.main = main;
  }
  
  public Player(String name) //Used to create a default character with the given name 
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
     this.name = name;
  }
  
  
  
  public void drawPlayer()
  {
    //This blue triangle represents the player's body
    main.fill(200, 200, 255);
    main.triangle(x, y, x + (Main.screenX * 0.2f), y, x + (Main.screenX * 0.1f), y - (Main.screenX * 0.2f));
    //this (hopefully) peach colored circle represents the player's head
    main.fill(255, 245, 200);
    main.ellipse(x + (Main.screenX * 0.1f), y - (Main.screenX * 0.3f), Main.screenX * 0.2f, Main.screenX * 0.2f);
  }
  
  
  
  
  
  
  
  
  
  
}