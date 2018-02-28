class Player 
{
  //1. Global Variables - will expand over time. For right now, need health and runes. 
  //TODO: Expand player - allow customization, keep track of unlocked skills and abilities, 
  //Track stats, experience, level, appearance, inventory, equipment, etc. 
  
  float health; //Tracks the player's health value
  float energy; //Tracks the player's energy value (gets reduced when player attacks, fills from clearing runes) 
  ArrayList<String> unlockedRunes; //Keeps track of what runes the player currently has available
  
  Player()
  {
     health = 100.0; 
     unlockedRunes = new ArrayList<String>(); 
     unlockedRunes.add("Fire"); 
     unlockedRunes.add("Slash"); 
     unlockedRunes.add("Heal"); 
  }
  
  
  
  
  
  
  
  
  
  
}