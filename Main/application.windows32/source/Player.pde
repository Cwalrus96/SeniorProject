class Player 
{
  //1. Global Variables - will expand over time. For right now, need health and runes. 
  //TODO: Expand player - allow customization, keep track of unlocked skills and abilities, 
  //Track stats, experience, level, appearance, inventory, equipment, etc. 
  
  String name; 
  float maxHealth; //Keeps track of the player's maximum health value
  float health; //Tracks the player's health value
  float maxEnergy; //Keeps track of the player's maximum energy value
  float energy; //Tracks the player's energy value (gets reduced when player attacks, fills from clearing runes) 
  float x; //represents the x position of the player's bottom left corner
  float y; //represents the y position of the player's bottom left corner
  ArrayList<String> unlockedRunes; //Keeps track of what runes the player currently has available
  
  Player() //Used to create a null or default character
  {
     maxHealth = 100.0;
     health = maxHealth;
     maxEnergy = 50; 
     energy = maxEnergy;
     unlockedRunes = new ArrayList<String>(); 
     unlockedRunes.add("Fire"); 
     unlockedRunes.add("Slash"); 
     unlockedRunes.add("Heal"); 
     x = Main.screenX * 0.2;
     y = Main.screenY - Main.screenX;
     name = "NULL"; 
  }
  
  Player(String name) //Used to create a default character with the given name 
  {
     maxHealth = 100.0;
     health = maxHealth;
     maxEnergy = 50; 
     energy = maxEnergy;
     unlockedRunes = new ArrayList<String>(); 
     unlockedRunes.add("Fire"); 
     unlockedRunes.add("Slash"); 
     unlockedRunes.add("Heal"); 
     x = Main.screenX * 0.2;
     y = Main.screenY - Main.screenX;
     this.name = name;
  }
  
  
  
  void drawPlayer()
  {
    //This blue triangle represents the player's body
    fill(200, 200, 255);
    triangle(x, y, x + (Main.screenX * 0.2), y, x + (Main.screenX * 0.1), y - (Main.screenX * 0.2));
    //this (hopefully) peach colored circle represents the player's head
    fill(255, 245, 200);
    ellipse(x + (Main.screenX * 0.1), y - (Main.screenX * 0.3), Main.screenX * 0.2, Main.screenX * 0.2);
  }
  
  
  
  
  
  
  
  
  
  
}
